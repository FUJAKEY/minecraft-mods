package com.pipimod.bladder;

public class Bladder {
    private int level = 0;
    private static final int MAX_LEVEL = 1000;

    public void tick() {
        if (level < MAX_LEVEL) {
            level++;
        }
    }

    public boolean isFull() {
        return level >= MAX_LEVEL;
    }

    public void empty() {
        level = 0;
    }

    public int getLevel() {
        return level;
    }
}
