package com.pipimod.energy;

public enum WireMode {
    TAKE,
    GIVE;

    public WireMode next() {
        return this == TAKE ? GIVE : TAKE;
    }
}
