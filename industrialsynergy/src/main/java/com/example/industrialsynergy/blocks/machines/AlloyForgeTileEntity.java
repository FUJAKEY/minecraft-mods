package com.example.industrialsynergy.blocks.machines;

import com.example.industrialsynergy.blocks.ModBlocks;
import com.example.industrialsynergy.energy.ModEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AlloyForgeTileEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler items = new ItemStackHandler(4); // два входа, катализатор, выход
    private final LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> items);
    private final ModEnergyStorage energy = new ModEnergyStorage(80000, 600, 600) {
        @Override
        protected void onEnergyChanged() { markDirty(); }
    };
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energy);
    private int progress = 0;

    public AlloyForgeTileEntity() { super(ModBlocks.ALLOY_FORGE_TILE.get()); }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        ItemStack a = items.getStackInSlot(0);
        ItemStack b = items.getStackInSlot(1);
        if (a.isEmpty() || b.isEmpty()) { progress = 0; return; }
        if (energy.extractEnergy(80, true) < 80) return;
        progress++;
        energy.extractEnergy(80, false);
        if (progress >= 180) {
            items.extractItem(0, 1, false);
            items.extractItem(1, 1, false);
            items.insertItem(3, new ItemStack(ModBlocks.getAlloyForInputs(a.getItem(), b.getItem())), false);
            progress = 0;
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        items.deserializeNBT(nbt.getCompound("Items"));
        energy.receiveEnergy(nbt.getInt("Energy") - energy.getEnergyStored(), false);
        progress = nbt.getInt("Progress");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("Items", items.serializeNBT());
        nbt.putInt("Energy", energy.getEnergyStored());
        nbt.putInt("Progress", progress);
        return nbt;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable net.minecraft.util.Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return itemHandler.cast();
        if (cap == CapabilityEnergy.ENERGY) return energyHandler.cast();
        return super.getCapability(cap, side);
    }
}
