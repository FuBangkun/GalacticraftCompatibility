package com.fubangkun.galacticraftcompatibility.mixin.galaxyspace;

import com.fubangkun.galacticraftcompatibility.compat.GCCArmorCompat;
import galaxyspace.core.events.GSEventHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes ExtraPlanets suits count as protection in GalaxySpace's pressure and radiation checks.
 *
 * <p>The GS implementation overwrites its per-slot check array on every iteration and keeps only
 * the last configured entry, so a full EP set is accepted here directly.</p>
 */
@Mixin(value = GSEventHandler.class, remap = false)
public abstract class GSEventHandlerMixin {
    @Inject(method = "getProtectArmor", at = @At("HEAD"), cancellable = true)
    private static void gcc$epSpaceSuitValid(EntityPlayerMP player, CallbackInfoReturnable<Boolean> cir) {
        if (GCCArmorCompat.isFullEPSuit(player)) {
            cir.setReturnValue(true);
        }
    }
}
