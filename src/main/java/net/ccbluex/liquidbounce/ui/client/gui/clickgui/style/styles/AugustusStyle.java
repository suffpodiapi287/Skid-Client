/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.gui.clickgui.style.styles;

import java.awt.Color;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.Panel;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.elements.ButtonElement;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.elements.ModuleElement;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.style.Style;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class AugustusStyle
extends Style {
    private final Color backgroundColor = new Color(45, 45, 45, 255);
    private final Color panelColor = new Color(35, 35, 35, 255);
    private final Color accentColor = new Color(64, 128, 255, 255);
    private final Color textColor = new Color(255, 255, 255, 255);

    @Override
    public void drawPanel(int mouseX, int mouseY, Panel panel) {
        RenderUtils.drawBorderedRect(panel.getX(), panel.getY(), panel.getX() + panel.getWidth(), panel.getY() + 19 + panel.getFade(), 3.0f, this.panelColor.getRGB(), this.panelColor.getRGB());
        if (panel.getFade() > 0) {
            RenderUtils.drawRect((float)panel.getX(), (float)(panel.getY() + 19), (float)(panel.getX() + panel.getWidth()), (float)(panel.getY() + 19 + panel.getFade()), this.backgroundColor.getRGB());
        }
        Fonts.font35.drawString(panel.getName(), panel.getX() + 5, panel.getY() + 7, this.textColor.getRGB());
    }

    @Override
    public void drawDescription(int mouseX, int mouseY, String text) {
        int textWidth = Fonts.font35.getStringWidth(text);
        RenderUtils.drawBorderedRect(mouseX + 9, mouseY, mouseX + textWidth + 14, mouseY + Fonts.font35.FONT_HEIGHT + 3, 3.0f, this.backgroundColor.getRGB(), this.panelColor.getRGB());
        Fonts.font35.drawString(text, mouseX + 12, mouseY + 2, this.textColor.getRGB());
    }

    @Override
    public void drawButtonElement(int mouseX, int mouseY, ButtonElement buttonElement) {
        Color elementColor = buttonElement.getColor() != Integer.MAX_VALUE ? new Color(buttonElement.getColor()) : (buttonElement.isHovering(mouseX, mouseY) ? new Color(70, 70, 70, 255) : this.panelColor);
        RenderUtils.drawRect((float)buttonElement.getX(), (float)buttonElement.getY(), (float)(buttonElement.getX() + buttonElement.getWidth()), (float)(buttonElement.getY() + buttonElement.getHeight()), elementColor.getRGB());
        Fonts.font35.drawString(buttonElement.getDisplayName(), buttonElement.getX() + 5, buttonElement.getY() + 5, this.textColor.getRGB());
    }

    @Override
    public void drawModuleElement(int mouseX, int mouseY, ModuleElement moduleElement) {
        Color moduleColor = moduleElement.getModule().getState() ? this.accentColor : (moduleElement.isHovering(mouseX, mouseY) ? new Color(70, 70, 70, 255) : this.panelColor);
        RenderUtils.drawRect((float)moduleElement.getX(), (float)moduleElement.getY(), (float)(moduleElement.getX() + moduleElement.getWidth()), (float)(moduleElement.getY() + moduleElement.getHeight()), moduleColor.getRGB());
        Color moduleTextColor = moduleElement.getModule().getState() ? Color.WHITE : this.textColor;
        Fonts.font35.drawString(moduleElement.getModule().getName(), moduleElement.getX() + 5, moduleElement.getY() + 5, moduleTextColor.getRGB());
    }
}