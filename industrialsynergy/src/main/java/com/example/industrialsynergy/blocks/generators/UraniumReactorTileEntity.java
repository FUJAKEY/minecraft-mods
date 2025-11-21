package com.example.industrialsynergy.blocks.generators;

import com.example.industrialsynergy.blocks.ModBlocks;
import com.example.industrialsynergy.energy.ModEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UraniumReactorTileEntity extends TileEntity implements ITickableTileEntity {
    private final ModEnergyStorage storage = new ModEnergyStorage(500000, 0, 2000) {
        @Override
        protected void onEnergyChanged() { markDirty(); }
    };
    private final LazyOptional<IEnergyStorage> energyOpt = LazyOptional.of(() -> storage);
    private int heat = 0;
    private int fuel = 0;

    public UraniumReactorTileEntity() { super(ModBlocks.URANIUM_REACTOR_TILE.get()); }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        if (fuel > 0) {
            fuel--;
            storage.receiveEnergy(500, false);
            heat += 2;
        } else {
            heat = Math.max(0, heat - 1);
        }
        if (heat > 200) {
            // Небольшой урон игрокам рядом
            for (PlayerEntity p : world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos).grow(3))) {
                p.attackEntityFrom(DamageSource.ON_FIRE, 2.0f);
            }
            fuel = 0;
        }
    }

    public void insertFuel(int ticks) { this.fuel += ticks; }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) return energyOpt.cast();
        return super.getCapability(cap, side);
    }
}
