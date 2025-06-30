package com.pipimod.energy;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, EnergyMod.MODID);

    public static final RegistryObject<TileEntityType<EnergyCellTileEntity>> ENERGY_CELL = TILE_ENTITIES.register("energy_cell", () -> TileEntityType.Builder.of(EnergyCellTileEntity::new, ModBlocks.ENERGY_CELL_ML.get(), ModBlocks.ENERGY_CELL_SR.get(), ModBlocks.ENERGY_CELL_MX.get(), ModBlocks.ENERGY_CELL_INF.get()).build(null));
    public static final RegistryObject<TileEntityType<WireBlockEntity>> WIRE = TILE_ENTITIES.register("energy_wire", () -> TileEntityType.Builder.of(WireBlockEntity::new, ModBlocks.ENERGY_WIRE.get()).build(null));
}
