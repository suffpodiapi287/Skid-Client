package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.value.BoolValue
import net.ccbluex.liquidbounce.features.value.IntegerValue
import net.ccbluex.liquidbounce.features.value.ListValue
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.server.S14PacketEntity
import net.minecraft.util.AxisAlignedBB
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import java.util.*
import kotlin.collections.HashMap

class Backtrack2 : Module("Backtrack2", category = ModuleCategory.COMBAT) {

    private val modeValue = ListValue("Mode", arrayOf("Legacy", "Modern"), "Legacy")
    private val espModeValue = ListValue("ESPMode", arrayOf("None", "Box", "Wireframe"), "Box")
    private val styleValue = ListValue("Style", arrayOf("Smooth", "Pulse"), "Smooth")
    private val smartValue = BoolValue("Smart", true)
    private val maxTicksValue = IntegerValue("MaxTicks", 10, 1, 40)
    private val maxDelayValue = IntegerValue("MaxDelay", 250, 50, 1000) // ms

    private val backtrackMap = HashMap<Int, MutableList<BacktrackPos>>()

    data class BacktrackPos(val x: Double, val y: Double, val z: Double, val timestamp: Long)

@EventTarget
fun onPacket(event: PacketEvent) {
    if (!state) return
    if (event.packet !is S14PacketEntity) return

    val packet = event.packet as S14PacketEntity
    val entityIdField = S14PacketEntity::class.java.getDeclaredField("entityId")
    entityIdField.isAccessible = true
    val entityId = entityIdField.getInt(packet)

    val entity = mc.theWorld.getEntityByID(entityId)
    if (entity !is EntityPlayer || entity === mc.thePlayer) return


        val list = backtrackMap.getOrPut(entity.entityId) { mutableListOf() }

        val smart = smartValue.get()
        val dist = mc.thePlayer.getDistanceToEntity(entity)

        if (!smart || dist >= 1.5f) {
            list.add(BacktrackPos(entity.posX, entity.posY, entity.posZ, System.currentTimeMillis()))
        }

        when (modeValue.get().lowercase()) {
            "legacy" -> {
                if (list.size > maxTicksValue.get()) {
                    list.removeAt(0)
                }
            }

            "modern" -> {
                val cutoff = System.currentTimeMillis() - maxDelayValue.get()
                list.removeIf { it.timestamp < cutoff }
            }
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (!state) return

        val esp = espModeValue.get().lowercase()
        if (esp == "none") return

        glPushMatrix()
        glEnable(GL_BLEND)
        glDisable(GL_TEXTURE_2D)
        glDisable(GL_DEPTH_TEST)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        if (esp == "wireframe") {
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
            glLineWidth(2f)
        }

        val style = styleValue.get().lowercase()
        val now = System.currentTimeMillis()
        val maxDelay = maxDelayValue.get()

        for ((_, list) in backtrackMap) {
            val renderList = when (style) {
                "pulse" -> list.takeLast(1)
                "smooth" -> list
                else -> emptyList()
            }

            for (pos in renderList) {
                if (modeValue.get().equals("modern", true)) {
                    val age = now - pos.timestamp
                    if (age > maxDelay) continue
                }

                val x = pos.x - mc.renderManager.renderPosX
                val y = pos.y - mc.renderManager.renderPosY
                val z = pos.z - mc.renderManager.renderPosZ

                val box = AxisAlignedBB(x - 0.3, y, z - 0.3, x + 0.3, y + 1.8, z + 0.3)
                drawBox(box, Color(0, 255, 100, 100))
            }
        }

        if (esp == "wireframe") {
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
        }

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_TEXTURE_2D)
        glDisable(GL_BLEND)
        glPopMatrix()
    }

    private fun drawBox(box: AxisAlignedBB, color: Color) {
        val r = color.red / 255f
        val g = color.green / 255f
        val b = color.blue / 255f
        val a = color.alpha / 255f

        glColor4f(r, g, b, a)
        glBegin(GL_LINE_LOOP)
        glVertex3d(box.minX, box.minY, box.minZ)
        glVertex3d(box.maxX, box.minY, box.minZ)
        glVertex3d(box.maxX, box.minY, box.maxZ)
        glVertex3d(box.minX, box.minY, box.maxZ)
        glEnd()

        glBegin(GL_LINE_LOOP)
        glVertex3d(box.minX, box.maxY, box.minZ)
        glVertex3d(box.maxX, box.maxY, box.minZ)
        glVertex3d(box.maxX, box.maxY, box.maxZ)
        glVertex3d(box.minX, box.maxY, box.maxZ)
        glEnd()
    }

    override fun onDisable() {
        backtrackMap.clear()
    }

    override val tag: String
        get() = modeValue.get()
}
