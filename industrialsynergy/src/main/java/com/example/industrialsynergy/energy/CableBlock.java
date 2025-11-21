package com.example.industrialsynergy.energy;

import com.example.industrialsynergy.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

/**
 * Базовый блок кабеля. Хранит тип (пропускная способность) через поля maxTransfer.
 */
public class CableBlock extends Block {
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    private final int transferRate;
    private final java.util.function.Supplier<? extends TileEntity> tileSupplier;

    public CableBlock(int transferRate, java.util.function.Supplier<? extends TileEntity> tileSupplier) {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(2.0f, 6.0f)
                .sound(SoundType.METAL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .notSolid());
        this.transferRate = transferRate;
        this.tileSupplier = tileSupplier;
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, false).with(EAST, false).with(SOUTH, false)
                .with(WEST, false).with(UP, false).with(DOWN, false));
    }

    public int getTransferRate() {
        return transferRate;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return tileSupplier.get();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // При установке сразу вычисляем соединения
        return updateConnections(context.getWorld(), context.getPos(), this.getDefaultState());
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        worldIn.setBlockState(pos, updateConnections(worldIn, pos, state), 2);
    }

    private BlockState updateConnections(World world, BlockPos pos, BlockState state) {
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.offset(dir);
            TileEntity te = world.getTileEntity(neighborPos);
            boolean connected = te != null && te.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).isPresent();
            state = state.with(getProperty(dir), connected);
        }
        return state;
    }

    private BooleanProperty getProperty(Direction dir) {
        switch (dir) {
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case EAST:
                return EAST;
            case WEST:
                return WEST;
            case UP:
                return UP;
            case DOWN:
            default:
                return DOWN;
        }
    }
}
