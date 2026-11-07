package com.fubangkun.galacticraftcompatibility.mixin;

import net.minecraftforge.fml.common.Loader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class GCCMixinConfigPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch (mixinClassName.split("\\.")[4]) {
            case "asmodeuscore":
                return Loader.isModLoaded("asmodeuscore");
            case "galaxyspace":
                return Loader.isModLoaded("galaxyspace");
            case "extraplanets":
                return Loader.isModLoaded("extraplanets");
            case "sol":
                return Loader.isModLoaded("sol");
            case "galacticresearch":
                return Loader.isModLoaded("galacticresearch");
            default:
                return true;
        }
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }
}
