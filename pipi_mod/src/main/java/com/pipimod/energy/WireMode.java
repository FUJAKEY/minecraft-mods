package com.pipimod.energy;

public enum WireMode {
    AUTO,
    TAKE,
    GIVE,
    DISABLED;

    public WireMode next() {
        switch (this) {
            case AUTO:
                return TAKE;
            case TAKE:
                return GIVE;
            case GIVE:
                return DISABLED;
            case DISABLED:
            default:
                return AUTO;
        }
    }
}
