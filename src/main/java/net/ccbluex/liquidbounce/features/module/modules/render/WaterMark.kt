
package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.value.FloatValue
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import java.awt.Color
import kotlin.math.max

class WaterMark : Module("WaterMark", category = ModuleCategory.RENDER) {

    private val posX = FloatValue("X", 10f, 0f, 1920f)
    private val posY = FloatValue("Y", 10f, 0f, 1080f)

    private val backgroundTexture = ResourceLocation("fdpclient/textures/gui/WaterMark.png")

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        val mc = Minecraft.getMinecraft()
        val username = mc.session.username
        val fps = Minecraft.getDebugFPS()
        val ping = try {
            mc.netHandler.getPlayerInfo(mc.thePlayer.uniqueID)?.responseTime ?: -1
        } catch (e: Exception) {
            -1
        }

        val iconFont = Fonts.font40 ?: return
        val infoFont = Fonts.font35 ?: return

        val iconText = "R"
        val infoText = "$username | ${fps}fps | ${ping}ms"

        val padding = 10f
        val spacing = 8f
        val iconBoxSize = iconFont.FONT_HEIGHT + 6f
        val textWidth = infoFont.getStringWidth(infoText).toFloat()
        val totalWidth = iconBoxSize + spacing + textWidth + padding * 2
        val height = iconBoxSize + padding

        val x = posX.get()
        val y = posY.get()

        // Draw background image (NinePatch-style if needed)
        mc.textureManager.bindTexture(backgroundTexture)
        GlStateManager.enableBlend()
        GlStateManager.color(1f, 1f, 1f, 1f)
        net.ccbluex.liquidbounce.utils.render.RenderUtils.drawImage(backgroundTexture, x.toInt(), y.toInt(), totalWidth.toInt(), height.toInt())

        // White box (fake icon box)
        net.ccbluex.liquidbounce.utils.render.RenderUtils.drawRect(
            x + padding,
            y + (height - iconBoxSize) / 2f,
            x + padding + iconBoxSize,
            y + (height - iconBoxSize) / 2f + iconBoxSize,
            Color.WHITE.rgb
        )

        // Draw "R"
        val iconBoxX = x + padding
        val iconBoxY = y + (height - iconBoxSize) / 2f
        val rX = iconBoxX + (iconBoxSize - iconFont.getStringWidth(iconText)) / 2f
        val rY = iconBoxY + (iconBoxSize - iconFont.FONT_HEIGHT) / 2f + 1f
        iconFont.drawString(iconText, rX, rY, Color(150, 150, 255).rgb, true)

        // Draw info
        val textX = iconBoxX + iconBoxSize + spacing
        val textY = y + (height - infoFont.FONT_HEIGHT) / 2f
        infoFont.drawString(infoText, textX, textY, Color.WHITE.rgb, true)
    }
}
