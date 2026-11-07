package com.fubangkun.galacticraftcompatibility.mixin.sol;

import mod.sol.TheSol;
import mod.sol.init.SolFluid;
import mod.sol.util.handler.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The Sol registers its celestial bodies in the FMLInitializationEvent handler, after every other
 * mod's init. Running that registration once the Sol item registry event has finished moves it into
 * the pre-initialization phase while keeping item/block/fluid references valid.
 */
@Mixin(value = RegistryHandler.class, remap = false)
public abstract class RegistryHandlerMixin {
    @Inject(method = "onItemRegister", at = @At("TAIL"))
    private static void gcc$registerBodiesEarly(RegistryEvent.Register<Item> event, CallbackInfo ci) {
        // Safety net: SolBlocks static initialization needs the fluid registered if the block
        // registry event has not run yet
        SolFluid.registerFluids();
        TheSol.instance.init(null);
    }
}
