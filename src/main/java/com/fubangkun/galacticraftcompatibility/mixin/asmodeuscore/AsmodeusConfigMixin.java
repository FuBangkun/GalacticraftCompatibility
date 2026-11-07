package com.fubangkun.galacticraftcompatibility.mixin.asmodeuscore;

import asmodeuscore.core.configs.AsmodeusConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Locks the AsmodeusCore map switches so the AC map is always used.
 * The fields are overwritten after config sync, so editing the config file has no effect.
 */
@Mixin(value = AsmodeusConfig.class, remap = false)
public abstract class AsmodeusConfigMixin {
    @Inject(method = "syncConfig", at = @At("RETURN"))
    private static void gcc$lockGalaxyMap(boolean load, CallbackInfo ci) {
        AsmodeusConfig.enableNewGalaxyMap = true;
        AsmodeusConfig.enableNewTierSystem = true;
    }
}
