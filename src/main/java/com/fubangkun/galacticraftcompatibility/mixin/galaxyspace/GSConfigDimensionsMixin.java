package com.fubangkun.galacticraftcompatibility.mixin.galaxyspace;

import galaxyspace.core.configs.GSConfigDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Forces every GalaxySpace body and station switch on.
 * Duplicate bodies are handled by the map's mode filter instead of by disabling content.
 */
@Mixin(value = GSConfigDimensions.class, remap = false)
public abstract class GSConfigDimensionsMixin {
    @Inject(method = "syncConfig", at = @At("RETURN"))
    private static void gcc$lockBodies(boolean load, CallbackInfo ci) {
        GSConfigDimensions.enableMercury = true;
        GSConfigDimensions.enableVenus = true;
        GSConfigDimensions.enableCeres = true;
        GSConfigDimensions.enableKuiperBelt = true;
        GSConfigDimensions.enablePluto = true;
        GSConfigDimensions.enableHaumea = true;
        GSConfigDimensions.enableMakemake = true;
        GSConfigDimensions.enablePhobos = true;
        GSConfigDimensions.enableDeimos = true;
        GSConfigDimensions.enableEuropa = true;
        GSConfigDimensions.enableCallisto = true;
        GSConfigDimensions.enableIo = true;
        GSConfigDimensions.enableGanymede = true;
        GSConfigDimensions.enableEnceladus = true;
        GSConfigDimensions.enableTitan = true;
        GSConfigDimensions.enableOberon = true;
        GSConfigDimensions.enableMiranda = true;
        GSConfigDimensions.enableProteus = true;
        GSConfigDimensions.enableTriton = true;
        GSConfigDimensions.enableJupiter = true;
        GSConfigDimensions.enableSaturn = true;
        GSConfigDimensions.enableUranus = true;
        GSConfigDimensions.enableNeptune = true;
        GSConfigDimensions.enableVenusSpaceStation = true;
        GSConfigDimensions.enableMarsSpaceStation = true;
    }
}
