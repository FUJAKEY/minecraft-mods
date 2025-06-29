package com.pipimod.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EnergyCellBlock extends Block {
    private final int capacity;

    public EnergyCellBlock(int capacity) {
        super(Properties.of(Material.METAL).strength(2f));
        this.capacity = capacity;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        EnergyCellTileEntity te = new EnergyCellTileEntity();
        te.setCapacity(capacity);
        return te;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, net.minecraft.util.math.BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof EnergyCellTileEntity) {
                EnergyCellTileEntity cell = (EnergyCellTileEntity) tile;
                player.displayClientMessage(new StringTextComponent(cell.getEnergyStored() + "/" + cell.getMaxEnergyStored() + " FE"), true);
            }
        }
        return ActionResultType.SUCCESS;
    }
}
