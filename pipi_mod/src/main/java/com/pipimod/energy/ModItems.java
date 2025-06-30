package com.pipimod.energy;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnergyMod.MODID);

    public static final RegistryObject<Item> ENERGY_CELL_ML_ITEM = ITEMS.register("energy_cell_ml", () -> new EnergyCellItem(ModBlocks.ENERGY_CELL_ML.get(), new Item.Properties().tab(ModItemGroup.GROUP), 100000));
    public static final RegistryObject<Item> ENERGY_CELL_SR_ITEM = ITEMS.register("energy_cell_sr", () -> new EnergyCellItem(ModBlocks.ENERGY_CELL_SR.get(), new Item.Properties().tab(ModItemGroup.GROUP), 1000000));
    public static final RegistryObject<Item> ENERGY_CELL_MX_ITEM = ITEMS.register("energy_cell_mx", () -> new EnergyCellItem(ModBlocks.ENERGY_CELL_MX.get(), new Item.Properties().tab(ModItemGroup.GROUP), 10000000));
    public static final RegistryObject<Item> ENERGY_CELL_INF_ITEM = ITEMS.register("energy_cell_inf", () -> new EnergyCellItem(ModBlocks.ENERGY_CELL_INF.get(), new Item.Properties().tab(ModItemGroup.GROUP), Integer.MAX_VALUE));
    public static final RegistryObject<Item> ENERGY_WIRE_ITEM = ITEMS.register("energy_wire", () -> new WireItem(ModBlocks.ENERGY_WIRE.get(), new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> WIRE_TOOL = ITEMS.register("wire_tool", () -> new WireToolItem(new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> METAL_FILLER_ITEM = ITEMS.register("metal_filler", () -> new BlockItem(ModBlocks.METAL_FILLER.get(), new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> THERMAL_GENERATOR = ITEMS.register("thermal_generator", () -> new BlockItem(ModBlocks.THERMAL_GENERATOR.get(), new Item.Properties().tab(ModItemGroup.GENERATORS)));
    public static final RegistryObject<Item> SOLAR_PANEL = ITEMS.register("solar_panel", () -> new BlockItem(ModBlocks.SOLAR_PANEL.get(), new Item.Properties().tab(ModItemGroup.GENERATORS)));
    public static final RegistryObject<Item> WIND_TURBINE = ITEMS.register("wind_turbine", () -> new BlockItem(ModBlocks.WIND_TURBINE.get(), new Item.Properties().tab(ModItemGroup.GENERATORS)));

    public static final RegistryObject<Item> COPPER_ORE_ITEM = ITEMS.register("copper_ore", () -> new BlockItem(ModBlocks.COPPER_ORE.get(), new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> LEAD_ORE_ITEM = ITEMS.register("lead_ore", () -> new BlockItem(ModBlocks.LEAD_ORE.get(), new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> OSMIUM_ORE_ITEM = ITEMS.register("osmium_ore", () -> new BlockItem(ModBlocks.OSMIUM_ORE.get(), new Item.Properties().tab(ModItemGroup.GROUP)));

    public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item(new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> LEAD_INGOT = ITEMS.register("lead_ingot", () -> new Item(new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> OSMIUM_INGOT = ITEMS.register("osmium_ingot", () -> new Item(new Item.Properties().tab(ModItemGroup.GROUP)));
    public static final RegistryObject<Item> CAST_IRON_INGOT = ITEMS.register("cast_iron_ingot", () -> new Item(new Item.Properties().tab(ModItemGroup.GROUP)));
}
