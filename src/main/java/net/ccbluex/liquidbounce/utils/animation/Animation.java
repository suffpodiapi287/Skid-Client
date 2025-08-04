/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.animation;

import net.ccbluex.liquidbounce.utils.animation.Easing;

public class Animation {
    public Easing easing;
    public long duration;
    public long millis;
    public long startTime;
    public double startValue;
    public double destinationValue;
    public double value;
    public boolean finished;

    public Animation(Easing easing, long duration) {
        this.easing = easing;
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
    }

    public void run(double destinationValue) {
        this.millis = System.currentTimeMillis();
        if (this.destinationValue != destinationValue) {
            this.destinationValue = destinationValue;
            this.reset();
        } else {
            boolean bl = this.finished = this.millis - this.duration > this.startTime;
            if (this.finished) {
                this.value = destinationValue;
                return;
            }
        }
        double result = this.easing.getFunction().apply(this.getProgress());
        this.value = this.value > destinationValue ? this.startValue - (this.startValue - destinationValue) * result : this.startValue + (destinationValue - this.startValue) * result;
    }

    public double getProgress() {
        return (double)(System.currentTimeMillis() - this.startTime) / (double)this.duration;
    }

    public void reset() {
        this.startTime = System.currentTimeMillis();
        this.startValue = this.value;
        this.finished = false;
    }
}

