package com.pipimod.energy;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
    public static final ItemGroup GENERATORS = new ItemGroup(EnergyMod.MODID + "_generators") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.THERMAL_GENERATOR.get());
        }
    };
    public static final ItemGroup GROUP = new ItemGroup(EnergyMod.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.ENERGY_CELL_ML.get());
        }
    };
}
