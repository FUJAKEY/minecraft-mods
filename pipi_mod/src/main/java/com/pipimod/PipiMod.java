package com.pipimod;

import com.pipimod.event.BladderEvents;
import com.pipimod.network.NetworkHandler;
import com.pipimod.util.TemporaryItemManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Entry point of the pipi mod. It registers the network handler and attaches
 * our event listeners.
 */
@Mod(PipiMod.MODID)
public class PipiMod {
    public static final String MODID = "pipimod";

    public PipiMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(BladderEvents.class);
        MinecraftForge.EVENT_BUS.register(TemporaryItemManager.class);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }
}
