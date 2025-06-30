package com.pipimod.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import javax.annotation.Nullable;

public class WindTurbineBlock extends Block {
    private static final VoxelShape SHAPE = VoxelShapes.or(
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(5, 16, 5, 11, 32, 11),
            Block.box(7, 32, 7, 9, 48, 9),
            Block.box(0, 44, 7, 16, 46, 9),
            Block.box(7, 44, 0, 9, 46, 16)
    );

    public WindTurbineBlock() {
        super(Properties.of(Material.METAL).strength(2f).noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WindTurbineTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return SHAPE;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, net.minecraft.util.math.BlockRayTraceResult hit) {
        if (!world.isClientSide) {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof GeneratorTileEntity) {
                GeneratorTileEntity gen = (GeneratorTileEntity) tile;
                player.displayClientMessage(new TranslationTextComponent("message.energymod.energy", gen.getEnergyStored(), gen.getMaxEnergyStored()), true);
            }
        }
        return ActionResultType.SUCCESS;
    }
}
