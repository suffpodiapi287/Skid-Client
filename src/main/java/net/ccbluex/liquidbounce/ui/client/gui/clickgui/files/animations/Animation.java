/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.gui.clickgui.files.animations;

import net.ccbluex.liquidbounce.ui.client.gui.clickgui.files.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.gui.clickgui.files.normal.TimerUtil;

public abstract class Animation {
    public final TimerUtil timerUtil = new TimerUtil();
    protected int duration;
    protected double endPoint;
    protected Direction direction;

    public Animation(int ms, double endPoint) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = Direction.FORWARDS;
    }

    public Animation(int ms, double endPoint, Direction direction) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = direction;
    }

    public boolean isDone() {
        return this.timerUtil.hasTimeElapsed(this.duration);
    }

    protected boolean correctOutput() {
        return false;
    }

    public double getOutput() {
        if (this.direction == Direction.FORWARDS) {
            if (this.isDone()) {
                return this.endPoint;
            }
            return this.getEquation(this.timerUtil.getTime()) * this.endPoint;
        }
        if (this.isDone()) {
            return 0.0;
        }
        if (this.correctOutput()) {
            double revTime = Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getTime()));
            return this.getEquation(revTime) * this.endPoint;
        }
        return (1.0 - this.getEquation(this.timerUtil.getTime())) * this.endPoint;
    }

    protected abstract double getEquation(double var1);
}

