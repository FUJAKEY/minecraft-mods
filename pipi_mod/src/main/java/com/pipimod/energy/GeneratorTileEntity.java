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
import net.minecraft.block.BlockState;

public abstract class GeneratorTileEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    protected final EnergyStorage storage;
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> this);
    private final int generateRate;

    protected GeneratorTileEntity(int capacity, int rate, TileEntityType<?> type) {
        super(type);
        this.storage = new EnergyStorage(capacity, capacity, 0);
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
                int gen = generateRate * getEfficiency() / 100;
                if (gen > 0) {
                    storage.receiveEnergy(gen, false);
                    setChanged();
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                }
            }
        }
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
    @Override public int extractEnergy(int maxExtract, boolean simulate) { return storage.extractEnergy(maxExtract, simulate); }
    @Override public int getEnergyStored() { return storage.getEnergyStored(); }
    @Override public int getMaxEnergyStored() { return storage.getMaxEnergyStored(); }
    @Override public boolean canExtract() { return true; }
    @Override public boolean canReceive() { return false; }
}
