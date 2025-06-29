package com.pipimod.energy;

public enum WireMode {
    AUTO,
    TAKE,
    GIVE;

    public WireMode next() {
        switch (this) {
            case AUTO:
                return TAKE;
            case TAKE:
                return GIVE;
            case GIVE:
            default:
                return AUTO;
        }
    }
}
