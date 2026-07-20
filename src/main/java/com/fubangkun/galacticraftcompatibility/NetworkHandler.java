package com.fubangkun.galacticraftcompatibility;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("gcc_channel");

    public static void init() {
        INSTANCE.registerMessage(PacketConfigCheck.Handler.class, PacketConfigCheck.class, 0, Side.SERVER);
    }
}