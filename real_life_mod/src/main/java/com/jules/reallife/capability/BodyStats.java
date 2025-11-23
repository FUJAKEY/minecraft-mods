package com.jules.reallife.capability;

import net.minecraft.nbt.CompoundNBT;

public class BodyStats implements IBodyStats {
    private float strength = 1.0f;
    private float endurance = 1.0f;
    private float lungCapacity = 100.0f;
    private float heartHealth = 100.0f;
    private float muscleIntegrity = 100.0f;
    private float stamina = 100.0f;

    // New stats
    private float water = 100.0f;
    private float temperature = 36.6f;
    private float protein = 0.0f;
    private float carbs = 50.0f;
    private float liverHealth = 100.0f;
    private boolean legBroken = false;
    private boolean bleeding = false;

    // Hardcore Stats
    private float sanity = 100.0f;
    private float fatigue = 0.0f;
    private float immunity = 100.0f;
    private float hygiene = 100.0f;
    private int genetics = 0; // 0=Normal, 1=Athlete, 2=Runner, 3=Brittle
    private boolean sick = false;
    private boolean foodPoisoned = false;

    @Override
    public float getStrength() { return strength; }
    @Override
    public void setStrength(float value) { this.strength = value; }
    @Override
    public void addStrength(float amount) { this.strength += amount; }

    @Override
    public float getEndurance() { return endurance; }
    @Override
    public void setEndurance(float value) { this.endurance = value; }
    @Override
    public void addEndurance(float amount) { this.endurance += amount; }

    @Override
    public float getLungCapacity() { return lungCapacity; }
    @Override
    public void setLungCapacity(float value) { this.lungCapacity = value; }
    @Override
    public void addLungCapacity(float amount) { this.lungCapacity += amount; }

    @Override
    public float getHeartHealth() { return heartHealth; }
    @Override
    public void setHeartHealth(float value) { this.heartHealth = value; }
    @Override
    public void damageHeart(float amount) { this.heartHealth = Math.max(0, this.heartHealth - amount); }

    @Override
    public float getMuscleIntegrity() { return muscleIntegrity; }
    @Override
    public void setMuscleIntegrity(float value) { this.muscleIntegrity = value; }
    @Override
    public void damageMuscles(float amount) { this.muscleIntegrity = Math.max(0, this.muscleIntegrity - amount); }

    @Override
    public float getStamina() { return stamina; }
    @Override
    public void setStamina(float value) { this.stamina = Math.max(0, Math.min(value, getMaxStamina())); }
    @Override
    public void consumeStamina(float amount) { setStamina(stamina - amount); }
    @Override
    public void restoreStamina(float amount) { setStamina(stamina + amount); }

    @Override
    public float getMaxStamina() {
        return 100.0f * endurance; // Stamina scales with endurance
    }

    // New Implementations
    @Override public float getWater() { return water; }
    @Override public void setWater(float value) { this.water = Math.max(0, Math.min(100, value)); }
    @Override public void consumeWater(float amount) { setWater(water - amount); }

    @Override public float getTemperature() { return temperature; }
    @Override public void setTemperature(float value) { this.temperature = value; }

    @Override public float getProtein() { return protein; }
    @Override public void setProtein(float value) { this.protein = Math.max(0, value); }
    @Override public void addProtein(float amount) { this.protein += amount; }

    @Override public float getCarbs() { return carbs; }
    @Override public void setCarbs(float value) { this.carbs = Math.max(0, value); }
    @Override public void addCarbs(float amount) { this.carbs += amount; }

    @Override public float getLiverHealth() { return liverHealth; }
    @Override public void setLiverHealth(float value) { this.liverHealth = Math.max(0, Math.min(100, value)); }
    @Override public void damageLiver(float amount) { setLiverHealth(liverHealth - amount); }

    @Override public boolean isLegBroken() { return legBroken; }
    @Override public void setLegBroken(boolean value) { this.legBroken = value; }

    @Override public boolean isBleeding() { return bleeding; }
    @Override public void setBleeding(boolean value) { this.bleeding = value; }

    // Hardcore Impl
    @Override public float getSanity() { return sanity; }
    @Override public void setSanity(float value) { this.sanity = Math.max(0, Math.min(100, value)); }
    @Override public void changeSanity(float amount) { setSanity(sanity + amount); }

