package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

@ModuleInfo(name = "Criticals2", category = ModuleCategory.COMBAT)
class Criticals2 : Module(name = "Criticals2", category = ModuleCategory.COMBAT) {

    private val modeValue = ListValue("Mode", arrayOf(
        "Packet", "NoGround", "Jump", "LowJump", "Grim", "BlocksMC", "Visual", "Blink"
    ), "Packet")

    private val delayValue = IntegerValue("Delay", 0, 0, 500)
    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)

    private val blinkDelayValue = IntegerValue("BlinkDelay", 400, 0, 1000)
    private val blinkRangeValue = FloatValue("BlinkRange", 4.0f, 0f, 10f)

    private val timer = MSTimer()
    private var blinkPackets = mutableListOf<C03PacketPlayer>()
    private var isBlinkActive = false
    private var nextBlinkDelay = 500

    @EventTarget
    fun onAttack(event: AttackEvent) {
        val player = MinecraftInstance.mc.thePlayer ?: return
        val entity = event.targetEntity as? EntityLivingBase ?: return

        if (!player.onGround || player.isOnLadder || player.isInWeb || player.isInWater ||
            player.ridingEntity != null || entity.hurtTime > hurtTimeValue.get() || !timer.hasTimePassed(delayValue.get().toLong())
        ) return

        val x = player.posX
        val y = player.posY
        val z = player.posZ

        when (modeValue.get().lowercase()) {
            "packet" -> {
                sendPacket(C04PacketPlayerPosition(x, y + 0.0625, z, true))
                sendPacket(C04PacketPlayerPosition(x, y, z, false))
                player.onCriticalHit(entity)
            }

            "grim" -> {
                sendPacket(C04PacketPlayerPosition(x, y - 0.000001, z, false))
                player.onCriticalHit(entity)
            }

            "blocksmc" -> {
                if (player.ticksExisted % 4 == 0) {
                    sendPacket(C04PacketPlayerPosition(x, y + 0.0011, z, true))
                    sendPacket(C04PacketPlayerPosition(x, y, z, false))
                    player.onCriticalHit(entity)
                }
            }

            "jump" -> {
                player.motionY = 0.42
                player.onCriticalHit(entity)
            }

            "lowjump" -> {
                player.motionY = 0.1
                player.fallDistance = 0.1f
                player.onGround = false
                player.onCriticalHit(entity)
            }

            "visual" -> player.onCriticalHit(entity)
        }

        timer.reset()
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        val player = MinecraftInstance.mc.thePlayer ?: return

        if (packet is C03PacketPlayer) {
            if (modeValue.get().equals("NoGround", true)) {
                packet.onGround = false
            }

            if (modeValue.get().equals("Blink", true)) {
                if (!player.onGround || player.isInWater || player.isInWeb) {
                    isBlinkActive = false
                    return
                }

                if (!timer.hasTimePassed(nextBlinkDelay.toLong())) {
                    event.cancelEvent()
                    blinkPackets.add(packet)
                    isBlinkActive = true
                } else {
                    nextBlinkDelay = blinkDelayValue.get()
                    isBlinkActive = false
                    blinkPackets.clear()
                }
            }
        }
    }

    override fun onDisable() {
        if (modeValue.get().equals("Blink", true)) {
            isBlinkActive = false
            blinkPackets.forEach { sendPacket(it) }
            blinkPackets.clear()
        }
    }

    private fun sendPacket(packet: C03PacketPlayer) {
        MinecraftInstance.mc.netHandler.addToSendQueue(packet)
    }

    override val tag: String
        get() = modeValue.get()
}
