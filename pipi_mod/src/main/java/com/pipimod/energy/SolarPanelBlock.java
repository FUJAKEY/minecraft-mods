package com.pipimod.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import javax.annotation.Nullable;

public class SolarPanelBlock extends Block {
    public SolarPanelBlock() {
        super(Properties.of(Material.METAL).strength(2f).noOcclusion());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SolarPanelTileEntity();
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
