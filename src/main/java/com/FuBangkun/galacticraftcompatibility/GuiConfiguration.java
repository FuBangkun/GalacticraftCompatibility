package com.FuBangkun.galacticraftcompatibility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.Objects;

import static com.FuBangkun.galacticraftcompatibility.GCC.ConfigDirectory;

@SideOnly(Side.CLIENT)
public class GuiConfiguration extends GuiScreen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecraft:textures/blocks/dirt.png");
    public static String currentScreen = "gui.galacticraftcompatibility.map";
    public int[] selectedButtonsIndex = {-1, -1, -1, -1, -1, -1};

    public GuiConfiguration() {}

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();

        if (!Config.enableConfiguration) addButton(new GuiButton(100, 50, height - 40, 100, 20, I18n.format("gui.galacticraftcompatibility.exit")));

        switch (currentScreen) {
            case "gui.galacticraftcompatibility.map":
                addButton(new GuiButton(0, width / 2 - 100, height / 2 - 30, 200, 20, I18n.format("gui.galacticraftcompatibility.gc")));
                addButton(new GuiButton(1, width / 2 - 100, height / 2 , 200, 20, I18n.format("gui.galacticraftcompatibility.ac")));
                addButton(new GuiButton(2, width / 2 - 100, height / 2 + 30, 200, 20, I18n.format("gui.galacticraftcompatibility.ep")));
                break;
            case "gui.galacticraftcompatibility.mars":
                addButton(new GuiButton(0, width / 2 - 20, height - 40, 40, 20, I18n.format("gui.back")));
                addButton(new GuiButton(1, width / 2 - 100, height / 2 - 15, 200, 20, I18n.format("gui.galacticraftcompatibility.gs")));
                addButton(new GuiButton(2, width / 2 - 100, height / 2 + 15, 200, 20, I18n.format("gui.galacticraftcompatibility.ep")));
                break;
            case "gui.galacticraftcompatibility.venus":
                addButton(new GuiButton(0, width / 2 - 20, height - 40, 40, 20, I18n.format("gui.back")));
                addButton(new GuiButton(1, width / 2 - 100, height / 2 - 15, 200, 20, I18n.format("gui.galacticraftcompatibility.gs")));
                addButton(new GuiButton(2, width / 2 - 100, height / 2 + 15, 200, 20, I18n.format("gui.galacticraftcompatibility.ep")));
                break;
            case "gui.galacticraftcompatibility.shaders":
                if (GCC.galaxyspace && GCC.extraplanets) addButton(new GuiButton(0, width / 2 - 20, height - 40, 40, 20, I18n.format("gui.back")));
                addButton(new GuiButton(1, width / 2 - 100, height / 2 - 15, 200, 20, I18n.format("gui.yes")));
                addButton(new GuiButton(2, width / 2 - 100, height / 2 + 15, 200, 20, I18n.format("gui.no")));
                break;
            case "gui.galacticraftcompatibility.menu":
            case "gui.galacticraftcompatibility.craft":
                addButton(new GuiButton(0, width / 2 - 20, height - 40, 40, 20, I18n.format("gui.back")));
                addButton(new GuiButton(1, width / 2 - 100, height / 2 - 15, 200, 20, I18n.format("gui.yes")));
                addButton(new GuiButton(2, width / 2 - 100, height / 2 + 15, 200, 20, I18n.format("gui.no")));
                break;
            case "gui.galacticraftcompatibility.warning":
                addButton(new GuiButton(0, width / 2 - 20, height - 40, 40, 20, I18n.format("gui.back")));
                addButton(new GuiButton(1, width / 2 - 100, height / 2, 200, 20, I18n.format("menu.quit")));
            case "gui.galacticraftcompatibility.done":
                addButton(new GuiButton(0, width / 2 - 20, height - 40, 40, 20, I18n.format("gui.back")));
                if (Config.enableConfiguration) {
                    addButton(new GuiButton(1, width / 2 - 100, height / 2, 200, 20, I18n.format("gui.galacticraftcompatibility.quit")));
                } else {
                    addButton(new GuiButton(1, width / 2 - 250, height / 2, 200, 20, I18n.format("gui.galacticraftcompatibility.quit")));
                    addButton(new GuiButton(2, width / 2 + 50, height / 2, 200, 20, I18n.format("gui.galacticraftcompatibility.continue")));
                }
                break;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 100) {
            Minecraft.getMinecraft().displayGuiScreen(null);
            return;
        }

        switch (currentScreen) {
            case "gui.galacticraftcompatibility.map":
                currentScreen = "gui.galacticraftcompatibility.mars";
                selectedButtonsIndex[0] = button.id;
                break;
            case "gui.galacticraftcompatibility.mars":
                if (button.id == 0) {
                    currentScreen = "gui.galacticraftcompatibility.map";
                } else {
                    currentScreen = "gui.galacticraftcompatibility.venus";
                    selectedButtonsIndex[1] = button.id;
                }
                break;
            case "gui.galacticraftcompatibility.venus":
                if (button.id == 0) {
                    currentScreen = "gui.galacticraftcompatibility.mars";
                } else {
                    currentScreen = "gui.galacticraftcompatibility.shaders";
                    selectedButtonsIndex[2] = button.id;
                }
                break;
            case "gui.galacticraftcompatibility.shaders":
                if (button.id == 0) {
                    currentScreen = "gui.galacticraftcompatibility.venus";
                } else {
                    currentScreen = "gui.galacticraftcompatibility.menu";
                    selectedButtonsIndex[3] = button.id;
                }
                break;
            case "gui.galacticraftcompatibility.menu":
                if (button.id == 0) {
                    currentScreen = "gui.galacticraftcompatibility.shaders";
                } else {
                    currentScreen = "gui.galacticraftcompatibility.craft";
                    selectedButtonsIndex[4] = button.id;
                }
                break;
            case "gui.galacticraftcompatibility.craft":
                if (button.id == 0) {
                    currentScreen = "gui.galacticraftcompatibility.menu";
                } else {
                    if ((Loader.isModLoaded("tothestars") || (Loader.isModLoaded("planetprogression") && Loader.isModLoaded("galacticresearch")))) {
                        currentScreen = "gui.galacticraftcompatibility.warning";
                    } else {
                        currentScreen = "gui.galacticraftcompatibility.done";
                        selectedButtonsIndex[5] = button.id;
                        Modify();
                    }
                }
                break;
            case "gui.galacticraftcompatibility.warning":
                if (button.id == 0) {
                    currentScreen = "gui.galacticraftcompatibility.craft";
                } else {
                    Minecraft.getMinecraft().shutdown();
                }
                break;
            case "gui.galacticraftcompatibility.done":
                switch (button.id) {
                    case 0:
                        currentScreen = "gui.galacticraftcompatibility.craft";
                        break;
                    case 1:
                        Config.enableConfiguration = false;
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
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        this.drawTexturedModalRect(0, 0, 0, 0, this.width, this.height);

        drawTexturedModalRect(0, 0, 0, 0, width, height);
        drawCenteredString(fontRenderer, I18n.format("gui.galacticraftcompatibility.title"), width / 2, 20, 0xffffff);
        if (!Objects.equals(currentScreen, "gui.galacticraftcompatibility.warning")) {
            drawCenteredString(fontRenderer, I18n.format(currentScreen), width / 2, height / 5, 0xffffff);
        } else {
            String var = "";
            if (Loader.isModLoaded("tothestars") && !Loader.isModLoaded("tothestarsremake")) {
                var += I18n.format("gui.galacticraftcompatibility.warning1" + System.lineSeparator());
            }
            if (Loader.isModLoaded("tothestars") && Loader.isModLoaded("tothestarsremake")) {
                var += I18n.format("gui.galacticraftcompatibility.warning2" + System.lineSeparator());
            }
            if (Loader.isModLoaded("planetprogression") && Loader.isModLoaded("galacticresearch")) {
                var += I18n.format("gui.galacticraftcompatibility.warning3");
            }
            drawCenteredString(fontRenderer, var, width / 2, height / 5, 0xffffff);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void Modify() {
        Configuration ac = new Configuration(new File(ConfigDirectory, "AsmodeusCore/core.conf"));
        Configuration ep = new Configuration(new File(ConfigDirectory, "ExtraPlanets.cfg"));
        Configuration gsc = new Configuration(new File(ConfigDirectory, "GalaxySpace/core.conf"));
        Configuration gsd = new Configuration(new File(ConfigDirectory, "GalaxySpace/dimensions.conf"));
        if (GCC.galaxyspace && GCC.extraplanets) {
            switch (selectedButtonsIndex[0]) {
                case 0:
                    setConfigValue(ep, false, "general settings", "Use Custom Galaxy Map/Celestial Selection Screen");
                    setConfigValue(ac, false, "galaxymap", "enableNewGalaxyMap");
                    break;
                case 1:
                    setConfigValue(ep, false, "general settings", "Use Custom Galaxy Map/Celestial Selection Screen");
                    setConfigValue(ac, true, "galaxymap", "enableNewGalaxyMap");
                    break;
                case 2:
                    setConfigValue(ep, true, "general settings", "Use Custom Galaxy Map/Celestial Selection Screen");
                    setConfigValue(ac, false, "galaxymap", "enableNewGalaxyMap");
                    break;
            }
            switch (selectedButtonsIndex[1]) {
                case 1:
                    setConfigValue(ep, true, "space stations", "Mars SpaceStation");
                    setConfigValue(gsd, false, "general", "enableMarsSpaceStation");
                case 2:
                    setConfigValue(ep, false, "space stations", "Mars SpaceStation");
                    setConfigValue(gsd, true, "general", "enableMarsSpaceStation");
            }
            switch (selectedButtonsIndex[2]) {
                case 1:
                    setConfigValue(ep, true, "space stations", "Venus SpaceStation");
                    setConfigValue(gsd, false, "general", "enableVenusSpaceStation");
                case 2:
                    setConfigValue(ep, false, "space stations", "Venus SpaceStation");
                    setConfigValue(gsd, true, "general", "enableVenusSpaceStation");
            }
        }
        if (GCC.galaxyspace) {
            switch (selectedButtonsIndex[3]) {
                case 1:
                    setConfigValue(ac, false, "client", "enableSkyAsteroids");
                    setConfigValue(ac, false, "client", "enableSkyMoon");
                    setConfigValue(ac, false, "client", "enableSkyOverworld");
                    setConfigValue(ac, false, "client", "enableSkyOverworldOrbit");
                case 2:
                    setConfigValue(ac, true, "client", "enableSkyAsteroids");
                    setConfigValue(ac, true, "client", "enableSkyMoon");
                    setConfigValue(ac, true, "client", "enableSkyOverworld");
                    setConfigValue(ac, true, "client", "enableSkyOverworldOrbit");
            }
            switch (selectedButtonsIndex[4]) {
                case 1:
                    setConfigValue(gsc, true, "client", "enableNewMenu");
                case 2:
                    setConfigValue(gsc, false, "client", "enableNewMenu");
            }
            switch (selectedButtonsIndex[5]) {
                case 1:
                    setConfigValue(gsc, true, "hardmode", "enableAdvancedRocketCraft");
                case 2:
                    setConfigValue(gsc, false, "hardmode", "enableAdvancedRocketCraft");
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return super.doesGuiPauseGame();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {}

    private void setConfigValue(Configuration config, boolean value, String category, String key) {
        config.load();
        config.get(category, key, true).set(value);
        config.save();
    }

}
