package com.example.industrialsynergy.blocks.cell;

import com.example.industrialsynergy.energy.ModEnergyStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AbstractEnergyCellTileEntity extends TileEntity implements ITickableTileEntity {
    private final ModEnergyStorage storage;
    private final LazyOptional<IEnergyStorage> energyOpt;

    public AbstractEnergyCellTileEntity(TileEntityType<?> type, int capacity) {
        super(type);
        this.storage = new ModEnergyStorage(capacity, capacity / 4, capacity / 4) {
            @Override
            protected void onEnergyChanged() { markDirty(); }
        };
        this.energyOpt = LazyOptional.of(() -> storage);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        for (Direction dir : Direction.values()) {
            TileEntity neighbor = world.getTileEntity(pos.offset(dir));
            if (neighbor == null) continue;
            neighbor.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).ifPresent(target -> {
                int extracted = storage.extractEnergy(storage.getMaxExtract(), true);
                int accepted = target.receiveEnergy(extracted, true);
                if (accepted > 0) {
                    storage.extractEnergy(accepted, false);
                    target.receiveEnergy(accepted, false);
                }
            });
        }
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) return energyOpt.cast();
        return super.getCapability(cap, side);
    }
}
