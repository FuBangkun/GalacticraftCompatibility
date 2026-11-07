package com.fubangkun.galacticraftcompatibility.mixin.extraplanets;

import com.fubangkun.galacticraftcompatibility.compat.GCCArmorCompat;
import com.mjr.extraplanets.handlers.MainHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Makes GalaxySpace suits count as valid protection in ExtraPlanets' pressure and radiation
 * checks. A full GS set is accepted and treated as tier 1.
 */
@Mixin(value = MainHandlerServer.class, remap = false)
public abstract class MainHandlerServerMixin {
    @Inject(method = "isValidSpaceSuit", at = @At("HEAD"), cancellable = true)
    private void gcc$gsSpaceSuitValid(EntityPlayer player, ItemStack helmet, ItemStack chest, ItemStack leggins, ItemStack boots, List<String> list, boolean pressure, CallbackInfoReturnable<Boolean> cir) {
        if (GCCArmorCompat.isFullGSuit(player)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getTier", at = @At("HEAD"), cancellable = true)
    private void gcc$gsSpaceSuitTier(ItemStack testItem, List<String> list, CallbackInfoReturnable<Integer> cir) {
        if (GCCArmorCompat.isGSuit(testItem)) {
            // Treat GS suits as tier 1; a negative tier would invert the damage calculation.
            cir.setReturnValue(1);
        }
    }
}
