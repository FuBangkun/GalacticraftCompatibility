package com.fubangkun.galacticraftcompatibility.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Two-way space suit compatibility between GalaxySpace and ExtraPlanets.
 *
 * <p>Suits are recognized by registry name only, so neither mod's classes are referenced and
 * a missing mod cannot break class loading.</p>
 */
public final class GCCArmorCompat {
    private static final String GS_NAMESPACE = "galaxyspace";
    private static final String GS_PATH_PREFIX = "space_suit";

    private static final String EP_NAMESPACE = "extraplanets";
    private static final String EP_PATH_MARK = "space_suit";

    private GCCArmorCompat() {
    }

    public static boolean isGSuit(ItemStack stack) {
        return isSuit(stack, GS_NAMESPACE, GS_PATH_PREFIX, true, false);
    }

    public static boolean isEPSuit(ItemStack stack) {
        return isSuit(stack, EP_NAMESPACE, EP_PATH_MARK, false, true);
    }

    public static boolean isFullGSuit(EntityPlayer player) {
        return player != null
                && isGSuit(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD))
                && isGSuit(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST))
                && isGSuit(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS))
                && isGSuit(player.getItemStackFromSlot(EntityEquipmentSlot.FEET));
    }

    public static boolean isFullEPSuit(EntityPlayer player) {
        return player != null
                && isEPSuit(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD))
                && isEPSuit(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST))
                && isEPSuit(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS))
                && isEPSuit(player.getItemStackFromSlot(EntityEquipmentSlot.FEET));
    }

    private static boolean isSuit(ItemStack stack, String namespace, String pathPart, boolean prefix, boolean contains) {
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof ItemArmor)) {
            return false;
        }
        ResourceLocation registryName = stack.getItem().getRegistryName();
        if (registryName == null || !namespace.equals(registryName.getNamespace())) {
            return false;
        }
        String path = registryName.getPath();
        return prefix ? path.startsWith(pathPart) : contains && path.contains(pathPart);
    }
}
