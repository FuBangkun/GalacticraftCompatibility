package com.fubangkun.galacticraftcompatibility.mixin.vanilla;

import com.fubangkun.galacticraftcompatibility.compat.ShaderCompat;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Skips custom sky renderers while an OptiFine shader pack is active.
 *
 * <p>Add-ons draw their skies with direct OpenGL calls, outside the shader program OptiFine
 * binds around the sky pass. The raw geometry ends up in the wrong buffers and the sky turns
 * black or flickers. Reporting no custom renderer lets the vanilla sky path run inside the
 * shader pass instead. When the pack is turned off, the renderer is used again.</p>
 *
 * <p>The renderer is left installed in the world provider on purpose. Several add-ons build
 * their sky lazily inside {@code getSkyRenderer()}, so repeatedly clearing the provider would
 * recreate the renderer every frame.</p>
 */
@Mixin(value = RenderGlobal.class, remap = false)
public abstract class RenderGlobalMixin {
    @Redirect(method = "renderSky(FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldProvider;getSkyRenderer()Lnet/minecraftforge/client/IRenderHandler;"))
    private IRenderHandler gcc$hideCustomSky(WorldProvider provider) {
        if (ShaderCompat.isShaderPackActive()) {
            return null;
        }
        return provider.getSkyRenderer();
    }
}
