package com.pipimod.energy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;

public class MetalFillerContainer extends Container {
    private final IInventory inv;
    private final IIntArray data;

    public MetalFillerContainer(int id, PlayerInventory player) {
        this(id, player, new MetalFillerTileEntity(), new net.minecraft.util.IntArray(3));
    }

    public MetalFillerContainer(int id, PlayerInventory player, net.minecraft.network.PacketBuffer buf) {
        this(id, player, (IInventory)player.player.level.getBlockEntity(buf.readBlockPos()), new net.minecraft.util.IntArray(3));
    }

    public MetalFillerContainer(int id, PlayerInventory player, IInventory inv, IIntArray data) {
        super(ModContainers.METAL_FILLER.get(), id);
        this.inv = inv;
        this.data = data;
        inv.startOpen(player.player);

        // fuel slot (coal) on the left
        this.addSlot(new Slot(inv, 0, 26, 35));
        // iron input in the center
        this.addSlot(new Slot(inv, 1, 80, 35));
        // output on the right
        this.addSlot(new Slot(inv, 2, 134, 35) {
            @Override
            public boolean mayPlace(net.minecraft.item.ItemStack stack) { return false; }
        });

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(player, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(player, col, 8 + col * 18, 142));
        }

        this.addDataSlots(data);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return inv.stillValid(player);
    }

    public int getCarbon() { return data.get(0); }
    public int getProgress() { return data.get(1); }
    public int getEnergy() { return data.get(2); }
}
