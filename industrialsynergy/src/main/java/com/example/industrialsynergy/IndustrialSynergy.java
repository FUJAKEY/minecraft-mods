package com.example.industrialsynergy;

import com.example.industrialsynergy.compat.MekanismCompat;
import com.example.industrialsynergy.setup.ModConfigs;
import com.example.industrialsynergy.setup.Registration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Главный класс мода. Здесь мы подключаем все регистры и подписываемся на события Forge.
 */
@Mod(IndustrialSynergy.MODID)
public class IndustrialSynergy {
    public static final String MODID = "industrialsynergy";
    public static final Logger LOGGER = LogManager.getLogger();

    public IndustrialSynergy() {
        // Получаем мод-шину событий Forge (для регистрации контента)
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Инициализируем deferred-register'ы
        Registration.init(modEventBus);

        // Регистрируем конфиги (общие/клиентские)
        ModConfigs.register();

        // Подписываемся на общие и клиентские события
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);

        // Подписка на события майнкрафтовской шины (серверные события, worldgen и т.д.)
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("IndustrialSynergy: общая инициализация");
        // Здесь можно добавить интеграции, настройку пакетов, сетевые каналы и т.п.
        event.enqueueWork(MekanismCompat::registerConditionalRecipes);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("IndustrialSynergy: клиентская инициализация");
        Registration.registerScreens();
    }
}
