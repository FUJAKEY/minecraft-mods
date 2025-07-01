package com.pipimod.energy;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, EnergyMod.MODID);

    public static final RegistryObject<ContainerType<MetalFillerContainer>> METAL_FILLER = CONTAINERS.register("metal_filler", () -> IForgeContainerType.create((id, inv, buf) -> new MetalFillerContainer(id, inv, buf)));
}
