package com.fubangkun.galacticraftcompatibility.mixin.galacticraftcore;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialObject;
import micdoodle8.mods.galacticraft.core.util.list.CelestialList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

/**
 * {@code CelestialList} keys its map by body name, so a later registration with the same name is
 * dropped and never shows up through {@code getRegistered()}. The first entry keeps the plain
 * name key to preserve name lookups; later same-name entries with a different ownerId go under a
 * name + ownerId key, so {@code getRegistered()} exposes every variant.
 */
@Mixin(value = CelestialList.class, remap = false)
public abstract class CelestialListMixin {
    @Shadow
    private Map<String, CelestialObject> celestialListMap;

    /**
     * Stores the body under the plain name on first registration and under a name + ownerId key
     * afterward.
     *
     * @param name   body name
     * @param object body to store
     * @return null on the first write, the existing value when the key is already present
     * @author FuBangkun
     * @reason keep every same-name variant visible through {@code getRegistered()}
     */
    @Overwrite
    private CelestialObject mapPut(String name, CelestialObject object) {
        if (!this.celestialListMap.containsKey(name)) {
            return this.celestialListMap.put(name, object);
        }
        String owner = object.getOwnerId();
        String variantKey = name + '\u0001' + (owner == null ? "" : owner);
        return this.celestialListMap.putIfAbsent(variantKey, object);
    }
}
