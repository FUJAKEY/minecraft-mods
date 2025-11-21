package com.example.industrialsynergy.blocks.cell;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class EnergyCellBlock extends Block {
    private final Supplier<? extends TileEntity> tileFactory;
    public EnergyCellBlock(int capacity, Supplier<? extends TileEntity> tileFactory) {
        super(Properties.create(Material.IRON).hardnessAndResistance(4.0f).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE));
        this.tileFactory = tileFactory;
    }

    @Override
    public boolean hasTileEntity(BlockState state) { return true; }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return tileFactory.get();
    }
}
