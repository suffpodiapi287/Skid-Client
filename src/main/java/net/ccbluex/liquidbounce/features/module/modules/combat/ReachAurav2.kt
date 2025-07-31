package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.value.FloatValue
import net.ccbluex.liquidbounce.features.value.IntegerValue
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.utils.Rotation
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.AxisAlignedBB
import net.ccbluex.liquidbounce.FDPClient
import net.ccbluex.liquidbounce.features.module.ModuleCategory

class ReachAurav2 : Module(name = "ReachAurav2", category = ModuleCategory.COMBAT) {

    private val reachValue = FloatValue("Reach", 3.0f, 3.0f, 6.0f)
    private val delayValue = IntegerValue("Delay", 70, 0, 1000)
    private val timer = MSTimer()
    var currentTarget: EntityLivingBase? = null

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (!event.isPre()) return
        if (!timer.hasTimePassed(delayValue.get().toLong())) return

        val reach = reachValue.get().toDouble()
        val eyePos = mc.thePlayer.getPositionEyes(1.0f)
        val lookVec = mc.thePlayer.getLook(1.0f)
        val endVec = eyePos.addVector(lookVec.xCoord * reach, lookVec.yCoord * reach, lookVec.zCoord * reach)

        val targets = mc.theWorld.loadedEntityList
            .filterIsInstance<EntityLivingBase>()
            .filter {
                it != mc.thePlayer &&
                it.isEntityAlive &&
                !it.isInvisible &&
                mc.thePlayer.getDistanceToEntity(it) <= reach &&
                EntityUtils.isSelected(it, true)
            }
            .mapNotNull { entity ->
                val intercept = entity.entityBoundingBox.expand(0.05, 0.1, 0.05)
                    .calculateIntercept(eyePos, endVec) ?: return@mapNotNull null
                val yawDiff = Math.abs(getRotationToEntity(entity).yaw - mc.thePlayer.rotationYaw) % 360
                val fixedYawDiff = if (yawDiff > 180) 360 - yawDiff else yawDiff
                Triple(entity, eyePos.distanceTo(intercept.hitVec), fixedYawDiff)
            }
            .sortedWith(compareBy({ it.third }, { it.second }))

        val target = targets.firstOrNull()?.first

        if (target != null) {
            val rot = getRotationToEntity(target)
            RotationUtils.targetRotation = rot
            RotationUtils.serverRotation = rot
            mc.thePlayer.swingItem()
            mc.playerController.attackEntity(mc.thePlayer, target)
            timer.reset()
        }

        currentTarget = target
    }

    private fun getRotationToEntity(entity: EntityLivingBase): Rotation {
        val diffX = entity.posX - mc.thePlayer.posX
        val diffY = (entity.posY + entity.eyeHeight / 1.3) - (mc.thePlayer.posY + mc.thePlayer.eyeHeight)
        val diffZ = entity.posZ - mc.thePlayer.posZ
        val distXZ = Math.sqrt(diffX * diffX + diffZ * diffZ)
        val yaw = Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0
        val pitch = -Math.toDegrees(Math.atan2(diffY, distXZ))
        return Rotation(yaw.toFloat(), pitch.toFloat())
    }
}
