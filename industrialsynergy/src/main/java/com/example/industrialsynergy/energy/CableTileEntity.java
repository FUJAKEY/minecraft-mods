package com.example.industrialsynergy.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Базовый тайл кабеля. Конкретные реализации (basic/advanced/elite/ultimate) задают пропускную способность через конструктор.
 */
public class CableTileEntity extends TileEntity implements ITickableTileEntity {
    private final int transferRate;
    private final ModEnergyStorage storage;
    private final LazyOptional<IEnergyStorage> energyCapability;

    public CableTileEntity(TileEntityType<?> type, int transferRate) {
        super(type);
        this.transferRate = transferRate;
        this.storage = new ModEnergyStorage(transferRate * 4, transferRate, transferRate) {
            @Override
            protected void onEnergyChanged() {
                markDirty();
            }
        };
        this.energyCapability = LazyOptional.of(() -> storage);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        storage.receiveEnergy(nbt.getInt("Energy") - storage.getEnergyStored(), false);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putInt("Energy", storage.getEnergyStored());
        return nbt;
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        for (Direction dir : Direction.values()) {
            TileEntity neighbor = world.getTileEntity(pos.offset(dir));
            if (neighbor == null) continue;
            neighbor.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).ifPresent(target -> {
                int canSend = Math.min(storage.getEnergyStored(), transferRate);
                int accepted = target.receiveEnergy(canSend, false);
                if (accepted > 0) {
                    storage.extractEnergy(accepted, false);
                }
            });
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return energyCapability.cast();
        }
        return super.getCapability(cap, side);
    }
}
