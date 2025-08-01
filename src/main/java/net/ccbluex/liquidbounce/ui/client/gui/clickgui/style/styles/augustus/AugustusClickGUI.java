/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.gui.clickgui.style.styles.augustus;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.FDPClient;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.value.BoolValue;
import net.ccbluex.liquidbounce.features.value.FloatValue;
import net.ccbluex.liquidbounce.features.value.IntegerValue;
import net.ccbluex.liquidbounce.features.value.ListValue;
import net.ccbluex.liquidbounce.features.value.Value;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class AugustusClickGUI
extends GuiScreen {
    private static final Color TITLEBAR_COLOR = new Color(25, 27, 33, 250);
    private static final Color BACKGROUND_COLOR = new Color(35, 37, 43, 240);
    private static final Color SIDEBAR_COLOR = new Color(30, 32, 38, 220);
    private static final Color CONTENT_COLOR = new Color(32, 34, 40, 200);
    private static final Color TEXT_COLOR = new Color(255, 255, 255, 255);
    private static final Color SECONDARY_TEXT = new Color(160, 160, 170, 255);
    private static final Color ENABLED_COLOR = new Color(102, 112, 255, 255);
    private static final Color TITLE_COLOR = new Color(102, 112, 255, 255);
    private static final Color DISABLED_COLOR = new Color(255, 100, 100, 255);
    private static final int SIDEBAR_WIDTH = 160;
    private static final int TITLEBAR_HEIGHT = 30;
    private static final int CATEGORY_TAB_HEIGHT = 26;
    private static final int MODULE_HEIGHT = 22;
    private static final int CORNER_RADIUS = 8;
    private ModuleCategory selectedCategory = ModuleCategory.COMBAT;
    private Module selectedModule = null;
    private int scrollOffset = 0;
    private int settingsScrollOffset = 0;
    private boolean mouseDown = false;
    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private long lastClickTime = 0L;
    private Module lastClickedModule = null;
    private boolean bindingKey = false;
    private boolean wasMousePressed = false;
    private boolean justClicked = false;
    private boolean middleMouseDragging = false;
    private int middleDragStartY = 0;
    private int middleDragStartScrollOffset = 0;
    private int lastMiddleMouseY = 0;
    private int guiWidth = 520;
    private int guiHeight = 420;
    private int guiX;
    private int guiY;
    private static GameFontRenderer espFontTitle;
    private static GameFontRenderer espFontModuleTitle;

    private static GameFontRenderer getEspFontTitle() {
        if (espFontTitle == null) {
            try {
                
try {
    Font font = new Font("Arial", Font.PLAIN, 18);
    GameFontRenderer customFont = new GameFontRenderer(font);
} catch (Exception e) {
    e.printStackTrace();
}

                Font awtFont = new Font("Arial", Font.PLAIN, 20);
        awtFont = awtFont.deriveFont(Font.PLAIN, 42.0f);
                espFontTitle = new GameFontRenderer(awtFont);
            }
            catch (Exception e) {
                e.printStackTrace();
                espFontTitle = Fonts.font40;
            }
        }
        return espFontTitle;
    }

    private static GameFontRenderer getEspFontModuleTitle() {
        if (espFontModuleTitle == null) {
            try {
                
try {
    Font font = new Font("Arial", Font.PLAIN, 18);
    GameFontRenderer customFont = new GameFontRenderer(font);
} catch (Exception e) {
    e.printStackTrace();
}

                Font awtFont = new Font("Arial", Font.PLAIN, 20);
        awtFont = awtFont.deriveFont(Font.PLAIN, 40.0f);
                espFontModuleTitle = new GameFontRenderer(awtFont);
            }
            catch (Exception e) {
                e.printStackTrace();
                espFontModuleTitle = Fonts.font40;
            }
        }
        return espFontModuleTitle;
    }

    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.guiX = (sr.getScaledWidth() - this.guiWidth) / 2;
        this.guiY = (sr.getScaledHeight() - this.guiHeight) / 2;
        this.middleMouseDragging = false;
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.dragging) {
            this.guiX = mouseX - this.dragOffsetX;
            this.guiY = mouseY - this.dragOffsetY;
        }
        boolean currentMousePressed = Mouse.isButtonDown((int)0);
        this.justClicked = !this.wasMousePressed && currentMousePressed;
        this.wasMousePressed = currentMousePressed;
        this.drawCleanRoundedRect(this.guiX, this.guiY, this.guiX + this.guiWidth, this.guiY + this.guiHeight, 8, BACKGROUND_COLOR);
        this.drawTitleBarBackground();
        this.drawTitleBar(mouseX, mouseY);
        this.drawSidebar(mouseX, mouseY);
        this.drawCategoryTabs(mouseX, mouseY);
        this.drawMainContent(mouseX, mouseY);
        if (this.middleMouseDragging && this.selectedModule != null) {
            this.drawMiddleMouseDragIndicator(mouseX, mouseY);
        }
        if (this.selectedModule != null) {
            this.drawSettingsScrollBar();
        }
        if (this.bindingKey) {
            this.drawKeyBindingOverlay();
        }
        if (!currentMousePressed) {
            this.mouseDown = false;
            this.dragging = false;
        }
        if (!Mouse.isButtonDown((int)2)) {
            this.middleMouseDragging = false;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawTitleBarBackground() {
        this.drawCleanRoundedRectTop(this.guiX, this.guiY, this.guiX + this.guiWidth, this.guiY + 30, 8, TITLEBAR_COLOR);
    }

    private void drawCleanRoundedRectTop(int x1, int y1, int x2, int y2, int radius, Color color) {
        RenderUtils.drawRect((float)(x1 + radius), (float)y1, (float)(x2 - radius), (float)y2, color.getRGB());
        RenderUtils.drawRect((float)x1, (float)(y1 + radius), (float)x2, (float)y2, color.getRGB());
        this.drawCleanCorner(x1, y1, radius, color, 1);
        this.drawCleanCorner(x2 - radius, y1, radius, color, 2);
    }

    private void drawCleanRoundedRect(int x1, int y1, int x2, int y2, int radius, Color color) {
        RenderUtils.drawRect((float)(x1 + radius), (float)y1, (float)(x2 - radius), (float)y2, color.getRGB());
        RenderUtils.drawRect((float)x1, (float)(y1 + radius), (float)x2, (float)(y2 - radius), color.getRGB());
        this.drawCleanCorner(x1, y1, radius, color, 1);
        this.drawCleanCorner(x2 - radius, y1, radius, color, 2);
        this.drawCleanCorner(x2 - radius, y2 - radius, radius, color, 3);
        this.drawCleanCorner(x1, y2 - radius, radius, color, 4);
    }

    private void drawCleanCorner(int x, int y, int radius, Color color, int corner) {
        for (int i = 0; i < radius; ++i) {
            for (int j = 0; j < radius; ++j) {
                double distance = Math.sqrt((radius - i - 1) * (radius - i - 1) + (radius - j - 1) * (radius - j - 1));
                if (!(distance <= (double)(radius - 1))) continue;
                int pixelX = x;
                int pixelY = y;
                switch (corner) {
                    case 1: {
                        pixelX = x + i;
                        pixelY = y + j;
                        break;
                    }
                    case 2: {
                        pixelX = x + radius - 1 - i;
                        pixelY = y + j;
                        break;
                    }
                    case 3: {
                        pixelX = x + radius - 1 - i;
                        pixelY = y + radius - 1 - j;
                        break;
                    }
                    case 4: {
                        pixelX = x + i;
                        pixelY = y + radius - 1 - j;
                    }
                }
                RenderUtils.drawRect((float)pixelX, (float)pixelY, (float)(pixelX + 1), (float)(pixelY + 1), color.getRGB());
            }
        }
    }

    private void drawTitleBar(int mouseX, int mouseY) {
        String title = "CLICKGUI";
        AugustusClickGUI.getEspFontTitle().drawString(title, this.guiX + 15, this.guiY + 12, TITLE_COLOR.getRGB());
    }

    private void drawSidebar(int mouseX, int mouseY) {
        int sidebarX = this.guiX;
        int sidebarY = this.guiY + 30;
        int sidebarWidth = 160;
        int sidebarHeight = this.guiHeight - 30;
        RenderUtils.drawRect((float)(sidebarX + 3), (float)sidebarY, (float)(sidebarX + sidebarWidth), (float)(sidebarY + sidebarHeight - 3), SIDEBAR_COLOR.getRGB());
        int clipX = sidebarX + 3;
        int clipY = sidebarY + 26;
        int clipWidth = sidebarWidth - 3;
        int clipHeight = sidebarHeight - 26 - 3;
        List<Module> modules = this.getFilteredModules();
        int yOffset = clipY + 5 - this.scrollOffset;
        for (Module module : modules) {
            boolean isSelected;
            if (yOffset + 22 < clipY || yOffset > clipY + clipHeight) {
                yOffset += 22;
                continue;
            }
            boolean isHovered = mouseX >= clipX + 5 && mouseX <= clipX + clipWidth - 5 && mouseY >= yOffset && mouseY <= yOffset + 22;
            boolean bl = isSelected = module == this.selectedModule;
            Color moduleTextColor = module.getState() ? ENABLED_COLOR : (isHovered ? new Color(220, 220, 230, 255) : TEXT_COLOR);
            if (yOffset >= clipY && yOffset + 22 <= clipY + clipHeight) {
                this.mc.fontRendererObj.drawString(module.getName(), sidebarX + 15, yOffset + 6, moduleTextColor.getRGB());
            }
            if (isHovered && this.justClicked) {
                long currentTime = System.currentTimeMillis();
                if (module == this.lastClickedModule && currentTime - this.lastClickTime < 300L) {
                    module.toggle();
                    this.lastClickedModule = null;
                    this.lastClickTime = 0L;
                } else {
                    this.selectedModule = module;
                    this.settingsScrollOffset = 0;
                    this.lastClickedModule = module;
                    this.lastClickTime = currentTime;
                }
            }
            yOffset += 22;
        }
    }

    private void drawCategoryTabs(int mouseX, int mouseY) {
        int tabY = this.guiY + 30;
        int categoryTabsX = this.guiX + 160;
        int categoryTabsWidth = this.guiWidth - 160 - 3;
        RenderUtils.drawRect((float)categoryTabsX, (float)tabY, (float)(categoryTabsX + categoryTabsWidth), (float)(tabY + 26), BACKGROUND_COLOR.getRGB());
        ModuleCategory[] categories = new ModuleCategory[]{ModuleCategory.COMBAT, ModuleCategory.MOVEMENT, ModuleCategory.MISC, ModuleCategory.PLAYER, ModuleCategory.RENDER, ModuleCategory.CLIENT, ModuleCategory.WORLD};
        String[] categoryNames = new String[]{"COMBAT", "MOVEMENT", "OTHER", "PLAYER", "RENDER", "CLIENT", "WORLD"};
        int tabsStartX = this.guiX + 160;
        int availableWidth = this.guiWidth - 160;
        int tabCount = categoryNames.length;
        int extraSpacing = 15;
        int adjustedWidth = availableWidth - extraSpacing;
        int tabWidth = adjustedWidth / tabCount;
        for (int i = 0; i < categoryNames.length; ++i) {
            boolean isHovered;
            ModuleCategory category = categories[i];
            String name = categoryNames[i];
            ModuleCategory actualCategory = name.equals("OTHER") ? ModuleCategory.MISC : category;
            int tabX = i <= 1 ? tabsStartX + i * tabWidth : tabsStartX + i * tabWidth + extraSpacing;
            int tabEndX = tabX + tabWidth;
            if (tabEndX > this.guiX + this.guiWidth - 3) {
                tabEndX = this.guiX + this.guiWidth - 3;
            }
            if (tabEndX <= tabX) continue;
            boolean isSelected = actualCategory == this.selectedCategory;
            boolean bl = isHovered = mouseX >= tabX && mouseX <= tabEndX && mouseY >= tabY && mouseY <= tabY + 26;
            Color tabTextColor = isSelected ? ENABLED_COLOR : (isHovered ? new Color(200, 200, 210, 255) : SECONDARY_TEXT);
            int textWidth = this.mc.fontRendererObj.getStringWidth(name);
            int actualTabWidth = tabEndX - tabX;
            int textX = tabX + (actualTabWidth - textWidth) / 2;
            if (name.equals("MOVEMENT")) {
                textX += 30;
            }
            if (textX + textWidth > tabEndX) {
                textX = tabEndX - textWidth - 4;
            }
            if (textX < tabX + 4) {
                textX = tabX + 4;
            }
            this.mc.fontRendererObj.drawString(name, textX, tabY + 6, tabTextColor.getRGB());
            if (isSelected) {
                int underlineY = tabY + 26 - 6;
                int underlineHeight = 2;
                int underlineStartX = textX;
                int underlineEndX = textX + textWidth;
                RenderUtils.drawRect((float)underlineStartX, (float)underlineY, (float)underlineEndX, (float)(underlineY + underlineHeight), ENABLED_COLOR.getRGB());
            }
            if (!isHovered || !this.justClicked) continue;
            this.selectedCategory = actualCategory;
            this.selectedModule = null;
            this.scrollOffset = 0;
            this.settingsScrollOffset = 0;
        }
    }

    private void drawMainContent(int mouseX, int mouseY) {
        int contentX = this.guiX + 160;
        int contentY = this.guiY + 30 + 26;
        int contentWidth = this.guiWidth - 160;
        int contentHeight = this.guiHeight - 30 - 26;
        RenderUtils.drawRect((float)contentX, (float)contentY, (float)(contentX + contentWidth - 3), (float)(contentY + contentHeight - 3), CONTENT_COLOR.getRGB());
        if (this.selectedModule != null) {
            GL11.glEnable((int)3089);
            GL11.glScissor((int)(contentX * 2), (int)((this.height - (contentY + contentHeight - 3)) * 2), (int)((contentWidth - 3) * 2), (int)((contentHeight - 3) * 2));
            this.drawModuleSettings(mouseX, mouseY, contentX + 20, contentY + 20, contentWidth - 40, contentHeight - 40);
            GL11.glDisable((int)3089);
        } else {
            String categoryInfo = "Select a module from " + this.selectedCategory.getDisplayName();
            String subInfo = "Double-click to toggle \u2022 Single-click to configure";
            String scrollInfo = "Hold middle mouse button to drag scroll";
            int textWidth = this.mc.fontRendererObj.getStringWidth(categoryInfo);
            int subTextWidth = this.mc.fontRendererObj.getStringWidth(subInfo);
            int scrollTextWidth = this.mc.fontRendererObj.getStringWidth(scrollInfo);
            this.mc.fontRendererObj.drawString(categoryInfo, contentX + (contentWidth - textWidth) / 2, contentY + 50, SECONDARY_TEXT.getRGB());
            this.mc.fontRendererObj.drawString(subInfo, contentX + (contentWidth - subTextWidth) / 2, contentY + 75, new Color(140, 140, 150, 255).getRGB());
            this.mc.fontRendererObj.drawString(scrollInfo, contentX + (contentWidth - scrollTextWidth) / 2, contentY + 100, new Color(102, 112, 255, 180).getRGB());
        }
    }

    private void drawModuleSettings(int mouseX, int mouseY, int startX, int startY, int width, int height) {
        int yOffset = startY - this.settingsScrollOffset;
        String moduleTitle = this.selectedModule.getName();
        AugustusClickGUI.getEspFontModuleTitle().drawString(moduleTitle, startX, yOffset, ENABLED_COLOR.getRGB());
        String description = this.selectedModule.getDescription();
        if (description != null && !description.isEmpty()) {
            this.mc.fontRendererObj.drawString(description, startX, yOffset + 20, SECONDARY_TEXT.getRGB());
            yOffset += 35;
        } else {
            yOffset += 25;
        }
        String keyName = this.selectedModule.getKeyBind() == 0 ? "NONE" : Keyboard.getKeyName((int)this.selectedModule.getKeyBind());
        boolean keyHovered = mouseX >= startX && mouseX <= startX + 200 && mouseY >= yOffset && mouseY <= yOffset + 20;
        Color keyColor = keyHovered ? ENABLED_COLOR : TEXT_COLOR;
        String keyText = "Keybind: " + keyName;
        this.mc.fontRendererObj.drawString(keyText, startX, yOffset, keyColor.getRGB());
        if (keyHovered && this.justClicked) {
            this.bindingKey = true;
        }
        RenderUtils.drawRect((float)startX, (float)((yOffset += 25) - 3), (float)(startX + width), (float)(yOffset - 1), new Color(60, 60, 70, 120).getRGB());
        List<Value<?>> values2 = this.selectedModule.getValues();
        for (Value<?> value : values2) {
            if (!value.getDisplayable()) continue;
            int oldY = yOffset;
            yOffset = this.drawModernSetting(mouseX, mouseY, startX, yOffset, value, width);
            if (value instanceof ListValue) {
                int actualLinesUsed = Math.max(1, (yOffset - oldY) / 14 + 1);
                yOffset += 18 + (actualLinesUsed - 1) * 3;
                continue;
            }
            if (value instanceof BoolValue) {
                yOffset += 20;
                continue;
            }
            if (value instanceof FloatValue || value instanceof IntegerValue) {
                yOffset += 22;
                continue;
            }
            yOffset += 20;
        }
    }

    private int drawModernSetting(int mouseX, int mouseY, int x, int y, Value<?> value, int maxWidth) {
        String settingName = value.getName();
        if (value instanceof BoolValue) {
            boolean valueHovered;
            BoolValue boolValue = (BoolValue)value;
            boolean currentValue = (Boolean)boolValue.get();
            this.mc.fontRendererObj.drawString(settingName + ":", x, y + 6, TEXT_COLOR.getRGB());
            String valueText = currentValue ? "ON" : "OFF";
            int nameWidth = this.mc.fontRendererObj.getStringWidth(settingName + ": ");
            int valueX = x + nameWidth + 10;
            if (valueX + this.mc.fontRendererObj.getStringWidth(valueText) > x + maxWidth) {
                valueX = x + maxWidth - this.mc.fontRendererObj.getStringWidth(valueText) - 10;
            }
            boolean bl = valueHovered = mouseX >= valueX && mouseX <= valueX + this.mc.fontRendererObj.getStringWidth(valueText) && mouseY >= y && mouseY <= y + 18;
            Color valueColor = valueHovered ? ENABLED_COLOR : (currentValue ? ENABLED_COLOR : DISABLED_COLOR);
            this.mc.fontRendererObj.drawString(valueText, valueX, y + 6, valueColor.getRGB());
            if (valueHovered && this.justClicked) {
                boolValue.set(!currentValue);
            }
            return y;
        }
        if (value instanceof ListValue) {
            int startX;
            ListValue listValue = (ListValue)value;
            this.mc.fontRendererObj.drawString(settingName + ":", x, y + 6, TEXT_COLOR.getRGB());
            String[] allValues = listValue.getValues();
            String currentValue = (String)listValue.get();
            int nameWidth = this.mc.fontRendererObj.getStringWidth(settingName + ": ");
            int currentX = startX = x + nameWidth + 10;
            int currentY = y;
            int lineHeight = 15;
            for (int i = 0; i < allValues.length; ++i) {
                boolean optionHovered;
                int commaWidth;
                String option = allValues[i];
                boolean isSelected = option.equals(currentValue);
                int optionWidth = this.mc.fontRendererObj.getStringWidth(option);
                int n = commaWidth = i < allValues.length - 1 ? this.mc.fontRendererObj.getStringWidth(", ") : 0;
                if (currentX + optionWidth + commaWidth > x + maxWidth - 20 && currentX > startX) {
                    currentY += lineHeight;
                    currentX = startX;
                }
                boolean bl = optionHovered = mouseX >= currentX && mouseX <= currentX + optionWidth && mouseY >= currentY && mouseY <= currentY + 15;
                Color optionColor = isSelected ? ENABLED_COLOR : (optionHovered ? new Color(200, 200, 210, 255) : SECONDARY_TEXT);
                this.mc.fontRendererObj.drawString(option, currentX, currentY + 6, optionColor.getRGB());
                if (optionHovered && this.justClicked) {
                    listValue.set(option);
                }
                currentX += optionWidth;
                if (i >= allValues.length - 1) continue;
                if (currentX + commaWidth > x + maxWidth - 20) {
                    currentY += lineHeight;
                    currentX = startX;
                    continue;
                }
                this.mc.fontRendererObj.drawString(", ", currentX, currentY + 6, SECONDARY_TEXT.getRGB());
                currentX += commaWidth;
            }
            return currentY;
        }
        if (value instanceof FloatValue || value instanceof IntegerValue) {
            if (value instanceof FloatValue) {
                FloatValue floatValue = (FloatValue)value;
                this.mc.fontRendererObj.drawString(settingName + ":", x, y + 4, TEXT_COLOR.getRGB());
                int nameWidth = this.mc.fontRendererObj.getStringWidth(settingName + ": ") + 10;
                int sliderWidth = Math.min(120, maxWidth - nameWidth - 20);
                float newValue = this.drawNormalSlider(((Float)floatValue.get()).floatValue(), floatValue.getMinimum(), floatValue.getMaximum(), x + nameWidth, y + 2, sliderWidth, mouseX, mouseY);
                if (newValue != ((Float)floatValue.get()).floatValue()) {
                    floatValue.set(Float.valueOf(newValue));
                }
                return y;
            }
            IntegerValue intValue = (IntegerValue)value;
            this.mc.fontRendererObj.drawString(settingName + ":", x, y + 4, TEXT_COLOR.getRGB());
            int nameWidth = this.mc.fontRendererObj.getStringWidth(settingName + ": ") + 10;
            int sliderWidth = Math.min(120, maxWidth - nameWidth - 20);
            float newValue = this.drawNormalSlider(((Integer)intValue.get()).intValue(), intValue.getMinimum(), intValue.getMaximum(), x + nameWidth, y + 2, sliderWidth, mouseX, mouseY);
            if ((int)newValue != (Integer)intValue.get()) {
                intValue.set((int)newValue);
            }
            return y;
        }
        return y;
    }

    private float drawNormalSlider(float value, float min, float max, int x, int y, int width, int mouseX, int mouseY) {
        int sliderHeight = 12;
        int sliderWidth = Math.min(120, width);
        Color SLIDER_BG = new Color(45, 45, 50, 255);
        Color SLIDER_BORDER = new Color(200, 200, 200, 255);
        RenderUtils.drawRect((float)(x - 1), (float)(y - 1), (float)(x + sliderWidth + 1), (float)(y + sliderHeight + 1), SLIDER_BORDER.getRGB());
        RenderUtils.drawRect((float)x, (float)y, (float)(x + sliderWidth), (float)(y + sliderHeight), SLIDER_BG.getRGB());
        float percentage = (value - min) / (max - min);
        int progressWidth = (int)((float)sliderWidth * percentage);
        if (progressWidth > 0) {
            RenderUtils.drawRect((float)x, (float)y, (float)(x + progressWidth), (float)(y + sliderHeight), ENABLED_COLOR.getRGB());
        }
        String valueText = String.valueOf((float)Math.round(value * 100.0f) / 100.0f);
        int textWidth = this.mc.fontRendererObj.getStringWidth(valueText);
        int textX = x + (sliderWidth - textWidth) / 2;
        int textY = y + 2;
        this.mc.fontRendererObj.drawString(valueText, textX, textY, TEXT_COLOR.getRGB());
        if (mouseX >= x && mouseX <= x + sliderWidth && mouseY >= y && mouseY <= y + sliderHeight && Mouse.isButtonDown((int)0)) {
            float newPercentage = MathHelper.clamp_float((float)((float)(mouseX - x) / (float)sliderWidth), (float)0.0f, (float)1.0f);
            return min + (max - min) * newPercentage;
        }
        return value;
    }

    private void drawMiddleMouseDragIndicator(int mouseX, int mouseY) {
        int contentX = this.guiX + 160;
        int contentWidth = this.guiWidth - 160;
        if (mouseX >= contentX && mouseX <= contentX + contentWidth) {
            int indicatorX = contentX + 10;
            int indicatorY = mouseY;
            this.drawCircle(indicatorX, indicatorY, 4, new Color(102, 112, 255, 180).getRGB());
        }
    }

    private void drawSettingsScrollBar() {
        int maxScroll = this.calculateMaxSettingsScrollOffset();
        if (maxScroll <= 0) {
            return;
        }
        int contentX = this.guiX + 160;
        int contentY = this.guiY + 30 + 26;
        int contentWidth = this.guiWidth - 160;
        int contentHeight = this.guiHeight - 30 - 26;
        int scrollBarX = contentX + contentWidth - 10;
        int scrollBarY = contentY + 10;
        int scrollBarHeight = contentHeight - 23;
        int scrollBarWidth = 4;
        RenderUtils.drawRect((float)scrollBarX, (float)scrollBarY, (float)(scrollBarX + scrollBarWidth), (float)(scrollBarY + scrollBarHeight), new Color(40, 40, 40, 120).getRGB());
        float scrollPercentage = (float)this.settingsScrollOffset / (float)maxScroll;
        int thumbHeight = Math.max(20, scrollBarHeight / 4);
        int thumbY = scrollBarY + (int)(scrollPercentage * (float)(scrollBarHeight - thumbHeight));
        Color thumbColor = this.middleMouseDragging ? new Color(102, 112, 255, 220) : new Color(80, 80, 90, 150);
        RenderUtils.drawRect((float)scrollBarX, (float)thumbY, (float)(scrollBarX + scrollBarWidth), (float)(thumbY + thumbHeight), thumbColor.getRGB());
    }

    private int calculateMaxSettingsScrollOffset() {
        if (this.selectedModule == null) {
            return 0;
        }
        int totalHeight = 0;
        totalHeight += 25;
        String description = this.selectedModule.getDescription();
        totalHeight = description != null && !description.isEmpty() ? (totalHeight += 35) : (totalHeight += 25);
        totalHeight += 25;
        totalHeight += 10;
        List<Value<?>> values2 = this.selectedModule.getValues();
        for (Value<?> value : values2) {
            if (!value.getDisplayable()) continue;
            if (value instanceof ListValue) {
                ListValue listValue = (ListValue)value;
                String[] allValues = listValue.getValues();
                int currentX = 0;
                int lines = 1;
                int maxWidth = this.guiWidth - 160 - 60;
                for (int i = 0; i < allValues.length; ++i) {
                    int commaWidth;
                    String option = allValues[i];
                    int optionWidth = this.mc.fontRendererObj.getStringWidth(option);
                    int n = commaWidth = i < allValues.length - 1 ? this.mc.fontRendererObj.getStringWidth(", ") : 0;
                    if (currentX + optionWidth + commaWidth > maxWidth && currentX > 0) {
                        ++lines;
                        currentX = optionWidth + commaWidth;
                        continue;
                    }
                    currentX += optionWidth + commaWidth;
                }
                totalHeight += 18 + (lines - 1) * 15 + 18;
                continue;
            }
            if (value instanceof BoolValue) {
                totalHeight += 20;
                continue;
            }
            if (value instanceof FloatValue || value instanceof IntegerValue) {
                totalHeight += 22;
                continue;
            }
            totalHeight += 20;
        }
        int displayHeight = this.guiHeight - 30 - 26 - 40;
        return Math.max(0, totalHeight - displayHeight);
    }

    private void drawCircle(int x, int y, int radius, int color) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)6);
        GL11.glVertex2f((float)x, (float)y);
        for (int i = 0; i <= 20; ++i) {
            double angle = Math.PI * 2 * (double)i / 20.0;
            GL11.glVertex2f((float)((float)((double)x + Math.cos(angle) * (double)radius)), (float)((float)((double)y + Math.sin(angle) * (double)radius)));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    private List<Module> getFilteredModules() {
        ArrayList<Module> modules = new ArrayList<Module>();
        for (Module module : FDPClient.moduleManager.getModules()) {
            if (module.getCategory() != this.selectedCategory) continue;
            modules.add(module);
        }
        return modules;
    }

    private void drawKeyBindingOverlay() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtils.drawRect(0.0f, 0.0f, (float)sr.getScaledWidth(), (float)sr.getScaledHeight(), new Color(0, 0, 0, 180).getRGB());
        String message = "Press a key to bind to " + this.selectedModule.getName() + " or ESC to cancel";
        int textWidth = this.mc.fontRendererObj.getStringWidth(message);
        int x = (sr.getScaledWidth() - textWidth) / 2;
        int y = sr.getScaledHeight() / 2;
        this.drawCleanRoundedRect(x - 40, y - 30, x + textWidth + 40, y + 40, 8, new Color(30, 30, 40, 250));
        this.mc.fontRendererObj.drawString(message, x, y, TEXT_COLOR.getRGB());
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.bindingKey) {
            if (keyCode == 1) {
                this.bindingKey = false;
            } else {
                this.selectedModule.setKeyBind(keyCode);
                this.bindingKey = false;
            }
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 2 && this.selectedModule != null) {
            int contentX = this.guiX + 160;
            int contentWidth = this.guiWidth - 160;
            if (mouseX >= contentX && mouseX <= contentX + contentWidth) {
                this.middleMouseDragging = true;
                this.middleDragStartY = mouseY;
                this.middleDragStartScrollOffset = this.settingsScrollOffset;
                this.lastMiddleMouseY = mouseY;
            }
            return;
        }
        if (mouseButton == 0 && mouseX >= this.guiX && mouseX <= this.guiX + this.guiWidth && mouseY >= this.guiY && mouseY <= this.guiY + 30) {
            this.dragging = true;
            this.dragOffsetX = mouseX - this.guiX;
            this.dragOffsetY = mouseY - this.guiY;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 2) {
            this.middleMouseDragging = false;
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (this.middleMouseDragging && this.selectedModule != null) {
            int deltaY = mouseY - this.lastMiddleMouseY;
            this.settingsScrollOffset -= deltaY * 2;
            int maxScrollOffset = this.calculateMaxSettingsScrollOffset();
            this.settingsScrollOffset = Math.max(0, Math.min(this.settingsScrollOffset, maxScrollOffset));
            this.lastMiddleMouseY = mouseY;
        }
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            if (mouseX <= this.guiX + 160) {
                this.scrollOffset -= wheel / 120 * 20;
                this.scrollOffset = Math.max(0, this.scrollOffset);
            } else if (this.selectedModule != null) {
                this.settingsScrollOffset -= wheel / 120 * 20;
                this.settingsScrollOffset = Math.max(0, this.settingsScrollOffset);
            }
        }
    }

    public boolean func_73868_f() {
        return false;
    }
}

