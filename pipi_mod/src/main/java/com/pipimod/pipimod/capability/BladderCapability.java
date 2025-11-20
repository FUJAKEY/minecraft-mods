package com.pipimod.pipimod.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.pipimod.pipimod.PipiMod;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = PipiMod.MOD_ID)
public class BladderCapability {
    @CapabilityInject(IBladderData.class)
    public static Capability<IBladderData> BLADDER = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IBladderData.class, new Storage(), BladderData::new);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Object> event) {
        if (event.getObject() instanceof net.minecraft.entity.player.PlayerEntity) {
            event.addCapability(BladderProvider.ID, new BladderProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        event.getOriginal().getCapability(BLADDER).ifPresent(oldCap ->
                event.getPlayer().getCapability(BLADDER).ifPresent(newCap -> newCap.readTag(oldCap.writeTag())));
    }

    public static class Storage implements Capability.IStorage<IBladderData> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IBladderData> capability, IBladderData instance, Direction side) {
            return instance.writeTag();
        }

        @Override
        public void readNBT(Capability<IBladderData> capability, IBladderData instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                instance.readTag((CompoundNBT) nbt);
            }
        }
    }

    public static class BladderProvider implements ICapabilitySerializable<INBT>, ICapabilityProvider {
        public static final net.minecraft.util.ResourceLocation ID = new net.minecraft.util.ResourceLocation(PipiMod.MOD_ID, "bladder");
        private final LazyOptional<IBladderData> holder = LazyOptional.of(BladderData::new);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == BLADDER ? holder.cast() : LazyOptional.empty();
        }

        @Override
        public INBT serializeNBT() {
            return BLADDER.getStorage().writeNBT(BLADDER, holder.orElseThrow(IllegalStateException::new), null);
        }

        @Override
        public void deserializeNBT(INBT nbt) {
            BLADDER.getStorage().readNBT(BLADDER, holder.orElseThrow(IllegalStateException::new), null, nbt);
        }
    }
}
