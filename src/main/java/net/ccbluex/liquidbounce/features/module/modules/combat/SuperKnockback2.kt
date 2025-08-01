package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.value.*
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C0BPacketEntityAction
import kotlin.math.abs
import kotlin.math.hypot

@ModuleInfo(name = "SuperKnockback2", category = ModuleCategory.COMBAT)
class SuperKnockback2 : Module(name = "SuperKnockback2", category = ModuleCategory.COMBAT) {

    private val modeValue = ListValue("Mode", arrayOf(
        "WTap", "SprintTap", "Packet", "Silent", "LegitFast", "STap"
    ), "WTap")

    private val hurtTimeValue = IntegerValue("HurtTime", 10, 0, 10)
    private val delayValue = IntegerValue("Delay", 0, 0, 500)
    private val chanceValue = IntegerValue("Chance", 100, 0, 100)

    private val onlyGroundValue = BoolValue("OnlyGround", false)
    private val onlyMovingValue = BoolValue("OnlyMoving", true)
    private val onlyForwardValue = BoolValue("OnlyForward", false)
    private val onlyBackMoveTarget = BoolValue("OnlyBackMoveTarget", false)
    private val minAngleDiffValue = IntegerValue("MinAngleDiff", 0, 0, 180)

    private val timer = MSTimer()
    private var ticks = 0

    @EventTarget
    fun onAttack(event: AttackEvent) {
        val player = mc.thePlayer ?: return
        val target = event.targetEntity as? EntityLivingBase ?: return

        if (target.hurtTime > hurtTimeValue.get()) return
        if (!timer.hasTimePassed(delayValue.get().toLong())) return
        if (onlyGroundValue.get() && !player.onGround) return
        if (onlyMovingValue.get() && !MovementUtils.isMoving()) return
        if ((0..100).random() > chanceValue.get()) return
        if (onlyForwardValue.get() && !mc.gameSettings.keyBindForward.isKeyDown) return

        val moveYaw = getMovingYaw()
        val lookYaw = player.rotationYaw
        val angleDiff = abs(moveYaw - lookYaw)
        if (angleDiff > minAngleDiffValue.get()) return

        if (onlyBackMoveTarget.get()) {
            val dx = target.posX - target.prevPosX
            val dz = target.posZ - target.prevPosZ
            val distNow = hypot(target.posX - player.posX, target.posZ - player.posZ)
            val distPredicted = hypot(target.posX + dx - player.posX, target.posZ + dz - player.posZ)

            if (distPredicted <= distNow) return
        }

        when (modeValue.get().lowercase()) {
            "wtap", "sprinttap" -> {
                if (player.isSprinting) {
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(player, C0BPacketEntityAction.Action.STOP_SPRINTING))
                    ticks = 2
                }
            }

            "packet" -> {
                mc.netHandler.addToSendQueue(C0BPacketEntityAction(player, C0BPacketEntityAction.Action.STOP_SPRINTING))
                mc.netHandler.addToSendQueue(C0BPacketEntityAction(player, C0BPacketEntityAction.Action.START_SPRINTING))
            }

            "silent" -> {
                if (player.isSprinting) {
                    ticks = 2
                }
            }

            "legitfast" -> {
                try {
                    val field = player.javaClass.getDeclaredField("sprintingTicksLeft")
                    field.isAccessible = true
                    field.setInt(player, 0)
                } catch (_: Exception) {
                }
            }

            "stap" -> {
                if (player.isSprinting) {
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(player, C0BPacketEntityAction.Action.STOP_SPRINTING))
                    repeat(2) {
                        mc.netHandler.addToSendQueue(C0BPacketEntityAction(player, C0BPacketEntityAction.Action.START_SPRINTING))
                        mc.netHandler.addToSendQueue(C0BPacketEntityAction(player, C0BPacketEntityAction.Action.STOP_SPRINTING))
                    }
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(player, C0BPacketEntityAction.Action.START_SPRINTING))
                    player.motionX *= 0.97
                    player.motionZ *= 0.97
                }
            }
        }

        timer.reset()
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val player = mc.thePlayer ?: return

        when (modeValue.get().lowercase()) {
            "wtap", "sprinttap" -> {
                when (ticks) {
                    2 -> {
                        player.isSprinting = false
                        ticks--
                    }
                    1 -> {
                        player.isSprinting = true
                        ticks--
                    }
                }
            }

            "silent" -> {
                if (ticks == 2) {
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(player, C0BPacketEntityAction.Action.STOP_SPRINTING))
                    ticks--
                } else if (ticks == 1 && player.isSprinting) {
                    mc.netHandler.addToSendQueue(C0BPacketEntityAction(player, C0BPacketEntityAction.Action.START_SPRINTING))
                    ticks--
                }
            }
        }
    }

    override val tag: String
        get() = modeValue.get()
}

fun getMovingYaw(): Float {
    val player = MinecraftInstance.mc.thePlayer ?: return 0f
    var yaw = player.rotationYaw

    val forward = player.movementInput.moveForward
    val strafe = player.movementInput.moveStrafe

    if (forward != 0f) {
        if (strafe > 0f) yaw += if (forward > 0f) -45 else 45
        else if (strafe < 0f) yaw += if (forward > 0f) 45 else -45
    } else {
        if (strafe > 0f) yaw -= 90f
        else if (strafe < 0f) yaw += 90f
    }

    return yaw
}