package com.fubangkun.galacticraftcompatibility.mixin.extraplanets;

import com.mjr.extraplanets.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Locks the ExtraPlanets options that affect GalaxySpace compatibility.
 *
 * <p>Other settings keep the player's values. The config file stays in place; values here are
 * overwritten whenever {@code load()} returns, so local edits to these keys do not stick.</p>
 */
@Mixin(value = Config.class, remap = false)
public abstract class ConfigMixin {
    @Inject(method = "load", at = @At("RETURN"))
    private static void gcc$lockCompatConfig(CallbackInfo ci) {
        // Keep body names as registered; EP's GS compatibility mode renames them and changes tiers.
        Config.GALAXY_SPACE_COMPATIBILITY = false;
        // Use the AsmodeusCore map instead of EP's own map.
        Config.USE_CUSTOM_CELESTIAL_SELECTION = false;

        // Galacticraft allows one space station type per planet, so the GS and EP Venus/Mars
        // stations conflict. GS stations are kept and EP's are disabled; other EP stations are
        // unaffected.
        Config.VENUS_SPACE_STATION = false;
        Config.MARS_SPACE_STATION = false;

        // Keep every body registered; the map hides duplicates per mode.
        Config.MERCURY = true;
        Config.CERES = true;
        Config.JUPITER = true;
        Config.SATURN = true;
        Config.URANUS = true;
        Config.NEPTUNE = true;
        Config.PLUTO = true;
        Config.ERIS = true;
        Config.TRITON = true;
        Config.EUROPA = true;
        Config.IO = true;
        Config.DEIMOS = true;
        Config.PHOBOS = true;
        Config.CALLISTO = true;
        Config.GANYMEDE = true;
        Config.RHEA = true;
        Config.TITAN = true;
        Config.OBERON = true;
        Config.TITANIA = true;
        Config.IAPETUS = true;
        Config.MAKEMAKE = true;
        Config.HAUMEA = true;
        Config.KUIPER_BELT = true;
        Config.KEPLER_SOLAR_SYSTEMS = true;
        Config.KEPLER22B = true;
        Config.GENERATE_UNREACHABLEMOONS = true;
    }
}
