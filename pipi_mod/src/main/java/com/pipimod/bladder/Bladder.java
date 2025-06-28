package com.pipimod.bladder;

public class Bladder {
    private int level = 0;
    public static final int MAX_LEVEL = 1200; // one minute of peeing at 20tps

    private int fillCounter = 0;
    private int peeTicks = 0;

    public void tick() {
        if (peeTicks > 0) {
            peeTicks--; // pause filling while peeing
            return;
        }
        if (level < MAX_LEVEL) {
            fillCounter++;
            if (fillCounter >= 200) { // 10 seconds per 1%
                level += MAX_LEVEL / 100;
                if (level > MAX_LEVEL) level = MAX_LEVEL;
                fillCounter = 0;
            }
        }
    }

    public boolean isFull() {
        return level >= MAX_LEVEL;
    }

    public void empty() {
        level = 0;
    }

    public void setLevel(int amount) {
        if (amount < 0) amount = 0;
        if (amount > MAX_LEVEL) amount = MAX_LEVEL;
        level = amount;
    }

    public void addLevel(int amount) {
        setLevel(level + amount);
    }

    public void reduceLevel(int amount) {
        setLevel(level - amount);
    }

    public void drain(int amount) {
        if (level <= 0) return;
        level -= amount;
        if (level < 0) level = 0;
        peeTicks = 2; // small grace period to keep filling paused
    }

    public int getPercent() {
        return level * 100 / MAX_LEVEL;
    }

    public int getLevel() {
        return level;
    }
}
