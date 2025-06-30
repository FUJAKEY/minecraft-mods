package com.pipimod.energy;

import net.minecraft.tileentity.TileEntityType;

public class WindTurbineTileEntity extends GeneratorTileEntity {
    private float rotation;

    public WindTurbineTileEntity() {
        super(8000, 30, ModTileEntities.WIND_TURBINE.get());
    }

    @Override
    public int getEfficiency() {
        if (level != null && level.isRaining()) {
            return 120;
        }
        return 100;
    }

    public float getRotation() {
        return rotation;
    }

    @Override
    public void tick() {
        super.tick();
        if (level != null && level.isClientSide) {
            rotation = (rotation + 9f) % 360f;
        }
    }
}