    @Override public float getFatigue() { return fatigue; }
    @Override public void setFatigue(float value) { this.fatigue = Math.max(0, Math.min(100, value)); }
    @Override public void changeFatigue(float amount) { setFatigue(fatigue + amount); }

    @Override public float getImmunity() { return immunity; }
    @Override public void setImmunity(float value) { this.immunity = Math.max(0, Math.min(100, value)); }

    @Override public float getHygiene() { return hygiene; }
    @Override public void setHygiene(float value) { this.hygiene = Math.max(0, Math.min(100, value)); }
    @Override public void changeHygiene(float amount) { setHygiene(hygiene + amount); }

    @Override public int getGenetics() { return genetics; }
    @Override public void setGenetics(int value) { this.genetics = value; }

    @Override public boolean isSick() { return sick; }
    @Override public void setSick(boolean value) { this.sick = value; }

    @Override public boolean isFoodPoisoned() { return foodPoisoned; }
    @Override public void setFoodPoisoned(boolean value) { this.foodPoisoned = value; }

    @Override
    public void copyFrom(IBodyStats other) {
        this.strength = other.getStrength();
        this.endurance = other.getEndurance();
        this.lungCapacity = other.getLungCapacity();
        this.heartHealth = other.getHeartHealth();
        this.muscleIntegrity = other.getMuscleIntegrity();
        this.stamina = other.getStamina();

        this.water = other.getWater();
        this.temperature = other.getTemperature();
        this.protein = other.getProtein();
        this.carbs = other.getCarbs();
        this.liverHealth = other.getLiverHealth();
        this.legBroken = other.isLegBroken();
        this.bleeding = other.isBleeding();

        this.sanity = other.getSanity();
        this.fatigue = other.getFatigue();
        this.immunity = other.getImmunity();
        this.hygiene = other.getHygiene();
        this.genetics = other.getGenetics();
        this.sick = other.isSick();
        this.foodPoisoned = other.isFoodPoisoned();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putFloat("strength", strength);
        tag.putFloat("endurance", endurance);
        tag.putFloat("lungCapacity", lungCapacity);
        tag.putFloat("heartHealth", heartHealth);
        tag.putFloat("muscleIntegrity", muscleIntegrity);
        tag.putFloat("stamina", stamina);

        tag.putFloat("water", water);
        tag.putFloat("temperature", temperature);
        tag.putFloat("protein", protein);
        tag.putFloat("carbs", carbs);
        tag.putFloat("liverHealth", liverHealth);
        tag.putBoolean("legBroken", legBroken);
        tag.putBoolean("bleeding", bleeding);

        tag.putFloat("sanity", sanity);
        tag.putFloat("fatigue", fatigue);
        tag.putFloat("immunity", immunity);
        tag.putFloat("hygiene", hygiene);
        tag.putInt("genetics", genetics);
        tag.putBoolean("sick", sick);
        tag.putBoolean("foodPoisoned", foodPoisoned);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        strength = nbt.getFloat("strength");
        endurance = nbt.getFloat("endurance");
        lungCapacity = nbt.getFloat("lungCapacity");
        heartHealth = nbt.getFloat("heartHealth");
        muscleIntegrity = nbt.getFloat("muscleIntegrity");
        stamina = nbt.getFloat("stamina");

        if (nbt.contains("water")) water = nbt.getFloat("water");
        if (nbt.contains("temperature")) temperature = nbt.getFloat("temperature");
        if (nbt.contains("protein")) protein = nbt.getFloat("protein");
        if (nbt.contains("carbs")) carbs = nbt.getFloat("carbs");
        if (nbt.contains("liverHealth")) liverHealth = nbt.getFloat("liverHealth");
        if (nbt.contains("legBroken")) legBroken = nbt.getBoolean("legBroken");
        if (nbt.contains("bleeding")) bleeding = nbt.getBoolean("bleeding");

        if (nbt.contains("sanity")) sanity = nbt.getFloat("sanity");
        if (nbt.contains("fatigue")) fatigue = nbt.getFloat("fatigue");
        if (nbt.contains("immunity")) immunity = nbt.getFloat("immunity");
        if (nbt.contains("hygiene")) hygiene = nbt.getFloat("hygiene");
        if (nbt.contains("genetics")) genetics = nbt.getInt("genetics");
        if (nbt.contains("sick")) sick = nbt.getBoolean("sick");
        if (nbt.contains("foodPoisoned")) foodPoisoned = nbt.getBoolean("foodPoisoned");
    }
}
