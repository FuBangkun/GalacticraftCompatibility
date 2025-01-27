/*
 * Copyright (c) 2025 FuBangkun. All Rights Reserved.
 */

package com.FuBangkun.galacticraftcompatibility.client;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderTier2Rocket extends Render<EntitySpaceshipBase> {
    protected ModelBase modelRocket;

    public RenderTier2Rocket(RenderManager manager, ModelBase spaceshipModel) {
        super(manager);
        this.modelRocket = spaceshipModel;
        this.shadowSize = 0.9F;
    }

    @Override
    public void doRender(EntitySpaceshipBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y + 1.625F, (float) z);
        GL11.glRotatef(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, entity.getRenderOffsetY(), 0.0F);
        float var28 = entity.rollAmplitude - partialTicks;
        if (var28 > 0.0F) {
            float angle = MathHelper.sin(var28) * var28 * (entity.getLaunched() ? (float) (5 - MathHelper.floor((float) (entity.timeUntilLaunch / 85))) / 10.0F : 0.3F) * partialTicks;
            GL11.glRotatef(angle, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(angle, 1.0F, 0.0F, 1.0F);
        }
        this.bindEntityTexture(entity);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelRocket.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    public boolean shouldRender(EntitySpaceshipBase rocket, ICamera camera, double camX, double camY, double camZ) {
        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(rocket.getRenderBoundingBox().grow(0.6, 1.0, 0.6));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySpaceshipBase entity) {
        return new ResourceLocation("galacticraftplanets", "textures/model/rocket_t2.png");
    }
}