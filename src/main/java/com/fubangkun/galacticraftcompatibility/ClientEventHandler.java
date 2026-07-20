package com.fubangkun.galacticraftcompatibility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Tags.MOD_ID)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        if (GCCConfig.firstLaunch && event.getGui() instanceof GuiMainMenu) {
            event.setGui(new GuiConfiguration());
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote && event.getEntity() instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) event.getEntity();
            if (player == Minecraft.getMinecraft().player) {
                NetworkHandler.INSTANCE.sendToServer(new PacketConfigCheck(ConfigCheckData.readCurrent()));
            }
        }
    }
}