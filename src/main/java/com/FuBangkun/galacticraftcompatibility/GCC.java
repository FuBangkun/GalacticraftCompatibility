/*
 * Copyright (c) 2025 FuBangkun. All Rights Reserved.
 */

package com.FuBangkun.galacticraftcompatibility;

import com.FuBangkun.galacticraftcompatibility.client.RenderTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.FuBangkun.galacticraftcompatibility.Constants.*;
import static com.FuBangkun.galacticraftcompatibility.GuiConfiguration.setConfigValue;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
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
    private static final String name = "GCC Translation Correction Resource Pack";
    public static        File   ConfigDirectory;
    public static        Logger logger;

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigDirectory = event.getModConfigurationDirectory();
        logger = event.getModLog();
        Constants.init();
        if (LRM) throw new RuntimeException(I18n.format(front + "error4"));
        if (PP && GR) throw new RuntimeException(I18n.format(front + "error3"));
        if (TTS && TTSR) throw new RuntimeException(I18n.format(front + "error2"));
        if (TTS) logger.info(I18n.format(front + "error1"));
        RenderingRegistry.registerEntityRenderingHandler(EntityTier2Rocket.class, manager -> new RenderTier2Rocket(manager, new ModelTier2Rocket()));
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

        try (JarFile jarFile = new JarFile(event.getSourceFile())) {
            extractResourcePack(jarFile);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandOpenConfiguration());
    }

    private void extractResourcePack(JarFile jarFile) throws IOException {
        String resourcePackPath = "resourcepacks/" + name + "/";
        File   targetDir        = new File(new File(Minecraft.getMinecraft().gameDir, "resourcepacks"), name);
        if (jarFile.getJarEntry(resourcePackPath + "pack.mcmeta") == null) targetDir.delete();
        FileUtils.deleteDirectory(targetDir);
        targetDir.mkdirs();
        for (JarEntry entry : jarFile.stream().toArray(JarEntry[]::new))
            if (entry.getName().startsWith(resourcePackPath)) {
                String relativePath = entry.getName().substring(resourcePackPath.length());
                File   targetFile   = new File(targetDir, relativePath);
                if (entry.isDirectory()) targetFile.mkdirs();
                else {
                    try (InputStream is = jarFile.getInputStream(entry);
                         FileOutputStream fos = new FileOutputStream(targetFile)) {
                        byte[] buffer = new byte[1024];
                        int    length;
                        while ((length = is.read(buffer)) > 0) fos.write(buffer, 0, length);
                    }
                }
            }
    }
}