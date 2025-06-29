package com.pipimod.energy;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnergyMod.MODID);

    public static final RegistryObject<Item> ENERGY_CELL_ML_ITEM = ITEMS.register("energy_cell_ml", () -> new BlockItem(ModBlocks.ENERGY_CELL_ML.get(), new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> ENERGY_CELL_SR_ITEM = ITEMS.register("energy_cell_sr", () -> new BlockItem(ModBlocks.ENERGY_CELL_SR.get(), new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> ENERGY_CELL_MX_ITEM = ITEMS.register("energy_cell_mx", () -> new BlockItem(ModBlocks.ENERGY_CELL_MX.get(), new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> ENERGY_CELL_INF_ITEM = ITEMS.register("energy_cell_inf", () -> new BlockItem(ModBlocks.ENERGY_CELL_INF.get(), new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> ENERGY_WIRE_ITEM = ITEMS.register("energy_wire", () -> new BlockItem(ModBlocks.ENERGY_WIRE.get(), new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> WIRE_TOOL = ITEMS.register("wire_tool", () -> new WireToolItem(new Item.Properties().tab(ModItemGroup.GROUP)));
}
