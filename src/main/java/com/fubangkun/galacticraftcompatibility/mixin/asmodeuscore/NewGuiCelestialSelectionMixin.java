package com.fubangkun.galacticraftcompatibility.mixin.asmodeuscore;

import asmodeuscore.core.astronomy.gui.screen.NewGuiCelestialSelection;
import com.fubangkun.galacticraftcompatibility.compat.CelestialVariants;
import com.fubangkun.galacticraftcompatibility.compat.GCCCelestialSelectionAccess;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.IChildBody;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * AsmodeusCore map integration:
 * <ul>
 *     <li>filters rendered and child bodies to the current mode;</li>
 *     <li>cycles the GalaxySpace / ExtraPlanets / The Sol variants with a map button;</li>
 *     <li>shows the active mode in that button, styled like the map's own controls.</li>
 * </ul>
 *
 * <p>The map fields are declared in the GC base class and are reached through
 * {@link GCCCelestialSelectionAccess}.</p>
 */
@Mixin(value = NewGuiCelestialSelection.class, remap = false)
public abstract class NewGuiCelestialSelectionMixin {
    @Unique
    private static final boolean TTS_MAP_CONFLICT = Loader.isModLoaded("tothestars");

    /**
     * Both research mods swap the celestial selection map for their own subclass and read a
     * separate progress store, so whichever replaces the map last hides the other's unlocks.
     */
    @Unique
    private static final boolean RESEARCH_MAP_CONFLICT =
            Loader.isModLoaded("galacticresearch") && Loader.isModLoaded("planetprogression");

    /**
     * Galacticraft's map atlas, the one the map headers and toggle buttons are drawn from.
     */
    @Unique
    private static final ResourceLocation GCC_MAP_TEXTURE =
            new ResourceLocation("galacticraftcore", "textures/gui/celestialselection.png");

    @Shadow
    private void refreshBodies() {
        throw new AssertionError();
    }

    @Inject(method = "refreshBodies", at = @At("TAIL"))
    private void gcc$filterBodies(CallbackInfo ci) {
        GCCCelestialSelectionAccess access = (GCCCelestialSelectionAccess) this;
        List<CelestialBody> before = access.gcc$getBodiesToRender();
        List<CelestialBody> after = CelestialVariants.filter(before, CelestialVariants.mode());
        access.gcc$setBodiesToRender(after);
    }

    @Inject(method = "getChildren(Ljava/lang/Object;II)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private void gcc$filterChildren(Object object, int start, int size, CallbackInfoReturnable<List<CelestialBody>> cir) {
        cir.setReturnValue(CelestialVariants.filter(cir.getReturnValue(), CelestialVariants.mode()));
    }

    @Inject(method = "initGui", at = @At("HEAD"), remap = true)
    private void gcc$initMode(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null) {
            CelestialVariants.resetFromDimension(mc.player.dimension);
        }
    }

    /**
     * Top-left corner of the mode button, which sits one row above the map's toggle column.
     * The toggle column starts at {@code width - lhs - 138} and the buttons span 85 pixels.
     */
    @Unique
    private int[] gcc$modeButton(int width, int height) {
        int border = width / 65;
        int lhs = border + border / 4;
        return new int[]{width - lhs - 138, height - lhs - 58};
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true, remap = true)
    private void gcc$clickVariant(int x, int y, int button, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen == null || button != 0) {
            return;
        }
        GCCCelestialSelectionAccess access = (GCCCelestialSelectionAccess) this;
        if (access.gcc$isRenamingSpaceStation()) {
            return;
        }
        int[] pos = this.gcc$modeButton(mc.currentScreen.width, mc.currentScreen.height);
        if (x < pos[0] + 53 || x >= pos[0] + 138 || y < pos[1] || y >= pos[1] + 12) {
            return;
        }
        this.gcc$cycleVariant();
        ci.cancel();
    }

    @Unique
    private void gcc$cycleVariant() {
        GCCCelestialSelectionAccess access = (GCCCelestialSelectionAccess) this;
        CelestialVariants.toggle();
        CelestialBody variant = CelestialVariants.variantOf(access.gcc$getSelectedBody(), CelestialVariants.mode());
        if (variant != null) {
            access.gcc$setSelectedBody(variant);
            if (variant instanceof IChildBody) {
                access.gcc$setSelectedParent(((IChildBody) variant).getParentPlanet());
            } else if (variant instanceof Planet) {
                access.gcc$setSelectedParent(((Planet) variant).getParentSolarSystem());
            }
            access.gcc$setSelectedStationOwner("");
        }
        this.refreshBodies();
    }

    @Inject(method = "drawButtons", at = @At("TAIL"))
    private void gcc$drawModeIndicator(int mousePosX, int mousePosY, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen != null) {
            GuiCelestialSelection map = (GuiCelestialSelection) (Object) this;
            int width = mc.currentScreen.width;
            int height = mc.currentScreen.height;
            // Fourth row of the map's button stack, drawn with the exact sprite pieces the three
            // toggles below it use, so it reads as part of the same control column.
            int[] pos = this.gcc$modeButton(width, height);
            int posX1 = pos[0];
            int barY = pos[1];
            mc.getTextureManager().bindTexture(GCC_MAP_TEXTURE);
            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1.0F);
            map.drawTexturedModalRect(posX1 + 98, barY, 40, 12, 0, 456, 40, 12, true, false);
            map.drawTexturedModalRect(posX1 + 53, barY, 45, 12, 50, 456, 45, 12, true, false);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            String text = I18n.format(CelestialVariants.mode().translationKey);
            mc.fontRenderer.drawString(text, posX1 + 61, barY + 2, 0xFFFF00);
            if (TTS_MAP_CONFLICT) {
                String warning = I18n.format("gcc.celestial_map.tothestars_conflict");
                mc.fontRenderer.drawStringWithShadow(warning, posX1 + 138 - mc.fontRenderer.getStringWidth(warning), barY - 12, 0xFF5555);
            }
            if (RESEARCH_MAP_CONFLICT) {
                String warning = I18n.format("gcc.celestial_map.research_conflict");
                mc.fontRenderer.drawStringWithShadow(warning, posX1 + 138 - mc.fontRenderer.getStringWidth(warning), barY - 24, 0xFF5555);
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
