package com.pipimod.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class MetalFillerBlock extends Block {
    public MetalFillerBlock() {
        super(Properties.of(Material.METAL).strength(2f));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MetalFillerTileEntity();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, net.minecraft.util.math.BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof INamedContainerProvider && player instanceof ServerPlayerEntity) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) te, pos);
            }
            return ActionResultType.CONSUME;
        }
        return ActionResultType.SUCCESS;
    }
}
