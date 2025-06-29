package com.pipimod.energy;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemTooltipFlag;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EnergyCellItem extends BlockItem {
    private final int capacity;

    public EnergyCellItem(Block block, Properties props, int capacity) {
        super(block, props);
        this.capacity = capacity;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ItemTooltipFlag flag) {
        int energy = 0;
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains("Energy")) {
            energy = tag.getInt("Energy");
        }
        String max = capacity == Integer.MAX_VALUE ? "∞" : Integer.toString(capacity);
        tooltip.add(new StringTextComponent(energy + "/" + max + " FE"));
    }
}

