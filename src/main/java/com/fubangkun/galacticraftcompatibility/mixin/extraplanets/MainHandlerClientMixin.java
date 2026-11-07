package com.fubangkun.galacticraftcompatibility.mixin.extraplanets;

import com.mjr.extraplanets.client.handlers.MainHandlerClient;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Disables ExtraPlanets' own map replacement and its crash notice.
 *
 * <p>EP swaps the GC map for its {@code CustomCelestialSelection} and throws a crash report when
 * the AC map is already open. Canceling the event leaves the AC map in place.</p>
 */
@Mixin(value = MainHandlerClient.class, remap = false)
public abstract class MainHandlerClientMixin {
    @Inject(method = "onGuiOpenEvent", at = @At("HEAD"), cancellable = true)
    private void gcc$disableCustomCelestialSelection(GuiOpenEvent event, CallbackInfo ci) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiCelestialSelection) {
            ci.cancel();
        }
    }
}
