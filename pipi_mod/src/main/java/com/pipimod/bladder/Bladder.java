package com.pipimod.bladder;

/**
 * Stores the bladder state for a player. The bladder gradually fills over
 * time and drains while the player is peeing.
 */
public class Bladder {
    /**
     * Maximum amount of fluid the bladder can hold. With a drain rate of two
     * units per network packet and packets sent every two ticks the bladder will
     * completely empty in sixty seconds at 20TPS.
     */
    public static final int MAX_LEVEL = 1200;

    /** Ticks between each one percent fill increment. */
    private static final int FILL_INTERVAL = 200; // ten seconds

    private int level;
    private int fillTimer;
    private int pauseTicks;

    /** Called every tick while the player exists. */
    public void tick() {
        if (pauseTicks > 0) {
            pauseTicks--;
            return;
        }
        if (level < MAX_LEVEL) {
            fillTimer++;
            if (fillTimer >= FILL_INTERVAL) {
                level += MAX_LEVEL / 100;
                if (level > MAX_LEVEL) {
                    level = MAX_LEVEL;
                }
                fillTimer = 0;
            }
        }
    }

    /** Drains the given amount and pauses filling briefly. */
    public void drain(int amount) {
        if (amount <= 0 || level <= 0) {
            return;
        }
        level -= amount;
        if (level < 0) {
            level = 0;
        }
        pauseTicks = 2;
    }

    /** Empties the bladder completely. */
    public void empty() {
        level = 0;
        fillTimer = 0;
    }

    /** Sets the bladder level clamped to the valid range. */
    public void setLevel(int amount) {
        if (amount < 0) {
            amount = 0;
        }
        if (amount > MAX_LEVEL) {
            amount = MAX_LEVEL;
        }
        level = amount;
        fillTimer = 0;
    }

    public void addLevel(int amount) {
        setLevel(level + amount);
    }

    public void reduceLevel(int amount) {
        setLevel(level - amount);
    }

    public int getLevel() {
        return level;
    }

    public int getPercent() {
        return level * 100 / MAX_LEVEL;
    }
}
