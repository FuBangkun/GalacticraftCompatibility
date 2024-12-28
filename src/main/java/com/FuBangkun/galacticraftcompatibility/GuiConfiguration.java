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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.FuBangkun.galacticraftcompatibility.Constants.*;

@SideOnly(Side.CLIENT)
public class GuiConfiguration extends GuiScreen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("galacticraftcore:textures/blocks/deco_block.png");
    public static String currentScreen = front + "map";
    public int[] selectedButtonsIndex = {-1, -1, -1, -1, -1, -1, -1};

    public GuiConfiguration() {}

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();

        if (!Config.enableConfiguration) Button(100, 200, front + "exit", 100);
        switch (currentScreen) {
            case "gui.galacticraftcompatibility.map":
                Button(0, front + "gc", -30);
                Button(1, front + "ac", 0);
                Button(2, front + "ep", 30);
                break;
            case "gui.galacticraftcompatibility.mars":
            case "gui.galacticraftcompatibility.venus":
                Button(0, 20, "gui.back", 40);
                Button(1, front + "gs", -15);
                Button(2, front + "ep", 15);
                break;
            case "gui.galacticraftcompatibility.planets":
                Button(0, 20, "gui.back", 40);
                Button(1, front + "gs", -30);
                Button(2, front + "ep", 0);
                Button(3, front + "exit", 30);
                break;
            case "gui.galacticraftcompatibility.shaders":
                if (GE) Button(0, 20, "gui.back", 40);
                Button(1, "gui.yes", -15);
                Button(2, "gui.no", 15);
                break;
            case "gui.galacticraftcompatibility.menu":
            case "gui.galacticraftcompatibility.craft":
                Button(0, 20, "gui.back", 40);
                Button(1, "gui.yes", -15);
                Button(2, "gui.no", 15);
                break;
            case "gui.galacticraftcompatibility.done":
                Button(0, 20, "gui.back", 40);
                Button(1, Config.enableConfiguration ? 50 : 125, front + "quit");
                if (!Config.enableConfiguration) Button(2, -25, front + "continue");
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
            case front + "map":
                currentScreen = front + "mars";
                selectedButtonsIndex[0] = button.id;
                break;
            case "gui.galacticraftcompatibility.mars":
                ScreenChange(button.id,"map", "venus", 1);
                break;
            case "gui.galacticraftcompatibility.venus":
                ScreenChange(button.id,"mars", "planets", 2);
                break;
            case "gui.galacticraftcompatibility.planets":
                ScreenChange(button.id,"venus", "shaders", 3);
                break;
            case "gui.galacticraftcompatibility.shaders":
                if (button.id == 0) currentScreen = front + "planets";
                else {
                    if (GS) currentScreen = front + "menu";
                    else {
                        currentScreen = front + "done";
                        selectedButtonsIndex[4] = button.id;
                        Modify();
                    }
                }
                break;
            case "gui.galacticraftcompatibility.menu":
                ScreenChange(button.id,"shaders", "craft", 5);
                break;
            case "gui.galacticraftcompatibility.craft":
                if (button.id == 0) currentScreen = front + "menu";
                else {
                    currentScreen = front + "done";
                    selectedButtonsIndex[6] = button.id;
                    Modify();
                }
                break;
            case "gui.galacticraftcompatibility.done":
                switch (button.id) {
                    case 0:
                        if (GS) currentScreen = front + "craft";
                        else currentScreen = front + "shaders";
                        break;
                    case 1:
                        Config.enableConfiguration = false;
                        MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.OnConfigChangedEvent(MOD_ID, MOD_NAME, false, false));
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
        int w = width / 2;
        int h = height / 2;

        drawTexturedModalRect(0, 0, 0, 0, width, height);
        drawCenteredString(fontRenderer, I18n.format(front + "title"), w, h - 80, 0xffffff);
        drawCenteredString(fontRenderer, I18n.format(currentScreen), w, h - 60, 0xffffff);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void Modify() {
        if (SOL && (GS || EP)) {
            setConfigValue(sol, false, "The Sol - Misc", "Enable Custom Galaxymap?");
        }
        if (GE) {
            if (selectedButtonsIndex[0] == 0) {
                setConfigValue(ep, false, "general settings", "Use Custom Galaxy Map/Celestial Selection Screen");
                setConfigValue(ac, false, "galaxymap", "enableNewGalaxyMap");
            } else {
                setConfigValue(ac, selectedButtonsIndex[0] == 1, "galaxymap", "enableNewGalaxyMap");
                setConfigValue(ep, selectedButtonsIndex[0] == 2, "general settings", "Use Custom Galaxy Map/Celestial Selection Screen");
            }
            setConfigValue(gsd, selectedButtonsIndex[1] == 1, "general", "enableMarsSpaceStation");
            setConfigValue(ep, selectedButtonsIndex[1] == 2, "space stations", "Mars SpaceStation");
            setConfigValue(gsd, selectedButtonsIndex[2] == 1, "general", "enableVenusSpaceStation");
            setConfigValue(ep, selectedButtonsIndex[2] == 2, "space stations", "Venus SpaceStation");
            if (selectedButtonsIndex[3] == 1) {
                setConfigValue(ep, false, "main dimensions", "Mercury & Tier 4 Rocket");
                setConfigValue(ep, false, "main dimensions", "Jupiter & Tier 5 Rocket");
                setConfigValue(ep, false, "main dimensions", "Saturn & Tier 6 Rocket");
                setConfigValue(ep, false, "main dimensions", "Uranus & Tier 7 Rocket");
                setConfigValue(ep, false, "main dimensions", "Neptune & Tier 8 Rocket");
                setConfigValue(ep, false, "main dimensions", "Pluto & Tier 9 Rocket");
                setConfigValue(ep, false, "main dimensions", "Eris & Tier 10 Rocket");
                setConfigValue(ep, false, "other dimensions", "Triton");
                setConfigValue(ep, false, "other dimensions", "Europa");
                setConfigValue(ep, false, "other dimensions", "IO");
                setConfigValue(ep, false, "other dimensions", "Deimos");
                setConfigValue(ep, false, "other dimensions", "Callisto");
                setConfigValue(ep, false, "other dimensions", "Ganymede");
                setConfigValue(ep, false, "other dimensions", "Rhea");
                setConfigValue(ep, false, "other dimensions", "Titan");
                setConfigValue(ep, false, "other dimensions", "Oberon");
                setConfigValue(ep, false, "other dimensions", "Titania");
                setConfigValue(ep, false, "other dimensions", "Iapetus");
                setConfigValue(ep, false, "other dimensions", "Ceres");
            } else if (selectedButtonsIndex[3] == 2) {
                setConfigValue(ep, false, "compatibility support", "Enable Galaxy Space Compatibility");
                setConfigValue(gsd, false, "general", "enableMercury");
                setConfigValue(gsd, false, "general", "enableJupiter");
                setConfigValue(gsd, false, "general", "enableSaturn");
                setConfigValue(gsd, false, "general", "enableUranus");
                setConfigValue(gsd, false, "general", "enableNeptune");
                setConfigValue(gsd, false, "general", "enablePluto");
                setConfigValue(gsd, false, "general", "enableCeres");
            }
        }
        if (GS) {
            if (selectedButtonsIndex[4] == 1) {
                setConfigValue(ac, false, "client", "enableSkyAsteroids");
                setConfigValue(ac, false, "client", "enableSkyMoon");
                setConfigValue(ac, false, "client", "enableSkyOverworld");
                setConfigValue(ac, false, "client", "enableSkyOverworldOrbit");
            } else if (selectedButtonsIndex[4] == 2) {
                    setConfigValue(ac, true, "client", "enableSkyAsteroids");
                    setConfigValue(ac, true, "client", "enableSkyMoon");
                    setConfigValue(ac, true, "client", "enableSkyOverworld");
                    setConfigValue(ac, true, "client", "enableSkyOverworldOrbit");
            }
            setConfigValue(gsc, selectedButtonsIndex[5] == 1, "client", "enableNewMenu");
            setConfigValue(gsc, selectedButtonsIndex[6] == 1, "hardmode", "enableAdvancedRocketCraft");
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return super.doesGuiPauseGame();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {}

    public static void setConfigValue(Configuration config, boolean value, String category, String key) {
        config.load();
        config.get(category, key, true).set(value);
        config.save();
    }

    private void Button(int id, String value, int y) {
        addButton(new GuiButton(id, width / 2 - 50, height / 2 + y, 100, 20, I18n.format(value)));
    }

    private void Button(int id, int x, String value) {
        addButton(new GuiButton(id, width / 2 - x, height / 2, 100, 20, I18n.format(value)));
    }

    private void Button(int id, int x, String value, int w) {
        addButton(new GuiButton(id, width / 2 - x, height / 2 + 80, w, 20, I18n.format(value)));
    }

    private void ScreenChange(int id, String last, String next, int index) {
        if (id == 0) currentScreen = front + last;
        else {
            currentScreen = front + next;
            selectedButtonsIndex[index] = id;
        }
    }
}
