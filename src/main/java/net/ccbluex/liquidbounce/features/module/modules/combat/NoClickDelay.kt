package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.value.BoolValue
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemSword

class NoClickDelay : Module("NoClickDelay", category = ModuleCategory.COMBAT) {

    private val left = BoolValue("Left", true)
    private val right = BoolValue("Right", true)
    private val hitReg17 = BoolValue("1.7HitReg", true)
    private val noPlaceDelay = BoolValue("NoPlaceDelay", true)

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val player = mc.thePlayer ?: return
        val world = mc.theWorld ?: return

        if (left.get()) {
            mc.leftClickCounter = 0
        }

        if (right.get()) {
            mc.rightClickDelayTimer = 0
        }

        if (hitReg17.get()) {
            if (player.isBlocking || player.isUsingItem) {
                if (player.heldItem?.item is ItemSword) {
                    mc.leftClickCounter = 0
                }
            }
        }

        if (noPlaceDelay.get()) {
            if (player.heldItem?.item is ItemBlock) {
                mc.rightClickDelayTimer = 0
                player.itemInUseCount = 0
            }
        }
    }
}
