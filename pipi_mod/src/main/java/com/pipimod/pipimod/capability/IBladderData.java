package com.pipimod.pipimod.capability;

import net.minecraft.nbt.CompoundNBT;

public interface IBladderData {
    float getLevel();

    void setLevel(float level);

    void addLevel(float delta);

    float getCapacity();

    void setCapacity(float capacity);

    boolean isUrinating();

    void setUrinating(boolean urinating);

    int getDiscomfortStage();

    CompoundNBT writeTag();

    void readTag(CompoundNBT tag);

    int getTicksSinceSync();

    void setTicksSinceSync(int ticks);
}
