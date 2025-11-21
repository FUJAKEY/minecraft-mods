package com.example.industrialsynergy.blocks.cell;

import com.example.industrialsynergy.blocks.ModBlocks;

public class EnergyCellTileEntities {
    public static class BasicEnergyCellTileEntity extends AbstractEnergyCellTileEntity {
        public BasicEnergyCellTileEntity() { super(ModBlocks.BASIC_ENERGY_CELL_TILE.get(), 200000); }
    }
    public static class AdvancedEnergyCellTileEntity extends AbstractEnergyCellTileEntity {
        public AdvancedEnergyCellTileEntity() { super(ModBlocks.ADVANCED_ENERGY_CELL_TILE.get(), 1000000); }
    }
    public static class EliteEnergyCellTileEntity extends AbstractEnergyCellTileEntity {
        public EliteEnergyCellTileEntity() { super(ModBlocks.ELITE_ENERGY_CELL_TILE.get(), 5000000); }
    }
    public static class UltimateEnergyCellTileEntity extends AbstractEnergyCellTileEntity {
        public UltimateEnergyCellTileEntity() { super(ModBlocks.ULTIMATE_ENERGY_CELL_TILE.get(), 20000000); }
    }
}
