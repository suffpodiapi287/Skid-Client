/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 */
package net.ccbluex.liquidbounce.utils;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.FDPClient;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.modules.client.Interface;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.BlurUtils;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0019\u001a\u00020\u0012J\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bJ\b\u0010\u001c\u001a\u00020\u0006H\u0016J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0007J\b\u0010!\u001a\u00020\u001eH\u0002J\b\u0010\"\u001a\u00020\u001eH\u0002J\u000e\u0010#\u001a\u00020\u001e2\u0006\u0010$\u001a\u00020\u0012J\u000e\u0010%\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020\u0012J\u001e\u0010'\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020\u00122\u0006\u0010(\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010)\u001a\u00020\u001eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\b\"\u0004\b\u0015\u0010\nR\u001a\u0010\u0016\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\b\"\u0004\b\u0018\u0010\n\u00a8\u0006*"}, d2={"Lnet/ccbluex/liquidbounce/utils/SlotUtils;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "animProgress", "", "changed", "", "getChanged", "()Z", "setChanged", "(Z)V", "module", "", "getModule", "()Ljava/lang/String;", "setModule", "(Ljava/lang/String;)V", "prevSlot", "", "prevSpoofing", "getPrevSpoofing", "setPrevSpoofing", "spoofing", "getSpoofing", "setSpoofing", "getSlot", "getStack", "Lnet/minecraft/item/ItemStack;", "handleEvents", "onRender2D", "", "event", "Lnet/ccbluex/liquidbounce/event/Render2DEvent;", "renderItem", "renderRect", "scrollChangeItem", "currentSlot", "setPrevSlot", "slot", "setSlot", "spoof", "stopSet", "CrossSine"})
public final class SlotUtils
implements Listenable {
    @NotNull
    public static final SlotUtils INSTANCE = new SlotUtils();
    private static int prevSlot = -1;
    private static boolean spoofing;
    private static boolean prevSpoofing;
    private static boolean changed;
    @NotNull
    private static String module;
    private static float animProgress;

    private SlotUtils() {
    }

    public final boolean getSpoofing() {
        return spoofing;
    }

    public final void setSpoofing(boolean bl) {
        spoofing = bl;
    }

    public final boolean getPrevSpoofing() {
        return prevSpoofing;
    }

    public final void setPrevSpoofing(boolean bl) {
        prevSpoofing = bl;
    }

    public final boolean getChanged() {
        return changed;
    }

    public final void setChanged(boolean bl) {
        changed = bl;
    }

    @NotNull
    public final String getModule() {
        return module;
    }

    public final void setModule(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        module = string;
    }

    public final void setSlot(int slot, boolean spoof, @NotNull String module) {
        Intrinsics.checkNotNullParameter(module, "module");
        if (!changed) {
            prevSlot = MinecraftInstance.mc.thePlayer.inventory.currentItem;
            changed = true;
        }
        MinecraftInstance.mc.thePlayer.inventory.currentItem = slot;
        prevSpoofing = spoofing = spoof;
        SlotUtils.module = module;
    }

    public final void stopSet() {
        if (changed) {
            if (prevSlot != -1) {
                MinecraftInstance.mc.thePlayer.inventory.currentItem = prevSlot;
                prevSlot = -1;
            }
            spoofing = false;
            changed = false;
        }
        module = "";
    }

    public final int getSlot() {
        return spoofing ? prevSlot : MinecraftInstance.mc.thePlayer.inventory.currentItem;
    }

    @Nullable
    public final ItemStack getStack() {
        return spoofing ? MinecraftInstance.mc.thePlayer.inventory.getStackInSlot(prevSlot) : MinecraftInstance.mc.thePlayer.inventory.getCurrentItem();
    }

    public final void setPrevSlot(int slot) {
        prevSlot = slot;
    }

    public final void scrollChangeItem(int currentSlot) {
        int currentSlot1 = currentSlot;
        if (currentSlot1 > 0) {
            currentSlot1 = 1;
        }
        if (currentSlot1 < 0) {
            currentSlot1 = -1;
        }
        if (changed) {
            prevSlot -= currentSlot1;
            while (prevSlot < 0) {
                prevSlot += 9;
            }
            while (prevSlot >= 9) {
                prevSlot -= 9;
            }
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    @EventTarget
    public final void onRender2D(@NotNull Render2DEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)Interface.INSTANCE.getDynamicIsland().get()).booleanValue() || !prevSpoofing) {
            return;
        }
        this.renderRect();
        GlStateManager.resetColor();
        if (MinecraftInstance.mc.thePlayer.inventory.getCurrentItem() != null) {
            this.renderItem();
            GlStateManager.resetColor();
        }
    }

    ModuleManager moduleManager = FDPClient.INSTANCE.getModuleManager();

    private final void renderRect() {
        ItemStack itemStack = MinecraftInstance.mc.thePlayer.inventory.getCurrentItem();
        animProgress += 0.00375f * (float)RenderUtils.deltaTime * (changed && prevSlot != MinecraftInstance.mc.thePlayer.inventory.currentItem ? 1.0f : -1.0f);
        animProgress = RangesKt.coerceIn(animProgress, 0.0f, 1.0f);
        double percent = EaseUtils.easeOutBack(animProgress);
        int width = new ScaledResolution(MinecraftInstance.mc).getScaledWidth();
        float height = (float) new ScaledResolution(MinecraftInstance.mc).getScaledHeight() + 3.0f;
        Scaffold scaffold = (Scaffold) FDPClient.INSTANCE.getModuleManager().getModule(Scaffold.class);
        if (animProgress > 0.0f) {
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                String string = Intrinsics.stringPlus(
                "Amount: ",
                scaffold.getState() ? scaffold.getBlockAmount() - scaffold.getPlaceTick() : itemStack.stackSize
            );
                float stringWidth = (float) Fonts.font35.getStringWidth(string) + (itemStack.stackSize < 10 ? 3.0f : 0.0f);
                if (((Boolean)Interface.INSTANCE.getShaderValue().get()).booleanValue()) {
                    BlurUtils.INSTANCE.blurAreaRounded((float)width / 2.0f + -35.0f, height - 2.0f - 80.0f * (float)percent, (float)width / 2.0f + -35.0f + 32.0f + stringWidth, height - 2.0f - 80.0f * (float)percent + 20.0f, 5.0f, 10.0f);
                }
                RenderUtils.drawBloomRoundedRect((float)width / 2.0f + -35.0f, height - 2.0f - 80.0f * (float)percent, (float)width / 2.0f + -35.0f + 32.0f + stringWidth, height - 2.0f - 80.0f * (float)percent + 20.0f, 5.0f, 4.0f, 6.0f, new Color(0, 0, 0, (int)((double)80 * percent)), RenderUtils.ShaderBloom.BOTH);
                Fonts.font35.drawCenteredString(string, (float)(width / 2) + 15.0f, height + 5.0f - (float)((double)80.0f * percent), Color.WHITE.getRGB(), true);
            } else {
                if (((Boolean)Interface.INSTANCE.getShaderValue().get()).booleanValue()) {
                    BlurUtils.INSTANCE.blurAreaRounded((float)width / 2.0f + -11.0f, height - 2.0f - 80.0f * (float)percent, (float)width / 2.0f + -11.0f + 20.0f, height - 2.0f - 80.0f * (float)percent + 20.0f, 5.0f, 10.0f);
                }
                RenderUtils.drawBloomRoundedRect((float)width / 2.0f + -11.0f, height - 2.0f - 80.0f * (float)percent, (float)width / 2.0f + -11.0f + 20.0f, height - 2.0f - 80.0f * (float)percent + 20.0f, 5.0f, 4.0f, 6.0f, new Color(0, 0, 0, (int)((double)80 * percent)), RenderUtils.ShaderBloom.BOTH);
            }
        }
        if (animProgress == 0.0f && prevSpoofing) {
            prevSpoofing = false;
        }
    }

    private final void renderItem() {
    ScaledResolution scaledResolution = new ScaledResolution(MinecraftInstance.mc);
    int width = scaledResolution.getScaledWidth();      
    float height = scaledResolution.getScaledHeight() + 3.0f;  
    ItemStack itemStack = MinecraftInstance.mc.thePlayer.inventory.getCurrentItem();
    double percent = EaseUtils.easeOutBack(animProgress);
    if (itemStack != null && animProgress >= 0.0f) {
        if (itemStack.getItem() instanceof ItemBlock) {
            RenderUtils.renderItemIcon(width / 2 - 30, (int)(height - 80.0f * percent), itemStack);
        } else {
            RenderUtils.renderItemIcon(width / 2 - 9, (int)(height - 80.0f * percent), itemStack);
        }
    }
}

    static {
        module = "";
    }
}

