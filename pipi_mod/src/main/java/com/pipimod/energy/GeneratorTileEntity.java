package com.pipimod.energy;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.util.Direction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.block.BlockState;

public abstract class GeneratorTileEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    protected final EnergyStorage storage;
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> this);
    private final int generateRate;

    protected GeneratorTileEntity(int capacity, int rate, TileEntityType<?> type) {
        super(type);
        this.storage = new EnergyStorage(capacity, capacity, capacity);
        this.generateRate = rate;
    }

    public int getEfficiency() {
        return 100;
    }

    protected boolean canGenerate() {
        return true;
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide && canGenerate()) {
            if (storage.getEnergyStored() < storage.getMaxEnergyStored()) {
                // global nerf: generators now produce only half of their normal output
                int gen = generateRate * getEfficiency() / 100 / 2;
                if (gen > 0) {
                    storage.receiveEnergy(gen, false);
                    setChanged();
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                }
            }
        }
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
        storage.receiveEnergy(nbt.getInt("Energy") - storage.getEnergyStored(), false);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt("Energy", storage.getEnergyStored());
        return nbt;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY) return energy.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energy.invalidate();
    }

    // IEnergyStorage
    @Override public int receiveEnergy(int maxReceive, boolean simulate) { return 0; }
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int e = storage.extractEnergy(maxExtract, simulate);
        if (!simulate && e > 0) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
        return e;
    }
    @Override public int getEnergyStored() { return storage.getEnergyStored(); }
    @Override public int getMaxEnergyStored() { return storage.getMaxEnergyStored(); }
    @Override public boolean canExtract() { return true; }
    @Override public boolean canReceive() { return false; }
}
