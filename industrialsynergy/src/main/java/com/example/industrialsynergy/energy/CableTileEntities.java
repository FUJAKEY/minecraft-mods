package com.example.industrialsynergy.energy;

import com.example.industrialsynergy.blocks.ModBlocks;
import net.minecraft.tileentity.TileEntityType;

/**
 * Конкретные реализации тайл-сущностей кабеля, задающие пропускную способность.
 */
public class CableTileEntities {
    public static class BasicCableTileEntity extends CableTileEntity {
        public BasicCableTileEntity() {
            super(ModBlocks.BASIC_CABLE_TILE.get(), ModBlocks.BASIC_RATE);
        }
    }

    public static class AdvancedCableTileEntity extends CableTileEntity {
        public AdvancedCableTileEntity() {
            super(ModBlocks.ADVANCED_CABLE_TILE.get(), ModBlocks.ADVANCED_RATE);
        }
    }

    public static class EliteCableTileEntity extends CableTileEntity {
        public EliteCableTileEntity() {
            super(ModBlocks.ELITE_CABLE_TILE.get(), ModBlocks.ELITE_RATE);
        }
    }

    public static class UltimateCableTileEntity extends CableTileEntity {
        public UltimateCableTileEntity() {
            super(ModBlocks.ULTIMATE_CABLE_TILE.get(), ModBlocks.ULTIMATE_RATE);
        }
    }
}
