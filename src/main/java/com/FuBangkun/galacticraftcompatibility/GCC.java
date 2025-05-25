/*
 * Copyright (c) 2024-2025 FuBangkun. All Rights Reserved.
 */

package com.FuBangkun.galacticraftcompatibility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.File;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
@Config(modid = Tags.MOD_ID)
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
    private static final String        front                = "gui." + Tags.MOD_ID + ".";
    @Config.Name("Display GCC Configuration Interface")
    public static        boolean       displayConfiguration = true;
    private static       Configuration ac, ep, gsc, gsd, sol;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onGuiOpen(GuiOpenEvent event) {
        if (displayConfiguration && event.getGui() instanceof GuiMainMenu) event.setGui(new GuiConfiguration());
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Tags.MOD_ID)) ConfigManager.sync(Tags.MOD_ID, net.minecraftforge.common.config.Config.Type.INSTANCE);
    }

    private static void setConfigValue(Configuration config, boolean value, String category, String key) {
        config.load();
        config.get(category, key, true).set(value);
        config.save();
    }

    private static void setConfigValue(Configuration config, boolean value, String category, String... keys) {
        config.load();
        for (String key : keys) config.get(category, key, true).set(value);
        config.save();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configDirectory = event.getModConfigurationDirectory();
        ac  = new Configuration(new File(configDirectory, "AsmodeusCore/core.conf"));
        ep  = new Configuration(new File(configDirectory, "ExtraPlanets.cfg"));
        gsc = new Configuration(new File(configDirectory, "GalaxySpace/core.conf"));
        gsd = new Configuration(new File(configDirectory, "GalaxySpace/dimensions.conf"));
        sol = new Configuration(new File(configDirectory, "sol/sol.conf"));
        if (displayConfiguration) setConfigValue(ep, false, "space stations", "Venus SpaceStation", "Mars SpaceStation");
        if (Loader.isModLoaded("exoplanets")) setConfigValue(new Configuration(new File(configDirectory, "Exoplanets/Core.cfg")), false, "Core Mod Settings", "warnBetaBuild");
        if (Loader.isModLoaded("sol")) setConfigValue(sol, false, "The Sol - Misc", "Enable Custom Galaxymap?");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (event.getSide() == Side.CLIENT) {
            event.registerServerCommand(new CommandOpenConfiguration());
        }
    }

    @SideOnly(Side.CLIENT)
    private static class CommandOpenConfiguration extends CommandBase {
        @Override
        public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
            if (sender.getEntityWorld().isRemote) {
                Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiConfiguration()));
            }
        }

        @Nonnull
        @Override
        public String getName() {
            return "gcc";
        }

        @Nonnull
        @Override
        public String getUsage(@Nonnull ICommandSender sender) {
            return "/gcc";
        }
    }

    @SideOnly(Side.CLIENT)
    private static class GuiConfiguration extends GuiScreen {
        private static String currentScreen        = front + "map";
        private final  int[]  selectedButtonsIndex = {-1, -1, -1, -1, -1, -1};

        @Override
        public void initGui() {
            super.initGui();
            buttonList.clear();
            if (!displayConfiguration) Button(100, front + "exit", 45);
            if (!currentScreen.equals(front + "map")) Button(0, "gui.back", width / 2 - 50);
            GuiButton button;
            switch (currentScreen) {
                case front + "map":
                    Button(0, -30, front + "gc");
                    Button(1, 0, front + "ac");
                    button = new GuiButton(1, width / 2 - 50, height / 2, 100, 20, I18n.format(front + "ac"));
                    button.enabled = !Loader.isModLoaded("tothestarsremake");
                    addButton(button);
                    Button(2, 30, front + "ep");
                    break;
                case front + "ss":
                    Button(1, -15, front + "gs");
                    Button(2, 15, front + "ep");
                    break;
                case front + "planets":
                    button = new GuiButton(1, width / 2 - 50, height / 2 - 30, 100, 20, I18n.format(front + "gs"));
                    button.enabled = false;
                    addButton(button);
                    Button(2, 0, front + "ep");
                    Button(3, 30, front + "exit");
                    break;
                case front + "shaders":
                case front + "menu":
                case front + "craft":
                    Button(1, -15, "gui.yes");
                    Button(2, 15, "gui.no");
                    break;
                case front + "done":
                    Button(1, (displayConfiguration ? 0 : -15), front + "quit");
                    if (!displayConfiguration) Button(2, 15, front + "continue");
                    break;
            }
        }

        private void Button(int buttonId, int y, String buttonText) {
            addButton(new GuiButton(buttonId, width / 2 - 50, height / 2 + y, 100, 20, I18n.format(buttonText)));
        }

        private void Button(int buttonId, String buttonText, int x) {
            addButton(new GuiButton(buttonId, x, height - 45, 100, 20, I18n.format(buttonText)));
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            if (button.id == 100) {
                Minecraft.getMinecraft().displayGuiScreen(null);
                return;
            }
            switch (currentScreen) {
                case front + "map":
                    if (button.id != 1) {
                        currentScreen           = front + "ss";
                        selectedButtonsIndex[0] = button.id;
                    } else if (!Loader.isModLoaded("tothestarsremake")) {
                        currentScreen           = front + "ss";
                        selectedButtonsIndex[0] = button.id;
                    }
                    break;
                case front + "ss":
                    ScreenChange(button.id, "map", "planets", 1);
                    break;
                case front + "planets":
                    ScreenChange(button.id, "ss", "shaders", 2);
                    break;
                case front + "shaders":
                    ScreenChange(button.id, "planets", "menu", 3);
                    break;
                case front + "menu":
                    ScreenChange(button.id, "shaders", "craft", 4);
                    break;
                case front + "craft":
                    if (button.id == 0) currentScreen = front + "menu";
                    else {
                        currentScreen           = front + "done";
                        selectedButtonsIndex[5] = button.id;
                        Modify();
                    }
                    break;
                case front + "done":
                    switch (button.id) {
                        case 0:
                            currentScreen = front + "craft";
                            break;
                        case 1:
                            displayConfiguration = false;
                            MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.OnConfigChangedEvent(Tags.MOD_ID, Tags.MOD_NAME, false, false));
                            Minecraft.getMinecraft().shutdown();
                            break;
                        case 2:
                            Minecraft.getMinecraft().displayGuiScreen(null);
                            break;
                    }
                    break;
            }
            initGui();
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();
            int w = width / 2, h = height / 2;
            if (currentScreen.equals(front + "map") && Loader.isModLoaded("tothestarsremake")) drawCenteredString(fontRenderer, I18n.format(front + "map.warning1"), w, h - 65, 0xff0000);
            if (currentScreen.equals(front + "map") && Loader.isModLoaded("tothestarsremake")) drawCenteredString(fontRenderer, I18n.format(front + "map.warning2"), w, h - 50, 0xff0000);
            if (currentScreen.equals(front + "planets")) drawCenteredString(fontRenderer, I18n.format(front + "planets.warning1"), w, h - 65, 0xff0000);
            if (currentScreen.equals(front + "planets")) drawCenteredString(fontRenderer, I18n.format(front + "planets.warning2"), w, h - 50, 0xff0000);
            drawCenteredString(fontRenderer, I18n.format(front + "title"), w, h - 95, 0xffffff);
            drawCenteredString(fontRenderer, I18n.format(currentScreen), w, h - 80, 0xffffff);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @SideOnly(Side.CLIENT)
        private void Modify() {
            if (Loader.isModLoaded("sol")) setConfigValue(sol, false, "The Sol - Misc", "Enable Custom Galaxymap?");
            setConfigValue(ac, selectedButtonsIndex[0] == 1, "galaxymap", "enableNewGalaxyMap");
            setConfigValue(ep, selectedButtonsIndex[0] == 2, "general settings", "Use Custom Galaxy Map/Celestial Selection Screen");
            setConfigValue(gsd, selectedButtonsIndex[1] == 1, "general", "enableMarsSpaceStation", "enableVenusSpaceStation");
            setConfigValue(ep, selectedButtonsIndex[1] == 2, "space stations", "Mars SpaceStation", "Venus SpaceStation");
            if (selectedButtonsIndex[2] == 1) {
                //TODO: ERROR
                setConfigValue(ep, false, "main dimensions", "Mercury & Tier 4 Rocket", "Jupiter & Tier 5 Rocket", "Saturn & Tier 6 Rocket", "Uranus & Tier 7 Rocket", "Neptune & Tier 8 Rocket", "Pluto & Tier 9 Rocket", "Eris & Tier 10 Rocket");
                setConfigValue(ep, false, "other dimensions", "Triton", "Europa", "IO", "Deimos", "Callisto", "Ganymede", "Rhea", "Titan", "Oberon", "Titania", "Iapetus", "Ceres");
            } else {
                setConfigValue(ep, false, "compatibility support", "Enable Galaxy Space Compatibility");
                setConfigValue(gsd, false, "general", "enableMercury", "enableJupiter", "enableSaturn", "enableUranus", "enableNeptune", "enablePluto", "enableCeres");
            }
            setConfigValue(ac, selectedButtonsIndex[3] == 1, "client", "enableSkyAsteroids", "enableSkyMoon", "enableSkyOverworld", "enableSkyOverworldOrbit");
            setConfigValue(gsc, selectedButtonsIndex[4] == 1, "client", "enableNewMenu");
            setConfigValue(gsc, selectedButtonsIndex[5] == 1, "hardmode", "enableAdvancedRocketCraft");
        }

        @Override
        public void keyTyped(char typedChar, int keyCode) {
        }

        private void ScreenChange(int id, String last, String next, int index) {
            if (id == 0) currentScreen = front + last;
            else {
                currentScreen               = front + next;
                selectedButtonsIndex[index] = id;
            }
        }
    }
}