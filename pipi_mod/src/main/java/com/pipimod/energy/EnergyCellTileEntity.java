package com.pipimod.energy;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.util.Direction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.block.BlockState;

import com.pipimod.energy.MekanismCompat;

public class EnergyCellTileEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    private EnergyStorage storage = new EnergyStorage(0, 0, 0, 0);
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> this);
    private int capacity = 0;
    private int tickIn = 0;
    private int tickOut = 0;
    private int lastIn = 0;
    private int lastOut = 0;

    private void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public EnergyCellTileEntity() {
        super(ModTileEntities.ENERGY_CELL.get());
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        this.storage = new EnergyStorage(capacity, capacity, capacity, storage.getEnergyStored());
        setChanged();
        sync();
    }

    @Override
    public void onLoad() {
        if (capacity == 0 && level != null) {
            BlockState state = level.getBlockState(worldPosition);
            if (state.getBlock() instanceof EnergyCellBlock) {
                setCapacity(((EnergyCellBlock) state.getBlock()).getCapacity());
            }
        }
    }

    @Override
    public void tick() {
        lastIn = tickIn;
        lastOut = tickOut;
        tickIn = 0;
        tickOut = 0;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(getBlockState(), pkt.getTag());
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.capacity = nbt.getInt("Capacity");
        int energy = nbt.getInt("Energy");
        setCapacity(capacity);
        setEnergy(energy);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt("Capacity", capacity);
        nbt.putInt("Energy", storage.getEnergyStored());
        return nbt;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY) return energy.cast();
        if (MekanismCompat.isLoaded() && cap == MekanismCompat.getCapability()) {
            return MekanismCompat.createWrapper(this).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energy.invalidate();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int r = storage.receiveEnergy(maxReceive, simulate);
        if (!simulate && r > 0) {
            tickIn += r;
            setChanged();
            sync();
        }
        return r;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int e = storage.extractEnergy(maxExtract, simulate);
        if (!simulate && e > 0) {
            tickOut += e;
            setChanged();
            sync();
        }
        return e;
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    /** Returns energy received during the last tick. */
    public int getLastInput() {
        return lastIn;
    }

    /** Returns energy extracted during the last tick. */
    public int getLastOutput() {
        return lastOut;
    }

    public void setEnergy(int amount) {
        int current = storage.getEnergyStored();
        if (amount > current) {
            storage.receiveEnergy(amount - current, false);
        } else {
            storage.extractEnergy(current - amount, false);
        }
        setChanged();
        sync();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
