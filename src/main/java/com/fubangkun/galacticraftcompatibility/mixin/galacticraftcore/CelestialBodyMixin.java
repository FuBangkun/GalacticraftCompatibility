package com.fubangkun.galacticraftcompatibility.mixin.galacticraftcore;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

/**
 * GC compares celestial bodies by translationKey alone, so same-name bodies from different mods
 * are treated as one object: they cannot coexist in the registry and overwrite each other in
 * hash-based lists. Including ownerId keeps the GS / EP / Sol versions distinct.
 */
@Mixin(value = CelestialBody.class, remap = false)
public abstract class CelestialBodyMixin {
    /**
     * Compares translationKey and ownerId so same-name bodies from different mods stay distinct.
     *
     * @param other object to compare with
     * @return true when both translationKey and ownerId match
     * @author FuBangkun
     * @reason let same-name bodies with different ownerIds coexist in the registry
     */
    @Override
    @Overwrite
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CelestialBody)) {
            return false;
        }
        CelestialObject self = (CelestialObject) (Object) this;
        CelestialBody body = (CelestialBody) other;
        return self.getTranslationKey().equals(body.getTranslationKey()) && Objects.equals(self.getOwnerId(), body.getOwnerId());
    }

    /**
     * Consistent with {@link #equals(Object)}.
     *
     * @return hash of translationKey and ownerId
     * @author FuBangkun
     * @reason keep the hash contract in sync with the ownerId-aware equals
     */
    @Override
    @Overwrite
    public int hashCode() {
        CelestialObject self = (CelestialObject) (Object) this;
        return Objects.hash(self.getTranslationKey(), self.getOwnerId());
    }
}
