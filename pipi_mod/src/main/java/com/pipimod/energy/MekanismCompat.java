package com.pipimod.energy;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.ModList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Simple Mekanism compatibility using reflection so the mod can run without the dependency.
 */
public class MekanismCompat {
    private static Capability<?> strictEnergyCap;
    private static Class<?> handlerClass;
    private static Object actionExecute;
    private static Method insertEnergy;
    private static Method extractEnergy;
    private static Method getEnergy;
    private static Method getMaxEnergy;

    static {
        if (ModList.get().isLoaded("mekanism")) {
            try {
                handlerClass = Class.forName("mekanism.api.energy.IStrictEnergyHandler");
                Class<?> action = Class.forName("mekanism.api.Action");
                actionExecute = action.getField("EXECUTE").get(null);
                insertEnergy = handlerClass.getMethod("insertEnergy", double.class, action);
                extractEnergy = handlerClass.getMethod("extractEnergy", double.class, action);
                getEnergy = handlerClass.getMethod("getEnergy");
                getMaxEnergy = handlerClass.getMethod("getMaxEnergy");
                Class<?> caps = Class.forName("mekanism.api.annotations.capability.Capabilities");
                Field f = caps.getField("STRICT_ENERGY_CAPABILITY");
                strictEnergyCap = (Capability<?>) f.get(null);
            } catch (Exception e) {
                handlerClass = null;
            }
        }
    }

    public static boolean isLoaded() {
        return handlerClass != null && strictEnergyCap != null;
    }

    public static Capability<?> getCapability() {
        return strictEnergyCap;
    }

    public static LazyOptional<?> createWrapper(IEnergyStorage storage) {
        if (!isLoaded()) return LazyOptional.empty();
        Object proxy = java.lang.reflect.Proxy.newProxyInstance(MekanismCompat.class.getClassLoader(),
                new Class<?>[]{handlerClass},
                (obj, method, args) -> {
                    if (method.equals(insertEnergy)) {
                        int r = storage.receiveEnergy((int) Math.round((Double) args[0]), false);
                        return (double) r;
                    }
                    if (method.equals(extractEnergy)) {
                        int e = storage.extractEnergy((int) Math.round((Double) args[0]), false);
                        return (double) e;
                    }
                    if (method.equals(getEnergy)) {
                        return (double) storage.getEnergyStored();
                    }
                    if (method.equals(getMaxEnergy)) {
                        return (double) storage.getMaxEnergyStored();
                    }
                    return null;
                });
        return LazyOptional.of(() -> proxy);
    }
}
