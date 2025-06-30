package com.pipimod.energy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.common.ToolType;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EnergyMod.MODID);

    public static final RegistryObject<Block> ENERGY_CELL_ML = BLOCKS.register("energy_cell_ml", () -> new EnergyCellBlock(100000));
    public static final RegistryObject<Block> ENERGY_CELL_SR = BLOCKS.register("energy_cell_sr", () -> new EnergyCellBlock(1000000));
    public static final RegistryObject<Block> ENERGY_CELL_MX = BLOCKS.register("energy_cell_mx", () -> new EnergyCellBlock(10000000));
    public static final RegistryObject<Block> ENERGY_CELL_INF = BLOCKS.register("energy_cell_inf", () -> new InfiniteEnergyCellBlock());
    public static final RegistryObject<Block> ENERGY_WIRE = BLOCKS.register("energy_wire", EnergyWireBlock::new);
    public static final RegistryObject<Block> METAL_FILLER = BLOCKS.register("metal_filler", MetalFillerBlock::new);

    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () ->
            new Block(Block.Properties.of(Material.STONE)
                    .strength(3.0F)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(1)
                    .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> LEAD_ORE = BLOCKS.register("lead_ore", () ->
            new Block(Block.Properties.of(Material.STONE)
                    .strength(3.0F)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(1)
                    .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> OSMIUM_ORE = BLOCKS.register("osmium_ore", () ->
            new Block(Block.Properties.of(Material.STONE)
                    .strength(3.0F)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(1)
                    .requiresCorrectToolForDrops()));
}
