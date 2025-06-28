package com.pipimod.event;

import com.pipimod.bladder.Bladder;
import com.pipimod.bladder.BladderProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

@Mod.EventBusSubscriber(modid = "pipimod")
public class BladderEvents {

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<PlayerEntity> event) {
        event.addCapability(new net.minecraft.util.ResourceLocation("pipimod", "bladder"), new BladderProvider());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            event.player.getCapability(BladderProvider.BLADDER_CAPABILITY).ifPresent(b -> {
                b.tick();
                int percent = b.getPercent();
                if (percent >= 100) {
                    event.player.addEffect(new EffectInstance(Effects.CONFUSION, 40, 0, true, false));
                } else if (percent >= 70) {
                    event.player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 40, 1, true, false));
                } else if (percent >= 50) {
                    event.player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 40, 0, true, false));
                }
            });
        }
    }
}
