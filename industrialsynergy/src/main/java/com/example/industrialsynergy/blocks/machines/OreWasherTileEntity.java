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

public class OreWasherTileEntity extends TileEntity implements ITickableTileEntity {
    private final ItemStackHandler items = new ItemStackHandler(3);
    private final LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> items);
    private final ModEnergyStorage energy = new ModEnergyStorage(40000, 300, 300) {
        @Override
        protected void onEnergyChanged() { markDirty(); }
    };
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energy);
    private int water = 0; // простой счетчик воды
    private int progress = 0;

    public OreWasherTileEntity() {
        super(ModBlocks.ORE_WASHER_TILE.get());
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        ItemStack input = items.getStackInSlot(0);
        if (input.isEmpty() || water <= 0) { progress = 0; return; }
        if (energy.extractEnergy(40, true) < 40) return;
        progress++;
        energy.extractEnergy(40, false);
        if (progress >= 160) {
            ItemStack output = new ItemStack(ModBlocks.getWashedDust(input.getItem()), 1 + world.rand.nextInt(2));
            items.insertItem(1, output, false);
            input.shrink(1);
            water = Math.max(0, water - 250);
            progress = 0;
        }
    }

    public void addWater(int amount) { water = Math.min(4000, water + amount); }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        items.deserializeNBT(nbt.getCompound("Items"));
        energy.receiveEnergy(nbt.getInt("Energy") - energy.getEnergyStored(), false);
        water = nbt.getInt("Water");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("Items", items.serializeNBT());
        nbt.putInt("Energy", energy.getEnergyStored());
        nbt.putInt("Water", water);
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
