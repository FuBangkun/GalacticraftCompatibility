package com.fubangkun.galacticraftcompatibility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;

public class GuiConfiguration extends GuiScreen {
    private static final String front = "gui." + Tags.MOD_ID + ".";
    private static final int BTN_CANCEL = 1000;
    private static final int BTN_BACK = 1001;
    private static final int BTN_MAP_GC = 1;
    private static final int BTN_MAP_AC = 2;
    private static final int BTN_MAP_EP = 3;
    private static final int BTN_SS_GS = 4;
    private static final int BTN_SS_EP = 5;
    private static final int BTN_PLANETS_GS = 6;
    private static final int BTN_PLANETS_EP = 7;
    private static final int BTN_YES = 8;
    private static final int BTN_NO = 9;
    private static final int BTN_QUIT = 10;
    private static final int BTN_CONTINUE = 11;
    private int pendingButtonId = -1;
    private final UserSelections userSelections = new UserSelections();
    private ScreenState currentScreen = ScreenState.MAP_SELECTION;

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();

        createCommonButtons();
        createScreenSpecificButtons();
    }

    private void createCommonButtons() {
        if (!GCCConfig.displayConfiguration) {
            addButton(new GuiButton(BTN_CANCEL, 45, height - 45, 100, 20, I18n.format(front + "cancel")));
        }

        if (currentScreen != ScreenState.MAP_SELECTION) {
            addButton(new GuiButton(BTN_BACK, width / 2 - 50, height - 45, 100, 20, I18n.format("gui.back")));
        }
    }

    private void createScreenSpecificButtons() {
        int centerX = width / 2;
        int centerY = height / 2;

        switch (currentScreen) {
            case MAP_SELECTION:
                createMapSelectionButtons(centerX, centerY);
                break;

            case SPACE_STATION_SELECTION:
                createSpaceStationSelectionButtons(centerX, centerY);
                break;

            case DUPLICATE_SELECTION:
                createDuplicateSelectionButtons(centerX, centerY);
                break;

            case PLANETS_SELECTION:
                createPlanetsSelectionButtons(centerX, centerY);
                break;

            case SHADERS_SELECTION:
            case MENU_SELECTION:
            case CRAFT_SELECTION:
                createYesNoButtons(centerX, centerY);
                break;

            case COMPLETION:
                createCompletionButtons(centerX, centerY);
                break;
        }
    }

    private void createMapSelectionButtons(int centerX, int centerY) {
        addButton(new GuiButton(BTN_MAP_GC, centerX - 50, centerY - 30, 100, 20, I18n.format(front + "gc")));

        GuiButton acButton = new GuiButton(BTN_MAP_AC, centerX - 50, centerY, 100, 20, I18n.format(front + "ac"));
        acButton.enabled = !GCC.isTTSLoaded;
        addButton(acButton);

        addButton(new GuiButton(BTN_MAP_EP, centerX - 50, centerY + 30, 100, 20, I18n.format(front + "ep")));
    }

    private void createSpaceStationSelectionButtons(int centerX, int centerY) {
        addButton(new GuiButton(BTN_SS_GS, centerX - 50, centerY - 15, 100, 20, I18n.format(front + "gs")));
        addButton(new GuiButton(BTN_SS_EP, centerX - 50, centerY + 15, 100, 20, I18n.format(front + "ep")));
    }

    private void createDuplicateSelectionButtons(int centerX, int centerY) {
        addButton(new GuiButton(BTN_YES, centerX - 50, centerY - 15, 100, 20, I18n.format("gui.yes")));
        addButton(new GuiButton(BTN_NO, centerX - 50, centerY + 15, 100, 20, I18n.format("gui.no")));
    }

    private void createPlanetsSelectionButtons(int centerX, int centerY) {
        addButton(new GuiButton(BTN_PLANETS_GS, centerX - 50, centerY - 15, 100, 20, I18n.format(front + "gs")));
        addButton(new GuiButton(BTN_PLANETS_EP, centerX - 50, centerY + 15, 100, 20, I18n.format(front + "ep")));
    }

    private void createYesNoButtons(int centerX, int centerY) {
        addButton(new GuiButton(BTN_YES, centerX - 50, centerY - 15, 100, 20, I18n.format("gui.yes")));
        addButton(new GuiButton(BTN_NO, centerX - 50, centerY + 15, 100, 20, I18n.format("gui.no")));
    }

    private void createCompletionButtons(int centerX, int centerY) {
        addButton(new GuiButton(BTN_QUIT, centerX - 50, centerY + (GCCConfig.displayConfiguration ? 0 : -15), 100, 20, I18n.format(front + "quit")));
        if (!GCCConfig.displayConfiguration) {
            addButton(new GuiButton(BTN_CONTINUE, centerX - 50, centerY + 15, 100, 20, I18n.format(front + "continue")));
        }
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) {
        if (handleCommonButtons(button)) {
            return;
        }
        pendingButtonId = button.id;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (pendingButtonId != -1) {
            handleScreenSpecificButtons(new GuiButton(pendingButtonId, 0, 0, ""));
            pendingButtonId = -1;
            initGui();
        }
    }

    private boolean handleCommonButtons(GuiButton button) {
        switch (button.id) {
            case BTN_CANCEL:
                Minecraft.getMinecraft().displayGuiScreen(null);
                return true;

            case BTN_BACK:
                navigateBack();
                initGui();
                return true;
        }
        return false;
    }

    private void handleScreenSpecificButtons(GuiButton button) {
        switch (currentScreen) {
            case MAP_SELECTION:
                handleMapSelection(button);
                break;

            case SPACE_STATION_SELECTION:
                handleSpaceStationSelection(button);
                break;

            case DUPLICATE_SELECTION:
                handleDuplicateSelection(button);
                break;

            case PLANETS_SELECTION:
                handlePlanetsSelection(button);
                break;

            case SHADERS_SELECTION:
                handleShadersSelection(button);
                break;

            case MENU_SELECTION:
                handleMenuSelection(button);
                break;

            case CRAFT_SELECTION:
                handleCraftSelection(button);
                break;

            case COMPLETION:
                handleCompletion(button);
                break;
        }
    }

    private void handleMapSelection(GuiButton button) {
        switch (button.id) {
            case BTN_MAP_GC:
                userSelections.mapSelection = MapSelection.GALACTICRAFT;
                navigateTo(ScreenState.SPACE_STATION_SELECTION);
                break;

            case BTN_MAP_AC:
                if (!GCC.isTTSLoaded) {
                    userSelections.mapSelection = MapSelection.ADVANCED_ROCKETRY;
                    navigateTo(ScreenState.SPACE_STATION_SELECTION);
                }
                break;

            case BTN_MAP_EP:
                userSelections.mapSelection = MapSelection.EXTRAPLANETS;
                navigateTo(ScreenState.SPACE_STATION_SELECTION);
                break;
        }
    }

    private void handleSpaceStationSelection(GuiButton button) {
        switch (button.id) {
            case BTN_SS_GS:
                userSelections.spaceStationSelection = SpaceStationSelection.GALAXYSPACE;
                navigateTo(ScreenState.DUPLICATE_SELECTION);
                break;

            case BTN_SS_EP:
                userSelections.spaceStationSelection = SpaceStationSelection.EXTRAPLANETS;
                break;
        }
        navigateTo(ScreenState.DUPLICATE_SELECTION);
    }

    private void handleDuplicateSelection(GuiButton button) {
        switch (button.id) {
            case BTN_YES:
                userSelections.duplicateSelection = DuplicateSelection.YES;
                navigateTo(ScreenState.PLANETS_SELECTION);
                break;

            case BTN_NO:
                navigateTo(ScreenState.SHADERS_SELECTION);
                break;
        }
    }

    private void handlePlanetsSelection(GuiButton button) {
        switch (button.id) {
            case BTN_PLANETS_GS:
                userSelections.planetsSelection = PlanetsSelection.GALAXYSPACE;
                break;

            case BTN_PLANETS_EP:
                userSelections.planetsSelection = PlanetsSelection.EXTRAPLANETS;
                break;
        }
        navigateTo(ScreenState.SHADERS_SELECTION);
    }

    private void handleShadersSelection(GuiButton button) {
        userSelections.enableShaders = (button.id == BTN_YES);
        navigateTo(ScreenState.MENU_SELECTION);
    }

    private void handleMenuSelection(GuiButton button) {
        userSelections.enableNewMenu = (button.id == BTN_YES);
        navigateTo(ScreenState.CRAFT_SELECTION);
    }

    private void handleCraftSelection(GuiButton button) {
        userSelections.enableAdvancedCraft = (button.id == BTN_YES);
        applyConfiguration();
        navigateTo(ScreenState.COMPLETION);
    }

    private void handleCompletion(GuiButton button) {
        switch (button.id) {
            case BTN_QUIT:
                Minecraft.getMinecraft().displayGuiScreen(null);
                GCCConfig.displayConfiguration = false;
                MinecraftForge.EVENT_BUS.post(new ConfigChangedEvent.OnConfigChangedEvent(Tags.MOD_ID, Tags.MOD_NAME, false, false));
                Minecraft.getMinecraft().shutdown();
                break;

            case BTN_CONTINUE:
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
        }
    }

    private void navigateTo(ScreenState nextScreen) {
        currentScreen = nextScreen;
    }

    private void navigateBack() {
        switch (currentScreen) {
            case SPACE_STATION_SELECTION:
                currentScreen = ScreenState.MAP_SELECTION;
                break;
            case DUPLICATE_SELECTION:
                currentScreen = ScreenState.SPACE_STATION_SELECTION;
                break;
            case PLANETS_SELECTION:
                currentScreen = ScreenState.DUPLICATE_SELECTION;
                break;
            case SHADERS_SELECTION:
                currentScreen = userSelections.duplicateSelection == DuplicateSelection.YES ? ScreenState.PLANETS_SELECTION : ScreenState.DUPLICATE_SELECTION;
                break;
            case MENU_SELECTION:
                currentScreen = ScreenState.SHADERS_SELECTION;
                break;
            case CRAFT_SELECTION:
                currentScreen = ScreenState.MENU_SELECTION;
                break;
            case COMPLETION:
                currentScreen = ScreenState.CRAFT_SELECTION;
                break;
        }
    }

    private void applyConfiguration() {
        if (Loader.isModLoaded("sol")) {
            GCC.setConfigValue(GCC.sol, false, "The Sol - Misc", "Enable Custom Galaxymap?");
        }

        GCC.setConfigValue(GCC.ac, userSelections.mapSelection == MapSelection.ADVANCED_ROCKETRY, "galaxymap", "enableNewGalaxyMap");
        GCC.setConfigValue(GCC.ep, userSelections.mapSelection == MapSelection.EXTRAPLANETS, "general settings", "Use Custom Galaxy Map/Celestial Selection Screen");

        GCC.setConfigValue(GCC.gsd, userSelections.spaceStationSelection == SpaceStationSelection.GALAXYSPACE, "general", "enableMarsSpaceStation", "enableVenusSpaceStation");
        GCC.setConfigValue(GCC.ep, userSelections.spaceStationSelection == SpaceStationSelection.EXTRAPLANETS, "space stations", "Mars SpaceStation", "Venus SpaceStation");

        GCC.setConfigValue(GCC.ep, true, "compatibility support", "Enable Galaxy Space Compatibility");
        if (userSelections.duplicateSelection == DuplicateSelection.YES) {
            GCC.setConfigValue(GCC.ep, false, "compatibility support", "Enable Galaxy Space Compatibility");
            if (userSelections.planetsSelection == PlanetsSelection.GALAXYSPACE) {
                GCC.setConfigValue(GCC.ep, false, "main dimensions", "Mercury & Tier 4 Rocket", "Jupiter & Tier 5 Rocket", "Saturn & Tier 6 Rocket", "Uranus & Tier 7 Rocket", "Neptune & Tier 8 Rocket", "Pluto & Tier 9 Rocket", "Eris & Tier 10 Rocket");
                GCC.setConfigValue(GCC.ep, false, "other dimensions", "Triton", "Europa", "IO", "Deimos", "Callisto", "Ganymede", "Rhea", "Titan", "Oberon", "Titania", "Iapetus", "Ceres", "Kuiper Belt", "Unreachable moons on the Celestial Selection Screen");
            } else if (userSelections.planetsSelection == PlanetsSelection.EXTRAPLANETS) {
                GCC.setConfigValue(GCC.gsd, false, "general", "enableMercury", "enableJupiter", "enableSaturn", "enableUranus", "enablePluto", "enableCeres", "enableKuiperBelt");
                //GCC.setConfigValue(GCC.gsd, false, "enableNeptune"); //Galaxy Space's bug
            }
        }

        GCC.setConfigValue(GCC.ac, userSelections.enableShaders, "client", "enableSkyAsteroids", "enableSkyMoon", "enableSkyOverworld", "enableSkyOverworldOrbit");
        GCC.setConfigValue(GCC.gsc, userSelections.enableNewMenu, "client", "enableNewMenu");
        GCC.setConfigValue(GCC.gsc, userSelections.enableAdvancedCraft, "hardmode", "enableAdvancedRocketCraft");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int centerX = width / 2;
        int centerY = height / 2;

        drawCenteredString(fontRenderer, I18n.format(front + "title"), centerX, centerY - 95, 0xffffff);
        drawCenteredString(fontRenderer, I18n.format(getCurrentScreenTitle()), centerX, centerY - 80, 0xffffff);

        drawWarningMessages(centerX, centerY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private String getCurrentScreenTitle() {
        switch (currentScreen) {
            case MAP_SELECTION:
                return front + "map";
            case SPACE_STATION_SELECTION:
                return front + "ss";
            case DUPLICATE_SELECTION:
                return front + "duplicate";
            case PLANETS_SELECTION:
                return front + "planets";
            case SHADERS_SELECTION:
                return front + "shaders";
            case MENU_SELECTION:
                return front + "menu";
            case CRAFT_SELECTION:
                return front + "craft";
            case COMPLETION:
                return front + "done";
            default:
                return front + "title";
        }
    }

    private void drawWarningMessages(int centerX, int centerY) {
        if (currentScreen == ScreenState.MAP_SELECTION && GCC.isTTSLoaded) {
            drawCenteredString(fontRenderer, I18n.format(front + "map.warning1"), centerX, centerY - 65, 0xff0000);
            drawCenteredString(fontRenderer, I18n.format(front + "map.warning2"), centerX, centerY - 50, 0xff0000);
        }

        if (currentScreen == ScreenState.DUPLICATE_SELECTION) {
            drawCenteredString(fontRenderer, I18n.format(front + "duplicate.warning"), centerX, centerY - 65, 0xff0000);
        }

        if (currentScreen == ScreenState.PLANETS_SELECTION) {
            drawCenteredString(fontRenderer, I18n.format(front + "planets.warning1"), centerX, centerY - 65, 0xff0000);
            drawCenteredString(fontRenderer, I18n.format(front + "planets.warning2"), centerX, centerY - 50, 0xff0000);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1 && !GCCConfig.displayConfiguration) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    private enum ScreenState {
        MAP_SELECTION,
        SPACE_STATION_SELECTION,
        DUPLICATE_SELECTION,
        PLANETS_SELECTION,
        SHADERS_SELECTION,
        MENU_SELECTION,
        CRAFT_SELECTION,
        COMPLETION
    }

    private enum MapSelection {
        NONE, GALACTICRAFT, ADVANCED_ROCKETRY, EXTRAPLANETS
    }

    private enum SpaceStationSelection {
        NONE, GALAXYSPACE, EXTRAPLANETS
    }

    private enum DuplicateSelection {
        NONE, YES
    }

    private enum PlanetsSelection {
        NONE, GALAXYSPACE, EXTRAPLANETS
    }

    private static class UserSelections {
        public MapSelection mapSelection = MapSelection.NONE;
        public SpaceStationSelection spaceStationSelection = SpaceStationSelection.NONE;
        public DuplicateSelection duplicateSelection = DuplicateSelection.NONE;
        public PlanetsSelection planetsSelection = PlanetsSelection.NONE;
        public boolean enableShaders = false;
        public boolean enableNewMenu = false;
        public boolean enableAdvancedCraft = false;
    }
}