package com.pipimod.energy;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.util.ResourceLocation;
import java.util.Set;
import java.util.HashSet;

public class ThermalGeneratorTileEntity extends GeneratorTileEntity {
    public ThermalGeneratorTileEntity() {
        super(10000, 40, ModTileEntities.THERMAL_GENERATOR.get());
    }

    @Override
    protected boolean canGenerate() {
        return level != null && !level.isRainingAt(worldPosition);
    }

    @Override
    public int getEfficiency() {
        if (level == null) return 0;
        Biome biome = level.getBiome(worldPosition);

        boolean nearHotBlock = false;
        boolean nearWaterOrSnow = false;

        for (BlockPos p : BlockPos.betweenClosed(worldPosition.offset(-1, -1, -1), worldPosition.offset(1, 1, 1))) {
            BlockState state = level.getBlockState(p);
            if (state.getBlock() == Blocks.MAGMA_BLOCK || state.getBlock() == Blocks.FIRE ||
                state.getBlock() == Blocks.LAVA || state.getFluidState().getType() == Fluids.LAVA) {
                nearHotBlock = true;
            }
            if (state.getBlock() == Blocks.WATER || state.getFluidState().getType() == Fluids.WATER ||
                state.getBlock() == Blocks.SNOW_BLOCK || state.getBlock() == Blocks.SNOW) {
                nearWaterOrSnow = true;
            }
        }

        if (nearHotBlock) return 80;
        if (nearWaterOrSnow) return 0;

        // Check if biome is in the cold blacklist
        if (isColdBiome(biome)) {
            return 0;
        }

        float temp = biome.getBaseTemperature();
        if (temp < 0.15f) return 0;

        if (biome.getBiomeCategory() == Biome.Category.DESERT) {
            return 60;
        }

        if (!level.isDay()) {
            return 10;
        }

        return 50;
    }

    private boolean isColdBiome(Biome biome) {
        return COLD_BIOME_IDS.contains(biome.getRegistryName());
    }

    private static final Set<ResourceLocation> COLD_BIOME_IDS = new HashSet<>();
    static {
        COLD_BIOME_IDS.add(Biomes.ICE_SPIKES.location());
        COLD_BIOME_IDS.add(Biomes.SNOWY_TAIGA.location());
        COLD_BIOME_IDS.add(Biomes.SNOWY_TAIGA_HILLS.location());
        COLD_BIOME_IDS.add(Biomes.SNOWY_TAIGA_MOUNTAINS.location());
        COLD_BIOME_IDS.add(Biomes.SNOWY_MOUNTAINS.location());
        COLD_BIOME_IDS.add(Biomes.SNOWY_TUNDRA.location());
        COLD_BIOME_IDS.add(Biomes.SNOWY_BEACH.location());
        COLD_BIOME_IDS.add(Biomes.FROZEN_RIVER.location());
        COLD_BIOME_IDS.add(Biomes.FROZEN_OCEAN.location());
        COLD_BIOME_IDS.add(Biomes.DEEP_FROZEN_OCEAN.location());
        COLD_BIOME_IDS.add(Biomes.COLD_OCEAN.location());
        COLD_BIOME_IDS.add(Biomes.DEEP_COLD_OCEAN.location());
        COLD_BIOME_IDS.add(Biomes.MOUNTAINS.location());
        COLD_BIOME_IDS.add(Biomes.WOODED_MOUNTAINS.location());
        COLD_BIOME_IDS.add(Biomes.GRAVELLY_MOUNTAINS.location());
        COLD_BIOME_IDS.add(Biomes.MODIFIED_GRAVELLY_MOUNTAINS.location());
        COLD_BIOME_IDS.add(Biomes.STONE_SHORE.location());
        COLD_BIOME_IDS.add(Biomes.TAIGA.location());
        COLD_BIOME_IDS.add(Biomes.TAIGA_HILLS.location());
        COLD_BIOME_IDS.add(Biomes.TAIGA_MOUNTAINS.location());
        COLD_BIOME_IDS.add(Biomes.GIANT_SPRUCE_TAIGA.location());
        COLD_BIOME_IDS.add(Biomes.GIANT_SPRUCE_TAIGA_HILLS.location());
        COLD_BIOME_IDS.add(Biomes.GIANT_TREE_TAIGA.location());
        COLD_BIOME_IDS.add(Biomes.GIANT_TREE_TAIGA_HILLS.location());
    }
}
