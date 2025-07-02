package com.pipimod.energy.jei;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import com.pipimod.energy.ModItems;

public class MetalFillerRecipe {
    public ItemStack getInput() {
        return new ItemStack(Items.IRON_INGOT);
    }

    public ItemStack getFuel() {
        return new ItemStack(Items.COAL, 4);
    }

    public ItemStack getOutput() {
        return new ItemStack(ModItems.CAST_IRON_INGOT.get());
    }
}
