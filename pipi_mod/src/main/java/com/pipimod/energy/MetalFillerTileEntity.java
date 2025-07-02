package com.pipimod.energy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class MetalFillerTileEntity extends LockableTileEntity implements ITickableTileEntity, IEnergyStorage, INamedContainerProvider {
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private final EnergyStorage energy = new EnergyStorage(10000, 10000, 10000);
    private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> this);
    private int carbon;
    private int progress;

    public MetalFillerTileEntity() {
        super(ModTileEntities.METAL_FILLER.get());
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;
        // consume coal
        ItemStack fuel = items.get(0);
        if (carbon <= 490 && !fuel.isEmpty() && fuel.getItem() == Items.COAL) {
            fuel.shrink(1);
            carbon += 10;
            setChanged();
        }

        if (progress > 0) {
            if (energy.getEnergyStored() >= 8) {
                energy.extractEnergy(8, false);
                progress++;
                if (progress >= 100) {
                    ItemStack out = items.get(2);
                    ItemStack input = items.get(1);
                    if (!input.isEmpty() && input.getItem() == Items.IRON_INGOT) {
                        if (out.isEmpty()) {
                            items.set(2, new ItemStack(ModItems.CAST_IRON_INGOT.get()));
                            input.shrink(1);
                            progress = 0;
                            setChanged();
                        } else if (out.getItem() == ModItems.CAST_IRON_INGOT.get() && out.getCount() < out.getMaxStackSize()) {
                            out.grow(1);
                            input.shrink(1);
                            progress = 0;
                            setChanged();
                        } else {
                            progress = 99; // wait until output has space
                        }
                    } else {
                        progress = 0;
                    }
                }
            }
        } else {
            ItemStack input = items.get(1);
            if (!input.isEmpty() && input.getItem() == Items.IRON_INGOT && carbon >= 40 && energy.getEnergyStored() > 0) {
                carbon -= 40;
                progress = 1;
                setChanged();
            }
        }
    }

    public int getProgress() {
        return progress;
    }

    public int getCarbon() {
        return carbon;
    }

    @Override
    public int getEnergyStored() {
        return energy.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return energy.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() { return true; }

    @Override
    public boolean canReceive() { return true; }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int r = energy.receiveEnergy(maxReceive, simulate);
        if (!simulate && r > 0) setChanged();
        return r;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int e = energy.extractEnergy(maxExtract, simulate);
        if (!simulate && e > 0) setChanged();
        return e;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.energymod.metal_filler");
    }

    @Override
    public ITextComponent getDisplayName() {
        return getDefaultName();
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        net.minecraft.util.IIntArray data = new net.minecraft.util.IIntArray() {
            @Override public int get(int idx) { return idx == 0 ? carbon : idx == 1 ? progress : energy.getEnergyStored(); }
            @Override public void set(int idx, int value) {
                if (idx == 0) carbon = value; else if (idx == 1) progress = value; else energy.receiveEnergy(value - energy.getEnergyStored(), false);
            }
            @Override public int getCount() { return 3; }
        };
        return new MetalFillerContainer(id, player, this, data);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.items.clear();
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.carbon = nbt.getInt("Carbon");
        this.progress = nbt.getInt("Progress");
        this.energy.receiveEnergy(nbt.getInt("Energy"), false);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        ItemStackHelper.saveAllItems(nbt, this.items);
        nbt.putInt("Carbon", carbon);
        nbt.putInt("Progress", progress);
        nbt.putInt("Energy", energy.getEnergyStored());
        return nbt;
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

    @Override
    public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, net.minecraft.util.Direction side) {
        if (cap == CapabilityEnergy.ENERGY) return energyCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energyCap.invalidate();
    }

    // Inventory methods
    @Override
    public int getContainerSize() { return items.size(); }
    @Override
    public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override
    public ItemStack getItem(int index) { return items.get(index); }
    @Override
    public ItemStack removeItem(int index, int count) { ItemStack stack = ItemStackHelper.removeItem(items, index, count); if(!stack.isEmpty()) setChanged(); return stack; }
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = items.get(index);
        if (!stack.isEmpty()) {
            items.set(index, ItemStack.EMPTY);
            return stack;
        }
        return ItemStack.EMPTY;
    }
    @Override
    public void setItem(int index, ItemStack stack) { items.set(index, stack); if(stack.getCount()>getMaxStackSize()) stack.setCount(getMaxStackSize()); setChanged(); }
    @Override
    public boolean stillValid(PlayerEntity player) { return worldPosition.distSqr(player.position(), false) < 64; }
    @Override
    public void clearContent() { items.clear(); }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return createMenu(id, playerInventory);
    }
}
