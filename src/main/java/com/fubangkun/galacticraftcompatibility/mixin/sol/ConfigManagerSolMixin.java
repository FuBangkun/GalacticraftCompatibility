package com.fubangkun.galacticraftcompatibility.mixin.sol;

import mod.sol.config.ConfigManagerSol;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

/**
 * The Sol replaces the map with its own {@code SolCelestialSelection}. This forces the switch
 * off after config load so the AsmodeusCore map is used instead.
 */
@Mixin(value = ConfigManagerSol.class, remap = false)
public abstract class ConfigManagerSolMixin {
    @Inject(method = "init(Ljava/io/File;)V", at = @At("RETURN"))
    private static void gcc$lockCustomGalaxymap(File file, CallbackInfo ci) {
        ConfigManagerSol.enableCustomGalaxymap = false;
    }
}
