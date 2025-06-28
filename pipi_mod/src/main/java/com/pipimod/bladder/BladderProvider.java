package com.pipimod.bladder;

import net.minecraft.util.Direction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BladderProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(Bladder.class)
    public static Capability<Bladder> BLADDER_CAPABILITY = null;

    private final LazyOptional<Bladder> instance = LazyOptional.of(Bladder::new);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == BLADDER_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        instance.ifPresent(b -> tag.putInt("Level", b.getLevel()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        instance.ifPresent(b -> {
            b.empty();
            for (int i = 0; i < nbt.getInt("Level"); i++) {
                b.tick();
            }
        });
    }
}
