package com.fubangkun.galacticraftcompatibility;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
@Config(modid = Tags.MOD_ID, name = Tags.MOD_NAME)
public final class GCCConfig {
    @Config.Name("First Launch")
    @Config.Comment({
            "Set to true to show the GCC configuration GUI on the next launch. ",
            "Automatically set to false after the GUI has been shown."
    })
    @Config.RequiresMcRestart
    public static boolean firstLaunch = true;

    @Config.Name("Apply Configuration on Next Launch")
    @Config.Comment({
            "If true, GCC will apply the selected configuration during the next launch. ",
            "This is set automatically by the configuration GUI."
    })
    @Config.RequiresMcRestart
    public static boolean applyNextLaunch = false;

    @Config.Name("Map Selection")
    @Config.Comment({
            "Which galaxy map to use. 0 = Galacticraft, 1 = AsmodeusCore, 2 = ExtraPlanets, 3 = The Sol.",
            "ToTheStars is incompatible with AsmodeusCore's galaxy map, so don't choose AsmodeusCore when ToTheStars is installed.",
            "Consider using ToTheStarsRemake, which resolves this incompatibility."
    })
    @Config.RangeInt(min = 0, max = 3)
    public static int mapSelection = 1;

    @Config.Name("Space Station Selection")
    @Config.Comment("Which space station implementation to use. 0 = GalaxySpace, 1 = ExtraPlanets.")
    @Config.RangeInt(min = 0, max = 1)
    public static int spaceStationSelection = 0;

    @Config.Name("Remove Duplicate Planets")
    @Config.Comment("If true, duplicate planets shared by GalaxySpace and ExtraPlanets will be disabled to avoid conflicts.")
    public static boolean removeDuplicatePlanets = false;

    @Config.Name("Preferred Planet Mod")
    @Config.Comment({
            "Only effective when 'Remove Duplicate Planets' is enabled.",
            "Which mod's planets to keep when removing duplicates. 0 = GalaxySpace, 1 = ExtraPlanets.",
            "GalaxySpace's Neptune will not be disabled, as doing so would cause a crash due to a known issue in GalaxySpace."
    })
    @Config.RangeInt(min = 0, max = 1)
    public static int preferredPlanetMod = 0;

    @Config.Name("Enable Shaders")
    @Config.Comment("Enable AsmodeusCore custom sky shaders (client-side only).")
    public static boolean enableShaders = false;

    @Config.Name("Enable New Menu")
    @Config.Comment("Enable GalaxySpace's new main menu background (client-side only).")
    public static boolean enableNewMenu = false;

    @Config.Name("Enable Advanced Craft")
    @Config.Comment("Enable GalaxySpace's advanced (hard-mode) rocket crafting recipes.")
    public static boolean enableAdvancedCraft = true;

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Tags.MOD_ID)) {
            ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
        }
    }
}