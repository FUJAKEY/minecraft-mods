package com.example.industrialsynergy.blocks.generators;

import com.example.industrialsynergy.blocks.ModBlocks;
import com.example.industrialsynergy.energy.ModEnergyStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SolarPanelTileEntity extends TileEntity implements ITickableTileEntity {
    private final ModEnergyStorage storage = new ModEnergyStorage(20000, 0, 200) {
        @Override
        protected void onEnergyChanged() { markDirty(); }
    };
    private final LazyOptional<IEnergyStorage> energyOpt = LazyOptional.of(() -> storage);

    public SolarPanelTileEntity() { super(ModBlocks.SOLAR_PANEL_TILE.get()); }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        if (world.isDaytime() && world.canBlockSeeSky(pos.up())) {
            storage.receiveEnergy(50, false);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) return energyOpt.cast();
        return super.getCapability(cap, side);
    }
}
