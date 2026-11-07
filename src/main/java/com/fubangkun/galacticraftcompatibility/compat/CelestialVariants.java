package com.fubangkun.galacticraftcompatibility.compat;

import micdoodle8.mods.galacticraft.api.galaxies.*;

import java.util.*;

/**
 * Variant management for celestial bodies registered by Galacticraft Core, GalaxySpace,
 * ExtraPlanets and The Sol.
 *
 * <p>Same-name bodies (the GC original, the GalaxySpace Ex version, the ExtraPlanets version
 * and the The Sol version) form one group, and the map shows only the variant of the current
 * mode. V cycles through the available modes. When the current mode has no variant, the
 * Galacticraft Core body is used instead of swapping in an unreachable version.</p>
 *
 * <p>Only the Galacticraft API is referenced; no GalaxySpace, ExtraPlanets or AsmodeusCore
 * class is resolved directly, so a missing mod cannot cause class-loading errors.</p>
 */
public final class CelestialVariants {
    private static final String STATION_GS = "spacestation.";
    private static final String STATION_EP = "space_station.";

    private static final String OWNER_SOL = "sol";
    private static final String OWNER_GS = "galaxyspace";
    private static final String OWNER_EP = "extraplanets";
    private static final String OWNER_GC = "galacticraftcore";
    private static final String OWNER_GCP = "galacticraftplanets";

    /**
     * Extra slot holding the Galacticraft Core version of a group.
     */
    private static final int BASE_SLOT = Mode.values().length;

    private static volatile Mode currentMode = Mode.GS;

    private CelestialVariants() {
    }

    public static Mode mode() {
        return currentMode;
    }

    /**
     * Cycles to the next mode that currently has at least one variant.
     */
    public static void toggle() {
        Mode[] available = availableModes();
        int index = -1;
        for (int i = 0; i < available.length; i++) {
            if (available[i] == currentMode) {
                index = i;
                break;
            }
        }
        currentMode = available[(index + 1) % available.length];
    }

    /**
     * Picks the default mode from the dimension the player is in, so a map opened inside a
     * GalaxySpace, ExtraPlanets or The Sol dimension starts on the matching variant.
     */
    public static void resetFromDimension(int dimensionId) {
        Side side = sideOfDimension(dimensionId);
        if (side != null && side != Side.BASE) {
            currentMode = side.toMode();
        }
    }

    /**
     * Keeps one body per logical key, choosing the variant that matches the given mode.
     *
     * @param input bodies to filter
     * @param mode  mode to show, or null for the current mode
     * @return filtered list, or null when the input is null
     */
    public static List<CelestialBody> filter(List<CelestialBody> input, Mode mode) {
        if (input == null) {
            return null;
        }
        if (mode == null) {
            mode = currentMode;
        }
        Map<String, CelestialBody[]> groups = buildGroups();
        List<CelestialBody> result = new ArrayList<>(input.size());
        Set<String> seen = new HashSet<>();
        for (CelestialBody body : input) {
            String key = key(body);
            if (key == null) {
                result.add(body);
                continue;
            }
            if (!seen.add(key)) {
                continue;
            }
            CelestialBody[] group = groups.get(key);
            result.add(group == null ? body : pick(group, mode));
        }
        return result;
    }

    /**
     * Returns the variant of the given body for the given mode.
     *
     * @return the counterpart, or null when the body has no group or is already the counterpart
     */
    public static CelestialBody variantOf(CelestialBody body, Mode mode) {
        if (body == null) {
            return null;
        }
        String key = key(body);
        if (key == null) {
            return null;
        }
        CelestialBody[] group = buildGroups().get(key);
        if (group == null) {
            return null;
        }
        CelestialBody target = pick(group, mode);
        return target == body ? null : target;
    }

    private static CelestialBody pick(CelestialBody[] group, Mode mode) {
        CelestialBody chosen = group[mode.ordinal()];
        if (chosen != null) {
            return chosen;
        }
        // The current mode has no variant: use the Galacticraft Core version, then any other one.
        if (group[BASE_SLOT] != null) {
            return group[BASE_SLOT];
        }
        for (CelestialBody candidate : group) {
            if (candidate != null) {
                return candidate;
            }
        }
        return null;
    }

    private static Mode[] availableModes() {
        Map<String, CelestialBody[]> groups = buildGroups();
        boolean gs = false;
        boolean ep = false;
        boolean sol = false;
        for (CelestialBody[] group : groups.values()) {
            gs |= group[Mode.GS.ordinal()] != null;
            ep |= group[Mode.EP.ordinal()] != null;
            sol |= group[Mode.SOL.ordinal()] != null;
        }
        List<Mode> modes = new ArrayList<>(3);
        if (gs) {
            modes.add(Mode.GS);
        }
        if (ep) {
            modes.add(Mode.EP);
        }
        if (sol) {
            modes.add(Mode.SOL);
        }
        if (modes.isEmpty()) {
            modes.add(Mode.GS);
        }
        return modes.toArray(new Mode[0]);
    }

