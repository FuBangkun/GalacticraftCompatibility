package com.fubangkun.galacticraftcompatibility;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        acceptedMinecraftVersions = "1.12.2",
        dependencies = "required-after:galacticraftcore;" +
                "required-after:galacticraftplanets;" +
                "required-before:extraplanets;" +
                "required-before:galaxyspace;" +
                "before:moreplanets;" +
                "before:exoplanets;" +
                "before:sol"
)
public class GCC {
    public static Configuration ac, ep, gsc, gsd, sol;
    public static boolean isTTSLoaded;
    public static boolean isSolLoaded;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        if (GCCConfig.displayConfiguration && event.getGui() instanceof GuiMainMenu) event.setGui(new GuiConfiguration());
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Tags.MOD_ID)) ConfigManager.sync(Tags.MOD_ID, net.minecraftforge.common.config.Config.Type.INSTANCE);
    }

    public static void setConfigValue(Configuration config, boolean value, String category, String key) {
        config.load();
        config.get(category, key, true).set(value);
        config.save();
    }

    public static void setConfigValue(Configuration config, boolean value, String category, String... keys) {
        config.load();
        for (String key : keys) config.get(category, key, true).set(value);
        config.save();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configDirectory = event.getModConfigurationDirectory();
        ac = new Configuration(new File(configDirectory, "AsmodeusCore/core.conf"));
        ep = new Configuration(new File(configDirectory, "ExtraPlanets.cfg"));
        gsc = new Configuration(new File(configDirectory, "GalaxySpace/core.conf"));
        gsd = new Configuration(new File(configDirectory, "GalaxySpace/dimensions.conf"));
        sol = new Configuration(new File(configDirectory, "sol/sol.conf"));
        isTTSLoaded = Loader.isModLoaded("tothestars");
        isSolLoaded = Loader.isModLoaded("sol");
        if (GCCConfig.displayConfiguration) setConfigValue(ep, false, "space stations", "Venus SpaceStation", "Mars SpaceStation");
        if (Loader.isModLoaded("exoplanets")) setConfigValue(new Configuration(new File(configDirectory, "Exoplanets/Core.cfg")), false, "Core Mod Settings", "warnBetaBuild");
        if (GCCConfig.displayConfiguration && isSolLoaded) setConfigValue(sol, false, "the sol - misc", "Enable Custom Galaxymap?");
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandOpenConfiguration());
    }

    @SideOnly(Side.SERVER)
    @Mod.EventHandler
    public void onServerStart(FMLServerStartedEvent event) {
        Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
        LOGGER.error("==========================================================================================");
        LOGGER.error("| On the server side, the \"/gcc\" command of Galacticraft Compatibility is not available! |");
        LOGGER.error("==========================================================================================");
    }
}