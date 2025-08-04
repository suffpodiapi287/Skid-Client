/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemBlock
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.client;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.modules.client.TargetHUD;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.features.module.modules.exploit.Disabler;
import net.ccbluex.liquidbounce.features.module.modules.combat.InfiniteAura;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.extensions.AnimHelperKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.ServerUtils;
import net.ccbluex.liquidbounce.utils.SlotUtils;
import net.ccbluex.liquidbounce.utils.animation.Animation;
import net.ccbluex.liquidbounce.utils.SlotUtils;
import net.ccbluex.liquidbounce.utils.animation.Easing;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.extensions.EntityExtensionKt;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.Stencil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"J(\u0010#\u001a\u00020 2\u0006\u0010\u001c\u001a\u00020\f2\u0006\u0010$\u001a\u00020\f2\u0006\u0010%\u001a\u00020\u00142\u0006\u0010&\u001a\u00020'H\u0002J2\u0010(\u001a\u00020 2\u0006\u0010)\u001a\u00020\f2\u0006\u0010*\u001a\u00020\f2\u0006\u0010+\u001a\u00020\f2\u0006\u0010,\u001a\u00020\f2\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0011\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\f0\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006-"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/client/impl/DynamicIsland;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "alpha", "", "animAlpha", "Lnet/ccbluex/liquidbounce/utils/animation/Animation;", "animPosX", "animPosX2", "animPosY", "animPosY2", "disabler", "", "getDisabler", "()Ljava/lang/Float;", "setDisabler", "(Ljava/lang/Float;)V", "Ljava/lang/Float;", "healthMap", "", "Lnet/minecraft/entity/EntityLivingBase;", "mainTarget", "getMainTarget", "()Lnet/minecraft/entity/EntityLivingBase;", "setMainTarget", "(Lnet/minecraft/entity/EntityLivingBase;)V", "posX", "posX2", "posY", "posY2", "prevBlock", "draw", "", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "drawTH", "stringWidth", "target", "string", "", "set", "startX", "startY", "endX", "endY", "CrossSine"})
public final class DynamicIsland
extends MinecraftInstance {
    @NotNull
    public static final DynamicIsland INSTANCE = new DynamicIsland();
    @Nullable
    private static Float disabler;
    @Nullable
    private static EntityLivingBase mainTarget;
    private static float posX;
    private static float posY;
    private static float posX2;
    private static float posY2;
    private static int alpha;
    @NotNull
    private static final Animation animPosX;
    @NotNull
    private static final Animation animPosY;
    @NotNull
    private static final Animation animPosX2;
    @NotNull
    private static final Animation animPosY2;
    @NotNull
    private static final Animation animAlpha;
    @NotNull
    private static final Map<EntityLivingBase, Float> healthMap;
    private static float prevBlock;

    private DynamicIsland() {
    }

    @Nullable
    public final Float getDisabler() {
        return disabler;
    }

    public final void setDisabler(@Nullable Float f) {
        disabler = f;
    }

    @Nullable
    public final EntityLivingBase getMainTarget() {
        return mainTarget;
    }

    public final void setMainTarget(@Nullable EntityLivingBase entityLivingBase) {
        mainTarget = entityLivingBase;
    }

    public final void draw(@NotNull Render2DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        animPosX.run(posX);
        animPosY.run(posY);
        animPosX2.run(posX2);
        animPosY2.run(posY2);
        animAlpha.run(alpha);
        RenderUtils.drawBloomRoundedRect((float)DynamicIsland.animPosX.value, (float)DynamicIsland.animPosY.value, (float)DynamicIsland.animPosX2.value, (float)DynamicIsland.animPosY2.value, 12.0f, 8.0f, 3.0f, new Color(10, 10, 10, (int)DynamicIsland.animAlpha.value), RenderUtils.ShaderBloom.BOTH);
        GL11.glPopAttrib();
        GL11.glPushMatrix();
        Stencil.write(false);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils.fastRoundedRect((float)DynamicIsland.animPosX.value, (float)DynamicIsland.animPosY.value, (float)DynamicIsland.animPosX2.value, (float)DynamicIsland.animPosY2.value, 0.0f);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        Stencil.erase(true);
        String string = "";
        int width = event.getScaledResolution().getScaledWidth();
        float halfWidth = (float)width / 2.0f;
        mainTarget = MinecraftInstance.mc.currentScreen instanceof GuiChat ? (EntityLivingBase)MinecraftInstance.mc.thePlayer : (InfiniteAura.INSTANCE.getState() && InfiniteAura.INSTANCE.getLastTarget() != null ? InfiniteAura.INSTANCE.getLastTarget() : (Interface.INSTANCE.getAttackTarget() != null ? Interface.INSTANCE.getAttackTarget() : null));
        float stringWidth = 0.0f;
        if (!TargetHUD.INSTANCE.getState() && mainTarget != null) {
            EntityLivingBase entityLivingBase = mainTarget;
            Intrinsics.checkNotNull(entityLivingBase);
            String string2 = entityLivingBase.getName();
            Intrinsics.checkNotNullExpressionValue(string2, "mainTarget!!.name");
            string = string2;
            float posY = 0.0f;
            if (KillAura.INSTANCE.getState() && !(MinecraftInstance.mc.currentScreen instanceof GuiChat)) {
                for (Entity entity : MinecraftInstance.mc.theWorld.loadedEntityList) {
                    Object object;
                    Intrinsics.checkNotNullExpressionValue(entity, "entity");
                    if (!EntityUtils.INSTANCE.isSelected(entity, true)) continue;
                    if (KillAura.INSTANCE.getState()) {
                        object = MinecraftInstance.mc.thePlayer;
                        Intrinsics.checkNotNullExpressionValue(object, "mc.thePlayer");
                        if (EntityExtensionKt.getDistanceToEntityBox((Entity)object, entity) <= (double)((Number)KillAura.INSTANCE.getDiscoverRangeValue().get()).floatValue()) {
                            object = entity.getName();
                            Intrinsics.checkNotNullExpressionValue(object, "entity.name");
                            if ((float)Fonts.font40SemiBold.getStringWidth((String)object) > stringWidth) {
                                object = entity.getName();
                                Intrinsics.checkNotNullExpressionValue(object, "entity.name");
                                stringWidth = Fonts.font40SemiBold.getStringWidth((String)object);
                            }
                            EntityLivingBase entityLivingBase2 = (EntityLivingBase)entity;
                            object = ((EntityLivingBase)entity).getName();
                            Intrinsics.checkNotNullExpressionValue(object, "entity.name");
                            this.drawTH(posY, stringWidth, entityLivingBase2, (String)object);
                            posY += 30.0f;
                        }
                    }
                    EntityLivingBase entityLivingBase3 = (EntityLivingBase)entity;
                    object = ((EntityLivingBase)entity).getName();
                    Intrinsics.checkNotNullExpressionValue(object, "entity.name");
                    this.drawTH(posY, stringWidth, entityLivingBase3, (String)object);
                    posY += 30.0f;
                
                }  
            } else {
                float f = Fonts.font40SemiBold.getStringWidth(string);
                EntityLivingBase entityLivingBase4 = mainTarget;
                Intrinsics.checkNotNull(entityLivingBase4);
                this.drawTH(0.0f, f, entityLivingBase4, string);
            }
            
            Fonts.font40SemiBold.drawCenteredString(string, halfWidth, (float)DynamicIsland.animPosY.value + 9.0f, new Color(255, 255, 255, 255).getRGB());
            Fonts.font35SemiBold.drawCenteredString(string2, halfWidth, (float)DynamicIsland.animPosY.value + 9.0f + (float)Fonts.font40SemiBold.getHeight() + 2.0f, new Color(255, 255, 255, 255).getRGB());
            RenderUtils.drawRoundedRect((float)DynamicIsland.animPosX.value + 8.0f, (float)DynamicIsland.animPosY2.value - 12.0f, (float)DynamicIsland.animPosX2.value - 8.0f, (float)DynamicIsland.animPosY2.value - 5.0f, 3.5f, new Color(50, 50, 50, 255).getRGB());
            RenderUtils.drawGradientRoundedRect((int)(DynamicIsland.animPosX.value + 8.0f), (int)(DynamicIsland.animPosY2.value - 12.0f), (int)(DynamicIsland.animPosX.value + (int)(DynamicIsland.animPosX2.value - DynamicIsland.animPosX.value - (double)8.0f) * 1.0f), (int)(DynamicIsland.animPosY2.value - 5.0f), 3, new Color(255, 255, 255, 100).getRGB(), new Color(255, 255, 255, 10).getRGB());
            string = "Scaffold";
            string2 = Scaffold.INSTANCE.getBlockAmount() - Scaffold.INSTANCE.getPlaceTick() + " Blocks left - " + new DecimalFormat("#.##").format(MovementUtils.INSTANCE.getBps()) + " BPS";
            boolean bl = Fonts.font40SemiBold.getStringWidth(string) < Fonts.font35SemiBold.getStringWidth(string2);
            set(halfWidth - (float)(bl ? Fonts.font35SemiBold.getStringWidth(string2) : Fonts.font40SemiBold.getStringWidth(string)) / 2.0f - (MinecraftInstance.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock ? 15.0f : 13.0f) + (bl ? 2.0f : 0.0f), 11.0f, halfWidth + (float)(bl ? Fonts.font35SemiBold.getStringWidth(string2) : Fonts.font40SemiBold.getStringWidth(string)) / 2.0f + (MinecraftInstance.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock ? 15.0f : 13.0f) - (bl ? 2.0f : 0.0f), 14.0f + (float)Fonts.font40SemiBold.getHeight() + 12.0f + 4.0f + (float)Fonts.font35SemiBold.getHeight() + 7.0f);
            Fonts.font40SemiBold.drawCenteredString(string, halfWidth + 10.0f, (float)DynamicIsland.animPosY.value + 9.0f, new Color(255, 255, 255, 255).getRGB());
            Fonts.font35SemiBold.drawCenteredString(string2, halfWidth, (float)DynamicIsland.animPosY.value + 9.0f + (float)Fonts.font35SemiBold.getHeight() + 7.0f, new Color(255, 255, 255, 255).getRGB());
            RenderUtils.renderItemIcon((int)halfWidth - 37, 15, MinecraftInstance.mc.thePlayer.getHeldItem());
            RenderUtils.drawRoundedRect((float)DynamicIsland.animPosX.value + 8.0f, (float)DynamicIsland.animPosY2.value - 12.0f, (float)DynamicIsland.animPosX2.value - 8.0f, (float)DynamicIsland.animPosY2.value - 5.0f, 3.5f, new Color(50, 50, 50, 255).getRGB());
            RenderUtils.drawGradientRoundedRect((int)(DynamicIsland.animPosX.value + 8.0f), (int)(DynamicIsland.animPosY2.value - 12.0f), (int)(DynamicIsland.animPosX.value + (DynamicIsland.animPosX2.value - DynamicIsland.animPosX.value - 8.0f) * prevBlock), (int)(DynamicIsland.animPosY2.value - 5.0f), 3, new Color(255, 255, 255, 100).getRGB(), new Color(255, 255, 255, 10).getRGB()); prevBlock = AnimHelperKt.animSmooth(prevBlock, ((float)Scaffold.INSTANCE.getBlockAmount() - (float)Scaffold.INSTANCE.getPlaceTick()) / 64.0f, 0.001f);
        } else if (Disabler.INSTANCE.getState() && disabler != null) {
            string = "WatchDog Disabler";
            set(halfWidth - (float)Fonts.font40SemiBold.getStringWidth(string) / 2.0f - 70.0f, 11.0f, halfWidth + (float)Fonts.font40SemiBold.getStringWidth(string) / 2.0f + 70.0f, 14.0f + (float)Fonts.font40SemiBold.getHeight() + 12.0f + 20.0f);
            Fonts.font40SemiBold.drawCenteredString(string, halfWidth, (float)DynamicIsland.animPosY.value + 9.0f, new Color(255, 255, 255, 255).getRGB());
            RenderUtils.drawRoundedRect((float)DynamicIsland.animPosX.value + 8.0f, (float)DynamicIsland.animPosY2.value - 12.0f, (float)DynamicIsland.animPosX2.value - 8.0f, (float)DynamicIsland.animPosY2.value - 5.0f, 3.5f, new Color(50, 50, 50, 255).getRGB());
            float f = (float)DynamicIsland.animPosX.value + 8.0f;
            float f2 = (float)DynamicIsland.animPosY2.value - 12.0f;
            float f3 = (float)DynamicIsland.animPosX.value;
            float f4 = (float)(DynamicIsland.animPosX2.value - DynamicIsland.animPosX.value - (double)8.0f);
            Float f5 = disabler;
            Intrinsics.checkNotNull(f5);
            RenderUtils.drawGradientRoundedRect((int)f, (int)f2, (int)(f3 + f4 * f5.floatValue()), (int)(DynamicIsland.animPosY2.value - 5.0f), 3, new Color(255, 255, 255, 100).getRGB(), new Color(255, 255, 255, 10).getRGB());
        } else if (SlotUtils.INSTANCE.getSpoofing() && MinecraftInstance.mc.thePlayer.inventory.getCurrentItem() != null) {
            string = MinecraftInstance.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock ? MinecraftInstance.mc.thePlayer.getHeldItem().stackSize + " Blocks left" : "";
            String string2 = Intrinsics.stringPlus("Using : ", SlotUtils.INSTANCE.getModule());
            boolean bl = Fonts.font40SemiBold.getStringWidth(string) < Fonts.font35SemiBold.getStringWidth(string2);
            set(halfWidth - (float)(bl ? Fonts.font35SemiBold.getStringWidth(string2) : Fonts.font40SemiBold.getStringWidth(string)) / 2.0f - (MinecraftInstance.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock ? 15.0f : 13.0f) + (bl ? 2.0f : 0.0f), 11.0f, halfWidth + (float)(bl ? Fonts.font35SemiBold.getStringWidth(string2) : Fonts.font40SemiBold.getStringWidth(string)) / 2.0f + (MinecraftInstance.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock ? 15.0f : 13.0f) - (bl ? 2.0f : 0.0f), 14.0f + (float)Fonts.font40SemiBold.getHeight() + 12.0f + 4.0f + (float)Fonts.font35SemiBold.getHeight());
            Fonts.font40SemiBold.drawCenteredString(string, halfWidth + 10.0f, (float)DynamicIsland.animPosY.value + 9.0f, new Color(255, 255, 255, 255).getRGB());
            Fonts.font35SemiBold.drawCenteredString(string2, halfWidth, (float)DynamicIsland.animPosY.value + 9.0f + (float)Fonts.font35SemiBold.getHeight() + 7.0f, new Color(255, 255, 255, 255).getRGB());
            int fps = Minecraft.getDebugFPS();
            RenderUtils.renderItemIcon(MinecraftInstance.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock ? (int)halfWidth - 37 : (int)halfWidth - 8, 15, MinecraftInstance.mc.thePlayer.getHeldItem());
        } else {
            string = "FDPClient+++ \u00a7f| " + MinecraftInstance.mc.thePlayer.getName() + " | " + ServerUtils.getRemoteIp() + " | " + Minecraft.getDebugFPS() + "fps";
            set(halfWidth - (float)Fonts.font40SemiBold.getStringWidth(string) / 2.0f - 9.0f, 11.0f, halfWidth + (float)Fonts.font40SemiBold.getStringWidth(string) / 2.0f + 9.0f, 14.0f + (float)Fonts.font40SemiBold.getHeight() + 12.0f);
            Fonts.font40SemiBold.drawCenteredString(string, halfWidth, (float)DynamicIsland.animPosY.value + 9.0f, 0xFFFFFFFF, true);
        }
        GlStateManager.resetColor();
        Stencil.dispose();
        GL11.glPopMatrix();
    }

    private final void set(float startX, float startY, float endX, float endY, int alpha) {
        posX = startX;
        posY = startY;
        posX2 = endX;
        posY2 = endY;
        DynamicIsland.alpha = alpha;
    }

    static /* synthetic */ void set$default(DynamicIsland dynamicIsland, float f, float f2, float f3, float f4, int n, int n2, Object object) {
        if ((n2 & 0x10) != 0) {
            n = 180;
        }
        dynamicIsland.set(f, f2, f3, f4, n);
    }

    private final void drawTH(float posY, float stringWidth, EntityLivingBase target, String string) {
        Float f;
        int width = new ScaledResolution(MinecraftInstance.mc).getScaledWidth();
        int height = new ScaledResolution(MinecraftInstance.mc).getScaledHeight();
        float halfWidth = (float)width / 2.0f;
        float halfHeight = (float)height / 2.0f;
        float maxHealth = target.getMaxHealth();
        float currentHealth = RangesKt.coerceAtMost(target.getHealth(), maxHealth);
        Map<EntityLivingBase, Float> $this$getOrPut$iv = healthMap;
        boolean $i$f$getOrPut = false;
        Float value$iv = $this$getOrPut$iv.get(target);
        if (value$iv == null) {
            boolean bl = false;
            Float answer$iv = Float.valueOf(currentHealth);
            $this$getOrPut$iv.put(target, answer$iv);
            f = answer$iv;
        } else {
            f = value$iv;
        }
        float lastHealth = ((Number)f).floatValue();
        float speed = 0.2f;
        float animatedHealth = lastHealth + (currentHealth - lastHealth) * speed;
        Map<EntityLivingBase, Float> map = healthMap;
        Float f2 = Float.valueOf(animatedHealth);
        map.put(target, f2);
        this.set(halfWidth + 60.0f, halfHeight - 18.0f - posY / (float)2, halfWidth + 60.0f + 60.0f + stringWidth + 5.0f, halfHeight + 18.0f + posY / (float)2, 100);
        Fonts.font40SemiBold.drawCenteredString(string, (float)DynamicIsland.animPosX.value + ((float)DynamicIsland.animPosX2.value - (float)DynamicIsland.animPosX.value) / (float)2 + 15.0f, (float)DynamicIsland.animPosY.value + 7.0f + posY, new Color(255, 255, 255, 255).getRGB());
        Fonts.font40SemiBold.drawCenteredString(string, (float)DynamicIsland.animPosX.value + ((float)DynamicIsland.animPosX2.value - (float)DynamicIsland.animPosX.value) / (float)2 + 15.0f, (float)DynamicIsland.animPosY.value + 7.0f + posY, new Color(255, 255, 255, 255).getRGB());
        GL11.glPushMatrix();
        Stencil.write(false);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        RenderUtils.fastRoundedRect((float)DynamicIsland.animPosX.value + 6.0f, (float)DynamicIsland.animPosY.value + 5.0f + posY, (float)DynamicIsland.animPosX.value + 6.0f + 26.0f, (float)DynamicIsland.animPosY.value + 5.0f + 26.0f + posY, 6.0f);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        Stencil.erase(true);
        RenderUtils.drawHead(EntityExtensionKt.getSkin(target), (int)DynamicIsland.animPosX.value + 6, (int)DynamicIsland.animPosY.value + 5 + (int)posY, 26, 26, Color.WHITE.getRGB());
        GlStateManager.resetColor();
        Stencil.dispose();
        GL11.glPopMatrix();
        RenderUtils.drawRoundedRect((float)DynamicIsland.animPosX.value + 40.0f, (float)DynamicIsland.animPosY.value + 24.5f + posY, (float)DynamicIsland.animPosX2.value - 5.0f - ((float)DynamicIsland.animPosX.value + 40.0f) - 4.0f, 4.5f, 2.0f, new Color(0, 0, 0, 200).getRGB(), 1.0f, Color.WHITE.getRGB());
        float f3 = (float)DynamicIsland.animPosX.value + 40.0f;
        float f4 = (float)DynamicIsland.animPosY.value + 24.5f + posY;
        float f5 = (float)DynamicIsland.animPosX.value + 30.0f;
        float f6 = (float)DynamicIsland.animPosX2.value - 5.0f - ((float)DynamicIsland.animPosX.value + 30.0f);
        Float f7 = healthMap.get(target);
        Intrinsics.checkNotNull(f7);
        RenderUtils.drawGradientRoundedRect((int) f3, (int) f4, (int) (f5 + (f6 * (((Number) f7).floatValue() / target.getMaxHealth()) - 4.0f)), (int) ((float) DynamicIsland.animPosY.value + 29.0f + posY), 2, new Color(255, 255, 255, 100).getRGB(), new Color(255, 255, 255, 10).getRGB());
    }

    static {
        animPosX = new Animation(Easing.EASE_OUT_CIRC, 500L);
        animPosY = new Animation(Easing.EASE_OUT_CIRC, 500L);
        animPosX2 = new Animation(Easing.EASE_OUT_CIRC, 500L);
        animPosY2 = new Animation(Easing.EASE_OUT_CIRC, 500L);
        animAlpha = new Animation(Easing.EASE_OUT_CIRC, 1000L);
        healthMap = new LinkedHashMap();
    }

    private final void set(float startX, float startY, float endX, float endY) {
        set(startX, startY, endX, endY, DynamicIsland.alpha);
    }

    public void set(Object owner, float x1, float y1, float x2, float y2, int blurRadius, int cornerRadius, Object unused) {
        RenderUtils.drawRoundedRect(x1, y1, x2, y2, cornerRadius, new Color(0, 0, 0, 150).getRGB());
    }

}
