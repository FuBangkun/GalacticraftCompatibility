package com.fubangkun.galacticraftcompatibility;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class ConfigCheckData {
    public boolean acGalaxyMap;
    public boolean epGalaxyMap;
    public boolean solGalaxyMap;

    public boolean gsdMarsSpaceStation;
    public boolean gsdVenusSpaceStation;
    public boolean epMarsSpaceStation;
    public boolean epVenusSpaceStation;

    public boolean epGSCompat;
    public boolean gscAdvancedCraft;

    public boolean epMercury;
    public boolean epJupiter;
    public boolean epSaturn;
    public boolean epUranus;
    public boolean epNeptune;
    public boolean epPluto;
    public boolean epEris;

    public boolean epTriton;
    public boolean epEuropa;
    public boolean epIO;
    public boolean epDeimos;
    public boolean epCallisto;
    public boolean epGanymede;
    public boolean epRhea;
    public boolean epTitan;
    public boolean epOberon;
    public boolean epTitania;
    public boolean epIapetus;
    public boolean epCeres;
    public boolean epKuiperBelt;
    public boolean epUnreachableMoons;

    public boolean gsdMercury;
    public boolean gsdJupiter;
    public boolean gsdSaturn;
    public boolean gsdUranus;
    public boolean gsdPluto;
    public boolean gsdCeres;
    public boolean gsdKuiperBelt;

    public ConfigCheckData() {
    }

    public static ConfigCheckData readCurrent() {
        ConfigCheckData data = new ConfigCheckData();

        if (GCC.ac != null) {
            data.acGalaxyMap = GCC.ac.get("galaxymap", "enableNewGalaxyMap", true).getBoolean();
        }

        if (GCC.ep != null) {
            data.epGalaxyMap = GCC.ep.get("general settings", "Use Custom Galaxy Map/Celestial Selection Screen", false).getBoolean();
            data.epMarsSpaceStation = GCC.ep.get("space stations", "Mars SpaceStation", true).getBoolean();
            data.epVenusSpaceStation = GCC.ep.get("space stations", "Venus SpaceStation", true).getBoolean();
            data.epGSCompat = GCC.ep.get("compatibility support", "Enable Galaxy Space Compatibility", false).getBoolean();

            data.epMercury = GCC.ep.get("main dimensions", "Mercury & Tier 4 Rocket", true).getBoolean();
            data.epJupiter = GCC.ep.get("main dimensions", "Jupiter & Tier 5 Rocket", true).getBoolean();
            data.epSaturn = GCC.ep.get("main dimensions", "Saturn & Tier 6 Rocket", true).getBoolean();
            data.epUranus = GCC.ep.get("main dimensions", "Uranus & Tier 7 Rocket", true).getBoolean();
            data.epNeptune = GCC.ep.get("main dimensions", "Neptune & Tier 8 Rocket", true).getBoolean();
            data.epPluto = GCC.ep.get("main dimensions", "Pluto & Tier 9 Rocket", true).getBoolean();
            data.epEris = GCC.ep.get("main dimensions", "Eris & Tier 10 Rocket", true).getBoolean();

            data.epTriton = GCC.ep.get("other dimensions", "Triton", true).getBoolean();
            data.epEuropa = GCC.ep.get("other dimensions", "Europa", true).getBoolean();
            data.epIO = GCC.ep.get("other dimensions", "IO", true).getBoolean();
            data.epDeimos = GCC.ep.get("other dimensions", "Deimos", true).getBoolean();
            data.epCallisto = GCC.ep.get("other dimensions", "Callisto", true).getBoolean();
            data.epGanymede = GCC.ep.get("other dimensions", "Ganymede", true).getBoolean();
            data.epRhea = GCC.ep.get("other dimensions", "Rhea", true).getBoolean();
            data.epTitan = GCC.ep.get("other dimensions", "Titan", true).getBoolean();
            data.epOberon = GCC.ep.get("other dimensions", "Oberon", true).getBoolean();
            data.epTitania = GCC.ep.get("other dimensions", "Titania", true).getBoolean();
            data.epIapetus = GCC.ep.get("other dimensions", "Iapetus", true).getBoolean();
            data.epCeres = GCC.ep.get("other dimensions", "Ceres", true).getBoolean();
            data.epKuiperBelt = GCC.ep.get("other dimensions", "Kuiper Belt", true).getBoolean();
            data.epUnreachableMoons = GCC.ep.get("other dimensions", "Unreachable moons on the Celestial Selection Screen", true).getBoolean();
        }

        if (GCC.isSolLoaded && GCC.sol != null) {
            data.solGalaxyMap = GCC.sol.get("the sol - misc", "Enable Custom Galaxymap?", false).getBoolean();
        }

        if (GCC.gsd != null) {
            data.gsdMarsSpaceStation = GCC.gsd.get("general", "enableMarsSpaceStation", true).getBoolean();
            data.gsdVenusSpaceStation = GCC.gsd.get("general", "enableVenusSpaceStation", true).getBoolean();

            data.gsdMercury = GCC.gsd.get("general", "enableMercury", true).getBoolean();
            data.gsdJupiter = GCC.gsd.get("general", "enableJupiter", true).getBoolean();
            data.gsdSaturn = GCC.gsd.get("general", "enableSaturn", true).getBoolean();
            data.gsdUranus = GCC.gsd.get("general", "enableUranus", true).getBoolean();
            data.gsdPluto = GCC.gsd.get("general", "enablePluto", true).getBoolean();
            data.gsdCeres = GCC.gsd.get("general", "enableCeres", true).getBoolean();
            data.gsdKuiperBelt = GCC.gsd.get("general", "enableKuiperBelt", true).getBoolean();
        }

        if (GCC.gsc != null) {
            data.gscAdvancedCraft = GCC.gsc.get("hardmode", "enableAdvancedRocketCraft", false).getBoolean();
        }

        return data;
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(acGalaxyMap);
        buf.writeBoolean(epGalaxyMap);
        buf.writeBoolean(solGalaxyMap);

        buf.writeBoolean(gsdMarsSpaceStation);
        buf.writeBoolean(gsdVenusSpaceStation);
        buf.writeBoolean(epMarsSpaceStation);
        buf.writeBoolean(epVenusSpaceStation);

        buf.writeBoolean(epGSCompat);
        buf.writeBoolean(gscAdvancedCraft);

        buf.writeBoolean(epMercury);
        buf.writeBoolean(epJupiter);
        buf.writeBoolean(epSaturn);
        buf.writeBoolean(epUranus);
        buf.writeBoolean(epNeptune);
        buf.writeBoolean(epPluto);
        buf.writeBoolean(epEris);

        buf.writeBoolean(epTriton);
        buf.writeBoolean(epEuropa);
        buf.writeBoolean(epIO);
        buf.writeBoolean(epDeimos);
        buf.writeBoolean(epCallisto);
        buf.writeBoolean(epGanymede);
        buf.writeBoolean(epRhea);
        buf.writeBoolean(epTitan);
        buf.writeBoolean(epOberon);
        buf.writeBoolean(epTitania);
        buf.writeBoolean(epIapetus);
        buf.writeBoolean(epCeres);
        buf.writeBoolean(epKuiperBelt);
        buf.writeBoolean(epUnreachableMoons);

        buf.writeBoolean(gsdMercury);
        buf.writeBoolean(gsdJupiter);
        buf.writeBoolean(gsdSaturn);
        buf.writeBoolean(gsdUranus);
        buf.writeBoolean(gsdPluto);
        buf.writeBoolean(gsdCeres);
        buf.writeBoolean(gsdKuiperBelt);
    }

    public void fromBytes(ByteBuf buf) {
        acGalaxyMap = buf.readBoolean();
        epGalaxyMap = buf.readBoolean();
        solGalaxyMap = buf.readBoolean();

        gsdMarsSpaceStation = buf.readBoolean();
        gsdVenusSpaceStation = buf.readBoolean();
        epMarsSpaceStation = buf.readBoolean();
        epVenusSpaceStation = buf.readBoolean();

        epGSCompat = buf.readBoolean();
        gscAdvancedCraft = buf.readBoolean();

        epMercury = buf.readBoolean();
        epJupiter = buf.readBoolean();
        epSaturn = buf.readBoolean();
        epUranus = buf.readBoolean();
        epNeptune = buf.readBoolean();
        epPluto = buf.readBoolean();
        epEris = buf.readBoolean();

        epTriton = buf.readBoolean();
        epEuropa = buf.readBoolean();
        epIO = buf.readBoolean();
        epDeimos = buf.readBoolean();
        epCallisto = buf.readBoolean();
        epGanymede = buf.readBoolean();
        epRhea = buf.readBoolean();
        epTitan = buf.readBoolean();
        epOberon = buf.readBoolean();
        epTitania = buf.readBoolean();
        epIapetus = buf.readBoolean();
        epCeres = buf.readBoolean();
        epKuiperBelt = buf.readBoolean();
        epUnreachableMoons = buf.readBoolean();

        gsdMercury = buf.readBoolean();
        gsdJupiter = buf.readBoolean();
        gsdSaturn = buf.readBoolean();
        gsdUranus = buf.readBoolean();
        gsdPluto = buf.readBoolean();
        gsdCeres = buf.readBoolean();
        gsdKuiperBelt = buf.readBoolean();
    }

    public List<DiffEntry> getDifferences(ConfigCheckData other) {
        List<DiffEntry> diffs = new ArrayList<>();

        addDiffIfMismatch(diffs, "core.conf [AsmodeusCore] -> enableNewGalaxyMap", this.acGalaxyMap, other.acGalaxyMap);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Use Custom Galaxy Map", this.epGalaxyMap, other.epGalaxyMap);
        if (GCC.isSolLoaded) {
            addDiffIfMismatch(diffs, "sol.conf -> Enable Custom Galaxymap?", this.solGalaxyMap, other.solGalaxyMap);
        }

        addDiffIfMismatch(diffs, "dimensions.conf [GalaxySpace] -> enableMarsSpaceStation", this.gsdMarsSpaceStation, other.gsdMarsSpaceStation);
        addDiffIfMismatch(diffs, "dimensions.conf [GalaxySpace] -> enableVenusSpaceStation", this.gsdVenusSpaceStation, other.gsdVenusSpaceStation);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Mars SpaceStation", this.epMarsSpaceStation, other.epMarsSpaceStation);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Venus SpaceStation", this.epVenusSpaceStation, other.epVenusSpaceStation);

        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Enable Galaxy Space Compatibility", this.epGSCompat, other.epGSCompat);
        addDiffIfMismatch(diffs, "core.conf [GalaxySpace] -> enableAdvancedRocketCraft", this.gscAdvancedCraft, other.gscAdvancedCraft);

        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Mercury & Tier 4 Rocket", this.epMercury, other.epMercury);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Jupiter & Tier 5 Rocket", this.epJupiter, other.epJupiter);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Saturn & Tier 6 Rocket", this.epSaturn, other.epSaturn);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Uranus & Tier 7 Rocket", this.epUranus, other.epUranus);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Neptune & Tier 8 Rocket", this.epNeptune, other.epNeptune);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Pluto & Tier 9 Rocket", this.epPluto, other.epPluto);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Eris & Tier 10 Rocket", this.epEris, other.epEris);

        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Triton", this.epTriton, other.epTriton);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Europa", this.epEuropa, other.epEuropa);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> IO", this.epIO, other.epIO);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Deimos", this.epDeimos, other.epDeimos);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Callisto", this.epCallisto, other.epCallisto);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Ganymede", this.epGanymede, other.epGanymede);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Rhea", this.epRhea, other.epRhea);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Titan", this.epTitan, other.epTitan);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Oberon", this.epOberon, other.epOberon);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Titania", this.epTitania, other.epTitania);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Iapetus", this.epIapetus, other.epIapetus);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Ceres", this.epCeres, other.epCeres);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Kuiper Belt", this.epKuiperBelt, other.epKuiperBelt);
        addDiffIfMismatch(diffs, "ExtraPlanets.cfg -> Unreachable moons...", this.epUnreachableMoons, other.epUnreachableMoons);

        addDiffIfMismatch(diffs, "dimensions.conf [GalaxySpace] -> enableMercury", this.gsdMercury, other.gsdMercury);
        addDiffIfMismatch(diffs, "dimensions.conf [GalaxySpace] -> enableJupiter", this.gsdJupiter, other.gsdJupiter);
        addDiffIfMismatch(diffs, "dimensions.conf [GalaxySpace] -> enableSaturn", this.gsdSaturn, other.gsdSaturn);
        addDiffIfMismatch(diffs, "dimensions.conf [GalaxySpace] -> enableUranus", this.gsdUranus, other.gsdUranus);
        addDiffIfMismatch(diffs, "dimensions.conf [GalaxySpace] -> enablePluto", this.gsdPluto, other.gsdPluto);
        addDiffIfMismatch(diffs, "dimensions.conf [GalaxySpace] -> enableCeres", this.gsdCeres, other.gsdCeres);
        addDiffIfMismatch(diffs, "dimensions.conf [GalaxySpace] -> enableKuiperBelt", this.gsdKuiperBelt, other.gsdKuiperBelt);

        return diffs;
    }

    private void addDiffIfMismatch(List<DiffEntry> list, String configPath, boolean serverVal, boolean clientVal) {
        if (serverVal != clientVal) {
            list.add(new DiffEntry(configPath, serverVal, clientVal));
        }
    }

    public ITextComponent getDiffReportForUI(List<DiffEntry> diffs, int maxDisplayLines) {
        if (diffs.isEmpty()) {
            return null;
        }

        TextComponentString root = new TextComponentString("");
        int displayCount = Math.min(diffs.size(), maxDisplayLines);

        for (int i = 0; i < displayCount; i++) {
            DiffEntry entry = diffs.get(i);
            ITextComponent line = new TextComponentString(" §f* " + entry.configPath + ": ");
            line.appendSibling(new TextComponentTranslation("gui.galacticraftcompatibility.intercept.server"));
            line.appendText(": §b" + entry.serverVal + "§r | ");
            line.appendSibling(new TextComponentTranslation("gui.galacticraftcompatibility.intercept.client"));
            line.appendText(": §c" + entry.clientVal + "§r\n");
            root.appendSibling(line);
        }

        if (diffs.size() > maxDisplayLines) {
            int hiddenCount = diffs.size() - maxDisplayLines;
            ITextComponent moreLine = new TextComponentString(" §7");
            moreLine.appendSibling(new TextComponentTranslation("gui.galacticraftcompatibility.intercept.more", hiddenCount));
            moreLine.appendText("\n");
            root.appendSibling(moreLine);
        }

        return root;
    }

    public static class DiffEntry {
        public final String configPath;
        public final boolean serverVal;
        public final boolean clientVal;

        public DiffEntry(String configPath, boolean serverVal, boolean clientVal) {
            this.configPath = configPath;
            this.serverVal = serverVal;
            this.clientVal = clientVal;
        }
    }
}