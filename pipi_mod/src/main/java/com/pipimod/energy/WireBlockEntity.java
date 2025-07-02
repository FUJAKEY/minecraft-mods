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
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayDeque;
import java.util.Queue;

public class WireBlockEntity extends TileEntity implements ITickableTileEntity, IEnergyStorage {
    private static final int CAPACITY = 1280;
    private static final int TRANSFER_RATE = 1280;
    private final EnergyStorage storage = new EnergyStorage(CAPACITY, CAPACITY, CAPACITY);
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> this);
    private final EnumMap<Direction, WireMode> modes = new EnumMap<>(Direction.class);
    private long lastNetworkTick = -1;

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

    private void collectNetwork(Set<WireBlockEntity> out) {
        Queue<WireBlockEntity> queue = new ArrayDeque<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            WireBlockEntity w = queue.poll();
            if (!out.add(w)) continue;
            for (Direction d : Direction.values()) {
                TileEntity te = w.level.getBlockEntity(w.worldPosition.relative(d));
                if (te instanceof WireBlockEntity) {
                    queue.add((WireBlockEntity) te);
                }
            }
        }
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;
        long time = level.getGameTime();
        if (lastNetworkTick == time) return;

        Set<WireBlockEntity> network = new HashSet<>();
        collectNetwork(network);
        for (WireBlockEntity w : network) {
            w.lastNetworkTick = time;
        }

        int capacity = CAPACITY * network.size();
        int totalEnergy = 0;
        for (WireBlockEntity w : network) {
            totalEnergy += w.storage.getEnergyStored();
        }

        // Pull from TAKE sides or AUTO connected to external blocks
        for (WireBlockEntity w : network) {
            for (Direction dir : Direction.values()) {
                WireMode mode = w.modes.get(dir);
                if (mode == WireMode.DISABLED) continue;
                boolean treatAsTake = mode == WireMode.TAKE;
                TileEntity te = w.level.getBlockEntity(w.worldPosition.relative(dir));
                if (te != null) {
                    boolean external = !(te instanceof WireBlockEntity);
                    IEnergyStorage cap = te.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).orElse(null);
                    if (cap != null) {
                        if (mode == WireMode.AUTO && external) {
                            treatAsTake = cap.getEnergyStored() > w.storage.getEnergyStored();
                        }
                        if (treatAsTake) {
                            int space = capacity - totalEnergy;
                            if (space > 0) {
                                int pulled = cap.extractEnergy(Math.min(space, TRANSFER_RATE), false);
                                if (pulled > 0) {
                                    totalEnergy += pulled;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Gather all output targets
        java.util.List<IEnergyStorage> outputs = new java.util.ArrayList<>();
        for (WireBlockEntity w : network) {
            for (Direction dir : Direction.values()) {
                WireMode mode = w.modes.get(dir);
                if (mode == WireMode.DISABLED) continue;
                if (mode == WireMode.TAKE) continue;
                TileEntity te = w.level.getBlockEntity(w.worldPosition.relative(dir));
                if (te != null && !(te instanceof WireBlockEntity)) {
                    IEnergyStorage cap = te.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).orElse(null);
                    if (cap != null) {
                        if (mode == WireMode.AUTO) {
                            if (w.storage.getEnergyStored() > cap.getEnergyStored()) {
                                outputs.add(cap);
                            }
                        } else {
                            outputs.add(cap);
                        }
                    }
                }
            }
        }

        // Distribute energy fairly between outputs
        for (int i = 0; i < outputs.size() && totalEnergy > 0; i++) {
            IEnergyStorage cap = outputs.get(i);
            int share = Math.min(TRANSFER_RATE, totalEnergy / (outputs.size() - i));
            int sent = cap.receiveEnergy(share, false);
            totalEnergy -= sent;
        }

        // Evenly distribute remaining energy to wires
        int per = totalEnergy / network.size();
        int rem = totalEnergy % network.size();
        for (WireBlockEntity w : network) {
            int target = per + (rem > 0 ? 1 : 0);
            if (rem > 0) rem--;
            int current = w.storage.getEnergyStored();
            if (current < target) {
                w.storage.receiveEnergy(target - current, false);
            } else if (current > target) {
                w.storage.extractEnergy(current - target, false);
            }
            if (w.storage.getEnergyStored() != current) {
                w.setChanged();
                w.sync();
            }
        }
    }

    public void toggleMode(Direction side) {
        setMode(side, modes.get(side).next());
    }

    public void setMode(Direction side, WireMode mode) {
        modes.put(side, mode);
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            if (state.getBlock() instanceof EnergyWireBlock) {
                state = state.setValue(EnergyWireBlock.getProperty(side), mode != WireMode.DISABLED);
                level.setBlock(worldPosition, state, 3);
            }
        }
        setChanged();
        sync();
    }

    public WireMode getMode(Direction side) {
        return modes.get(side);
    }

    public int getDisplayEnergy() {
        return storage.getEnergyStored();
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
