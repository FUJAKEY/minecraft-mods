package com.pipimod.energy;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyCellTileEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    private final EnergyStorage storage;

    public EnergyCellTileEntity(int capacity) {
        super(ModTileEntities.ENERGY_CELL.get());
        this.storage = new EnergyStorage(capacity, capacity, capacity, 0);
    }

    @Override
    public void tick() {
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
        storage.receiveEnergy(amount, false);
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
