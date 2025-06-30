package com.pipimod.energy;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class ThermalGeneratorTileEntity extends GeneratorTileEntity {
    public ThermalGeneratorTileEntity() {
        super(10000, 40, ModTileEntities.THERMAL_GENERATOR.get());
    }

    @Override
    public int getEfficiency() {
        if (level == null) return 0;
        net.minecraft.world.biome.Biome biome = level.getBiome(worldPosition);
        float temp = biome.getBaseTemperature();
        if (temp < 0.15f) return 0;
        int eff = biome.getBiomeCategory() == net.minecraft.world.biome.Biome.Category.DESERT ? 60 : 50;
        for (BlockPos p : BlockPos.betweenClosed(worldPosition.offset(-1, -1, -1), worldPosition.offset(1, 1, 1))) {
            if (level.getBlockState(p).getBlock() == net.minecraft.block.Blocks.LAVA) {
                return 100;
            }
        }
        return eff;
    }
}
