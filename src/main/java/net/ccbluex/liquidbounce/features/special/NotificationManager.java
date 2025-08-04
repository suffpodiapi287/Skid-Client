/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.special;

import java.util.ArrayList;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.features.special.NotificationUtil;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R!\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/special/NotificationManager;", "", "()V", "list", "Ljava/util/ArrayList;", "Lnet/ccbluex/liquidbounce/features/special/NotificationUtil;", "Lkotlin/collections/ArrayList;", "getList", "()Ljava/util/ArrayList;", "CrossSine"})
public final class NotificationManager {
    @NotNull
    private final ArrayList<NotificationUtil> list = new ArrayList();

    @NotNull
    public final ArrayList<NotificationUtil> getList() {
        return this.list;
    }
}

