package com.example.industrialsynergy.blocks.machines;

import com.example.industrialsynergy.blocks.ModBlocks;
import com.example.industrialsynergy.energy.ModEnergyStorage;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SmeltingRecipe;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IndustrialSmelterTileEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler items = new ItemStackHandler(2);
    private final LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> items);
    private final ModEnergyStorage energy = new ModEnergyStorage(60000, 400, 400) {
        @Override
        protected void onEnergyChanged() { markDirty(); }
    };
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energy);
    private int cookTime = 0;
    private int cookTimeTotal = 100;

    public IndustrialSmelterTileEntity() {
        super(ModBlocks.INDUSTRIAL_SMELTER_TILE.get());
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        if (energy.extractEnergy(30, true) < 30) return;
        world.getRecipeManager().getRecipe(IRecipeType.SMELTING, items, world).ifPresent(recipe -> {
            cookTime++;
            energy.extractEnergy(30, false);
            if (cookTime >= cookTimeTotal) {
                items.extractItem(0, 1, false);
                items.insertItem(1, recipe.getRecipeOutput().copy(), false);
                cookTime = 0;
            }
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return itemHandler.cast();
        if (cap == CapabilityEnergy.ENERGY) return energyHandler.cast();
        return super.getCapability(cap, side);
    }
}
