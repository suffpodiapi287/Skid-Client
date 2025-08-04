/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 */
package net.ccbluex.liquidbounce.utils.render.shader.shaders;

import java.awt.Color;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.render.shader.Shader;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002JA\u0010\u0003\u001a\u00020\u00002\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0086\bJ\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u000eH\u0016\u00a8\u0006\u0010"}, d2={"Lnet/ccbluex/liquidbounce/utils/render/shader/shaders/RoundedRectShader;", "Lnet/ccbluex/liquidbounce/utils/render/shader/Shader;", "()V", "draw", "x", "", "y", "x2", "y2", "radius", "shadow", "color", "Ljava/awt/Color;", "setupUniforms", "", "updateUniforms", "CrossSine"})
public final class RoundedRectShader
extends Shader {
    @NotNull
    public static final RoundedRectShader INSTANCE = new RoundedRectShader();

    private RoundedRectShader() {
        super("roundedrect.frag");
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("u_size");
        this.setupUniform("u_radius");
        this.setupUniform("u_color");
    }

    @Override
    public void updateUniforms() {
    }

    @NotNull
    public final RoundedRectShader draw(float x, float y, float x2, float y2, float radius, float shadow, @NotNull Color color) {
        Intrinsics.checkNotNullParameter(color, "color");
        boolean $i$f$draw = false;
        float width = Math.abs(x2 - x);
        float height = Math.abs(y2 - y);
        INSTANCE.startShader();
        float[] fArray = new float[]{width, height};
        INSTANCE.setUniformf("u_size", fArray);
        fArray = new float[]{radius};
        INSTANCE.setUniformf("u_radius", fArray);
        fArray = new float[]{(float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f};
        INSTANCE.setUniformf("u_color", fArray);
        fArray = new float[]{shadow};
        INSTANCE.setUniformf("u_shadow", fArray);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)771);
        GlStateManager.	enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        Shader.drawQuad(x, y, width, height);
        GlStateManager.disableBlend();
        INSTANCE.stopShader();
        return INSTANCE;
    }
}

