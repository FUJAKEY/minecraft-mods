package com.pipimod.energy;

import net.minecraft.tileentity.TileEntityType;

public class WindTurbineTileEntity extends GeneratorTileEntity {
    public WindTurbineTileEntity() {
        super(8000, 30, ModTileEntities.WIND_TURBINE.get());
    }

    @Override
    public int getEfficiency() {
        return 100;
    }
}
