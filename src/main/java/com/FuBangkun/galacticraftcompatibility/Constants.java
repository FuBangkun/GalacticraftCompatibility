/*
 * Copyright (c) 2025 FuBangkun. All Rights Reserved.
 */

package com.FuBangkun.galacticraftcompatibility;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

import static com.FuBangkun.galacticraftcompatibility.GCC.ConfigDirectory;

public class Constants {
    public static final String        front    = "gui.galacticraftcompatibility.";
    public static boolean AC;
    public static boolean       EP;
    public static boolean       GS;
    public static boolean       MP;
    public static boolean       EXO;
    public static boolean       TTS;
    public static boolean       TTSR;
    public static boolean       PP;
    public static boolean       GR;
    public static boolean       SOL;
    public static boolean       LRM;
    public static boolean       GE;
    public static Configuration ac;
    public static Configuration ep;
    public static Configuration gsc;
    public static Configuration gsd;
    public static Configuration exo;
    public static Configuration sol;

    public static void init() {
        AC = Loader.isModLoaded("asmodeuscore");
        EP   = Loader.isModLoaded("extraplanets");
        GS   = Loader.isModLoaded("galaxyspace");
        MP   = Loader.isModLoaded("moreplanets");
        EXO  = Loader.isModLoaded("exoplanets");
        TTS  = Loader.isModLoaded("tothestars");
        TTSR = Loader.isModLoaded("tothestarsremake");
        PP   = Loader.isModLoaded("planetprogression");
        GR   = Loader.isModLoaded("galacticresearch");
        SOL  = Loader.isModLoaded("sol");
        LRM  = Loader.isModLoaded("legacy_rocket_model");
        GE   = EP && GS;
        ac   = new Configuration(new File(ConfigDirectory, "AsmodeusCore/core.conf"));
        ep   = new Configuration(new File(ConfigDirectory, "ExtraPlanets.cfg"));
        gsc  = new Configuration(new File(ConfigDirectory, "GalaxySpace/core.conf"));
        gsd  = new Configuration(new File(ConfigDirectory, "GalaxySpace/dimensions.conf"));
        exo  = new Configuration(new File(ConfigDirectory, "Exoplanets/Core.cfg"));
        sol  = new Configuration(new File(ConfigDirectory, "sol/sol.conf"));
    }
}