package com.fubangkun.galacticraftcompatibility.mixin.galaxyspace;

import galaxyspace.core.configs.GSConfigCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keeps the GalaxySpace pressure and radiation systems enabled, so the suit compatibility
 * paths stay reachable, and turns off the GalaxySpace main menu replacement. All other GS
 * settings keep the player's values.
 */
@Mixin(value = GSConfigCore.class, remap = false)
public abstract class GSConfigCoreMixin {
    @Inject(method = "syncConfig", at = @At("RETURN"))
    private static void gcc$lockEnvironmentSystems(boolean load, CallbackInfo ci) {
        GSConfigCore.enablePressureSystem = true;
        GSConfigCore.enableRadiationSystem = true;
        GSConfigCore.enableNewMenu = false;
    }
}
