
package net.ccbluex.liquidbounce.utils.render;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RoundedUtils {

    public static void drawRound(float x, float y, float width, float height, float radius, Color color) {
        int segments = 20;
        float right = x + width;
        float bottom = y + height;

        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, a);
        GL11.glBegin(GL11.GL_POLYGON);

        // Top-left corner
        for (int i = 0; i <= segments; i++) {
            double theta = Math.PI / 2 * i / segments;
            GL11.glVertex2d(x + radius - Math.cos(theta) * radius, y + radius - Math.sin(theta) * radius);
        }

        // Bottom-left corner
        for (int i = 0; i <= segments; i++) {
            double theta = Math.PI / 2 * i / segments + Math.PI / 2;
            GL11.glVertex2d(x + radius - Math.cos(theta) * radius, bottom - radius - Math.sin(theta) * radius);
        }

        // Bottom-right corner
        for (int i = 0; i <= segments; i++) {
            double theta = Math.PI / 2 * i / segments + Math.PI;
            GL11.glVertex2d(right - radius - Math.cos(theta) * radius, bottom - radius - Math.sin(theta) * radius);
        }

        // Top-right corner
        for (int i = 0; i <= segments; i++) {
            double theta = Math.PI / 2 * i / segments + 3 * Math.PI / 2;
            GL11.glVertex2d(right - radius - Math.cos(theta) * radius, y + radius - Math.sin(theta) * radius);
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopAttrib();
    }
}
