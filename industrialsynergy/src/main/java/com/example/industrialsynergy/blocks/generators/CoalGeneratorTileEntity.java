package com.example.industrialsynergy.blocks.generators;

import com.example.industrialsynergy.blocks.ModBlocks;
import com.example.industrialsynergy.energy.ModEnergyStorage;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CoalGeneratorTileEntity extends TileEntity implements ITickableTileEntity {
    private final ModEnergyStorage storage = new ModEnergyStorage(50000, 0, 200) {
        @Override
        protected void onEnergyChanged() { markDirty(); }
    };
    private final LazyOptional<IEnergyStorage> energyOpt = LazyOptional.of(() -> storage);
    private int burnTime = 0;

    public CoalGeneratorTileEntity() { super(ModBlocks.COAL_GENERATOR_TILE.get()); }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        if (burnTime > 0) {
            burnTime--;
            storage.receiveEnergy((int)(40 * com.example.industrialsynergy.setup.ModConfigs.COMMON.generatorMultiplier.get()), false);
        }
    }

    public void addFuel(int ticks) { burnTime += ticks; }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) return energyOpt.cast();
        return super.getCapability(cap, side);
    }
}
