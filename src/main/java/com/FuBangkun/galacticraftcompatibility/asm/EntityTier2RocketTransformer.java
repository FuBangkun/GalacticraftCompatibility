/*
 * Copyright (c) 2024 FuBangkun. All Rights Reserved.
 */

package com.FuBangkun.galacticraftcompatibility.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class EntityTier2RocketTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!"micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket".equals(transformedName)) {
            return basicClass;
        }

        ClassReader cr = new ClassReader(basicClass);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        for (MethodNode mn : cn.methods) {
            String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, mn.name, mn.desc);
            if(!"func_70042_X".equals(methodName) && !"getMountedYOffset".equals(methodName))
                continue;
            InsnList insnList = new InsnList();
            insnList.add(new LdcInsnNode(1.2D));
            insnList.add(new InsnNode(Opcodes.DRETURN));
            mn.instructions.clear();
            mn.instructions.add(insnList);
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }
}