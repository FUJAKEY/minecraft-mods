package com.example.industrialsynergy.blocks.machines;

import com.example.industrialsynergy.blocks.ModBlocks;
import com.example.industrialsynergy.energy.ModEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Простая логика дробления: потребляет энергию и превращает вход в выход.
 */
public class CrusherTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IUpgradableMachine {
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT = 1;
    private static final int SLOT_UPGRADE = 2;

    private final ItemStackHandler items = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final LazyOptional<ItemStackHandler> itemHandler = LazyOptional.of(() -> items);

    private final ModEnergyStorage energy = new ModEnergyStorage(50000, 500, 500) {
        @Override
        protected void onEnergyChanged() {
            markDirty();
        }
    };
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energy);

    private int progress = 0;
    private int maxProgress = 200; // 10 секунд по 20 тиков

    public CrusherTileEntity() {
        super(ModBlocks.CRUSHER_TILE.get());
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        if (energy.getEnergyStored() <= 0) return;
        ItemStack input = items.getStackInSlot(SLOT_INPUT);
        if (input.isEmpty()) {
            progress = 0;
            return;
        }
        // Ускорение от апгрейдов скорости
        int speedBonus = getSpeedUpgrades();
        int requiredEnergy = (int) (20 * (1 + speedBonus) *  ModBlocks.getEnergyUseMultiplier());
        if (energy.extractEnergy(requiredEnergy, true) >= requiredEnergy) {
            progress += 1 + speedBonus;
            energy.extractEnergy(requiredEnergy, false);
            if (progress >= maxProgress) {
                ItemStack output = new ItemStack(ModBlocks.getDustForOre(input.getItem()), 2);
                ItemStack existing = items.getStackInSlot(SLOT_OUTPUT);
                if (existing.isEmpty()) {
                    items.setStackInSlot(SLOT_OUTPUT, output);
                } else if (ItemStack.areItemsEqual(existing, output)) {
                    existing.grow(output.getCount());
                }
                input.shrink(1);
                progress = 0;
            }
        }
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        items.deserializeNBT(tag.getCompound("Items"));
        progress = tag.getInt("Progress");
        energy.receiveEnergy(tag.getInt("Energy") - energy.getEnergyStored(), false);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.put("Items", items.serializeNBT());
        tag.putInt("Progress", progress);
        tag.putInt("Energy", energy.getEnergyStored());
        return tag;
    }

    public IIntArray getIntArray() {
        return new IIntArray() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return progress;
                    case 1: return maxProgress;
                    case 2: return energy.getEnergyStored();
                    case 3: return energy.getMaxEnergyStored();
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) progress = value;
            }

            @Override
            public int size() { return 4; }
        };
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.industrialsynergy.crusher");
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        return new CrusherContainer(windowId, world, pos, inv, player);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return itemHandler.cast();
        if (cap == CapabilityEnergy.ENERGY) return energyHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public int getSpeedUpgrades() {
        return items.getStackInSlot(SLOT_UPGRADE).getCount();
    }

    @Override
    public int getEfficiencyUpgrades() {
        return 0; // Можно расширить для энерго-апгрейдов
    }

    @Override
    public int getCapacityUpgrades() {
        return 0; // Можно увеличить буфер энергии по аналогии
    }
}
