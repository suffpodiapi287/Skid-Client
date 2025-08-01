package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.features.value.BoolValue
import net.ccbluex.liquidbounce.features.value.IntegerValue
import net.ccbluex.liquidbounce.features.value.ListValue
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayClient
import net.minecraft.network.play.INetHandlerPlayServer
import java.util.*

@ModuleInfo(name = "FakeLag", category = ModuleCategory.COMBAT)
class FakeLag : Module(name = "FakeLag", category = ModuleCategory.COMBAT) {

    private val fakeLagMode = ListValue("Mode", arrayOf("All", "InBound", "OutBound"), "All")
    private val fakeLagMoveOnly = BoolValue("MoveOnly", false)

    private val minRand: IntegerValue = object : IntegerValue("MinDelay", 170, 0, 20000, "ms") {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val v = maxRand.get()
            if (v < newValue) set(v)
        }
    }
    private val maxRand: IntegerValue = object : IntegerValue("MaxDelay", 500, 0, 20000, "ms") {
        override fun onChanged(oldValue: Int, newValue: Int) {
            val v = minRand.get()
            if (v > newValue) set(v)
        }
    }

    private val fakeLagInclude = BoolValue("Include", true)
    private val fakeLagExclude = BoolValue("Exclude", false)

    // variables
    private val outBus = LinkedList<Packet<INetHandlerPlayServer>>()
    private val inBus = LinkedList<Packet<INetHandlerPlayClient>>()

    private val ignoreBus = LinkedList<Packet<out INetHandler>>()

    private val inTimer = MSTimer()
    private val outTimer = MSTimer()

    private var inDelay = 0
    private var outDelay = 0

    override fun onEnable() {
        inBus.clear()
        outBus.clear()
        ignoreBus.clear()

        inTimer.reset()
        outTimer.reset()
    }

    override fun onDisable() {
        while (inBus.size > 0)
            inBus.poll()?.processPacket(mc.netHandler)

        while (outBus.size > 0) {
            val upPacket = outBus.poll() ?: continue
            PacketUtils.sendPacketNoEvent(upPacket)
        }

        inBus.clear()
        outBus.clear()
        ignoreBus.clear()
    }

    @EventTarget(priority = -100)
    fun onPacket(event: PacketEvent) {
        mc.thePlayer ?: return
        mc.theWorld ?: return
        val packet = event.packet
        if (ignoreBus.remove(packet)) return

        if ((fakeLagMode.get().equals("outbound", true) || fakeLagMode.get().equals("all", true))
            && packet::class.java.simpleName.startsWith("C", true)
            && (!fakeLagInclude.get() || "c0f,confirmtransaction,packetplayer,c17".split(",")
                .find { packet::class.java.simpleName.contains(it, true) } != null)
            && (!fakeLagExclude.get() || "c0f,confirmtransaction,packetplayer,c17".split(",")
                .find { packet::class.java.simpleName.contains(it, true) } == null)
        ) {
            outBus.add(packet as Packet<INetHandlerPlayServer>)
            ignoreBus.add(packet)
            event.cancelEvent()
        }

        if ((fakeLagMode.get().equals("inbound", true) || fakeLagMode.get().equals("all", true))
            && packet::class.java.simpleName.startsWith("S", true)
            && (!fakeLagInclude.get() || "c0f,confirmtransaction,packetplayer,c17".split(",")
                .find { packet::class.java.simpleName.contains(it, true) } != null)
            && (!fakeLagExclude.get() || "c0f,confirmtransaction,packetplayer,c17".split(",")
                .find { packet::class.java.simpleName.contains(it, true) } == null)
        ) {
            inBus.add(packet as Packet<INetHandlerPlayClient>)
            ignoreBus.add(packet)
            event.cancelEvent()
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        inBus.clear()
        outBus.clear()
        ignoreBus.clear()

        inTimer.reset()
        outTimer.reset()
    }

    @EventTarget(priority = -5)
    fun onUpdate(event: UpdateEvent) {
        mc.netHandler ?: return

        if (!inBus.isEmpty() && ((fakeLagMoveOnly.get() && !MovementUtils.isMoving()) || inTimer.hasTimePassed(inDelay.toLong()))) {
            while (inBus.size > 0)
                inBus.poll()?.processPacket(mc.netHandler)
            inDelay = RandomUtils.nextInt(minRand.get(), maxRand.get())
            inTimer.reset()
        }
        if (!outBus.isEmpty() && ((fakeLagMoveOnly.get() && !MovementUtils.isMoving()) || outTimer.hasTimePassed(
                outDelay.toLong()
            ))
        ) {
            while (outBus.size > 0) {
                val upPacket = outBus.poll() ?: continue
                PacketUtils.sendPacketNoEvent(upPacket)
            }
            outDelay = RandomUtils.nextInt(minRand.get(), maxRand.get())
            outTimer.reset()
        }
    }
}