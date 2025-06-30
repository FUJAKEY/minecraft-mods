package com.pipimod.energy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.shapes.ISelectionContext;

import javax.annotation.Nullable;

public class EnergyWireBlock extends Block {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    private static final VoxelShape CENTER = Block.box(4, 4, 4, 12, 12, 12);
    private static final VoxelShape NORTH_SHAPE = Block.box(4, 4, 0, 12, 12, 4);
    private static final VoxelShape SOUTH_SHAPE = Block.box(4, 4, 12, 12, 12, 16);
    private static final VoxelShape WEST_SHAPE = Block.box(0, 4, 4, 4, 12, 12);
    private static final VoxelShape EAST_SHAPE = Block.box(12, 4, 4, 16, 12, 12);
    private static final VoxelShape DOWN_SHAPE = Block.box(4, 0, 4, 12, 4, 12);
    private static final VoxelShape UP_SHAPE = Block.box(4, 12, 4, 12, 16, 12);

    public EnergyWireBlock() {
        super(Properties.of(Material.METAL).strength(0.2F));
        this.registerDefaultState(stateDefinition.any().setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public WireBlockEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WireBlockEntity();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = this.defaultBlockState();
        for (Direction dir : Direction.values()) {
            BlockPos np = context.getClickedPos().relative(dir);
            BlockState neighbor = context.getLevel().getBlockState(np);
            boolean connects = neighbor.getBlock() instanceof EnergyWireBlock || neighbor.getBlock() instanceof EnergyCellBlock;
            if (!connects) {
                TileEntity te = context.getLevel().getBlockEntity(np);
                connects = te != null && te.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).isPresent();
            }
            if (connects) {
                state = state.setValue(getProperty(dir), true);
            }
        }
        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, IWorld world, BlockPos currentPos, BlockPos neighborPos) {
        boolean connected = neighborState.getBlock() instanceof EnergyWireBlock || neighborState.getBlock() instanceof EnergyCellBlock;
        if (!connected) {
            TileEntity te = world.getBlockEntity(neighborPos);
            connected = te != null && te.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).isPresent();
        }
        return state.setValue(getProperty(direction), connected);
    }

    public static BooleanProperty getProperty(Direction dir) {
        switch (dir) {
            case NORTH: return NORTH;
            case SOUTH: return SOUTH;
            case EAST: return EAST;
            case WEST: return WEST;
            case UP: return UP;
            default: return DOWN;
        }
    }

    private VoxelShape buildShape(BlockState state) {
        VoxelShape shape = CENTER;
        if (state.getValue(NORTH)) shape = VoxelShapes.or(shape, NORTH_SHAPE);
        if (state.getValue(SOUTH)) shape = VoxelShapes.or(shape, SOUTH_SHAPE);
        if (state.getValue(WEST)) shape = VoxelShapes.or(shape, WEST_SHAPE);
        if (state.getValue(EAST)) shape = VoxelShapes.or(shape, EAST_SHAPE);
        if (state.getValue(UP)) shape = VoxelShapes.or(shape, UP_SHAPE);
        if (state.getValue(DOWN)) shape = VoxelShapes.or(shape, DOWN_SHAPE);
        return shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return buildShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
        return buildShape(state);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }
}
