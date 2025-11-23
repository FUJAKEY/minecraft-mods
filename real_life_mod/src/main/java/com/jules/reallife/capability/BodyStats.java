package com.jules.reallife.capability;

import net.minecraft.nbt.CompoundNBT;

public class BodyStats implements IBodyStats {
    private float strength = 1.0f;
    private float endurance = 1.0f;
    private float lungCapacity = 100.0f;
    private float heartHealth = 100.0f;
    private float muscleIntegrity = 100.0f;
    private float stamina = 100.0f;

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

    @Override
    public void copyFrom(IBodyStats other) {
        this.strength = other.getStrength();
        this.endurance = other.getEndurance();
        this.lungCapacity = other.getLungCapacity();
        this.heartHealth = other.getHeartHealth();
        this.muscleIntegrity = other.getMuscleIntegrity();
        this.stamina = other.getStamina();
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
    }
}
