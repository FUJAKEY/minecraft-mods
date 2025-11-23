package com.jules.reallife.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IBodyStats extends INBTSerializable<CompoundNBT> {
    float getStrength();
    void setStrength(float value);
    void addStrength(float amount);

    float getEndurance();
    void setEndurance(float value);
    void addEndurance(float amount);

    float getLungCapacity();
    void setLungCapacity(float value);
    void addLungCapacity(float amount);

    float getHeartHealth();
    void setHeartHealth(float value);
    void damageHeart(float amount);

    float getMuscleIntegrity();
    void setMuscleIntegrity(float value);
    void damageMuscles(float amount);

    float getStamina();
    void setStamina(float value);
    void consumeStamina(float amount);
    void restoreStamina(float amount);
    float getMaxStamina();

    void copyFrom(IBodyStats other);
}
