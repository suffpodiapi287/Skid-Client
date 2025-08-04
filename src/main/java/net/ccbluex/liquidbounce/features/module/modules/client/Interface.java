/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.input.Keyboard
 */
package net.ccbluex.liquidbounce.features.module.modules.client;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.client.Interface;
import net.ccbluex.liquidbounce.features.module.modules.client.DynamicIsland;
import net.ccbluex.liquidbounce.features.module.modules.client.impl.Notification;
import net.ccbluex.liquidbounce.features.module.modules.client.impl.PlayerStats;
import net.ccbluex.liquidbounce.features.value.BoolValue;
import net.ccbluex.liquidbounce.features.value.IntegerRangeValue;
import net.ccbluex.liquidbounce.features.value.TitleValue;
import net.ccbluex.liquidbounce.features.value.Value;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.SlotUtils;
import net.ccbluex.liquidbounce.utils.animation.Animation;
import net.ccbluex.liquidbounce.utils.animation.Easing;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name="Interface", category = ModuleCategory.CLIENT, array=false, defaultOn=true)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.H\u0007J\u0010\u0010/\u001a\u00020,2\u0006\u0010-\u001a\u000200H\u0007J\u0010\u00101\u001a\u00020,2\u0006\u0010-\u001a\u000202H\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0015R\u0011\u0010\u0018\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001bR\u0011\u0010\u001e\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001bR\u0011\u0010 \u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001bR\u0011\u0010\"\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001bR\u0011\u0010$\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001bR\u000e\u0010&\u001a\u00020'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0015\u00a8\u00063"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/client/Interface;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "animationSlot", "Lnet/ccbluex/liquidbounce/utils/animation/Animation;", "getAnimationSlot", "()Lnet/ccbluex/liquidbounce/utils/animation/Animation;", "asdasd", "Lnet/ccbluex/liquidbounce/features/value/IntegerRangeValue;", "getAsdasd", "()Lnet/ccbluex/liquidbounce/features/value/IntegerRangeValue;", "attackTarget", "Lnet/minecraft/entity/EntityLivingBase;", "getAttackTarget", "()Lnet/minecraft/entity/EntityLivingBase;", "setAttackTarget", "(Lnet/minecraft/entity/EntityLivingBase;)V", "bloomValue", "Lnet/ccbluex/liquidbounce/features/value/Value;", "", "getBloomValue", "()Lnet/ccbluex/liquidbounce/features/value/Value;", "blurValue", "getBlurValue", "dynamicIsland", "Lnet/ccbluex/liquidbounce/features/value/BoolValue;", "getDynamicIsland", "()Lnet/ccbluex/liquidbounce/features/value/BoolValue;", "inventoryAnimation", "getInventoryAnimation", "notification", "getNotification", "playerStats", "getPlayerStats", "roundedHotbar", "getRoundedHotbar", "shaderValue", "getShaderValue", "targetTimer", "Lnet/ccbluex/liquidbounce/utils/timer/TimerMS;", "title", "", "getTitle", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onRender2D", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "onTick", "Lnet/ccbluex/liquidbounce/event/TickEvent;", "CrossSine"})
public final class Interface
extends Module {
    @NotNull
    public static final Interface INSTANCE = new Interface();
    @NotNull
    private static final IntegerRangeValue asdasd = new IntegerRangeValue("wreg", 4, 5, 1, 10);
    @NotNull
    private static final BoolValue dynamicIsland = new BoolValue("DynamicIsland", true);
    @NotNull
    private static final BoolValue notification = new BoolValue("Notification", true);
    @NotNull
    private static final BoolValue playerStats = new BoolValue("PlayerStats", true);
    @NotNull
    private static final BoolValue inventoryAnimation = new BoolValue("InventoryAnimation", false);
    @NotNull
    private static final BoolValue roundedHotbar = new BoolValue("RoundedHotBar", true);
    @NotNull
    public static final BoolValue shaderValue = new BoolValue("Shader", true);
    @NotNull
    public static final BoolValue titleToggle = new BoolValue("TitleToggle", true);
    @NotNull
    public static final BoolValue blurToggle = new BoolValue("BlurToggle", true);
    @NotNull
    public static final BoolValue bloomToggle = new BoolValue("BloomToggle", true);
    @NotNull
    private static final Value<Boolean> bloomValue = new BoolValue("Bloom", true).displayable(() -> bloomToggle.get());
    @NotNull
    public static final Value<Boolean> blurValue = new BoolValue("Blur", false).displayable(() -> blurToggle.get());
    @NotNull
    private static final Value<String> title = new TitleValue("Blur only work with FastRender disabled").displayable(() -> titleToggle.get());
    @NotNull
    private static final Animation animationSlot = new Animation(Easing.EASE_OUT_CIRC, 300L);
    @Nullable
    private static EntityLivingBase attackTarget;
    @NotNull
    private static final MSTimer targetTimer;

    public Interface() {
    super("Interface", "Client UI settings", ModuleCategory.CLIENT, 0, false, false, EnumAutoDisableType.NONE, false, false, false);
}

    @NotNull
    public final IntegerRangeValue getAsdasd() {
        return asdasd;
    }

    @NotNull
    public final BoolValue getDynamicIsland() {
        return dynamicIsland;
    }

    @NotNull
    public final BoolValue getNotification() {
        return notification;
    }

    @NotNull
    public final BoolValue getPlayerStats() {
        return playerStats;
    }

    @NotNull
    public final BoolValue getInventoryAnimation() {
        return inventoryAnimation;
    }

    @NotNull
    public final BoolValue getRoundedHotbar() {
        return roundedHotbar;
    }

    @NotNull
    public final BoolValue getShaderValue() {
        return shaderValue;
    }

    @NotNull
    public final Value<Boolean> getBloomValue() {
        return bloomValue;
    }

    @NotNull
    public final Value<Boolean> getBlurValue() {
        return blurValue;
    }

    @NotNull
    public final Value<String> getTitle() {
        return title;
    }

    @NotNull
    public final Animation getAnimationSlot() {
        return animationSlot;
    }

    @Nullable
    public final EntityLivingBase getAttackTarget() {
        return attackTarget;
    }

    public final void setAttackTarget(@Nullable EntityLivingBase entityLivingBase) {
        attackTarget = entityLivingBase;
    }

    @EventTarget
    public final void onTick(@NotNull TickEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        MinecraftInstance.mc.displayGuiScreen(null);
        if (Keyboard.isKeyDown((int)52) && MinecraftInstance.mc.currentScreen == null) {
            MinecraftInstance.mc.displayGuiScreen((GuiScreen)new GuiChat("."));
        }
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
        Intrinsics.checkNotNullParameter(event, "event");
        animationSlot.run((double)SlotUtils.INSTANCE.getSlot() * 20.0);
        if (targetTimer.hasTimePassed(500L)) {
            attackTarget = null;
        }
        GlStateManager.pushMatrix();
        if (((Boolean)dynamicIsland.get()).booleanValue()) {
            DynamicIsland.INSTANCE.draw(event);
        }
        if (MinecraftInstance.mc.currentScreen == null && ((Boolean)notification.get()).booleanValue()) {
            Notification.INSTANCE.draw();
        }
        if (((Boolean)playerStats.get()).booleanValue() && MinecraftInstance.mc.getRenderViewEntity() instanceof EntityPlayer) {
            Entity entity = MinecraftInstance.mc.getRenderViewEntity();
            if (entity == null) {
                throw new NullPointerException("null cannot be cast to non-null type net.minecraft.entity.player.EntityPlayer");
            }
            PlayerStats.INSTANCE.draw((EntityPlayer)entity, event.getScaledResolution());
        }
        GlStateManager.popMatrix();
        GlStateManager.disableLighting();
    }

    static {
        targetTimer = new MSTimer();
    }
}

