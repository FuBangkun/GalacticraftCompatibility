/*
 * Copyright (c) 2024 FuBangkun. All Rights Reserved.
 */

package com.FuBangkun.galacticraftcompatibility;

import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.FuBangkun.galacticraftcompatibility.Constants.*;
import static com.FuBangkun.galacticraftcompatibility.GuiConfiguration.setConfigValue;

@Mod(
        modid = MOD_ID,
        name = MOD_NAME,
        version = Tags.VERSION,
        dependencies =
                "required-after:galacticraftcore;" +
                        "after:galacticraftplanets;" +
                        "before:extraplanets;" +
                        "before:galaxyspace;" +
                        "before:moreplanets;" +
                        "before:exoplanets;" +
                        "before:asmodeuscore;" +
                        "before:sol"
)
public class GCC {
    public static File ConfigDirectory;
    public static Logger logger;

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        if (LRM) throw new RuntimeException("Please remove Legacy Rocket Model.");
        if (PP && GR) throw new RuntimeException("Please remove Planet Progression or Galactic Research.");
        if (TTS && TTSR) throw new RuntimeException("Please remove ToTheStars (Not ToTheStarsRemake).");
        if (TTS) logger.info("Please remove ToTheStars and install ToTheStarsRemake");
        RenderingRegistry.registerEntityRenderingHandler(EntityTier2Rocket.class, manager -> new RenderTier2Rocket(manager, new ModelTier2Rocket()));
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
        if (EXO) setConfigValue(exo, false, "Core Mod Settings", "warnBetaBuild");
        if (SOL && (GS || EP)) setConfigValue(sol, false, "The Sol - Misc", "Enable Custom Galaxymap?");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandOpenConfiguration());
    }
}
