package com.fubangkun.galacticraftcompatibility;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required-after:galacticraftcore;" +
                "required-after:galacticraftplanets;" +
                "required-before:extraplanets;" +
                "required-before:galaxyspace;" +
                "before:moreplanets;" +
                "before:exoplanets;" +
                "before:sol"
)
public class GCC {
    public static Configuration ac, ep, gsc, gsd, sol, gcc;
    public static boolean isTTSLoaded;
    public static boolean isSolLoaded;
    public static Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    private static void setConfigValue(Configuration config, boolean value, String category, String key) {
        if (config == null) {
            return;
        }
        config.load();
        config.get(category, key, true).set(value);
        config.save();
    }

    private static void setConfigValue(Configuration config, boolean value, String category, String... keys) {
        if (config == null) {
            return;
        }
        config.load();
        for (String key : keys) {
            config.get(category, key, true).set(value);
        }
        config.save();
    }

    private static void applySelectedConfiguration(int mapSelection, int spaceStationSelection, boolean removeDuplicatePlanets, int preferredPlanetMod, boolean enableShaders, boolean enableNewMenu, boolean enableAdvancedCraft, boolean isClient) {
        setConfigValue(ac, mapSelection == 1, "galaxymap", "enableNewGalaxyMap");
        setConfigValue(ep, mapSelection == 2, "general settings", "Use Custom Galaxy Map/Celestial Selection Screen");

        if (isSolLoaded) {
            setConfigValue(sol, mapSelection == 3, "the sol - misc", "Enable Custom Galaxymap?");
        }

        setConfigValue(gsd, spaceStationSelection == 0, "general", "enableMarsSpaceStation", "enableVenusSpaceStation");
        setConfigValue(ep, spaceStationSelection == 1, "space stations", "Mars SpaceStation", "Venus SpaceStation");

        setConfigValue(ep, true, "compatibility support", "Enable Galaxy Space Compatibility");
        if (removeDuplicatePlanets) {
            setConfigValue(ep, false, "compatibility support", "Enable Galaxy Space Compatibility");
            if (preferredPlanetMod == 0) {
                setConfigValue(ep, false, "main dimensions", "Mercury & Tier 4 Rocket", "Jupiter & Tier 5 Rocket", "Saturn & Tier 6 Rocket", "Uranus & Tier 7 Rocket", "Neptune & Tier 8 Rocket", "Pluto & Tier 9 Rocket", "Eris & Tier 10 Rocket");
                setConfigValue(ep, false, "other dimensions", "Triton", "Europa", "IO", "Deimos", "Callisto", "Ganymede", "Rhea", "Titan", "Oberon", "Titania", "Iapetus", "Ceres", "Kuiper Belt", "Unreachable moons on the Celestial Selection Screen");
            } else if (preferredPlanetMod == 1) {
                setConfigValue(gsd, false, "general", "enableMercury", "enableJupiter", "enableSaturn", "enableUranus", "enablePluto", "enableCeres", "enableKuiperBelt");
                // setConfigValue(gsd, false, "enableNeptune"); //Galaxy Space's bug
            }
        }

        setConfigValue(gsc, enableAdvancedCraft, "hardmode", "enableAdvancedRocketCraft");

        if (isClient) {
            setConfigValue(ac, enableShaders, "client", "enableSkyAsteroids", "enableSkyMoon", "enableSkyOverworld", "enableSkyOverworldOrbit");
            setConfigValue(gsc, enableNewMenu, "client", "enableNewMenu");
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configDirectory = event.getModConfigurationDirectory();

        ac = new Configuration(new File(configDirectory, "AsmodeusCore/core.conf"));
        ep = new Configuration(new File(configDirectory, "ExtraPlanets.cfg"));
        gsc = new Configuration(new File(configDirectory, "GalaxySpace/core.conf"));
        gsd = new Configuration(new File(configDirectory, "GalaxySpace/dimensions.conf"));
        sol = new Configuration(new File(configDirectory, "sol/sol.conf"));
        gcc = new Configuration(new File(configDirectory, Tags.MOD_NAME + ".cfg"));

        isTTSLoaded = Loader.isModLoaded("tothestars");
        isSolLoaded = Loader.isModLoaded("sol");

        boolean isClient = event.getSide() == Side.CLIENT;

        if (GCCConfig.firstLaunch) {
            if (!isClient) {
                LOGGER.fatal("=====================================================================");
                LOGGER.fatal("| First launch detected!                                            |");
                LOGGER.fatal("| Please configure Galacticraft Compatibility manually in config.   |");
                LOGGER.fatal("| The server will now stop. Modify the config and start again.      |");
                LOGGER.fatal("=====================================================================");

                GCCConfig.firstLaunch = false;
                GCCConfig.applyNextLaunch = true;

                setConfigValue(gcc, false, "general", "First Launch");
                setConfigValue(gcc, true, "general", "Apply Configuration on Next Launch");

                FMLCommonHandler.instance().exitJava(0, false);
            } else {
                setConfigValue(ep, false, "space stations", "Venus SpaceStation", "Mars SpaceStation");
                if (isSolLoaded) {
                    setConfigValue(sol, false, "the sol - misc", "Enable Custom Galaxymap?");
                }
            }
        } else if (GCCConfig.applyNextLaunch) {
            applySelectedConfiguration(
                    GCCConfig.mapSelection,
                    GCCConfig.spaceStationSelection,
                    GCCConfig.removeDuplicatePlanets,
                    GCCConfig.preferredPlanetMod,
                    GCCConfig.enableShaders,
                    GCCConfig.enableNewMenu,
                    GCCConfig.enableAdvancedCraft,
                    isClient
            );

            GCCConfig.applyNextLaunch = false;

            setConfigValue(gcc, false, "general", "Apply Configuration on Next Launch");
        }

        if (Loader.isModLoaded("exoplanets")) {
            setConfigValue(new Configuration(new File(configDirectory, "Exoplanets/Core.cfg")), false, "Core Mod Settings", "warnBetaBuild");
        }

        NetworkHandler.init();
    }
}