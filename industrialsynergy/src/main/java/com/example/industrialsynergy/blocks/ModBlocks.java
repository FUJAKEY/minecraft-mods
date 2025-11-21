package com.example.industrialsynergy.blocks;

import com.example.industrialsynergy.IndustrialSynergy;
import com.example.industrialsynergy.blocks.cell.EnergyCellBlock;
import com.example.industrialsynergy.blocks.cell.EnergyCellTileEntities;
import com.example.industrialsynergy.blocks.generators.CoalGeneratorBlock;
import com.example.industrialsynergy.blocks.generators.CoalGeneratorTileEntity;
import com.example.industrialsynergy.blocks.generators.SolarPanelBlock;
import com.example.industrialsynergy.blocks.generators.SolarPanelTileEntity;
import com.example.industrialsynergy.blocks.generators.UraniumReactorBlock;
import com.example.industrialsynergy.blocks.generators.UraniumReactorTileEntity;
import com.example.industrialsynergy.blocks.machines.*;
import com.example.industrialsynergy.energy.CableBlock;
import com.example.industrialsynergy.energy.CableTileEntities;
import com.example.industrialsynergy.energy.CableTileEntity;
import com.example.industrialsynergy.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * Регистрация всех блоков и тайл-сущностей.
 */
public class ModBlocks {
    // Кабели
    public static final int BASIC_RATE = 1000;
    public static final int ADVANCED_RATE = 10000;
    public static final int ELITE_RATE = 100000;
    public static final int ULTIMATE_RATE = 1000000;

    public static RegistryObject<Block> BASIC_ENERGY_CABLE;
    public static RegistryObject<Block> ADVANCED_ENERGY_CABLE;
    public static RegistryObject<Block> ELITE_ENERGY_CABLE;
    public static RegistryObject<Block> ULTIMATE_ENERGY_CABLE;

    public static RegistryObject<TileEntityType<CableTileEntities.BasicCableTileEntity>> BASIC_CABLE_TILE;
    public static RegistryObject<TileEntityType<CableTileEntities.AdvancedCableTileEntity>> ADVANCED_CABLE_TILE;
    public static RegistryObject<TileEntityType<CableTileEntities.EliteCableTileEntity>> ELITE_CABLE_TILE;
    public static RegistryObject<TileEntityType<CableTileEntities.UltimateCableTileEntity>> ULTIMATE_CABLE_TILE;

    // Руды
    public static RegistryObject<Block> BAUXITE_ORE;
    public static RegistryObject<Block> TITANIUM_ORE;
    public static RegistryObject<Block> URANIUM_ORE;

    // Машины
    public static RegistryObject<Block> CRUSHER;
    public static RegistryObject<Block> ORE_WASHER;
    public static RegistryObject<Block> INDUSTRIAL_SMELTER;
    public static RegistryObject<Block> ALLOY_FORGE;

    public static RegistryObject<TileEntityType<CrusherTileEntity>> CRUSHER_TILE;
    public static RegistryObject<TileEntityType<OreWasherTileEntity>> ORE_WASHER_TILE;
    public static RegistryObject<TileEntityType<IndustrialSmelterTileEntity>> INDUSTRIAL_SMELTER_TILE;
    public static RegistryObject<TileEntityType<AlloyForgeTileEntity>> ALLOY_FORGE_TILE;

    // Генераторы
    public static RegistryObject<Block> COAL_GENERATOR;
    public static RegistryObject<Block> SOLAR_PANEL;
    public static RegistryObject<Block> URANIUM_REACTOR;
    public static RegistryObject<TileEntityType<CoalGeneratorTileEntity>> COAL_GENERATOR_TILE;
    public static RegistryObject<TileEntityType<SolarPanelTileEntity>> SOLAR_PANEL_TILE;
    public static RegistryObject<TileEntityType<UraniumReactorTileEntity>> URANIUM_REACTOR_TILE;

    // Энергохранилища
    public static RegistryObject<Block> BASIC_ENERGY_CELL;
    public static RegistryObject<Block> ADVANCED_ENERGY_CELL;
    public static RegistryObject<Block> ELITE_ENERGY_CELL;
    public static RegistryObject<Block> ULTIMATE_ENERGY_CELL;
    public static RegistryObject<TileEntityType<EnergyCellTileEntities.BasicEnergyCellTileEntity>> BASIC_ENERGY_CELL_TILE;
    public static RegistryObject<TileEntityType<EnergyCellTileEntities.AdvancedEnergyCellTileEntity>> ADVANCED_ENERGY_CELL_TILE;
    public static RegistryObject<TileEntityType<EnergyCellTileEntities.EliteEnergyCellTileEntity>> ELITE_ENERGY_CELL_TILE;
    public static RegistryObject<TileEntityType<EnergyCellTileEntities.UltimateEnergyCellTileEntity>> ULTIMATE_ENERGY_CELL_TILE;

