package com.jules.reallife.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BodyCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(IBodyStats.class)
    public static Capability<IBodyStats> BODY_CAPABILITY = null;

    private final IBodyStats instance = BODY_CAPABILITY.getDefaultInstance();
    private final LazyOptional<IBodyStats> optional = LazyOptional.of(() -> instance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == BODY_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        instance.deserializeNBT(nbt);
    }

    public static void register() {
        net.minecraftforge.common.capabilities.CapabilityManager.INSTANCE.register(
            IBodyStats.class,
            new net.minecraftforge.common.capabilities.Capability.IStorage<IBodyStats>() {
                @Override
                public INBT writeNBT(Capability<IBodyStats> capability, IBodyStats instance, Direction side) {
                    return instance.serializeNBT();
                }

                @Override
                public void readNBT(Capability<IBodyStats> capability, IBodyStats instance, Direction side, INBT nbt) {
                    if (!(instance instanceof BodyStats))
                        throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                    instance.deserializeNBT((CompoundNBT) nbt);
                }
            },
            BodyStats::new
        );
    }
}
