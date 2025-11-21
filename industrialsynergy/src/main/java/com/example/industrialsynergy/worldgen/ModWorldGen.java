package com.example.industrialsynergy.worldgen;

import com.example.industrialsynergy.blocks.ModBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Простой worldgen через BiomeLoadingEvent. Добавляем наши руды в каждый оверворлд биом.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModWorldGen {
    private static final ConfiguredFeature<?, ?> BAUXITE_ORE = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ModBlocks.BAUXITE_ORE.get().getDefaultState(), 6)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 20, 0, 64)));
    private static final ConfiguredFeature<?, ?> TITANIUM_ORE = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ModBlocks.TITANIUM_ORE.get().getDefaultState(), 5)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(6, 10, 0, 40)));
    private static final ConfiguredFeature<?, ?> URANIUM_ORE = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ModBlocks.URANIUM_ORE.get().getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 5, 0, 24)));

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, BAUXITE_ORE);
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, TITANIUM_ORE);
        event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, URANIUM_ORE);
    }
}
