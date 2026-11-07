package com.fubangkun.galacticraftcompatibility.mixin.galacticraftcore;

import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

/**
 * The client maps a planet index to {@code dimensionTypeIDs[index]}. When station counts and
 * DimensionType registration drift apart, that index shifts and a dimension is bound to the
 * wrong {@code WorldProvider}, which crashes world creation. Resolve the type by dimension ID
 * instead, independent of registration order.
 */
@Mixin(value = WorldUtil.class, remap = false)
public abstract class WorldUtilClientMixin {
    @Shadow
    public static List<Integer> registeredPlanets;

    /**
     * Resolves the {@code DimensionType} by dimension ID and registers the dimension.
     *
     * @param dimID         planet dimension ID
     * @param providerIndex unused; the original method used it as a provider index
     * @author FuBangkun
     * @reason resolve the provider by dimension ID instead of by a shifting index
     */
    @Overwrite
    public static void registerPlanetClient(Integer dimID, int providerIndex) {
        DimensionType type = WorldUtil.getDimensionTypeById(dimID);
        if (type == null) {
            return;
        }
        if (!registeredPlanets.contains(dimID)) {
            registeredPlanets.add(dimID);
            if (!DimensionManager.isDimensionRegistered(dimID)) {
                DimensionManager.registerDimension(dimID, type);
            }
        }
    }
}
