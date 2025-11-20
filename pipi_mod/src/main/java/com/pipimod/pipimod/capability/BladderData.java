package com.pipimod.pipimod.capability;

import net.minecraft.nbt.CompoundNBT;

public class BladderData implements IBladderData {
    private float level = 20f;
    private float capacity = 120f;
    private boolean urinating = false;
    private int ticksSinceSync = 0;

    @Override
    public float getLevel() {
        return level;
    }

    @Override
    public void setLevel(float level) {
        this.level = clamp(level, 0, capacity);
    }

    @Override
    public void addLevel(float delta) {
        setLevel(level + delta);
    }

    @Override
    public float getCapacity() {
        return capacity;
    }

    @Override
    public void setCapacity(float capacity) {
        this.capacity = Math.max(40f, capacity);
        this.level = clamp(level, 0, this.capacity);
    }

    @Override
    public boolean isUrinating() {
        return urinating;
    }

    @Override
    public void setUrinating(boolean urinating) {
        this.urinating = urinating;
    }

    @Override
    public int getDiscomfortStage() {
        float ratio = level / capacity;
        if (ratio >= 1.1f) {
            return 4; // overflow panic
        } else if (ratio >= 0.95f) {
            return 3; // about to burst
        } else if (ratio >= 0.75f) {
            return 2; // heavy pressure
        } else if (ratio >= 0.5f) {
            return 1; // mild urge
        }
        return 0; // comfortable
    }

    @Override
    public CompoundNBT writeTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putFloat("Level", level);
        tag.putFloat("Capacity", capacity);
        tag.putBoolean("Urinating", urinating);
        return tag;
    }

    @Override
    public void readTag(CompoundNBT tag) {
        level = tag.getFloat("Level");
        capacity = tag.contains("Capacity") ? tag.getFloat("Capacity") : capacity;
        urinating = tag.getBoolean("Urinating");
    }

    @Override
    public int getTicksSinceSync() {
        return ticksSinceSync;
    }

    @Override
    public void setTicksSinceSync(int ticks) {
        this.ticksSinceSync = Math.max(0, ticks);
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
