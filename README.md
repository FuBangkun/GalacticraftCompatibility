# GalacticraftCompatibility

A compatibility patch mod for Galacticraft 1.12.2. It lets the same-name celestial bodies of GalaxySpace, ExtraPlanets and The Sol coexist,
shows only one entry in the same map, and lets you switch which version is used in game.

Need MixinBooter.

## Features

- Same-name celestial bodies show only one entry, no longer duplicated or missing.
- A mode button sits above the map's toggle column and shows the variant in use (GalaxySpace / ExtraPlanets / The Sol). Click it to cycle; the name, texture and dimension change accordingly.
- When the map is opened, the mode matching the player's dimension is selected automatically.
- Uses the AsmodeusCore map exclusively.
- OptiFine shader packs work: while a pack is active, the add-on sky renderers are skipped and the vanilla sky is drawn inside the shader pass; disabling the pack restores them.
- GalaxySpace and ExtraPlanets space suits are equally effective in each other's environment.
- Compatibility-related config settings are fixed by the mod, so there is no need to manually edit config files.
- Planet Progression research papers also cover The Sol-only bodies.

## Mod Support

- AsmodeusCore (required)
- GalaxySpace
- ExtraPlanet
- TheSol
- MorePlanets
- InterstellarExoplanets
- ToTheStarsRemake
- GalacticResearch
- PlanetProgression

## Usage

1. Press `M` to open the map.
2. Click the mode button above the map's toggle column to cycle variants.
3. The button label shows the variant currently in use.

## Configuration

The config files remain in the `config` directory, but compatibility-related switches are automatically fixed by the mod, so manual changes have no effect.
Other settings are unaffected.

The Venus and Mars space stations keep the GalaxySpace version.

## Known Issues

- Some GalaxySpace dimensions do not have complete world information. The client skips them, and
  `There was a problem getting WorldProvider` may appear in the log, without affecting other dimensions.
- A few of The Sol's gas giants show `Class: Unknown` in the info panel, without affecting travel and switching.
- Galactic Research and Planet Progression both replace the celestial selection map with their own screen and keep separate
  research progress, so only one of them can be used at a time. When both are installed the mod logs a warning and shows
  one in the map.

## License

GPLv3
