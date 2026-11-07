package com.fubangkun.galacticraftcompatibility.mixin.galacticraftcore;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialObject;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.list.ImmutableCelestialList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The join handshake sends a translationKey list from the client, where same-name variants
 * collapse into one key, while the server checks every entry of {@code getAllRegisteredObjects()}
 * with {@code contains()}. Because the registry changes keep the GS / EP / Sol duplicates, one key
 * maps to several instances with different ownerIds, the comparison fails and the player is
 * kicked with "Missing Galacticraft Celestial Objects". The list used for that comparison is
 * deduplicated by translationKey here, keeping the first registered instance, which is also the
 * one the client resolves.
 */
@Mixin(value = PacketSimple.class, remap = false)
public abstract class PacketSimpleMixin {
    @Redirect(method = "handleServerSide", at = @At(value = "INVOKE", target = "Lmicdoodle8/mods/galacticraft/api/galaxies/GalaxyRegistry;getAllRegisteredObjects()Lmicdoodle8/mods/galacticraft/core/util/list/ImmutableCelestialList;"))
    private static ImmutableCelestialList<CelestialObject> gcc$dedupeRegisteredObjects() {
        List<CelestialObject> deduped = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (CelestialObject object : GalaxyRegistry.getAllRegisteredObjects()) {
            if (seen.add(object.getTranslationKey())) {
                deduped.add(object);
            }
        }
        return ImmutableCelestialList.of(deduped);
    }
}
