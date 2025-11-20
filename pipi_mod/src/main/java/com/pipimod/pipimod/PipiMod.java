package com.pipimod.pipimod;

import com.pipimod.pipimod.capability.BladderCapability;
import com.pipimod.pipimod.network.PipiNetwork;
import com.pipimod.pipimod.system.PlayerReliefHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PipiMod.MOD_ID)
public class PipiMod {
    public static final String MOD_ID = "pipimod";

    public PipiMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(new PlayerReliefHandler());
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        BladderCapability.register();
        PipiNetwork.registerMessages();
    }
}
