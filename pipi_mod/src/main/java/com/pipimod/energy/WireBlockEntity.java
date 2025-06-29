package com.pipimod.energy;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraft.util.Direction;

import java.util.EnumMap;

public class WireBlockEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    private final EnergyStorage storage = new EnergyStorage(10, 10, 10);
    private final EnumMap<Direction, WireMode> modes = new EnumMap<>(Direction.class);
    private int tickCounter = 0;

    public WireBlockEntity() {
        super(ModTileEntities.WIRE.get());
        for (Direction dir : Direction.values()) {
            modes.put(dir, WireMode.TAKE);
        }
    }

    @Override
    public void tick() {
        tickCounter++;
        if (tickCounter % 2 != 0) return; // 0.1 sec assuming 20 tps
        for (Direction dir : Direction.values()) {
            TileEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if (neighbor instanceof IEnergyStorage) {
                IEnergyStorage other = (IEnergyStorage) neighbor;
                if (modes.get(dir) == WireMode.TAKE) {
                    int received = other.extractEnergy(10 - storage.getEnergyStored(), false);
                    if (received > 0) storage.receiveEnergy(received, false);
                } else {
                    int toSend = Math.min(storage.getEnergyStored(), 10);
                    int accepted = other.receiveEnergy(toSend, false);
                    if (accepted > 0) storage.extractEnergy(accepted, false);
                }
            }
        }
    }

    public void toggleMode(Direction side) {
        modes.put(side, modes.get(side).next());
    }

    public WireMode getMode(Direction side) {
        return modes.get(side);
    }

    // IEnergyStorage implementation
    @Override public int receiveEnergy(int maxReceive, boolean simulate) { return storage.receiveEnergy(maxReceive, simulate); }
    @Override public int extractEnergy(int maxExtract, boolean simulate) { return storage.extractEnergy(maxExtract, simulate); }
    @Override public int getEnergyStored() { return storage.getEnergyStored(); }
    @Override public int getMaxEnergyStored() { return storage.getMaxEnergyStored(); }
    @Override public boolean canExtract() { return true; }
    @Override public boolean canReceive() { return true; }
}
