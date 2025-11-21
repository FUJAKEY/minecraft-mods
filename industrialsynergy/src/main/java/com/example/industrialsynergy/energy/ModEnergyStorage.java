package com.example.industrialsynergy.energy;

import net.minecraftforge.energy.EnergyStorage;

/**
 * Простая обертка над EnergyStorage с хуком onEnergyChanged(), чтобы реагировать на изменение заряда.
 */
public class ModEnergyStorage extends EnergyStorage {
    public ModEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer);
    }

    public ModEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = super.receiveEnergy(maxReceive, simulate);
        if (received > 0 && !simulate) {
            onEnergyChanged();
        }
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = super.extractEnergy(maxExtract, simulate);
        if (extracted > 0 && !simulate) {
            onEnergyChanged();
        }
        return extracted;
    }

    protected void onEnergyChanged() {
        // Можно переопределить в тайл-сущностях для уведомления клиентов
    }
}
