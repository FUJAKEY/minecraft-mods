package com.pipimod.energy;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class InfiniteEnergyCellBlock extends EnergyCellBlock {
    public InfiniteEnergyCellBlock() {
        super(Integer.MAX_VALUE);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        EnergyCellTileEntity te = new EnergyCellTileEntity();
        te.setCapacity(Integer.MAX_VALUE);
        te.setEnergy(Integer.MAX_VALUE);
        return te;
    }
}
