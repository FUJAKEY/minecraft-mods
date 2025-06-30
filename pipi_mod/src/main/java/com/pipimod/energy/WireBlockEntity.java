package com.pipimod.energy;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockState;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import com.pipimod.energy.MekanismCompat;

import java.util.EnumMap;

public class WireBlockEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    private static final int CAPACITY = 1280;
    private static final int TRANSFER_RATE = 1280;
    private final EnergyStorage storage = new EnergyStorage(CAPACITY, CAPACITY, CAPACITY);
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> this);
    private final EnumMap<Direction, WireMode> modes = new EnumMap<>(Direction.class);
    private int lastTransfer = 0;

    private void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public WireBlockEntity() {
        super(ModTileEntities.WIRE.get());
        for (Direction dir : Direction.values()) {
            modes.put(dir, WireMode.AUTO);
        }
    }

    @Override
    public void tick() {
        int receivedTotal = 0;
        // pull from TAKE or higher-energy neighbors
        for (Direction dir : Direction.values()) {
            WireMode mode = modes.get(dir);
            TileEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if (neighbor != null) {
                neighbor.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).ifPresent(other -> {
                    if (mode == WireMode.TAKE || (mode == WireMode.AUTO && other.getEnergyStored() > storage.getEnergyStored())) {
                        int space = storage.getMaxEnergyStored() - storage.getEnergyStored();
                        if (space > 0) {
                            int received = other.extractEnergy(Math.min(TRANSFER_RATE, space), false);
                            if (received > 0) {
                                storage.receiveEnergy(received, false);
                                receivedTotal += received;
                            }
                        }
                    }
                });
            }
        }

        // then push to GIVE or lower-energy neighbors
        for (Direction dir : Direction.values()) {
            WireMode mode = modes.get(dir);
            TileEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if (neighbor != null) {
                neighbor.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).ifPresent(other -> {
                    if (mode == WireMode.GIVE || (mode == WireMode.AUTO && other.getEnergyStored() < storage.getEnergyStored())) {
                        int available = storage.getEnergyStored();
                        if (available > 0) {
                            int sent = other.receiveEnergy(Math.min(TRANSFER_RATE, available), false);
                            if (sent > 0) {
                                storage.extractEnergy(sent, false);
                            }
                        }
                    }
                });
            }
        }
        if (receivedTotal > 0) {
            lastTransfer = receivedTotal;
            setChanged();
            sync();
        } else {
            lastTransfer = 0;
        }
    }

    public void toggleMode(Direction side) {
        setMode(side, modes.get(side).next());
    }

    public void setMode(Direction side, WireMode mode) {
        modes.put(side, mode);
        setChanged();
        sync();
    }

    public WireMode getMode(Direction side) {
        return modes.get(side);
    }

    public int getDisplayEnergy() {
        return Math.max(storage.getEnergyStored(), lastTransfer);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        int energyStored = nbt.getInt("Energy");
        storage.receiveEnergy(energyStored - storage.getEnergyStored(), false);
        lastTransfer = nbt.getInt("LastTransfer");
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
        nbt.putInt("LastTransfer", lastTransfer);
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

    // IEnergyStorage implementation
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int r = storage.receiveEnergy(maxReceive, simulate);
        if (!simulate && r > 0) {
            setChanged();
            sync();
        }
        return r;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int e = storage.extractEnergy(maxExtract, simulate);
        if (!simulate && e > 0) {
            setChanged();
            sync();
        }
        return e;
    }

    @Override public int getEnergyStored() { return storage.getEnergyStored(); }
    @Override public int getMaxEnergyStored() { return storage.getMaxEnergyStored(); }
    @Override public boolean canExtract() { return true; }
    @Override public boolean canReceive() { return true; }
}