    public static void register() {
        // Кабели с разными скоростями
        BASIC_ENERGY_CABLE = registerBlock("basic_energy_cable", () -> new CableBlock(BASIC_RATE, CableTileEntities.BasicCableTileEntity::new));
        ADVANCED_ENERGY_CABLE = registerBlock("advanced_energy_cable", () -> new CableBlock(ADVANCED_RATE, CableTileEntities.AdvancedCableTileEntity::new));
        ELITE_ENERGY_CABLE = registerBlock("elite_energy_cable", () -> new CableBlock(ELITE_RATE, CableTileEntities.EliteCableTileEntity::new));
        ULTIMATE_ENERGY_CABLE = registerBlock("ultimate_energy_cable", () -> new CableBlock(ULTIMATE_RATE, CableTileEntities.UltimateCableTileEntity::new));

        BASIC_CABLE_TILE = Registration.TILE_ENTITIES.register("basic_energy_cable", () -> TileEntityType.Builder.create(CableTileEntities.BasicCableTileEntity::new, BASIC_ENERGY_CABLE.get()).build(null));
        ADVANCED_CABLE_TILE = Registration.TILE_ENTITIES.register("advanced_energy_cable", () -> TileEntityType.Builder.create(CableTileEntities.AdvancedCableTileEntity::new, ADVANCED_ENERGY_CABLE.get()).build(null));
        ELITE_CABLE_TILE = Registration.TILE_ENTITIES.register("elite_energy_cable", () -> TileEntityType.Builder.create(CableTileEntities.EliteCableTileEntity::new, ELITE_ENERGY_CABLE.get()).build(null));
        ULTIMATE_CABLE_TILE = Registration.TILE_ENTITIES.register("ultimate_energy_cable", () -> TileEntityType.Builder.create(CableTileEntities.UltimateCableTileEntity::new, ULTIMATE_ENERGY_CABLE.get()).build(null));

        // Руды
        BAUXITE_ORE = registerBlock("bauxite_ore", () -> new Block(Block.Properties.from(Blocks.IRON_ORE)));
        TITANIUM_ORE = registerBlock("titanium_ore", () -> new Block(Block.Properties.from(Blocks.IRON_ORE)));
        URANIUM_ORE = registerBlock("uranium_ore", () -> new Block(Block.Properties.from(Blocks.IRON_ORE)));

        // Машины
        CRUSHER = registerBlock("crusher", CrusherBlock::new);
        ORE_WASHER = registerBlock("ore_washer", OreWasherBlock::new);
        INDUSTRIAL_SMELTER = registerBlock("industrial_smelter", IndustrialSmelterBlock::new);
        ALLOY_FORGE = registerBlock("alloy_forge", AlloyForgeBlock::new);

        CRUSHER_TILE = Registration.TILE_ENTITIES.register("crusher", () -> TileEntityType.Builder.create(CrusherTileEntity::new, CRUSHER.get()).build(null));
        ORE_WASHER_TILE = Registration.TILE_ENTITIES.register("ore_washer", () -> TileEntityType.Builder.create(OreWasherTileEntity::new, ORE_WASHER.get()).build(null));
        INDUSTRIAL_SMELTER_TILE = Registration.TILE_ENTITIES.register("industrial_smelter", () -> TileEntityType.Builder.create(IndustrialSmelterTileEntity::new, INDUSTRIAL_SMELTER.get()).build(null));
        ALLOY_FORGE_TILE = Registration.TILE_ENTITIES.register("alloy_forge", () -> TileEntityType.Builder.create(AlloyForgeTileEntity::new, ALLOY_FORGE.get()).build(null));

        // Генераторы
        COAL_GENERATOR = registerBlock("coal_generator", CoalGeneratorBlock::new);
        SOLAR_PANEL = registerBlock("solar_panel", SolarPanelBlock::new);
        URANIUM_REACTOR = registerBlock("uranium_reactor", UraniumReactorBlock::new);
        COAL_GENERATOR_TILE = Registration.TILE_ENTITIES.register("coal_generator", () -> TileEntityType.Builder.create(CoalGeneratorTileEntity::new, COAL_GENERATOR.get()).build(null));
        SOLAR_PANEL_TILE = Registration.TILE_ENTITIES.register("solar_panel", () -> TileEntityType.Builder.create(SolarPanelTileEntity::new, SOLAR_PANEL.get()).build(null));
        URANIUM_REACTOR_TILE = Registration.TILE_ENTITIES.register("uranium_reactor", () -> TileEntityType.Builder.create(UraniumReactorTileEntity::new, URANIUM_REACTOR.get()).build(null));

        // Энергохранилища
        BASIC_ENERGY_CELL_TILE = Registration.TILE_ENTITIES.register("basic_energy_cell", () -> TileEntityType.Builder.create(EnergyCellTileEntities.BasicEnergyCellTileEntity::new, BASIC_ENERGY_CELL.get()).build(null));
        ADVANCED_ENERGY_CELL_TILE = Registration.TILE_ENTITIES.register("advanced_energy_cell", () -> TileEntityType.Builder.create(EnergyCellTileEntities.AdvancedEnergyCellTileEntity::new, ADVANCED_ENERGY_CELL.get()).build(null));
        ELITE_ENERGY_CELL_TILE = Registration.TILE_ENTITIES.register("elite_energy_cell", () -> TileEntityType.Builder.create(EnergyCellTileEntities.EliteEnergyCellTileEntity::new, ELITE_ENERGY_CELL.get()).build(null));
        ULTIMATE_ENERGY_CELL_TILE = Registration.TILE_ENTITIES.register("ultimate_energy_cell", () -> TileEntityType.Builder.create(EnergyCellTileEntities.UltimateEnergyCellTileEntity::new, ULTIMATE_ENERGY_CELL.get()).build(null));
        BASIC_ENERGY_CELL = registerBlock("basic_energy_cell", () -> new EnergyCellBlock(200000, EnergyCellTileEntities.BasicEnergyCellTileEntity::new));
        ADVANCED_ENERGY_CELL = registerBlock("advanced_energy_cell", () -> new EnergyCellBlock(1000000, EnergyCellTileEntities.AdvancedEnergyCellTileEntity::new));
        ELITE_ENERGY_CELL = registerBlock("elite_energy_cell", () -> new EnergyCellBlock(5000000, EnergyCellTileEntities.EliteEnergyCellTileEntity::new));
        ULTIMATE_ENERGY_CELL = registerBlock("ultimate_energy_cell", () -> new EnergyCellBlock(20000000, EnergyCellTileEntities.UltimateEnergyCellTileEntity::new));
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> ret = Registration.BLOCKS.register(name, block);
        Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(net.minecraft.item.ItemGroup.REDSTONE)));
        return ret;
    }

    // Утилиты для машин (карты рецептов-плейсхолдеров)
    public static Item getDustForOre(Item ore) {
        ResourceLocation id = ore.getRegistryName();
        if (id != null && id.getPath().contains("bauxite")) return ForgeRegistries.ITEMS.getValue(new ResourceLocation(IndustrialSynergy.MODID, "bauxite_dust"));
        if (id != null && id.getPath().contains("titanium")) return ForgeRegistries.ITEMS.getValue(new ResourceLocation(IndustrialSynergy.MODID, "titanium_dust"));
        if (id != null && id.getPath().contains("uranium")) return ForgeRegistries.ITEMS.getValue(new ResourceLocation(IndustrialSynergy.MODID, "uranium_dust"));
        return ore; // fallback
    }

    public static Item getWashedDust(Item oreDust) {
        ResourceLocation id = oreDust.getRegistryName();
        if (id != null && id.getPath().contains("bauxite")) return ForgeRegistries.ITEMS.getValue(new ResourceLocation(IndustrialSynergy.MODID, "washed_bauxite_dust"));
        if (id != null && id.getPath().contains("titanium")) return ForgeRegistries.ITEMS.getValue(new ResourceLocation(IndustrialSynergy.MODID, "washed_titanium_dust"));
        if (id != null && id.getPath().contains("uranium")) return ForgeRegistries.ITEMS.getValue(new ResourceLocation(IndustrialSynergy.MODID, "washed_uranium_dust"));
        return oreDust;
    }

    public static Item getAlloyForInputs(Item a, Item b) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(IndustrialSynergy.MODID, "steel_alloy"));
    }

    public static double getEnergyUseMultiplier() {
        return com.example.industrialsynergy.setup.ModConfigs.COMMON.baseEnergyUse.get();
    }
}
