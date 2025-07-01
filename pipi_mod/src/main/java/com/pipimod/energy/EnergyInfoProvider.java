package com.pipimod.energy;

public interface EnergyInfoProvider {
    int getLastInput();
    int getLastOutput();
    default int getLastGeneration() { return 0; }
}
