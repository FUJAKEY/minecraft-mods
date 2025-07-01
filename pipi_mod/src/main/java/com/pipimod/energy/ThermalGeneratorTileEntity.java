package com.pipimod.energy;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
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

        // Check if biome is in the cold blacklist
        if (isColdBiome(biome)) {
            return 0;
        }

        boolean nearWaterOrSnow = false;
        boolean nearHotBlock = false;

        for (BlockPos p : BlockPos.betweenClosed(worldPosition.offset(-1, -1, -1), worldPosition.offset(1, 1, 1))) {
            BlockState state = level.getBlockState(p);
            if (state.getBlock() == Blocks.WATER || state.getFluidState().getType() == Fluids.WATER ||
                state.getBlock() == Blocks.SNOW_BLOCK || state.getBlock() == Blocks.SNOW) {
                nearWaterOrSnow = true;
            }
            if (state.getBlock() == Blocks.MAGMA_BLOCK || state.getBlock() == Blocks.FIRE ||
                state.getBlock() == Blocks.LAVA || state.getFluidState().getType() == Fluids.LAVA) {
                nearHotBlock = true;
            }
        }

        if (nearWaterOrSnow) return 0;
        if (nearHotBlock) return 80;

        float temp = biome.getBaseTemperature();
        if (temp < 0.15f) return 0;
        return biome.getBiomeCategory() == Biome.Category.DESERT ? 60 : 50;
    }

    private boolean isColdBiome(Biome biome) {
        return COLD_BIOMES.contains(biome);
    }

    private static final Set<Biome> COLD_BIOMES = new HashSet<>();
    static {
        COLD_BIOMES.add(Biomes.ICE_SPIKES);
        COLD_BIOMES.add(Biomes.SNOWY_TAIGA);
        COLD_BIOMES.add(Biomes.SNOWY_TAIGA_HILLS);
        COLD_BIOMES.add(Biomes.SNOWY_TAIGA_MOUNTAINS);
        COLD_BIOMES.add(Biomes.SNOWY_MOUNTAINS);
        COLD_BIOMES.add(Biomes.SNOWY_TUNDRA);
        COLD_BIOMES.add(Biomes.SNOWY_BEACH);
        COLD_BIOMES.add(Biomes.FROZEN_RIVER);
        COLD_BIOMES.add(Biomes.FROZEN_OCEAN);
        COLD_BIOMES.add(Biomes.DEEP_FROZEN_OCEAN);
        COLD_BIOMES.add(Biomes.COLD_OCEAN);
        COLD_BIOMES.add(Biomes.DEEP_COLD_OCEAN);
        COLD_BIOMES.add(Biomes.MOUNTAINS);
        COLD_BIOMES.add(Biomes.WOODED_MOUNTAINS);
        COLD_BIOMES.add(Biomes.GRAVELLY_MOUNTAINS);
        COLD_BIOMES.add(Biomes.MODIFIED_GRAVELLY_MOUNTAINS);
        COLD_BIOMES.add(Biomes.STONE_SHORE);
        COLD_BIOMES.add(Biomes.TAIGA);
        COLD_BIOMES.add(Biomes.TAIGA_HILLS);
        COLD_BIOMES.add(Biomes.TAIGA_MOUNTAINS);
        COLD_BIOMES.add(Biomes.GIANT_SPRUCE_TAIGA);
        COLD_BIOMES.add(Biomes.GIANT_SPRUCE_TAIGA_HILLS);
        COLD_BIOMES.add(Biomes.GIANT_TREE_TAIGA);
        COLD_BIOMES.add(Biomes.GIANT_TREE_TAIGA_HILLS);
    }
}
