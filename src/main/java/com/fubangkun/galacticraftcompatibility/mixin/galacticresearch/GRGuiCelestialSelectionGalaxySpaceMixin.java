package com.fubangkun.galacticraftcompatibility.mixin.galacticresearch;

import com.fubangkun.galacticraftcompatibility.compat.CelestialVariants;
import com.fubangkun.galacticraftcompatibility.compat.GCCCelestialSelectionAccess;
import igentuman.galacticresearch.client.gui.GRGuiCelestialSelectionGalaxySpace;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Galactic Research replaces the AsmodeusCore map with its own copy of the screen and overrides
 * the two body list methods the variant filter hooks ({@code refreshBodies} and
 * {@code getChildren}), so the filter does not run there. Repeating it on the copy keeps the
 * GalaxySpace / ExtraPlanets / The Sol switching functional while Galactic Research is installed.
 */
@Mixin(value = GRGuiCelestialSelectionGalaxySpace.class, remap = false)
public abstract class GRGuiCelestialSelectionGalaxySpaceMixin {
    @Inject(method = "refreshBodies", at = @At("TAIL"))
    private void gcc$filterBodies(CallbackInfo ci) {
        GCCCelestialSelectionAccess access = (GCCCelestialSelectionAccess) this;
        access.gcc$setBodiesToRender(
                CelestialVariants.filter(access.gcc$getBodiesToRender(), CelestialVariants.mode()));
    }

    @Inject(method = "getChildren(Ljava/lang/Object;II)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void gcc$filterChildren(Object object, int start, int size, CallbackInfoReturnable<List<CelestialBody>> cir) {
        cir.setReturnValue(CelestialVariants.filter(cir.getReturnValue(), CelestialVariants.mode()));
    }

    /**
     * {@code refreshBodies} is the only place this screen synchronizes its state when opened
     * (its own {@code initGui} does not call the parent one), so the mode is restored here.
     */
    @Inject(method = "refreshBodies", at = @At("HEAD"))
    private void gcc$initMode(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            CelestialVariants.resetFromDimension(mc.player.dimension);
        }
    }
}
