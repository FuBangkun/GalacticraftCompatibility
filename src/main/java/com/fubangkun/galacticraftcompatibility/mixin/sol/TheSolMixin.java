package com.fubangkun.galacticraftcompatibility.mixin.sol;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import mod.sol.TheSol;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The Sol registers many bodies whose names collide with GalaxySpace entries (Mercury, Venus,
 * Jupiter, Saturn, Pluto, the Kuiper Belt and their moons). Together with the
 * {@code CelestialBodyMixin} / {@code CelestialListMixin} registry changes these stay registered
 * and appear as the "The Sol" variant in the map.
 *
 * <p>Two kinds of registration are skipped:</p>
 * <ol>
 *     <li>objects GC already registered, such as The Sol's duplicate
 *     {@code AsteroidsModule.planetAsteroids}, so their ownerId is not rewritten to sol;</li>
 *     <li>moons whose parent is not a Sol planet. {@code phobos / deimos} orbit the GC Mars and
 *     would become orphan moons under the GS / EP Mars.</li>
 * </ol>
 */
@Mixin(value = TheSol.class, remap = false)
public abstract class TheSolMixin {
    /**
     * RegistryHandlerMixin runs init ahead of time at the end of the item registry event, which
     * already assigns the celestial body fields. Running init again in its real phase would
     * register everything twice, so skip it here.
     */
    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void gcc$skipLateInit(FMLInitializationEvent event, CallbackInfo ci) {
        if (TheSol.planetVenus != null) {
            ci.cancel();
        }
    }

    @Redirect(method = "init(Lnet/minecraftforge/fml/common/event/FMLInitializationEvent;)V", at = @At(value = "INVOKE", target = "Lmicdoodle8/mods/galacticraft/api/galaxies/GalaxyRegistry;registerPlanet(Lmicdoodle8/mods/galacticraft/api/galaxies/Planet;)Z"))
    private boolean gcc$registerPlanet(Planet planet) {
        if (planet == null || GalaxyRegistry.getPlanets().contains(planet)) {
            return false;
        }
        GalaxyRegistry.register(planet);
        return true;
    }

    @Redirect(method = "init(Lnet/minecraftforge/fml/common/event/FMLInitializationEvent;)V", at = @At(value = "INVOKE", target = "Lmicdoodle8/mods/galacticraft/api/galaxies/GalaxyRegistry;registerMoon(Lmicdoodle8/mods/galacticraft/api/galaxies/Moon;)Z"))
    private boolean gcc$registerMoon(Moon moon) {
        if (moon == null) {
            return false;
        }
        Planet parent = moon.getParentPlanet();
        if (parent != TheSol.planetJupiter && parent != TheSol.planetSaturn
                && parent != TheSol.planetUranus && parent != TheSol.planetNeptune
                && parent != TheSol.planetPluto) {
            return false;
        }
        GalaxyRegistry.register(moon);
        return true;
    }
}
