package com.fubangkun.galacticraftcompatibility.mixin.forge;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.toposort.ModSorter;
import net.minecraftforge.fml.common.toposort.TopologicalSort.DirectedGraph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * The Sol and Planet Progression both subscribe their registry handlers with
 * {@code @Mod.EventBusSubscriber}, so their call order inside the item registry event follows the
 * mod load order. Planet Progression enumerates the celestial bodies there, and without this edge
 * it runs before The Sol registers its bodies, which makes those bodies unresearcheable.
 *
 * <p>Adding the {@code sol -> planetprogression} edge makes The Sol load first, so the early body
 * registration in {@code sol.RegistryHandlerMixin} happens before Planet Progression builds its
 * research papers.</p>
 */
@Mixin(value = ModSorter.class, remap = false)
public abstract class ModSorterMixin {
    @Shadow
    private DirectedGraph<ModContainer> modGraph;

    @Inject(method = "sort", at = @At("HEAD"))
    private void gcc$loadSolBeforePlanetProgression(CallbackInfoReturnable<List<ModContainer>> cir) {
        if (!Loader.isModLoaded("sol") || !Loader.isModLoaded("planetprogression")) {
            return;
        }
        ModContainer sol = null;
        ModContainer planetProgression = null;
        for (ModContainer container : Loader.instance().getModList()) {
            if ("sol".equals(container.getModId())) {
                sol = container;
            } else if ("planetprogression".equals(container.getModId())) {
                planetProgression = container;
            }
        }
        if (sol != null && planetProgression != null) {
            modGraph.addEdge(sol, planetProgression);
        }
    }
}
