package net.ccbluex.liquidbounce.utils;

public class Animation {
    private float value;
    private long lastMS;

    public Animation(float start) {
        this.value = start;
        this.lastMS = System.currentTimeMillis();
    }

    public float getValue() {
        return value;
    }

    public void update(float target, float speed) {
        long now = System.currentTimeMillis();
        long delta = now - lastMS;
        lastMS = now;
        float diff = target - value;
        float change = diff * speed;
        value += change;
    }

    public void resetTo(float target) {
        this.value = target;
        this.lastMS = System.currentTimeMillis();
    }
}
