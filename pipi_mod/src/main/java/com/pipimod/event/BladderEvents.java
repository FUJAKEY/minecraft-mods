package com.pipimod.event;

import com.pipimod.bladder.Bladder;
import com.pipimod.bladder.BladderProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pipimod")
public class BladderEvents {

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<PlayerEntity> event) {
        event.addCapability(new net.minecraft.util.ResourceLocation("pipimod", "bladder"), new BladderProvider());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            event.player.getCapability(BladderProvider.BLADDER_CAPABILITY).ifPresent(Bladder::tick);
        }
    }
}
