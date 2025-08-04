/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.client;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.client.Interface;
import net.ccbluex.liquidbounce.features.module.modules.combat.InfiniteAura;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.value.BoolValue;
import net.ccbluex.liquidbounce.features.value.ListValue;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MouseUtils;
import net.ccbluex.liquidbounce.utils.extensions.EntityExtensionKt;
import net.ccbluex.liquidbounce.utils.render.BlendUtils;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="TargetHUD", category=ModuleCategory.CLIENT)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010>\u001a\u00020?H\u0002J\b\u0010@\u001a\u00020?H\u0002J\b\u0010A\u001a\u00020?H\u0002J\u0010\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020CH\u0002J\u0010\u0010E\u001a\u00020?2\u0006\u0010F\u001a\u00020GH\u0007J\u0010\u0010H\u001a\u00020?2\u0006\u0010F\u001a\u00020IH\u0007J\u0010\u0010J\u001a\u00020?2\u0006\u0010F\u001a\u00020KH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\t\"\u0004\b\u000e\u0010\u000bR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0016\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u001f\u001a\u00020 \u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u000e\u0010#\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010$\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\t\"\u0004\b&\u0010\u000bR\u000e\u0010'\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u001a\u0010(\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\t\"\u0004\b*\u0010\u000bR\u000e\u0010+\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0016\u0010,\u001a\u0004\u0018\u00010-8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b.\u0010/R\u000e\u00100\u001a\u000201X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u00102\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\t\"\u0004\b4\u0010\u000bR\u001a\u00105\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\t\"\u0004\b7\u0010\u000bR\u001a\u00108\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b9\u0010\t\"\u0004\b:\u0010\u000bR\u001a\u0010;\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b<\u0010\t\"\u0004\b=\u0010\u000b\u00a8\u0006L"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/client/TargetHUD;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "animProgress", "", "attackTarget", "Lnet/minecraft/entity/EntityLivingBase;", "dragOffsetX", "getDragOffsetX", "()F", "setDragOffsetX", "(F)V", "dragOffsetY", "getDragOffsetY", "setDragOffsetY", "draging", "", "getDraging", "()Z", "setDraging", "(Z)V", "easingHealth", "followTarget", "Lnet/ccbluex/liquidbounce/features/value/BoolValue;", "getFollowTarget", "()Lnet/ccbluex/liquidbounce/features/value/BoolValue;", "lastX", "", "lastY", "lastZ", "mainTarget", "mode", "Lnet/ccbluex/liquidbounce/features/value/ListValue;", "getMode", "()Lnet/ccbluex/liquidbounce/features/value/ListValue;", "outlineProgress", "posX", "getPosX", "setPosX", "posX2", "posY", "getPosY", "setPosY", "posY2", "tag", "", "getTag", "()Ljava/lang/String;", "targetTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "ux2_size", "getUx2_size", "setUx2_size", "ux_size", "getUx_size", "setUx_size", "uy2_size", "getUy2_size", "setUy2_size", "uy_size", "getUy_size", "setUy_size", "drawCrossSine", "", "drawRavenB4", "drawSimple", "fadeAlpha", "", "alpha", "onAttack", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onRender2D", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "CrossSine"})
public final class TargetHUD
extends Module {
    @NotNull
    public static final TargetHUD INSTANCE = new TargetHUD();
    @NotNull
    private static final ListValue mode;
    @NotNull
    private static final BoolValue followTarget;
    @Nullable
    private static EntityLivingBase attackTarget;
    @NotNull
    private static final MSTimer targetTimer;
    @Nullable
    private static EntityLivingBase mainTarget;
    private static float animProgress;
    private static float easingHealth;
    private static boolean draging;
    private static float posX;
    private static float posY;
    private static final float posY2;
    private static final float posX2;
    private static float dragOffsetX;
    private static float dragOffsetY;
    private static float ux_size;
    private static float uy_size;
    private static float ux2_size;
    private static float uy2_size;
    private static double lastX;
    private static double lastY;
    private static double lastZ;
    private static float outlineProgress;

    public TargetHUD() {
        super("TargetHUD", "Shows target info on screen", ModuleCategory.CLIENT);
    }

    @NotNull
    public final ListValue getMode() {
        return mode;
    }

    @NotNull
    public final BoolValue getFollowTarget() {
        return followTarget;
    }

    public final boolean getDraging() {
        return draging;
    }

    public final void setDraging(boolean bl) {
        draging = bl;
    }

    public final float getPosX() {
        return posX;
    }

    public final void setPosX(float f) {
        posX = f;
    }

    public final float getPosY() {
        return posY;
    }

    public final void setPosY(float f) {
        posY = f;
    }

    public final float getDragOffsetX() {
        return dragOffsetX;
    }

    public final void setDragOffsetX(float f) {
        dragOffsetX = f;
    }

    public final float getDragOffsetY() {
        return dragOffsetY;
    }

    public final void setDragOffsetY(float f) {
        dragOffsetY = f;
    }

    public final float getUx_size() {
        return ux_size;
    }

    public final void setUx_size(float f) {
        ux_size = f;
    }

    public final float getUy_size() {
        return uy_size;
    }

    public final void setUy_size(float f) {
        uy_size = f;
    }

    public final float getUx2_size() {
        return ux2_size;
    }

    public final void setUx2_size(float f) {
        ux2_size = f;
    }

    public final float getUy2_size() {
        return uy2_size;
    }

    public final void setUy2_size(float f) {
        uy2_size = f;
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (!Intrinsics.areEqual(attackTarget, event.getTargetEntity()) && EntityUtils.INSTANCE.isSelected(event.getTargetEntity(), true)) {
            attackTarget = (EntityLivingBase)event.getTargetEntity();
        }
        targetTimer.reset();
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        int mouseY;
        block23: {
            float f;
            Intrinsics.checkNotNullParameter(event, "event");
            EntityLivingBase actualTarget = InfiniteAura.INSTANCE.getLastTarget() != null? InfiniteAura.INSTANCE.getLastTarget() : (MinecraftInstance.mc.currentScreen instanceof GuiChat? (EntityLivingBase) MinecraftInstance.mc.thePlayer : (KillAura.INSTANCE.getState() && KillAura.INSTANCE.getCurrentTarget() != null? KillAura.INSTANCE.getCurrentTarget() : null));           
            animProgress += 0.00375f * (float)RenderUtils.deltaTime * (actualTarget != null ? -1.0f : 1.0f);
            animProgress = RangesKt.coerceIn(animProgress, 0.0f, 1.0f);
            outlineProgress += 0.00375f * (float)RenderUtils.deltaTime * (MinecraftInstance.mc.currentScreen instanceof GuiChat && MouseUtils.mouseWithinBounds(Mouse.getX() * MinecraftInstance.mc.currentScreen.width / MinecraftInstance.mc.displayWidth, MinecraftInstance.mc.currentScreen.height - Mouse.getY() * MinecraftInstance.mc.currentScreen.height / MinecraftInstance.mc.displayHeight - 1, ux_size, uy_size, ux2_size, uy2_size) ? 1.0f : -1.0f);
            outlineProgress = RangesKt.coerceIn(outlineProgress, 0.0f, 1.0f);
            if (actualTarget != null) {
                mainTarget = actualTarget;
            } else if (animProgress >= 1.0f) {
                mainTarget = null;
            }
            if (mainTarget == null) {
                easingHealth = 0.0f;
            }
            if (targetTimer.hasTimePassed(500L)) {
                attackTarget = null;
            }
            if (mainTarget != null) {
                EntityLivingBase entityLivingBase = mainTarget;
                Intrinsics.checkNotNull(entityLivingBase);
                f = (entityLivingBase.getMaxHealth() - easingHealth) / (float)Math.pow(2.0f, 7.0f) * (float)RenderUtils.deltaTime;
            } else {
                f = 0.0f;
            }
            easingHealth += f;
            double percent = EaseUtils.INSTANCE.easeInBack(animProgress);
            double tranXZ = (double)((ux2_size + posX) / 2.0f) * percent;
            double tranY = (double)((uy2_size + posY) / 2.0f) * percent;
            if (((Boolean)followTarget.get()).booleanValue() && !Intrinsics.areEqual(mainTarget, MinecraftInstance.mc.thePlayer)) break block23;
            GL11.glPushMatrix();
            if (!mode.equals("RavenB4")) {
                GL11.glTranslated((double)tranXZ, (double)tranY, (double)0.0);
                GL11.glScaled((double)(1.0 - percent), (double)(1.0 - percent), (double)(1.0 - percent));
            }
            String string = ((String)mode.get()).toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase(Locale.ROOT)");
            switch (string) {
                case "crosssine": {
                    this.drawCrossSine();
                    break;
                }
                case "ravenb4": {
                    this.drawRavenB4();
                    break;
                }
                case "simple": {
                    this.drawSimple();
                }
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
        }
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderUtils.drawRoundedOutline(ux_size, uy_size, ux2_size, uy2_size, 7.0f, 2.5f, new Color(255, 255, 255, (int)((float)255 * outlineProgress)).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        int mouseX = MinecraftInstance.mc.currentScreen == null ? 0 : Mouse.getX() * MinecraftInstance.mc.currentScreen.width / MinecraftInstance.mc.displayWidth;
        int n = mouseY = MinecraftInstance.mc.currentScreen == null ? 0 : MinecraftInstance.mc.currentScreen.height - Mouse.getY() * MinecraftInstance.mc.currentScreen.height / MinecraftInstance.mc.displayHeight - 1;
        if (draging) {
            posX = (float)mouseX - dragOffsetX;
            posY = (float)mouseY - dragOffsetY;
            if (!Mouse.isButtonDown((int)0) || MinecraftInstance.mc.currentScreen == null) {
                draging = false;
            }
        }
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (!((Boolean)followTarget.get()).booleanValue() || Intrinsics.areEqual(mainTarget, MinecraftInstance.mc.thePlayer) || mainTarget == null) {
            return;
        }
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.thePlayer;
        EntityLivingBase entityLivingBase = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase);
        float distance2 = entityPlayerSP.getDistanceToEntity((Entity)entityLivingBase) / 4.0f;
        if (distance2 < 1.0f) {
            distance2 = 1.0f;
        }
        float scale = distance2 / 150.0f;
        EntityLivingBase entityLivingBase2 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase2);
        double d = entityLivingBase2.lastTickPosX;
        EntityLivingBase entityLivingBase3 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase3);
        double d2 = entityLivingBase3.posX;
        EntityLivingBase entityLivingBase4 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase4);
        lastX += (d + (d2 - entityLivingBase4.lastTickPosX) * (double)MinecraftInstance.mc.timer.renderPartialTicks - MinecraftInstance.mc.getRenderManager().renderPosX - lastX) / (double)2;
        EntityLivingBase entityLivingBase5 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase5);
        double d3 = entityLivingBase5.lastTickPosY;
        EntityLivingBase entityLivingBase6 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase6);
        double d4 = entityLivingBase6.posY;
        EntityLivingBase entityLivingBase7 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase7);
        double d5 = d3 + (d4 - entityLivingBase7.lastTickPosY) * (double)MinecraftInstance.mc.timer.renderPartialTicks - MinecraftInstance.mc.getRenderManager().renderPosY;
        EntityLivingBase entityLivingBase8 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase8);
        lastY += (d5 + (double)entityLivingBase8.getEyeHeight() - lastY) / (double)2;
        EntityLivingBase entityLivingBase9 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase9);
        double d6 = entityLivingBase9.lastTickPosZ;
        EntityLivingBase entityLivingBase10 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase10);
        double d7 = entityLivingBase10.posZ;
        EntityLivingBase entityLivingBase11 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase11);
        lastZ += (d6 + (d7 - entityLivingBase11.lastTickPosZ) * (double)MinecraftInstance.mc.	timer.renderPartialTicks - MinecraftInstance.mc.getRenderManager().renderPosZ - lastZ) / (double)2;
        GlStateManager.pushMatrix();
        GL11.glTranslated((double)lastX, (double)lastY, (double)lastZ);
        GL11.glRotatef((float)(-MinecraftInstance.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)MinecraftInstance.mc.getRenderManager().playerViewX, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glScalef((float)(-scale * (float)2), (float)(-scale * (float)2), (float)(scale * (float)2));
        Object object = new int[]{2896, 2929};
        RenderUtils.disableGlCap((int[]) object);
        RenderUtils.enableGlCap(3042);
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.pushMatrix();
        String string = ((String)mode.get()).toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase(Locale.ROOT)");
        switch (string) {
            case "crosssine": {
                this.drawCrossSine();
                break;
            }
            case "ravenb4": {
                this.drawRavenB4();
                break;
            }
            case "simple": {
                this.drawSimple();
            }
        }
        GlStateManager.popMatrix();
        RenderUtils.resetCaps();
        GlStateManager.disableRescaleNormal();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private final void drawCrossSine() {
        if (mainTarget == null) {
            return;
        }
        if (((Boolean)Interface.INSTANCE.getShaderValue().get()).booleanValue()) {
            BlurUtils.blurAreaRounded(ux_size, uy_size, ux2_size, uy2_size, 2.0f, 10.0f);
        }
        GameFontRenderer fonts = Fonts.font35;
        GameFontRenderer fonts2 = Fonts.fontTiny;
        DecimalFormat decimalFormat3 = new DecimalFormat("0.#", new DecimalFormatSymbols(Locale.ENGLISH));
        StringBuilder stringBuilder = new StringBuilder();
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.thePlayer;
        EntityLivingBase entityLivingBase = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase);
        String string = stringBuilder.append((Object)decimalFormat3.format(Float.valueOf(entityPlayerSP.getDistanceToEntity((Entity)entityLivingBase)))).append("m - ").append(MinecraftInstance.mc.thePlayer.getMaxHealth() > easingHealth ? Intrinsics.stringPlus("+", decimalFormat3.format(Float.valueOf(MinecraftInstance.mc.thePlayer.getMaxHealth() - easingHealth))) : Intrinsics.stringPlus("-", decimalFormat3.format(Float.valueOf(easingHealth - MinecraftInstance.mc.thePlayer.getMaxHealth())))).toString();
        EntityLivingBase entityLivingBase2 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase2);
        String string2 = entityLivingBase2.getName();
        Intrinsics.checkNotNullExpressionValue(string2, "mainTarget!!.name");
        float width = (float)fonts.getStringWidth(string2) + 30.0f;
        RenderUtils.drawBloomRoundedRect(-2.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), -2.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 6.0f + width + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 33.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 2.0f, 2.0f, 6.0f, new Color(0, 0, 0, this.fadeAlpha(180)), RenderUtils.ShaderBloom.BLOOMONLY);
        GlStateManager.enableBlend();
        EntityLivingBase entityLivingBase3 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase3);
        RenderUtils.drawHead(EntityExtensionKt.getSkin(entityLivingBase3), 2 + (int)((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 2 + (int)((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 23, 23, new Color(255, 255, 255, this.fadeAlpha(255)).getRGB());
        EntityLivingBase entityLivingBase4 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase4);
        string2 = entityLivingBase4.getName();
        Intrinsics.checkNotNullExpressionValue(string2, "mainTarget!!.name");
        fonts.drawString(string2, 28.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 11.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), new Color(150, 150, 150, this.fadeAlpha(255)).getRGB());
        fonts2.drawString(string, width - (float)fonts2.getStringWidth(string) + 2.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 23.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), new Color(150, 150, 150, this.fadeAlpha(255)).getRGB());
        RenderUtils.drawRoundedRect(2.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 27.5f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 2.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX) + width, 27.5f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY) + 2.0f, 1.0f, new Color(0, 0, 0, this.fadeAlpha(180)).getRGB());
        float f = 2.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX);
        float f2 = 27.5f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY);
        float f3 = 2.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX);
        EntityLivingBase entityLivingBase5 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase5);
        RenderUtils.drawGradientRoundedRect((int) f, (int) f2, (int) (f3 + width * easingHealth / entityLivingBase5.getMaxHealth()), (int) (27.5f + ((Boolean) followTarget.get() && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY) + 2.0f), 1, new Color(255, 0, 0, this.fadeAlpha(255)).getRGB(), new Color(0, 255, 0, this.fadeAlpha(255)).getRGB());
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        ux_size = (Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX;
        EntityLivingBase entityLivingBase6 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase6);
        string2 = entityLivingBase6.getName();
        Intrinsics.checkNotNullExpressionValue(string2, "mainTarget!!.name");
        ux2_size = (float)fonts.getStringWidth(string2) + 30.0f + ux_size;
        uy_size = (Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY;
        uy2_size = 31.0f + uy_size;
    }

    private final void drawSimple() {
        if (mainTarget == null) {
            return;
        }
        if (((Boolean)Interface.INSTANCE.getShaderValue().get()).booleanValue()) {
            BlurUtils.blurAreaRounded(ux_size, uy_size, ux2_size, uy2_size, 2.0f, 10.0f);
        }
        FontRenderer fonts = Fonts.minecraftFont;
        EntityLivingBase entityLivingBase = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase);
        int leagth = fonts.getStringWidth(entityLivingBase.getName());
        RenderUtils.drawRoundedRect((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX, (Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY, ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX) + 40.0f + (float)leagth, ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY) + 24.0f, 2.0f, new Color(0, 0, 0, this.fadeAlpha(180)).getRGB());
        float f = 28.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX);
        float f2 = 20.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY);
        float f3 = (float)leagth + 6.0f;
        EntityLivingBase entityLivingBase2 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase2);
        float f4 = 28.0f + f3 * (easingHealth / entityLivingBase2.getMaxHealth()) + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX);
        float f5 = 21.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY);
        EntityLivingBase entityLivingBase3 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase3);
        float f6 = entityLivingBase3.getHealth();
        EntityLivingBase entityLivingBase4 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase4);
        Color color = BlendUtils.getHealthColor(f6, entityLivingBase4.getMaxHealth());
        Intrinsics.checkNotNullExpressionValue(color, "getHealthColor(mainTarge\u2026, mainTarget!!.maxHealth)");
        RenderUtils.drawRect(f, f2, f4, f5, ColorUtils.reAlpha(color, this.fadeAlpha(255)).getRGB());
        GlStateManager.enableBlend();
        EntityLivingBase entityLivingBase5 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase5);
        fonts.drawString(entityLivingBase5.getName(), 31.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 5.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), new Color(255, 255, 255, this.fadeAlpha(255)).getRGB(), true);
        EntityLivingBase entityLivingBase6 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase6);
        RenderUtils.drawHead(EntityExtensionKt.getSkin(entityLivingBase6), 2 + (int)((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 2 + (int)((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 20, 20, new Color(255, 255, 255, this.fadeAlpha(255)).getRGB());
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        ux_size = (Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX;
        EntityLivingBase entityLivingBase7 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase7);
        ux2_size = 40.0f + (float)Fonts.minecraftFont.getStringWidth(entityLivingBase7.getName()) + ux_size;
        uy_size = (Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY;
        uy2_size = 24.0f + uy_size;
    }

    private final void drawRavenB4() {
        if (mainTarget == null) {
            return;
        }
        DecimalFormat decimalFormat2 = new DecimalFormat("##0.0", new DecimalFormatSymbols(Locale.ENGLISH));
        FontRenderer font = Fonts.minecraftFont;
        EntityLivingBase entityLivingBase = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase);
        String hp = decimalFormat2.format(Float.valueOf(entityLivingBase.getHealth()));
        EntityLivingBase entityLivingBase2 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase2);
        int hplength = font.getStringWidth(decimalFormat2.format(Float.valueOf(entityLivingBase2.getHealth())));
        EntityLivingBase entityLivingBase3 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase3);
        int length = font.getStringWidth(entityLivingBase3.getDisplayName().getUnformattedText());
        if (((Boolean)Interface.INSTANCE.getShaderValue().get()).booleanValue()) {
            BlurUtils.blurAreaRounded(ux_size, uy_size, ux2_size, uy2_size, 4.0f, 10.0f);
        }
        GlStateManager.pushMatrix();
        RenderUtils.drawRoundedGradientOutlineCorner((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX, (Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY, (float)(length + hplength) + 23.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 35.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 2.0f, 8.0f, new Color(255, 0, 0, fadeAlpha(255)).getRGB(), new Color(255, 255, 0, fadeAlpha(255)).getRGB(), new Color(0, 255, 0, fadeAlpha(255)).getRGB(), new Color(0, 255, 255, fadeAlpha(255)).getRGB());
        RenderUtils.drawRoundedRect((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX, (Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY, (float)(length + hplength) + 23.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 35.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 4.0f, new Color(0, 0, 0, this.fadeAlpha(100)).getRGB());
        GlStateManager.enableBlend();
        EntityLivingBase entityLivingBase4 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase4);
        font.drawString(entityLivingBase4.getDisplayName().getUnformattedText(), (int)(6.0f + (((Boolean) followTarget.get() && !(MinecraftInstance.mc.currentScreen instanceof GuiChat)) ? posX2 : posX)), (int)(8.0f + (((Boolean) followTarget.get() && !(MinecraftInstance.mc.currentScreen instanceof GuiChat)) ? posY2 : posY)), new Color(255, 255, 255, this.fadeAlpha(255)).getRGB());
        EntityLivingBase entityLivingBase5 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase5);
        String string = entityLivingBase5.getHealth() > MinecraftInstance.mc.thePlayer.getHealth() ? "L" : "W";
        float f = (float)(length + hplength) + 11.6f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX);
        float f2 = 8.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY);
        EntityLivingBase entityLivingBase6 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase6);
        font.drawString(string, (int)f, (int)f2, entityLivingBase6.getHealth() > MinecraftInstance.mc.thePlayer.getHealth() ? new Color(255, 0, 0, this.fadeAlpha(255)).getRGB() : new Color(0, 255, 0, this.fadeAlpha(255)).getRGB());
        float f3 = (float)length + 8.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX);
        float f4 = 8.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY);
        EntityLivingBase entityLivingBase7 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase7);
        float f5 = entityLivingBase7.getHealth();
        EntityLivingBase entityLivingBase8 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase8);
        Color color = BlendUtils.getHealthColor(f5, entityLivingBase8.getMaxHealth());
        Intrinsics.checkNotNullExpressionValue(color, "getHealthColor(mainTarge\u2026, mainTarget!!.maxHealth)");
        font.drawString(hp, (int)f3, (int)f4, ColorUtils.reAlpha(color, this.fadeAlpha(255)).getRGB());
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        RenderUtils.drawRoundedRect(5.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 29.55f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), (float)(length + hplength) + 18.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 25.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 2.0f, new Color(0, 0, 0, this.fadeAlpha(110)).getRGB());
        RenderUtils.drawRoundedGradientRectCorner(5.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 25.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 8.0f + easingHealth / (float)20 * ((float)(length + hplength) + 10.0f) + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 29.5f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 4.0f, new Color(0, 255, 0, fadeAlpha(100)).getRGB(), new Color(0, 255, 0, fadeAlpha(100)).getRGB(), new Color(255, 255, 0, fadeAlpha(100)).getRGB(), new Color(255, 255, 0, fadeAlpha(100)).getRGB());
        float f6 = 5.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX);
        float f7 = 25.0f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY);
        EntityLivingBase entityLivingBase9 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase9);
        RenderUtils.drawRoundedGradientRectCorner(f6, f7, 8.0f + entityLivingBase9.getHealth() / (float)20 * ((float)(length + hplength) + 10.0f) + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX), 29.5f + ((Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY), 4.0f, new Color(0, 120, 255, fadeAlpha(255)).getRGB(), new Color(0, 120, 255, fadeAlpha(255)).getRGB(), new Color(150, 0, 255, fadeAlpha(255)).getRGB(), new Color(150, 0, 255, fadeAlpha(255)).getRGB());
        GlStateManager.popMatrix();
        ux_size = (Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posX2 : posX;
        FontRenderer fontRenderer = MinecraftInstance.mc.fontRendererObj;
        EntityLivingBase entityLivingBase10 = mainTarget;
        Intrinsics.checkNotNull(entityLivingBase10);
        ux2_size = 40.0f + (float)fontRenderer.getStringWidth(entityLivingBase10.getDisplayName().getFormattedText()) + ux_size;
        uy_size = (Boolean)followTarget.get() != false && !(MinecraftInstance.mc.currentScreen instanceof GuiChat) ? posY2 : posY;
        uy2_size = 35.0f + uy_size;
    }

    private final int fadeAlpha(int alpha) {
        return alpha - (int)(animProgress * (float)alpha);
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)mode.get();
    }

    static {
        String[] stringArray = new String[]{"CrossSine", "Simple", "RavenB4"};
        mode = new ListValue("Mode", stringArray, "CrossSine");
        followTarget = new BoolValue("FollowTarget", false);
        targetTimer = new MSTimer();
        posX = (float)new ScaledResolution(MinecraftInstance.mc).getScaledWidth() / 2.0f + 40.0f;
        posY = (float)new ScaledResolution(MinecraftInstance.mc).getScaledHeight() / 2.0f;
        posY2 = -40.0f;
        posX2 = 40.0f;
    }
}