    private static Side sideOfDimension(int dimensionId) {
        Map<String, CelestialBody[]> groups = buildGroups();
        for (CelestialBody body : allBodies()) {
            if (body.getDimensionID() != dimensionId) {
                continue;
            }
            String key = key(body);
            if (key == null || !groups.containsKey(key)) {
                continue;
            }
            Side side = sideOf(body);
            if (side != null) {
                return side;
            }
        }
        return null;
    }

    private static List<CelestialBody> allBodies() {
        List<CelestialBody> all = new ArrayList<>();
        all.addAll(GalaxyRegistry.getPlanets());
        all.addAll(GalaxyRegistry.getMoons());
        all.addAll(GalaxyRegistry.getSatellites());
        return all;
    }

    /**
     * Builds a key -> [GalaxySpace, ExtraPlanets, The Sol, Galacticraft Core] table.
     * A key becomes a group only when at least two variants exist.
     */
    private static Map<String, CelestialBody[]> buildGroups() {
        Map<String, CelestialBody[]> raw = new HashMap<>();
        for (CelestialBody body : allBodies()) {
            String key = key(body);
            if (key == null) {
                continue;
            }
            Side side = sideOf(body);
            if (side == null) {
                continue;
            }
            CelestialBody[] group = raw.computeIfAbsent(key, k -> new CelestialBody[Mode.values().length + 1]);
            int index = side.ordinal();
            if (group[index] == null) {
                group[index] = body;
            }
        }
        Map<String, CelestialBody[]> groups = new HashMap<>();
        for (Map.Entry<String, CelestialBody[]> entry : raw.entrySet()) {
            CelestialBody[] group = entry.getValue();
            int present = 0;
            for (CelestialBody body : group) {
                if (body != null) {
                    present++;
                }
            }
            if (present >= 2) {
                groups.put(entry.getKey(), group);
            }
        }
        return groups;
    }

    private static Side sideOf(CelestialBody body) {
        String owner = body.getOwnerId();
        if (OWNER_SOL.equals(owner)) {
            return Side.SOL;
        }
        if (OWNER_GS.equals(owner)) {
            return Side.GS;
        }
        if (OWNER_EP.equals(owner)) {
            return Side.EP;
        }
        if (OWNER_GC.equals(owner) || OWNER_GCP.equals(owner)) {
            return Side.BASE;
        }
        if (body.getClass().getName().startsWith("asmodeuscore.core.prefab.celestialbody.Ex")) {
            return Side.GS;
        }
        if (body instanceof Satellite) {
            String name = lower(body.getName());
            if (name.startsWith(STATION_GS)) {
                return Side.GS;
            }
            if (name.startsWith(STATION_EP)) {
                return Side.EP;
            }
        }
        // Bodies from other mods (for example More Planets) take no part in variant groups.
        return null;
    }

    /**
     * Logical key used for grouping.
     *
     * @return the key, or null when the body takes no part in variant groups
     */
    private static String key(CelestialBody body) {
        String name = body.getName();
        if (name == null) {
            return null;
        }
        if (body instanceof Satellite) {
            String station = lower(name);
            if (station.startsWith(STATION_GS)) {
                return "s:" + station.substring(STATION_GS.length());
            }
            if (station.startsWith(STATION_EP)) {
                return "s:" + station.substring(STATION_EP.length());
            }
            return null;
        }
        if (body instanceof IChildBody) {
            Planet parent = ((IChildBody) body).getParentPlanet();
            if (parent == null || parent.getName() == null) {
                return null;
            }
            return "m:p:" + norm(parent.getName()) + "/" + normMoon(name);
        }
        if (body instanceof Planet) {
            return "p:" + norm(name);
        }
        return null;
    }

    private static String norm(String name) {
        String value = lower(name);
        // ExtraPlanets' GS compatibility mode appends an EP suffix; strip it while normalizing.
        if (value.endsWith("ep") && value.length() > 2) {
            value = value.substring(0, value.length() - 2);
        }
        return value;
    }

    private static String normMoon(String name) {
        String value = norm(name);
        // GalaxySpace registers it as rheya; ExtraPlanets and The Sol use rhea.
        if ("rheya".equals(value)) {
            return "rhea";
        }
        return value;
    }

    private static String lower(String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    public enum Mode {
        GS("gcc.mode.galaxyspace"),
        EP("gcc.mode.extraplanets"),
        SOL("gcc.mode.sol");

        public final String translationKey;

        Mode(String translationKey) {
            this.translationKey = translationKey;
        }
    }

    /**
     * Registry side a body belongs to.
     */
    private enum Side {
        GS,
        EP,
        SOL,
        BASE;

        Mode toMode() {
            return Mode.values()[this.ordinal()];
        }
    }
}
