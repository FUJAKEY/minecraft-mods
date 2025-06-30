package com.pipimod.energy;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = EnergyMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModWorldGen {
    private static ConfiguredFeature<?, ?> COPPER_ORE;
    private static ConfiguredFeature<?, ?> LEAD_ORE;
    private static ConfiguredFeature<?, ?> OSMIUM_ORE;

    public static void setup(final FMLCommonSetupEvent event) {
        COPPER_ORE = Feature.ORE
                .configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        ModBlocks.COPPER_ORE.get().defaultBlockState(), 8))
                .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(1, 0, 30)))
                .squared()
                .count(20);
        LEAD_ORE = Feature.ORE
                .configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        ModBlocks.LEAD_ORE.get().defaultBlockState(), 8))
                .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(1, 0, 30)))
                .squared()
                .count(20);
        OSMIUM_ORE = Feature.ORE
                .configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                        ModBlocks.OSMIUM_ORE.get().defaultBlockState(), 8))
                .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(1, 0, 30)))
                .squared()
                .count(20);
    }

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        if (event.getCategory() != net.minecraft.world.biome.Biome.Category.NETHER &&
            event.getCategory() != net.minecraft.world.biome.Biome.Category.THEEND) {
            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, COPPER_ORE);
            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, LEAD_ORE);
            event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, OSMIUM_ORE);
        }
    }
}
