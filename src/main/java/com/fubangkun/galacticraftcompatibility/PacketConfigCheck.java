package com.fubangkun.galacticraftcompatibility;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class PacketConfigCheck implements IMessage {
    private static final int MAX_UI_DISPLAY_LINES = 5;

    public ConfigCheckData data;

    public PacketConfigCheck() {
        this.data = new ConfigCheckData();
    }

    public PacketConfigCheck(ConfigCheckData data) {
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        data.toBytes(buf);
    }

    public static class Handler implements IMessageHandler<PacketConfigCheck, IMessage> {
        @Override
        public IMessage onMessage(PacketConfigCheck message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if (player == null) {
                return null;
            }

            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
                ConfigCheckData serverData = ConfigCheckData.readCurrent();
                List<ConfigCheckData.DiffEntry> diffs = serverData.getDifferences(message.data);

                if (!diffs.isEmpty()) {
                    GCC.LOGGER.warn("Player {} failed config validation with {} mismatches:", player.getName(), diffs.size());
                    for (ConfigCheckData.DiffEntry diff : diffs) {
                        GCC.LOGGER.warn("  - Config: [{}] | Server: {} | Client: {}", diff.configPath, diff.serverVal, diff.clientVal);
                    }

                    ITextComponent diffReportUI = serverData.getDiffReportForUI(diffs, MAX_UI_DISPLAY_LINES);

                    ITextComponent finalMessage = new TextComponentString("");

                    finalMessage.appendSibling(new TextComponentTranslation("gui.galacticraftcompatibility.intercept.title").setStyle(new Style().setColor(TextFormatting.RED).setBold(true)));
                    finalMessage.appendText("\n\n");

                    finalMessage.appendSibling(new TextComponentTranslation("gui.galacticraftcompatibility.intercept.body"));
                    finalMessage.appendText("\n\n");

                    finalMessage.appendSibling(new TextComponentTranslation("gui.galacticraftcompatibility.intercept.diff").setStyle(new Style().setColor(TextFormatting.YELLOW).setUnderlined(true)));
                    finalMessage.appendText("\n");
                    finalMessage.appendSibling(diffReportUI);

                    finalMessage.appendText("\n");

                    finalMessage.appendSibling(new TextComponentTranslation("gui.galacticraftcompatibility.intercept.footer").setStyle(new Style().setColor(TextFormatting.GRAY).setItalic(true)));

                    player.connection.disconnect(finalMessage);
                }
            });
            return null;
        }
    }
}