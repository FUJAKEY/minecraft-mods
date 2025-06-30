package com.pipimod.energy;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import com.pipimod.energy.ThermalGeneratorTileEntity;
import com.pipimod.energy.SolarPanelTileEntity;
import com.pipimod.energy.WindTurbineTileEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, EnergyMod.MODID);

    public static final RegistryObject<TileEntityType<EnergyCellTileEntity>> ENERGY_CELL = TILE_ENTITIES.register("energy_cell", () -> TileEntityType.Builder.of(EnergyCellTileEntity::new, ModBlocks.ENERGY_CELL_ML.get(), ModBlocks.ENERGY_CELL_SR.get(), ModBlocks.ENERGY_CELL_MX.get(), ModBlocks.ENERGY_CELL_INF.get()).build(null));
    public static final RegistryObject<TileEntityType<WireBlockEntity>> WIRE = TILE_ENTITIES.register("energy_wire", () -> TileEntityType.Builder.of(WireBlockEntity::new, ModBlocks.ENERGY_WIRE.get()).build(null));
    public static final RegistryObject<TileEntityType<MetalFillerTileEntity>> METAL_FILLER = TILE_ENTITIES.register("metal_filler", () -> TileEntityType.Builder.of(MetalFillerTileEntity::new, ModBlocks.METAL_FILLER.get()).build(null));
    public static final RegistryObject<TileEntityType<ThermalGeneratorTileEntity>> THERMAL_GENERATOR = TILE_ENTITIES.register("thermal_generator", () -> TileEntityType.Builder.of(ThermalGeneratorTileEntity::new, ModBlocks.THERMAL_GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<SolarPanelTileEntity>> SOLAR_PANEL = TILE_ENTITIES.register("solar_panel", () -> TileEntityType.Builder.of(SolarPanelTileEntity::new, ModBlocks.SOLAR_PANEL.get()).build(null));
    public static final RegistryObject<TileEntityType<WindTurbineTileEntity>> WIND_TURBINE = TILE_ENTITIES.register("wind_turbine", () -> TileEntityType.Builder.of(WindTurbineTileEntity::new, ModBlocks.WIND_TURBINE.get()).build(null));
}
