package com.pipimod.energy;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraft.util.Direction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import com.pipimod.energy.MekanismCompat;

import java.util.EnumMap;

public class WireBlockEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    private final EnergyStorage storage = new EnergyStorage(10, 10, 10);
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> this);
    private final EnumMap<Direction, WireMode> modes = new EnumMap<>(Direction.class);
    private int tickCounter = 0;

    public WireBlockEntity() {
        super(ModTileEntities.WIRE.get());
        for (Direction dir : Direction.values()) {
            modes.put(dir, WireMode.AUTO);
        }
    }

    @Override
    public void tick() {
        tickCounter++;
        if (tickCounter % 2 != 0) return; // run every 0.1 sec

        boolean pullPhase = ((tickCounter / 2) % 2) == 0;

        if (pullPhase) {
            for (Direction dir : Direction.values()) {
                WireMode mode = modes.get(dir);
                TileEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
                if (neighbor != null) {
                    neighbor.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).ifPresent(other -> {
                        if (mode == WireMode.TAKE || (mode == WireMode.AUTO && other.getEnergyStored() > storage.getEnergyStored())) {
                            int space = storage.getMaxEnergyStored() - storage.getEnergyStored();
                            if (space > 0) {
                                int received = other.extractEnergy(Math.min(10, space), false);
                                if (received > 0) {
                                    storage.receiveEnergy(received, false);
                                    setChanged();
                                }
                            }
                        }
                    });
                }
            }
        } else { // push phase
            for (Direction dir : Direction.values()) {
                WireMode mode = modes.get(dir);
                TileEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
                if (neighbor != null) {
                    neighbor.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).ifPresent(other -> {
                        if (mode == WireMode.GIVE || (mode == WireMode.AUTO && other.getEnergyStored() < storage.getEnergyStored())) {
                            int available = storage.getEnergyStored();
                            if (available > 0) {
                                int sent = other.receiveEnergy(Math.min(10, available), false);
                                if (sent > 0) {
                                    storage.extractEnergy(sent, false);
                                    setChanged();
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    public void toggleMode(Direction side) {
        modes.put(side, modes.get(side).next());
        setChanged();
    }

    public WireMode getMode(Direction side) {
        return modes.get(side);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        int energyStored = nbt.getInt("Energy");
        storage.receiveEnergy(energyStored - storage.getEnergyStored(), false);
        for (Direction dir : Direction.values()) {
            String key = "Mode" + dir.getSerializedName();
            if (nbt.contains(key)) {
                int ord = nbt.getInt(key);
                modes.put(dir, WireMode.values()[ord % WireMode.values().length]);
            }
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        nbt.putInt("Energy", storage.getEnergyStored());
        for (Direction dir : Direction.values()) {
            nbt.putInt("Mode" + dir.getSerializedName(), modes.get(dir).ordinal());
        }
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

    // IEnergyStorage implementation
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int r = storage.receiveEnergy(maxReceive, simulate);
        if (!simulate && r > 0) setChanged();
        return r;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int e = storage.extractEnergy(maxExtract, simulate);
        if (!simulate && e > 0) setChanged();
        return e;
    }

    @Override public int getEnergyStored() { return storage.getEnergyStored(); }
    @Override public int getMaxEnergyStored() { return storage.getMaxEnergyStored(); }
    @Override public boolean canExtract() { return true; }
    @Override public boolean canReceive() { return true; }
}
