package com.fubangkun.galacticraftcompatibility.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Reads the OptiFine shader state without a compile-time dependency.
 *
 * <p>OptiFine ships its configuration class in the default package and is not available in the
 * development classpath, so both the preferred accessor and the backing flag are resolved
 * reflectively once and cached. Any lookup failure means "no shader pack", which keeps the
 * regular sky rendering untouched.</p>
 */
public final class ShaderCompat {
    private static final Method IS_SHADERS = findIsShaders();
    private static final Field SHADER_PACK_LOADED = findShaderPackLoaded();

    private ShaderCompat() {
    }

    public static boolean isShaderPackActive() {
        try {
            if (IS_SHADERS != null) {
                return (Boolean) IS_SHADERS.invoke(null);
            }
            if (SHADER_PACK_LOADED != null) {
                return SHADER_PACK_LOADED.getBoolean(null);
            }
        } catch (ReflectiveOperationException ignored) {
            // OptiFine is present but the class layout changed; treat shaders as inactive.
        }
        return false;
    }

    private static Method findIsShaders() {
        for (String className : new String[]{"Config", "net.optifine.Config"}) {
            try {
                return Class.forName(className).getMethod("isShaders");
            } catch (ReflectiveOperationException | LinkageError ignored) {
                // Try the next known layout.
            }
        }
        return null;
    }

    private static Field findShaderPackLoaded() {
        try {
            return Class.forName("net.optifine.shaders.Shaders").getField("shaderPackLoaded");
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return null;
        }
    }
}
