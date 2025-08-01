package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.network.play.server.S14PacketEntity
import net.minecraft.util.Vec3
import java.awt.Color
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class BackTrack2 : Module("BackTrack2", category = ModuleCategory.COMBAT) {

    private val mode = ListValue("Mode", arrayOf("Legacy", "Modern"), "Modern")

    private val nextBacktrackDelay = IntegerValue("NextBacktrackDelay", 0, 0, 10000)
    private val minDelay = IntegerValue("MinDelay", 80, 0, 10000)
    private val maxDelay = IntegerValue("MaxDelay", 120, 0, 10000)
    private val style = ListValue("Style", arrayOf("Pulse", "Smooth"), "Smooth")
    private val smart = BoolValue("Smart", true)
    private val distance = FloatValue("Distance", 3.0f, 0.0f, 8.0f)
    private val espMode = ListValue("ESPMode", arrayOf("None", "Box", "Model", "Wireframe"), "Box")
    private val wireframeWidth = FloatValue("WireframeWidth", 1f, 0.5f, 8f)
    private val espColor = ColorValue("ESPColor", Color(0, 255, 0).rgb)

    private val legacyPos = ListValue("CachingMode", arrayOf("ClientPos", "ServerPos"), "ClientPos")
    private val maxCachedPositions = IntegerValue("MaxCachedPositions", 10, 1, 20)

    private val cache = ConcurrentHashMap<Int, MutableList<Vec3>>()
    private val timeCache = ConcurrentHashMap<Int, Long>()

    private var nextBacktrackTime = 0L
    val colorInt = espColor.get()
    val colorObj = Color(colorInt)
    
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (!mode.get().equals("Modern", true)) return

        val now = System.currentTimeMillis()
        if (now < nextBacktrackTime) return

        for (entity in mc.theWorld.playerEntities) {
            if (entity is EntityOtherPlayerMP && entity != mc.thePlayer) {
                if (smart.get() && !entity.isEntityAlive) continue
                if (mc.thePlayer.getDistanceToEntity(entity) > distance.get()) continue

                val id = entity.entityId
                val list = cache.getOrPut(id) { mutableListOf() }
                val vec = Vec3(entity.posX, entity.posY, entity.posZ)
                list.add(0, vec)
                if (list.size > maxCachedPositions.get())
                    list.removeAt(list.size - 1)
                timeCache[id] = now
            }
        }

        val randomDelay = minDelay.get() + Random().nextInt(maxDelay.get() - minDelay.get() + 1)
        nextBacktrackTime = now + randomDelay + nextBacktrackDelay.get()
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (!mode.get().equals("Legacy", true)) return
        val packet = event.packet
        if (packet is S14PacketEntity) {
            val entity = packet.getEntity(mc.theWorld) as? EntityOtherPlayerMP ?: return
            val entityID = entity.entityId
            val list = cache.getOrPut(entityID) { mutableListOf() }
            val vec = Vec3(entity.posX, entity.posY, entity.posZ)
            list.add(0, vec)
            if (list.size > maxCachedPositions.get())
                list.removeAt(list.size - 1)
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (espMode.get() == "None") return
        val renderColor = (espColor.get() as java.awt.Color).rgb

        for ((id, positions) in cache) {
            val entity = mc.theWorld?.getEntityByID(id) as? EntityOtherPlayerMP ?: continue
            if (!entity.isEntityAlive || positions.isEmpty()) continue

            for (vec in positions) {
                RenderUtils.drawEntityBox(entity, colorObj, false)
            }
        }
    }

    override fun onDisable() {
        cache.clear()
        timeCache.clear()
    }
}