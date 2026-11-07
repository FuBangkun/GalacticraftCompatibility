package com.fubangkun.galacticraftcompatibility;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:galacticraftcore;required-after:mixinbooter;required-after:asmodeuscore")
public class GalacticraftCompatibility {
    private static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (Loader.isModLoaded("tothestars")) {
            LOGGER.warn("ToTheStars conflicts with the AsmodeusCore galaxy map. Install ToTheStarsRemake instead.");
        }
        if (Loader.isModLoaded("galacticresearch") && Loader.isModLoaded("planetprogression")) {
            LOGGER.warn("Galactic Research and Planet Progression both replace the celestial selection map "
                    + "and keep separate research progress. Use only one of them.");
        }
    }
}
