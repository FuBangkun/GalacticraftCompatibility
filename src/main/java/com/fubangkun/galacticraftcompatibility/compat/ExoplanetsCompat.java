package com.fubangkun.galacticraftcompatibility.compat;

import com.fubangkun.galacticraftcompatibility.Reference;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;

/**
 * Suppresses the Interstellar: Exoplanets beta-build screen. The setting is cleared through
 * reflection just before the main menu opens, so both the released package layout and the
 * current development layout are handled without a compile-time dependency.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public final class ExoplanetsCompat {
    private static final String[] CONFIG_CLASSES = {
            "net.rom.exoplanets.conf.ConfigCore",
            "com.readonlydev.common.config.ConfigCore"
    };

    private ExoplanetsCompat() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu) {
            suppressBetaNotice();
        }
    }

    private static void suppressBetaNotice() {
        if (!Loader.isModLoaded("exoplanets")) {
            return;
        }
        for (String className : CONFIG_CLASSES) {
            try {
                Class<?> configClass = Class.forName(className);
                Field field = configClass.getField("warnBetaBuild");
                field.setBoolean(null, false);
                return;
            } catch (ReflectiveOperationException ignored) {
                // Try the next package layout.
            }
        }
    }
}
