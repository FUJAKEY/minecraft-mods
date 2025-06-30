# minecraft-mods

## Building energymod

1. Install Java 8 JDK and a compatible Gradle version (7.3.3 is used).
2. Run `gradle wrapper` to generate the wrapper jar if it is missing. You may use a minimal wrapper script like `wrapper.gradle` with a `Wrapper` task.
3. Execute `./gradlew build` to compile the mod. After a successful build, remove the `build/` directory and any generated binaries to keep the repository clean.

## Texture paths

Place your 128×128 PNG textures under `pipi_mod/src/main/resources/assets/energymod/textures`.
The folders `textures/block` and `textures/item` are pre‑created so you can simply drop files in.

### Blocks

```
textures/block/energy_cell_ml.png    - Energy Cell (Small)
textures/block/energy_cell_sr.png    - Energy Cell (Medium)
textures/block/energy_cell_mx.png    - Energy Cell (Large)
textures/block/energy_cell_inf.png   - Infinite Energy Cell
textures/block/energy_wire.png       - Energy Wire
textures/block/metal_filler.png      - Metal Filler
textures/block/thermal_generator.png - Thermal Generator
textures/block/solar_panel.png       - Solar Panel
textures/block/wind_turbine.png      - Wind Turbine
textures/block/copper_ore.png        - Copper Ore
textures/block/lead_ore.png          - Lead Ore
textures/block/osmium_ore.png        - Osmium Ore
```

### Items

```
textures/item/energy_cell_ml.png     - Small Energy Cell item
textures/item/energy_cell_sr.png     - Medium Energy Cell item
textures/item/energy_cell_mx.png     - Large Energy Cell item
textures/item/energy_cell_inf.png    - Infinite Energy Cell item
textures/item/energy_wire.png        - Energy Wire item
textures/item/wire_tool.png          - Wire Tool
textures/item/metal_filler.png       - Metal Filler item
textures/item/thermal_generator.png  - Thermal Generator item
textures/item/solar_panel.png        - Solar Panel item
textures/item/wind_turbine.png       - Wind Turbine item
textures/item/copper_ore.png         - Copper Ore item
textures/item/lead_ore.png           - Lead Ore item
textures/item/osmium_ore.png         - Osmium Ore item
textures/item/copper_ingot.png       - Copper Ingot
textures/item/lead_ingot.png         - Lead Ingot
textures/item/osmium_ingot.png       - Osmium Ingot
textures/item/cast_iron_ingot.png    - Cast Iron Ingot
```
