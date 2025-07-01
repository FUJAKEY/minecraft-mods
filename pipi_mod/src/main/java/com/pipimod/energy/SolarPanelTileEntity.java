package com.pipimod.energy;

import net.minecraft.tileentity.TileEntityType;

public class SolarPanelTileEntity extends GeneratorTileEntity {
    public SolarPanelTileEntity() {
        super(5000, 20, ModTileEntities.SOLAR_PANEL.get());
    }

    @Override
    protected boolean canGenerate() {
        return level != null && level.canSeeSky(worldPosition.above()) && level.isDay()
                && !level.isRainingAt(worldPosition.above());
    }

    @Override
    public int getEfficiency() {
        return canGenerate() ? 100 : 0;
    }
}
