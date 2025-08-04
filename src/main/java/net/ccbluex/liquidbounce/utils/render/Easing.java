package net.ccbluex.liquidbounce.utils.render;

public final class Easing {

    public static float LINEAR(float t) {
        return t;
    }

    public static float EXPO_OUT(float t) {
        return (t == 1f) ? 1f : 1 - (float)Math.pow(2, -10 * t);
    }

    public static float EXPO_IN(float t) {
        return (t == 0f) ? 0f : (float)Math.pow(2, 10 * (t - 1));
    }

    public static float BACK_OUT(float t) {
        float f = 1 - t;
        return 1 - (f * f * f - f * (float)Math.sin(f * Math.PI));
    }

    public static float QUAD_IN(float t) {
        return t * t;
    }

    public static float QUAD_OUT(float t) {
        return t * (2 - t);
    }

    public static float CUBIC_IN(float t) {
        return t * t * t;
    }

    public static float CUBIC_OUT(float t) {
        float f = t - 1f;
        return f * f * f + 1f;
    }

    public static float SINE_IN(float t) {
        return (float)(1 - Math.cos(t * Math.PI / 2));
    }

    public static float SINE_OUT(float t) {
        return (float)Math.sin(t * Math.PI / 2);
    }

    public static float CIRC_OUT(float t) {
        return (float)Math.sqrt((2 - t) * t);
    }

    public static float CIRC_IN(float t) {
        return (float)(1 - Math.sqrt(1 - t * t));
    }
}
