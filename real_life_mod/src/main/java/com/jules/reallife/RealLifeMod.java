package com.jules.reallife;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.item.DumbbellItem;
import com.jules.reallife.network.PacketHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("reallife")
public class RealLifeMod {
    private static final Logger LOGGER = LogManager.getLogger();

    public RealLifeMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Register the capability
        BodyCapabilityProvider.register();
        // Initialize Networking
        PacketHandler.init();
        LOGGER.info("Real Life Mod Initialized: Capabilities and Networking ready.");
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new DumbbellItem());
        }
    }

    @ObjectHolder("reallife:dumbbell")
    public static final Item DUMBBELL = null;
}
