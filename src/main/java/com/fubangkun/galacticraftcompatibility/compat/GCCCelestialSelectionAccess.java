package com.fubangkun.galacticraftcompatibility.compat;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;

import java.util.List;

/**
 * Exposes the celestial map fields of the Galacticraft Core base class.
 *
 * <p>The fields are declared in {@code GuiCelestialSelection}, while {@code @Shadow} only
 * resolves members declared by the target class itself. The base-class mixin shadows them and
 * implements this interface, which the AsmodeusCore map mixin then uses.</p>
 */
public interface GCCCelestialSelectionAccess {
    List<CelestialBody> gcc$getBodiesToRender();

    void gcc$setBodiesToRender(List<CelestialBody> bodies);

    CelestialBody gcc$getSelectedBody();

    void gcc$setSelectedBody(CelestialBody body);

    void gcc$setSelectedParent(Object parent);

    void gcc$setSelectedStationOwner(String owner);

    boolean gcc$isRenamingSpaceStation();
}
