/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.utils.render

import net.ccbluex.liquidbounce.FDPClient
import net.ccbluex.liquidbounce.injection.access.StaticStorage
import net.ccbluex.liquidbounce.features.module.modules.client.Interface
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.client.shader.Framebuffer
import net.minecraft.client.shader.ShaderGroup
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation
import net.minecraft.client.shader.Shader
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats

object BlurUtils : MinecraftInstance() {
    private val blurShader: ShaderGroup = ShaderGroup(mc.textureManager, mc.resourceManager, mc.framebuffer, ResourceLocation("shaders/post/blurArea.json"))
    private lateinit var buffer: Framebuffer
    private var lastScale = 0
    private var lastScaleWidth = 0
    private var lastScaleHeight = 0
    private var lastFactor = 0
    private var lastWidth = 0
    private var lastHeight = 0
    private lateinit var framebuffer: Framebuffer
    private lateinit var frbuffer: Framebuffer
    private lateinit var shaderGroup: ShaderGroup
    private var lastStrength = -1f
    private var lastX = -1f
    private var lastY = -1f
    private var lastW = -1f
    private var lastH = -1f
    
    private fun setupFramebuffers() {
    try {
        shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight)
    } catch (e: Exception) {
        ClientUtils.logError("Exception caught while setting up shader group", e)
    }
}
    private fun sizeHasChanged(scale: Int, width: Int, height: Int): Boolean {
    return scale != lastScale || width != lastScaleWidth || height != lastScaleHeight
}
    private fun setValues(
    strength: Float,
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    screenWidth: Float,
    screenHeight: Float,
    force: Boolean
) {
    if (!force && strength == lastStrength && lastX == x && lastY == y && lastW == w && lastH == h) {
        return
    }

    lastStrength = strength
    lastX = x
    lastY = y
    lastW = w
    lastH = h

    for (i in 0 until 2) {
        val shader = BlurUtils.shaderGroup.listShaders[i]
        shader.getShaderManager().getShaderUniform("Radius").set(strength)
        shader.getShaderManager().getShaderUniform("BlurXY").set(x, screenHeight - y - h)
        shader.getShaderManager().getShaderUniform("BlurCoord").set(w, h)
    }
}
    
    fun blur(
    posX: Float,
    posY: Float,
    posXEnd: Float,
    posYEnd: Float,
    blurStrength: Float,
    displayClipMask: Boolean,
    triggerMethod: () -> Unit
) {
    if (!OpenGlHelper.isFramebufferEnabled()) return

    if (!(Interface.INSTANCE.shaderValue.get() as Boolean) || !(Interface.INSTANCE.blurValue.get() as Boolean)) return

    var x = posX
    var y = posY
    var x2 = posXEnd
    var y2 = posYEnd

    if (x > x2) {
        val tmp = x
        x = x2
        x2 = tmp
    }

    if (y > y2) {
        val tmp = y
        y = y2
        y2 = tmp
    }

    val scaledResolution = ScaledResolution(MinecraftInstance.mc)
    val scaleFactor = scaledResolution.scaleFactor
    val width = scaledResolution.scaledWidth
    val height = scaledResolution.scaledHeight

    if (BlurUtils.sizeHasChanged(scaleFactor, width, height)) {
    BlurUtils.setupFramebuffers()
    BlurUtils.setValues(
    blurStrength,
    x,
    y,
    x2 - x,
    y2 - y,
    width.toFloat(),
    height.toFloat(),
    true
)
}

    lastFactor = scaleFactor
    lastWidth = width
    lastHeight = height

    BlurUtils.setValues(
    blurStrength,
    x,
    y,
    x2 - x,
    y2 - y,
    width.toFloat(),
    height.toFloat(),
    false
)

    framebuffer.bindFramebuffer(true)
    shaderGroup.loadShaderGroup(MinecraftInstance.mc.timer.renderPartialTicks)
    MinecraftInstance.mc.framebuffer.bindFramebuffer(true)

    Stencil.write(displayClipMask)
    triggerMethod.invoke()
    Stencil.erase(true)

    GlStateManager.enableBlend()
    GlStateManager.blendFunc(770, 771)
    GlStateManager.pushMatrix()
    GlStateManager.colorMask(true, true, true, false)
    GlStateManager.enableAlpha()
    GlStateManager.depthMask(false)
    GlStateManager.disableLighting()
    GlStateManager.disableFog()
    GlStateManager.disableDepth()

    frbuffer.bindFramebufferTexture()

    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)

    val f2 = frbuffer.framebufferWidth.toDouble() / frbuffer.framebufferTextureWidth.toDouble()
    val f3 = frbuffer.framebufferHeight.toDouble() / frbuffer.framebufferTextureHeight.toDouble()

    val tessellator = Tessellator.getInstance()
    val worldrenderer = tessellator.worldRenderer

    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)

    worldrenderer.pos(0.0, height.toDouble(), 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex()
    worldrenderer.pos(width.toDouble(), height.toDouble(), 0.0).tex(f2, 0.0).color(255, 255, 255, 255).endVertex()
    worldrenderer.pos(width.toDouble(), 0.0, 0.0).tex(f2, f3).color(255, 255, 255, 255).endVertex()
    worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, f3).color(255, 255, 255, 255).endVertex()

    tessellator.draw()

    frbuffer.unbindFramebuffer()

    GlStateManager.disableAlpha()
    GlStateManager.depthMask(true)
    GlStateManager.colorMask(true, true, true, true)
    GlStateManager.popMatrix()
    GlStateManager.	disableBlend()
    Stencil.dispose()
    GlStateManager.enableDepth()
}
    @JvmStatic
    fun blurAreaRounded(x: Float, y: Float, x2: Float, y2: Float, rad: Float, blurStrength: Float) {
    if (!(Interface.shaderValue.get() as Boolean) || !(Interface.blurValue.get() as Boolean)) return

    BlurUtils.blur(x, y, x2, y2, blurStrength, false) {
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.blendFunc(770, 771)
        RenderUtils.fastRoundedRect(x, y, x2, y2, rad)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }
}

    private fun reinitShader() {
        blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight)
        buffer = Framebuffer(mc.displayWidth, mc.displayHeight, true)
        buffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f)
    }

    fun draw(x: Float, y: Float, width: Float, height: Float, radius: Float) {
        val scale = StaticStorage.scaledResolution ?: return
        val factor = scale.scaleFactor
        val factor2 = scale.scaledWidth
        val factor3 = scale.scaledHeight
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3) {
            reinitShader()
        }
        lastScale = factor
        lastScaleWidth = factor2
        lastScaleHeight = factor3
        blurShader.listShaders[0].shaderManager.getShaderUniform("BlurXY")[x] = factor3 - y - height
        blurShader.listShaders[1].shaderManager.getShaderUniform("BlurXY")[x] = factor3 - y - height
        blurShader.listShaders[0].shaderManager.getShaderUniform("BlurCoord")[width] = height
        blurShader.listShaders[1].shaderManager.getShaderUniform("BlurCoord")[width] = height
        blurShader.listShaders[0].shaderManager.getShaderUniform("Radius").set(radius)
        blurShader.listShaders[1].shaderManager.getShaderUniform("Radius").set(radius)
        blurShader.loadShaderGroup(mc.timer.renderPartialTicks)
        mc.framebuffer.bindFramebuffer(true)
    }
}
