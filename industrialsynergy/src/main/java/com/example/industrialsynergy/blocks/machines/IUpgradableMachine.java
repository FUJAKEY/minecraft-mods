package com.example.industrialsynergy.blocks.machines;

/**
 * Интерфейс для машин, поддерживающих апгрейды.
 */
public interface IUpgradableMachine {
    int getSpeedUpgrades();
    int getEfficiencyUpgrades();
    int getCapacityUpgrades();
}
