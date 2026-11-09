package com.fubangkun.galacticraftcompatibility.mixin.vanilla;

import com.fubangkun.galacticraftcompatibility.compat.ShaderCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Skips custom sky renderers while an OptiFine shader pack is active.
 *
 * <p>Add-ons draw their skies with direct OpenGL calls, outside the shader program OptiFine
 * binds around the sky pass. The raw geometry ends up in the wrong buffers and the sky turns
 * black or flickers. Reporting no custom renderer lets the vanilla sky path run inside the
 * shader pass instead. When the pack is turned off, the renderer is used again.</p>
 *
 * <p>Renderers are detached only while {@code renderSky} runs and put back right after, so the
 * add-ons that create a renderer whenever the provider reports none never see a missing one and
 * do not recreate it every frame.</p>
 *
 * <p>The lookup is done through the provider instead of redirecting
 * {@code WorldProvider.getSkyRenderer()} at the call site: that method is added by Forge and has
 * no obfuscated name, so a member target cannot be remapped for production and the injection
 * fails there even though it works in a development workspace.</p>
 */
@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {
    @Unique
    private IRenderHandler gcc$detachedSkyRenderer;

    @Inject(method = "renderSky(FI)V", at = @At("HEAD"), require = 0)
    private void gcc$detachCustomSky(float partialTicks, int pass, CallbackInfo ci) {
        this.gcc$detachedSkyRenderer = null;
        if (!ShaderCompat.isShaderPackActive()) {
            return;
        }
        WorldProvider provider = Minecraft.getMinecraft().world.provider;
        IRenderHandler renderer = provider.getSkyRenderer();
        if (renderer != null) {
            this.gcc$detachedSkyRenderer = renderer;
            provider.setSkyRenderer(null);
        }
    }

    @Inject(method = "renderSky(FI)V", at = @At("RETURN"), require = 0)
    private void gcc$reattachCustomSky(float partialTicks, int pass, CallbackInfo ci) {
        if (this.gcc$detachedSkyRenderer == null) {
            return;
        }
        Minecraft.getMinecraft().world.provider.setSkyRenderer(this.gcc$detachedSkyRenderer);
        this.gcc$detachedSkyRenderer = null;
    }
}
