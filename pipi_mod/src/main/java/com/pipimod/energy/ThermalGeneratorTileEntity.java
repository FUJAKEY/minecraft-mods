package com.pipimod.energy;

import net.minecraft.tileentity.TileEntityType;

public class ThermalGeneratorTileEntity extends GeneratorTileEntity {
    public ThermalGeneratorTileEntity() {
        super(10000, 40, ModTileEntities.THERMAL_GENERATOR.get());
    }
}
