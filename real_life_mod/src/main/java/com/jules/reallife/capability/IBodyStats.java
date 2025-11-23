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

    // New Stats
    float getWater();
    void setWater(float value);
    void consumeWater(float amount);

    float getTemperature();
    void setTemperature(float value);

    float getProtein();
    void setProtein(float value);
    void addProtein(float amount);

    float getCarbs();
    void setCarbs(float value);
    void addCarbs(float amount);

    float getLiverHealth();
    void setLiverHealth(float value);
    void damageLiver(float amount);

    boolean isLegBroken();
    void setLegBroken(boolean value);

    boolean isBleeding();
    void setBleeding(boolean value);

    void copyFrom(IBodyStats other);
}
