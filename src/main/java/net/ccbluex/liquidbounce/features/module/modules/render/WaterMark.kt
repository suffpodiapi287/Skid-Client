package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.minecraft.client.Minecraft
import java.awt.Color

class WaterMark : Module("WaterMark", category = ModuleCategory.RENDER) {

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        val clientName = "FDPClient+++"
        val user = mc.session.username
        val fps = Minecraft.getDebugFPS()
        val ping = try {
            mc.netHandler.getPlayerInfo(mc.thePlayer.uniqueID)?.responseTime ?: -1
        } catch (e: Exception) {
            -1
        }

        val info = "$user | ${fps}fps | ${ping}ms"

        val logoFont = Fonts.font40 ?: return
        val infoFont = Fonts.font35 ?: return

        // Tính kích thước watermark
        val logoWidth = logoFont.getStringWidth(clientName)
        val infoWidth = infoFont.getStringWidth(info)
        val totalWidth = logoWidth + 8 + infoWidth
        val height = maxOf(logoFont.FONT_HEIGHT, infoFont.FONT_HEIGHT) + 6

        val x = 5f
        val y = 5f

        // Vẽ nền xám bo góc
        RenderUtils.drawBorderedRect(
        x,
        y,
        x + totalWidth + 10f,
        y + height,
        1.2f,
        Color(32, 32, 32, 180).rgb,  // nền
        Color(255, 255, 255, 100).rgb // viền
)

        // Vẽ chữ logo (R)
        logoFont.drawString(clientName, x + 5, y + 4, Color(200, 200, 255).rgb, true)

        // Vẽ dòng text bên phải logo
        infoFont.drawString(info, x + 5 + logoWidth + 6, y + 5, Color.WHITE.rgb, true)
    }
}
