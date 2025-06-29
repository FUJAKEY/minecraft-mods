package com.pipimod.energy;

public enum WireMode {
    AUTO,
    TAKE,
    GIVE;

    public WireMode next() {
        if (this == TAKE) return GIVE;
        if (this == GIVE) return TAKE;
        return AUTO;
    }
}
