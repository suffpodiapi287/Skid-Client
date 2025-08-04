/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.ResourceLocation
 */
package net.ccbluex.liquidbounce.features.module.modules.client.impl;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.FDPClient;
import net.ccbluex.liquidbounce.features.special.NotificationUtil;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\nR\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/client/impl/Notification;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "fadeStates", "Ljava/util/HashMap;", "Lnet/ccbluex/liquidbounce/features/special/NotificationUtil;", "", "offsetXStates", "offsetYStates", "draw", "", "CrossSine"})
public final class Notification
extends MinecraftInstance {
    @NotNull
    public static final Notification INSTANCE = new Notification();
    @NotNull
    private static final HashMap<NotificationUtil, Float> fadeStates = new HashMap();
    @NotNull
    private static final HashMap<NotificationUtil, Float> offsetXStates = new HashMap();
    @NotNull
    private static final HashMap<NotificationUtil, Float> offsetYStates = new HashMap();

    private Notification() {
    }

    public final void draw() {
        int width = new ScaledResolution(MinecraftInstance.mc).getScaledWidth();
        int height = new ScaledResolution(MinecraftInstance.mc).getScaledHeight();
        if (!((Collection)FDPClient.INSTANCE.getNotification().getList()).isEmpty()) {
            float accumulatedOffsetY = 0.0f;
            Iterator<NotificationUtil> iterator2 = FDPClient.INSTANCE.getNotification().getList().iterator();
            Intrinsics.checkNotNullExpressionValue(iterator2, "FDPClient.notification.list.iterator()");
            Iterator<NotificationUtil> iterator3 = iterator2;
            while (iterator3.hasNext()) {
                NotificationUtil notificationUtil = iterator3.next();
                Intrinsics.checkNotNullExpressionValue(notificationUtil, "iterator.next()");
                NotificationUtil noti = notificationUtil;
                Float f = fadeStates.getOrDefault(noti, Float.valueOf(0.0f));
                Intrinsics.checkNotNullExpressionValue(f, "fadeStates.getOrDefault(noti, 0F)");
                float fadeState = ((Number)f).floatValue();
                Float f2 = offsetXStates.getOrDefault(noti, Float.valueOf(200.0f));
                Intrinsics.checkNotNullExpressionValue(f2, "offsetXStates.getOrDefault(noti, 200F)");
                float offsetX = ((Number)f2).floatValue();
                Float f3 = offsetYStates.getOrDefault(noti, Float.valueOf(50.0f));
                Intrinsics.checkNotNullExpressionValue(f3, "offsetYStates.getOrDefault(noti, 50F)");
                float offsetY = ((Number)f3).floatValue();
                float fadeDelta = 0.00525f * (float)RenderUtils.deltaTime * (System.currentTimeMillis() > noti.getSystem() + (long)noti.getTimer() ? 1.0f : -1.0f);
                float newFadeState = RangesKt.coerceIn(fadeState + fadeDelta, 0.0f, 1.0f);
                Map map = fadeStates;
                Float f4 = Float.valueOf(newFadeState);
                map.put(noti, f4);
                float targetOffsetX = 0.0f;
                float offsetXDelta = (targetOffsetX - offsetX) * 0.15f;
                float newOffsetX = offsetX + offsetXDelta;
                Map map2 = offsetXStates;
                Float f5 = Float.valueOf(newOffsetX);
                map2.put(noti, f5);
                float targetOffsetY = accumulatedOffsetY;
                float offsetYDelta = (targetOffsetY - offsetY) * 0.15f;
                float newOffsetY = offsetY + offsetYDelta;
                Map map3 = offsetYStates;
                Float f6 = Float.valueOf(newOffsetY);
                map3.put(noti, f6);
                float percent = (float)EaseUtils.INSTANCE.easeInCirc(newFadeState);
                if (newFadeState >= 1.0f && System.currentTimeMillis() > noti.getSystem() + (long)noti.getTimer()) {
                    iterator3.remove();
                    fadeStates.remove(noti);
                    offsetXStates.remove(noti);
                    offsetYStates.remove(noti);
                    continue;
                }
                int contentWidth = Fonts.font40SemiBold.getStringWidth(noti.getContent());
                float x1 = (float)width - 8.0f - (float)contentWidth - 32.0f + newOffsetX + (32.0f + (float)contentWidth) * percent;
                float x2 = (float)width + newOffsetX + (8.0f + (float)contentWidth) * percent;
                float y1 = (float)height - 8.0f - (float)Fonts.font40SemiBold.getHeight() - 16.0f - newOffsetY;
                float y2 = (float)height - 4.0f - newOffsetY;
                float timeProgress = RangesKt.coerceIn((float)(System.currentTimeMillis() - noti.getSystem()) / (float)noti.getTimer(), 0.0f, 1.0f);
                float xchange = x1 + (x2 - x1) * (1.0f - timeProgress);
                RenderUtils.drawBloomRoundedRect(x1, y1, xchange, y2, 4.0f, 4.0f, 2.5f, new Color(255, 255, 255, 80 - (int)(80f * newFadeState)), RenderUtils.ShaderBloom.ROUNDONLY);
                RenderUtils.drawBloomRoundedRect(x1, y1, x2, y2, 4.0f, 2.5f, 2.0f, new Color(0, 0, 0, 120 - (int)(120f * newFadeState)), RenderUtils.ShaderBloom.BOTH);
                RenderUtils.drawImage(new ResourceLocation("crosssine/ui/notifications/" + noti.getType().name() + ".png"), width - 37 - contentWidth + (int)((float)(contentWidth + 4) * percent) + (int)newOffsetX, height - 32 - (int)newOffsetY, 27, 27);
                Fonts.font40SemiBold.drawString(noti.getContent(), (float)width - 8.0f - (float)contentWidth + ((float)contentWidth + 4.0f) * percent + newOffsetX, (float)height - 6.0f - (float)Fonts.font40SemiBold.getHeight() - newOffsetY, new Color(255, 255, 255, 255 - (int)((float)255 * newFadeState)).getRGB());
                Fonts.font40SemiBold.drawString(noti.getTitle(), (float)width - 8.0f - (float)contentWidth + ((float)contentWidth + 4.0f) * percent + newOffsetX, (float)height - 8.0f - (float)(Fonts.font40SemiBold.getHeight() * 2) - newOffsetY, new Color(255, 255, 255, 255 - (int)((float)255 * newFadeState)).getRGB());
                accumulatedOffsetY += 35.0f;
            }
        }
    }
}

