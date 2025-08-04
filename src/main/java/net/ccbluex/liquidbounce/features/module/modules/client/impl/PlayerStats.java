/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.MathHelper
 */
package net.ccbluex.liquidbounce.features.module.modules.client.impl;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.animation.Animation;
import net.ccbluex.liquidbounce.utils.animation.Easing;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/client/impl/PlayerStats;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "airAnim", "Lnet/ccbluex/liquidbounce/utils/animation/Animation;", "armorAnim", "foodAnim", "healthAnim", "healthYellowAnim", "draw", "", "player", "Lnet/minecraft/entity/player/EntityPlayer;", "sr", "Lnet/minecraft/client/gui/ScaledResolution;", "CrossSine"})
public final class PlayerStats
extends MinecraftInstance {
    @NotNull
    public static final PlayerStats INSTANCE = new PlayerStats();
    @NotNull
    private static final Animation armorAnim = new Animation(Easing.LINEAR, 250L);
    @NotNull
    private static final Animation healthAnim = new Animation(Easing.LINEAR, 250L);
    @NotNull
    private static final Animation healthYellowAnim = new Animation(Easing.LINEAR, 250L);
    @NotNull
    private static final Animation foodAnim = new Animation(Easing.LINEAR, 250L);
    @NotNull
    private static final Animation airAnim = new Animation(Easing.LINEAR, 250L);

    private PlayerStats() {
    }

    public final void draw(@NotNull EntityPlayer player, @NotNull ScaledResolution sr) {
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(sr, "sr");
        if (MinecraftInstance.mc.playerController.isInCreativeMode()) {
            return;
        }
        int height = sr.getScaledHeight();
        float halfWidth = (float)sr.getScaledWidth() / 2.0f;
        EntityPlayer thePlayer = MinecraftInstance.mc.thePlayer;
            if (thePlayer == null) return;
        int l6 = thePlayer.getAir();
        int k7 = (int)Math.ceil((double)(l6 - 2) * 10.0 / 300.0);
        armorAnim.run((float)player.getTotalArmorValue() / 20.0f);
        healthAnim.run(player.getHealth() / 20.0f);
        healthYellowAnim.run(MinecraftInstance.mc.thePlayer.getAbsorptionAmount() / 4.0f);
        foodAnim.run((float)player.getFoodStats().getFoodLevel() / 20.0f);
        airAnim.run((float)k7 / 10.0f);
        if (player.getTotalArmorValue() > 0) {
            RenderUtils.drawBloomRoundedRect(halfWidth - 91.0f, (float) height - 52.0f - (PlayerStats.healthYellowAnim.value > 0.0 ? 12.0f : 0.0f), halfWidth - 6.0f, (float) height - 44.0f - (PlayerStats.healthYellowAnim.value > 0.0 ? 12.0f : 0.0f), 2.5f, 1.8f, 6.0f, new Color(50, 50, 50), RenderUtils.ShaderBloom.BOTH);
            RenderUtils.drawBloomRoundedRect(halfWidth - 91.0f, (float) height - 52.0f - (PlayerStats.healthYellowAnim.value > 0.0 ? 12.0f : 0.0f), halfWidth - 91.0f + 4.0f + 81.0f * (float) PlayerStats.armorAnim.value, (float) height - 44.0f - (PlayerStats.healthYellowAnim.value > 0.0 ? 12.0f : 0.0f), 2.5f, 1.0f, 6.0f, new Color(100, 100, 255), RenderUtils.ShaderBloom.BOTH);
        }
        if (PlayerStats.healthYellowAnim.value > 0.0) {
            RenderUtils.drawBloomRoundedRect(halfWidth - 91.0f, (float)height - 52.0f, halfWidth - 6.0f, (float)height - 44.0f, 2.5f, 1.8f, 6.0f, new Color(50, 50, 50), RenderUtils.ShaderBloom.BOTH);
            RenderUtils.drawBloomRoundedRect(halfWidth - 91.0f, (float)height - 52.0f, halfWidth - 91.0f + 4.0f + 81.0f * (float)PlayerStats.healthYellowAnim.value, (float)height - 44.0f, 2.5f, 1.0f, 6.0f, new Color(255, 255, 20), RenderUtils.ShaderBloom.BOTH);
        }
        RenderUtils.drawBloomRoundedRect(halfWidth - 91.0f, (float)height - 40.0f, halfWidth - 6.0f, (float)height - 32.0f, 2.5f, 1.8f, 6.0f, new Color(50, 50, 50), RenderUtils.ShaderBloom.BOTH);
        RenderUtils.drawBloomRoundedRect(halfWidth - 91.0f, (float)height - 40.0f, halfWidth - 91.0f + 4.0f + 81.0f * (float)PlayerStats.healthAnim.value, (float)height - 32.0f, 2.5f, 1.0f, 6.0f, new Color(255, 10, 10), RenderUtils.ShaderBloom.BOTH);
        RenderUtils.drawBloomRoundedRect(halfWidth + 6.0f, (float)height - 40.0f, halfWidth + 6.0f + 85.0f, (float)height - 32.0f, 2.5f, 1.8f, 6.0f, new Color(50, 50, 50), RenderUtils.ShaderBloom.BOTH);
        RenderUtils.drawBloomRoundedRect(halfWidth + 6.0f, (float)height - 40.0f, halfWidth + 6.0f + 4.0f + 81.0f * (float)PlayerStats.foodAnim.value, (float)height - 32.0f, 2.5f, 1.8f, 6.0f, new Color(28, 167, 222), RenderUtils.ShaderBloom.BOTH);
        if (player.isInsideOfMaterial(Material.water)) {
            airAnim.run((float)k7 / 10.0f);
            RenderUtils.drawBloomRoundedRect(halfWidth + (float)6, (float)height - 52.0f, halfWidth + (float)6 + 85.0f, (float)height - 52.0f + 8.0f, 2.5f, 2.5f, 6.0f, new Color(43, 42, 43), RenderUtils.ShaderBloom.BOTH);
            RenderUtils.drawBloomRoundedRect(halfWidth + (float)6, (float)height - 52.0f, halfWidth + (float)6 + (float)85 * (float)PlayerStats.airAnim.value, (float)height - 52.0f + 8.0f, 2.5f, 2.5f, 6.0f, new Color(28, 167, 222), RenderUtils.ShaderBloom.BOTH);
        }
    }
}

