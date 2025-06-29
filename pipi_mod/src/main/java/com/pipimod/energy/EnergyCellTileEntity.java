package com.pipimod.energy;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.util.Direction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.block.BlockState;

public class EnergyCellTileEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    private EnergyStorage storage = new EnergyStorage(0, 0, 0, 0);
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> this);
    private int capacity = 0;

    public EnergyCellTileEntity() {
        super(ModTileEntities.ENERGY_CELL.get());
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        this.storage = new EnergyStorage(capacity, capacity, capacity, storage.getEnergyStored());
    }

    @Override
    public void tick() {
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
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energy.invalidate();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    public void setEnergy(int amount) {
        int current = storage.getEnergyStored();
        if (amount > current) {
            storage.receiveEnergy(amount - current, false);
        } else {
            storage.extractEnergy(current - amount, false);
        }
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
