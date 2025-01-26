/*
 * Copyright (c) 2025 FuBangkun. All Rights Reserved.
 */

package com.FuBangkun.galacticraftcompatibility.client;

import asmodeuscore.core.registers.potions.ACPotions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import static com.FuBangkun.galacticraftcompatibility.Constants.MOD_ID;

@Mod.EventBusSubscriber(Side.CLIENT)
public class RenderOverlay {
    @SubscribeEvent
    public static void onRenderGameOverlayEvent(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            renderPotionOverlay(event.getResolution(), ACPotions.radiation, "radiation_overlay.png");
            renderPotionOverlay(event.getResolution(), ACPotions.hypothermia, "hypothermia_overlay.png");
            renderPotionOverlay(event.getResolution(), ACPotions.overheat, "hyperthermia_overlay.png");
        }
    }

    private static void renderPotionOverlay(ScaledResolution resolution, Potion potion, String texturePath) {
        if (Minecraft.getMinecraft().player.isPotionActive(potion)) {
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableAlpha();
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(MOD_ID, "textures/gui/overlay/" + texturePath));
            Tessellator   tessellator = Tessellator.getInstance();
            BufferBuilder buffer      = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(0, resolution.getScaledHeight(), -90).tex(0, 1).endVertex();
            buffer.pos(resolution.getScaledWidth(), resolution.getScaledHeight(), -90).tex(1, 1).endVertex();
            buffer.pos(resolution.getScaledWidth(), 0, -90).tex(1, 0).endVertex();
            buffer.pos(0, 0, -90).tex(0, 0).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.color(1, 1, 1, 1);
        }
    }
}