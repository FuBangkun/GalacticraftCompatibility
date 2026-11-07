package com.fubangkun.galacticraftcompatibility.mixin.galacticraftcore;

import com.fubangkun.galacticraftcompatibility.compat.CelestialVariants;
import com.fubangkun.galacticraftcompatibility.compat.GCCCelestialSelectionAccess;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Exposes the map fields declared by the GC base class and filters the single-argument
 * {@code getChildren}, which also drives moon counts, scroll limits and zoom layout.
 */
@Mixin(value = GuiCelestialSelection.class, remap = false)
public abstract class GuiCelestialSelectionMixin implements GCCCelestialSelectionAccess {
    @Shadow
    protected List<CelestialBody> bodiesToRender;

    @Shadow
    protected CelestialBody selectedBody;

    @Shadow
    protected Object selectedParent;

    @Shadow
    protected String selectedStationOwner;

    @Shadow
    protected boolean renamingSpaceStation;

    @Override
    public List<CelestialBody> gcc$getBodiesToRender() {
        return this.bodiesToRender;
    }

    @Override
    public void gcc$setBodiesToRender(List<CelestialBody> bodies) {
        this.bodiesToRender = bodies;
    }

    @Override
    public CelestialBody gcc$getSelectedBody() {
        return this.selectedBody;
    }

    @Override
    public void gcc$setSelectedBody(CelestialBody body) {
        this.selectedBody = body;
    }

    @Override
    public void gcc$setSelectedParent(Object parent) {
        this.selectedParent = parent;
    }

    @Override
    public void gcc$setSelectedStationOwner(String owner) {
        this.selectedStationOwner = owner;
    }

    @Override
    public boolean gcc$isRenamingSpaceStation() {
        return this.renamingSpaceStation;
    }

    @Inject(method = "getChildren(Ljava/lang/Object;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void gcc$filterChildren(Object object, CallbackInfoReturnable<List<CelestialBody>> cir) {
        cir.setReturnValue(CelestialVariants.filter(cir.getReturnValue(), CelestialVariants.mode()));
    }
}
