package com.jules.reallife;

import com.jules.reallife.block.BenchPressBlock;
import com.jules.reallife.block.TreadmillBlock;
import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.item.BandageItem;
import com.jules.reallife.item.DumbbellItem;
import com.jules.reallife.item.EnergyBarItem;
import com.jules.reallife.item.ProteinShakeItem;
import com.jules.reallife.item.SplintItem;
import com.jules.reallife.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
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
        public static Block TREADMILL;
        public static Block BENCH_PRESS;

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            TREADMILL = new TreadmillBlock();
            BENCH_PRESS = new BenchPressBlock();
            event.getRegistry().register(TREADMILL);
            event.getRegistry().register(BENCH_PRESS);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new DumbbellItem());
            event.getRegistry().register(new BandageItem());
            event.getRegistry().register(new SplintItem());
            event.getRegistry().register(new ProteinShakeItem());
            event.getRegistry().register(new EnergyBarItem());

            event.getRegistry().register(new BlockItem(TREADMILL, new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)).setRegistryName("treadmill"));
            event.getRegistry().register(new BlockItem(BENCH_PRESS, new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)).setRegistryName("bench_press"));
        }
    }

    @ObjectHolder("reallife:dumbbell")
    public static final Item DUMBBELL = null;
}
