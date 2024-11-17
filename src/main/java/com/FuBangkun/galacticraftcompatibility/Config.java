package com.FuBangkun.galacticraftcompatibility;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
@net.minecraftforge.common.config.Config(modid = Tags.MOD_ID)
public class Config {
    @Name("Enable configuration interface")
    public static boolean enableConfiguration = true;

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        if (enableConfiguration && event.getGui() instanceof GuiMainMenu) {
            if (!GCC.extraplanets || !GCC.galaxyspace) GuiConfiguration.currentScreen = "gui.galacticraftcompatibility.shaders";
            event.setGui(new GuiConfiguration());
        }
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Tags.MOD_ID)) {
            ConfigManager.sync(Tags.MOD_ID, net.minecraftforge.common.config.Config.Type.INSTANCE);
        }
    }
}
