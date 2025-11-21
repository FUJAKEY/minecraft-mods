package com.example.industrialsynergy.compat;

import com.example.industrialsynergy.IndustrialSynergy;
import net.minecraftforge.fml.ModList;

/**
 * Пример совместимости: проверяем наличие Mekanism и регистрируем условные рецепты.
 */
public class MekanismCompat {
    public static boolean isLoaded() {
        return ModList.get().isLoaded("mekanism");
    }

    public static void registerConditionalRecipes() {
        if (isLoaded()) {
            IndustrialSynergy.LOGGER.info("Mekanism обнаружен: включаем дополнительные рецепты и интеграции");
            // Здесь можно добавить вызовы data generators или загрузку condition-based рецептов
        }
    }
}
