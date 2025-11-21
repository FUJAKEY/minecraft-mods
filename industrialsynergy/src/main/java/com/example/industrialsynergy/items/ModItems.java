package com.example.industrialsynergy.items;

import com.example.industrialsynergy.IndustrialSynergy;
import com.example.industrialsynergy.blocks.ModBlocks;
import com.example.industrialsynergy.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;

/**
 * Регистрация предметов: слитки, пыли, апгрейды.
 */
public class ModItems {
    public static RegistryObject<Item> BAUXITE_DUST;
    public static RegistryObject<Item> TITANIUM_DUST;
    public static RegistryObject<Item> URANIUM_DUST;
    public static RegistryObject<Item> WASHED_BAUXITE_DUST;
    public static RegistryObject<Item> WASHED_TITANIUM_DUST;
    public static RegistryObject<Item> WASHED_URANIUM_DUST;
    public static RegistryObject<Item> ALUMINUM_INGOT;
    public static RegistryObject<Item> TITANIUM_INGOT;
    public static RegistryObject<Item> URANIUM_INGOT;
    public static RegistryObject<Item> STEEL_ALLOY;

    public static RegistryObject<Item> SPEED_UPGRADE;
    public static RegistryObject<Item> EFFICIENCY_UPGRADE;
    public static RegistryObject<Item> CAPACITY_UPGRADE;

    public static void register() {
        BAUXITE_DUST = register("bauxite_dust");
        TITANIUM_DUST = register("titanium_dust");
        URANIUM_DUST = register("uranium_dust");
        WASHED_BAUXITE_DUST = register("washed_bauxite_dust");
        WASHED_TITANIUM_DUST = register("washed_titanium_dust");
        WASHED_URANIUM_DUST = register("washed_uranium_dust");
        ALUMINUM_INGOT = register("aluminum_ingot");
        TITANIUM_INGOT = register("titanium_ingot");
        URANIUM_INGOT = register("uranium_ingot");
        STEEL_ALLOY = register("steel_alloy");

        SPEED_UPGRADE = register("speed_upgrade");
        EFFICIENCY_UPGRADE = register("energy_efficiency_upgrade");
        CAPACITY_UPGRADE = register("capacity_upgrade");
    }

    private static RegistryObject<Item> register(String name) {
        return Registration.ITEMS.register(name, () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    }
}
