/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.special;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.special.TYPE;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0014\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\r\"\u0004\b\u0019\u0010\u000fR\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/features/special/NotificationUtil;", "", "title", "", "content", "type", "Lnet/ccbluex/liquidbounce/features/special/TYPE;", "system", "", "timer", "", "(Ljava/lang/String;Ljava/lang/String;Lnet/ccbluex/liquidbounce/features/special/TYPE;JI)V", "getContent", "()Ljava/lang/String;", "setContent", "(Ljava/lang/String;)V", "getSystem", "()J", "setSystem", "(J)V", "getTimer", "()I", "setTimer", "(I)V", "getTitle", "setTitle", "getType", "()Lnet/ccbluex/liquidbounce/features/special/TYPE;", "setType", "(Lnet/ccbluex/liquidbounce/features/special/TYPE;)V", "CrossSine"})
public final class NotificationUtil {
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private TYPE type;
    private long system;
    private int timer;

    public NotificationUtil(@NotNull String title2, @NotNull String content, @NotNull TYPE type, long system, int timer) {
        Intrinsics.checkNotNullParameter(title2, "title");
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter((Object)type, "type");
        this.title = title2;
        this.content = content;
        this.type = type;
        this.system = system;
        this.timer = timer;
    }

    @NotNull
    public final String getTitle() {
        return this.title;
    }

    public final void setTitle(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.title = string;
    }

    @NotNull
    public final String getContent() {
        return this.content;
    }

    public final void setContent(@NotNull String string) {
        Intrinsics.checkNotNullParameter(string, "<set-?>");
        this.content = string;
    }

    @NotNull
    public final TYPE getType() {
        return this.type;
    }

    public final void setType(@NotNull TYPE tYPE) {
        Intrinsics.checkNotNullParameter((Object)tYPE, "<set-?>");
        this.type = tYPE;
    }

    public final long getSystem() {
        return this.system;
    }

    public final void setSystem(long l) {
        this.system = l;
    }

    public final int getTimer() {
        return this.timer;
    }

    public final void setTimer(int n) {
        this.timer = n;
    }
}

