package com.example.industrialsynergy.setup;

import com.example.industrialsynergy.IndustrialSynergy;
import com.example.industrialsynergy.blocks.ModBlocks;
import com.example.industrialsynergy.items.ModItems;
import com.example.industrialsynergy.blocks.machines.CrusherContainer;
import com.example.industrialsynergy.blocks.machines.CrusherScreen;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ScreenManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Централизованный класс регистрации контента.
 */
public class Registration {
    public static final DeferredRegister<net.minecraft.block.Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, IndustrialSynergy.MODID);
    public static final DeferredRegister<net.minecraft.item.Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IndustrialSynergy.MODID);
    public static final DeferredRegister<net.minecraft.tileentity.TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, IndustrialSynergy.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, IndustrialSynergy.MODID);

    // Контейнер дробителя регистрируем здесь, чтобы использовать в ScreenManager.
    public static final RegistryObject<ContainerType<CrusherContainer>> CRUSHER_CONTAINER = CONTAINERS.register("crusher", () -> IForgeContainerType.create((windowId, inv, data) -> new CrusherContainer(windowId, inv.player.world, data.readBlockPos(), inv, inv.player)));

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        CONTAINERS.register(modEventBus);

        ModBlocks.register();
        ModItems.register();
    }

    public static void registerScreens() {
        ScreenManager.registerFactory(CRUSHER_CONTAINER.get(), CrusherScreen::new);
    }
}
