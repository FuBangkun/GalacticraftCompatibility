package com.FuBangkun.galacticraftcompatibility;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.File;

import static com.FuBangkun.galacticraftcompatibility.Constants.*;

@Mod(
        modid = MOD_ID,
        name = MOD_NAME,
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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigDirectory = event.getModConfigurationDirectory();
        if (Config.enableConfiguration && EP) {
            Configuration config = ep;
            config.load();
            if (GS) {
                config.get("general settings", "Use Custom Galaxy Map/Celestial Selection Screen", true).set(false);
                config.get("compatibility support", "Enable Galaxy Space Compatibility", false).set(true);
                config.get("space stations", "Venus SpaceStation", true).set(false);
                config.get("space stations", "Mars SpaceStation", true).set(false);
            }
            if (MP) config.get("compatibility support", "Enable More Planets Compatibility", false).set(true);
            config.save();
        }
        if (EXO) GuiConfiguration.setConfigValue(exo, false, "Core Mod Settings", "warnBetaBuild");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandOpenConfiguration());
    }
}
