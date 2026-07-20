package com.fubangkun.galacticraftcompatibility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiConfiguration extends GuiScreen {
    private static final String front = "gui." + Tags.MOD_ID + ".";
    private static final int BTN_BACK = 1001;
    private static final int BTN_MAP_GC = 1;
    private static final int BTN_MAP_AC = 2;
    private static final int BTN_MAP_EP = 3;
    private static final int BTN_MAP_SOL = 4;
    private static final int BTN_SS_GS = 5;
    private static final int BTN_SS_EP = 6;
    private static final int BTN_PLANETS_GS = 7;
    private static final int BTN_PLANETS_EP = 8;
    private static final int BTN_YES = 9;
    private static final int BTN_NO = 10;
    private static final int BTN_QUIT = 11;
    private final UserSelections userSelections = new UserSelections();
    private int pendingButtonId = -1;
    private ScreenState currentScreen = ScreenState.MAP_SELECTION;

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();

        createCommonButtons();
        createScreenSpecificButtons();
    }

    private void createCommonButtons() {
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
        List<GuiButton> buttons = new ArrayList<>();

        buttons.add(new GuiButton(BTN_MAP_GC, 0, 0, 100, 20, I18n.format(front + "gc")));

        GuiButton acButton = new GuiButton(BTN_MAP_AC, 0, 0, 100, 20, I18n.format(front + "ac"));
        acButton.enabled = !GCC.isTTSLoaded;
        buttons.add(acButton);

        buttons.add(new GuiButton(BTN_MAP_EP, 0, 0, 100, 20, I18n.format(front + "ep")));

        if (GCC.isSolLoaded) {
            buttons.add(new GuiButton(BTN_MAP_SOL, 0, 0, 100, 20, I18n.format(front + "sol")));
        }

        int spacing = 30;
        int totalHeight = (buttons.size() - 1) * spacing;
        int startOffset = -totalHeight / 2;

        for (int i = 0; i < buttons.size(); i++) {
            GuiButton b = buttons.get(i);
            b.x = centerX - 50;
            b.y = centerY + startOffset + i * spacing;
            addButton(b);
        }
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
        addButton(new GuiButton(BTN_QUIT, centerX - 50, centerY, 100, 20, I18n.format(front + "quit")));
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
        if (button.id == BTN_BACK) {
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
                    userSelections.mapSelection = MapSelection.ASMODEUS_CORE;
                    navigateTo(ScreenState.SPACE_STATION_SELECTION);
                }
                break;

            case BTN_MAP_EP:
                userSelections.mapSelection = MapSelection.EXTRAPLANETS;
                navigateTo(ScreenState.SPACE_STATION_SELECTION);
                break;

            case BTN_MAP_SOL:
                if (GCC.isSolLoaded) {
                    userSelections.mapSelection = MapSelection.SOL;
                    navigateTo(ScreenState.SPACE_STATION_SELECTION);
                }
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
        navigateTo(ScreenState.COMPLETION);
    }

    private void handleCompletion(GuiButton button) {
        if (button.id == BTN_QUIT) {
            saveToGCCConfig();
            Minecraft.getMinecraft().shutdown();
        }
    }

    private void saveToGCCConfig() {
        GCCConfig.mapSelection = userSelections.mapSelection.ordinal() - 1;
        GCCConfig.spaceStationSelection = userSelections.spaceStationSelection.ordinal() - 1;
        GCCConfig.removeDuplicatePlanets = (userSelections.duplicateSelection == DuplicateSelection.YES);
        GCCConfig.preferredPlanetMod = Math.max(0, userSelections.planetsSelection.ordinal() - 1);
        GCCConfig.enableShaders = userSelections.enableShaders;
        GCCConfig.enableNewMenu = userSelections.enableNewMenu;
        GCCConfig.enableAdvancedCraft = userSelections.enableAdvancedCraft;
        GCCConfig.firstLaunch = false;
        GCCConfig.applyNextLaunch = true;

        ConfigManager.sync(Tags.MOD_ID, net.minecraftforge.common.config.Config.Type.INSTANCE);
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
        NONE, GALACTICRAFT, ASMODEUS_CORE, EXTRAPLANETS, SOL
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