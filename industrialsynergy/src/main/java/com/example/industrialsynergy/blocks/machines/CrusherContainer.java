package com.example.industrialsynergy.blocks.machines;

import com.example.industrialsynergy.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Контейнер для дробителя: вход, выход, апгрейд + инвентарь игрока.
 */
public class CrusherContainer extends Container {
    private final CrusherTileEntity tile;
    private final IIntArray data;

    public CrusherContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(Registration.CRUSHER_CONTAINER.get(), windowId);
        this.tile = (CrusherTileEntity) world.getTileEntity(pos);
        this.data = tile.getIntArray();
        this.trackIntArray(data);

        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElseThrow(IllegalStateException::new);
        // Слот входа
        addSlot(new SlotItemHandler(handler, 0, 56, 35));
        // Слот выхода (только извлечение)
        addSlot(new SlotItemHandler(handler, 1, 116, 35) {
            @Override
            public boolean isItemValid(net.minecraft.item.ItemStack stack) { return false; }
        });
        // Слот апгрейда
        addSlot(new SlotItemHandler(handler, 2, 152, 8));

        // Инвентарь игрока 3x9 + хотбар
        int startX = 8;
        int startY = 84;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }
        for (int hotbar = 0; hotbar < 9; ++hotbar) {
            addSlot(new Slot(playerInventory, hotbar, startX + hotbar * 18, startY + 58));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public int getProgress() { return data.get(0); }
    public int getMaxProgress() { return data.get(1); }
    public int getEnergy() { return data.get(2); }
    public int getMaxEnergy() { return data.get(3); }
}
