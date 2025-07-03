package com.pipimod.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import javax.annotation.Nullable;
import net.minecraft.util.IStringSerializable;

public class WindTurbineBlock extends Block {

    public enum Part implements IStringSerializable {
        BASE, MIDDLE, TOP;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape BASE_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape MIDDLE_SHAPE = Block.box(2, 0, 2, 14, 16, 14);
    private static final VoxelShape TOP_SHAPE = VoxelShapes.or(
            Block.box(4, 0, 4, 12, 16, 12),
            Block.box(0, 14, 7, 16, 16, 9),
            Block.box(7, 14, 0, 9, 16, 16)
    );

    public WindTurbineBlock() {
        super(Properties.of(Material.METAL).strength(2f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PART, Part.BASE)
                .setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        if (pos.getY() < ctx.getLevel().getMaxBuildHeight() - 2 &&
                ctx.getLevel().getBlockState(pos.above()).isAir() &&
                ctx.getLevel().getBlockState(pos.above(2)).isAir()) {
            return this.defaultBlockState()
                    .setValue(FACING, ctx.getHorizontalDirection());
        }
        return null;
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        Direction facing = state.getValue(FACING);
        world.setBlock(pos.above(), this.defaultBlockState().setValue(PART, Part.MIDDLE).setValue(FACING, facing), 3);
        world.setBlock(pos.above(2), this.defaultBlockState().setValue(PART, Part.TOP).setValue(FACING, facing), 3);
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        Part part = state.getValue(PART);
        if (part == Part.BASE) {
            world.removeBlock(pos.above(), false);
            world.removeBlock(pos.above(2), false);
        } else if (part == Part.MIDDLE) {
            BlockPos base = pos.below();
            world.destroyBlock(base, true);
            world.removeBlock(pos.above(), false);
        } else if (part == Part.TOP) {
            BlockPos base = pos.below(2);
            world.destroyBlock(base, true);
            world.removeBlock(pos.below(), false);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(PART) == Part.BASE;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return state.getValue(PART) == Part.BASE ? new WindTurbineTileEntity() : null;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        switch (state.getValue(PART)) {
            case MIDDLE:
                return MIDDLE_SHAPE;
            case TOP:
                return TOP_SHAPE;
            default:
                return BASE_SHAPE;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return getShape(state, world, pos, ctx);
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

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        Part part = state.getValue(PART);
        if (part == Part.MIDDLE) {
            world.destroyBlock(pos.below(), !player.isCreative());
            world.removeBlock(pos.above(), false);
        } else if (part == Part.TOP) {
            world.destroyBlock(pos.below(2), !player.isCreative());
            world.removeBlock(pos.below(), false);
        } else {
            world.removeBlock(pos.above(), false);
            world.removeBlock(pos.above(2), false);
        }
        super.playerWillDestroy(world, pos, state, player);
    }
}
