package com.example.industrialsynergy.blocks.generators;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class CoalGeneratorBlock extends Block {
    public CoalGeneratorBlock() {
        super(Properties.create(Material.IRON).hardnessAndResistance(3.0f).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new CoalGeneratorTileEntity(); }
}
