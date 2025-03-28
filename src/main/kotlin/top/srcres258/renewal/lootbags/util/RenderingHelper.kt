package top.srcres258.renewal.lootbags.util

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.renderer.CoreShaders
import net.minecraft.resources.ResourceLocation

fun setShaderTexture(shaderTexture: Int, textureLoc: ResourceLocation) {
    RenderSystem.setShader(CoreShaders.POSITION_TEX)
    RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
    RenderSystem.setShaderTexture(shaderTexture, textureLoc)
}