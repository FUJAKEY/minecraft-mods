package com.pipimod.pipimod.client;

import com.pipimod.pipimod.PipiMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = PipiMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    static KeyBindingHolder keyBindingHolder;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        keyBindingHolder = new KeyBindingHolder();
        ClientRegistry.registerKeyBinding(keyBindingHolder.peeKey());
    }
}
