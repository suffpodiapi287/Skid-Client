package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.util.AxisAlignedBB
import net.minecraft.client.gui.GuiGameOver
import net.minecraft.init.Blocks
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.server.*
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import kotlin.math.abs
import kotlin.math.roundToLong
import kotlin.math.sqrt

class FlagCheck : Module(name = "FlagCheck", category = ModuleCategory.MISC) {

    private val ghostBlockCheck = BoolValue("GhostBlock-Check", true)
    private val ghostBlockDelay = IntegerValue("GhostBlock-Delay", 750, 100, 2000)

    private val rubberbandCheck = BoolValue("Rubberband-Check", true)
    private val rubberbandThreshold = FloatValue("Rubberband-Threshold", 5.0f, 0.1f, 10.0f)

    private val resetFlagTicks = IntegerValue("Reset-Ticks", 5000, 1000, 20000)

    private val renderLagbackBox = BoolValue("RenderLagbackBox", true)
    private val showHudFlagCounter = BoolValue("ShowFlagHUD", true)

    private var flagCount = 0
    private var lastYaw = 0f
    private var lastPitch = 0f

    private val timer = MSTimer()
    private var tickCounter = 0

    private val blockPlacementAttempts = mutableMapOf<BlockPos, Long>()
    private val successfulPlacements = mutableSetOf<BlockPos>()

    private var lastServerPos: Vec3? = null
    private var serverPosTime = 0L

    private var lastPosX = 0.0
    private var lastPosY = 0.0
    private var lastPosZ = 0.0

    private var lastMotionX = 0.0
    private var lastMotionY = 0.0
    private var lastMotionZ = 0.0

    override fun onEnable() {
        flagCount = 0
        tickCounter = 0
        blockPlacementAttempts.clear()
        successfulPlacements.clear()
    }

    override fun onDisable() {
        onEnable()
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val player = mc.thePlayer ?: return
        val packet = event.packet

        if (player.ticksExisted <= 100 || player.isDead || (player.capabilities.isFlying && player.capabilities.disableDamage)) return

        when (packet) {
            is S08PacketPlayerPosLook -> {
                val yawDelta = calculateDelta(packet.yaw, lastYaw)
                val pitchDelta = calculateDelta(packet.pitch, lastPitch)

                lastServerPos = Vec3(packet.x, packet.y, packet.z)
                serverPosTime = System.currentTimeMillis()

                if (yawDelta > 90 || pitchDelta > 90) {
                    flagCount++
                    printFlag("ForceRotate", "${yawDelta.roundToLong()}° / ${pitchDelta.roundToLong()}°")
                } else {
                    flagCount++
                    printFlag("Lagback", "")
                }

                lastYaw = player.rotationYaw
                lastPitch = player.rotationPitch
            }

            is C08PacketPlayerBlockPlacement -> {
                val pos = packet.position
                blockPlacementAttempts[pos] = System.currentTimeMillis()
                successfulPlacements.add(pos)
            }

            }
        }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val player = mc.thePlayer ?: return
        val world = mc.theWorld ?: return

        if (player.ticksExisted <= 100 || player.isDead || mc.currentScreen is GuiGameOver) return

        val now = System.currentTimeMillis()

        // Reset lagback render after 5s
        if (lastServerPos != null && now - serverPosTime > 5000)
            lastServerPos = null

        // GhostBlock check
        if (ghostBlockCheck.get() && timer.hasTimePassed(1000)) {
            timer.reset()

            val toRemove = blockPlacementAttempts.filter { (pos, time) ->
                val expired = now - time > ghostBlockDelay.get()
                if (expired && successfulPlacements.contains(pos)) {
                    if (world.getBlockState(pos).block == Blocks.air) {
                        flagCount++
                        printFlag("GhostBlock", "")
                        successfulPlacements.clear()
                        return@filter true
                    }
                }
                false
            }.keys

            toRemove.forEach { blockPlacementAttempts.remove(it) }
        }

        // Invalid health/hunger
        val reasons = mutableListOf<String>()
        if (player.health <= 0f) reasons.add("Health")
        if (player.foodStats.foodLevel <= 0) reasons.add("Hunger")
        if (reasons.isNotEmpty()) {
            flagCount++
            printFlag("Invalid", reasons.joinToString(" & "))
        }

        // Rubberband check
        if (rubberbandCheck.get()) {
            val dx = player.posX - lastPosX
            val dy = player.posY - lastPosY
            val dz = player.posZ - lastPosZ

            val motionExceeded = abs(player.motionX) > rubberbandThreshold.get() ||
                                 abs(player.motionY) > rubberbandThreshold.get() ||
                                 abs(player.motionZ) > rubberbandThreshold.get()

            val dist = sqrt(dx * dx + dy * dy + dz * dz)
            if (dist > rubberbandThreshold.get() || motionExceeded) {
                flagCount++
                printFlag("Rubberband", "")
            }

            lastPosX = player.posX
            lastPosY = player.posY
            lastPosZ = player.posZ

            lastMotionX = player.motionX
            lastMotionY = player.motionY
            lastMotionZ = player.motionZ
        }

        // Reset flags timer
        tickCounter++
        if (tickCounter >= resetFlagTicks.get()) {
            tickCounter = 0
            flagCount = 0
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (!renderLagbackBox.get()) return
        val pos = lastServerPos ?: return

        val x = pos.xCoord - mc.renderManager.renderPosX
        val y = pos.yCoord - mc.renderManager.renderPosY
        val z = pos.zCoord - mc.renderManager.renderPosZ

        val bb = AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1)
        RenderUtils.drawBoundingBox(bb)
}

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        if (!showHudFlagCounter.get() || flagCount <= 0) return

        val text = "⚠ Flags: $flagCount"
        mc.fontRendererObj.drawStringWithShadow(text, 5f, 5f, 0xFFFF5555.toInt())
    }

    private fun printFlag(type: String, info: String) {
        val msg = "§c[FlagCheck] §f$type Detected" + if (info.isNotBlank()) " §7($info)" else "" + " §8[§c${flagCount}x§8]"
        mc.thePlayer?.addChatMessage(net.minecraft.util.ChatComponentText(msg))
    }

    private fun calculateDelta(a: Float, b: Float): Float {
        var delta = a - b
        if (delta > 180) delta -= 360
        if (delta < -180) delta += 360
        return abs(delta)
    }
}
