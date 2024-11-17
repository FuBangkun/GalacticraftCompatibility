package com.FuBangkun.galacticraftcompatibility;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies =
                "required-before:galacticraftcore;" +
                        "before:extraplanets;" +
                        "before:galaxyspace;" +
                        "before:moreplanets;" +
                        "before:exoplanets;" +
                        "before:asmodeuscore"
)
public class GCC {
    public static File ConfigDirectory;
    public static boolean extraplanets = Loader.isModLoaded("extraplanets");
    public static boolean galaxyspace = Loader.isModLoaded("galaxyspace");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config;
        ConfigDirectory = event.getModConfigurationDirectory();
        if (Config.enableConfiguration && extraplanets) {
            config = new Configuration(new File(ConfigDirectory, "ExtraPlanets.cfg"));
            config.load();
            if (galaxyspace) {
                config.get("general settings", "Use Custom Galaxy Map/Celestial Selection Screen", true).set(false);
                config.get("compatibility support", "Enable Galaxy Space Compatibility", false).set(true);
                config.get("space stations", "Venus SpaceStation", true).set(false);
                config.get("space stations", "Mars SpaceStation", true).set(false);
            }
            if (Loader.isModLoaded("moreplanets")) {
                config.get("compatibility support", "Enable More Planets Compatibility", false).set(true);
            }
            config.save();
        }
        if (Loader.isModLoaded("exoplanets")) {
            config = new Configuration(new File(ConfigDirectory, "Exoplanets/Core.cfg"));
            config.load();
            config.get("Core Mod Settings", "warnBetaBuild", true).set(false);
            config.save();
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandOpenConfiguration());
    }
}
