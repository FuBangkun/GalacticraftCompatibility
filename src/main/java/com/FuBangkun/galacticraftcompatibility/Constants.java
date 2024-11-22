package com.FuBangkun.galacticraftcompatibility;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

import static com.FuBangkun.galacticraftcompatibility.GCC.ConfigDirectory;

public class Constants {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final boolean EP = Loader.isModLoaded("extraplanets");
    public static final boolean GS = Loader.isModLoaded("galaxyspace");
    public static final boolean MP = Loader.isModLoaded("moreplanets");
    public static final boolean EXO = Loader.isModLoaded("exoplanets");
    public static final boolean TTS = Loader.isModLoaded("tothestars");
    public static final boolean TTSR = Loader.isModLoaded("tothestarsremake");
    public static final boolean PP = Loader.isModLoaded("planetprogression");
    public static final boolean GR = Loader.isModLoaded("galacticresearch");
    public static final boolean GE = EP && GS;
    public static final Configuration ac = new Configuration(new File(ConfigDirectory, "AsmodeusCore/core.conf"));
    public static final Configuration ep = new Configuration(new File(ConfigDirectory, "ExtraPlanets.cfg"));
    public static final Configuration gsc = new Configuration(new File(ConfigDirectory, "GalaxySpace/core.conf"));
    public static final Configuration gsd = new Configuration(new File(ConfigDirectory, "GalaxySpace/dimensions.conf"));
    public static final Configuration exo = new Configuration(new File(ConfigDirectory, "Exoplanets/Core.cfg"));
    public static final String front = "gui.galacticraftcompatibility.";
}