package com.FuBangkun.galacticraftcompatibility;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandOpenConfiguration extends CommandBase {
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiConfiguration()));
    }

    @Override
    public String getName() {
        return "gcc";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/gcc";
    }
}
