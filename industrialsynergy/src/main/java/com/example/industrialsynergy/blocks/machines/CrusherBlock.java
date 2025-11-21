package com.example.industrialsynergy.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import javax.annotation.Nullable;

/**
 * Блок дробителя. Просто делегирует создание тайл-сущности.
 */
public class CrusherBlock extends Block {
    public CrusherBlock() {
        super(Properties.create(Material.IRON).hardnessAndResistance(3.5f).sound(SoundType.METAL)
                .harvestLevel(1).harvestTool(ToolType.PICKAXE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrusherTileEntity();
    }
}
