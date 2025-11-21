package com.example.industrialsynergy.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Конфигурации мода: множители генерации, скорости машин, включение руд.
 */
public class ModConfigs {
    public static ForgeConfigSpec COMMON_SPEC;
    public static Common COMMON;

    public static class Common {
        public final ForgeConfigSpec.BooleanValue genBauxite;
        public final ForgeConfigSpec.BooleanValue genTitanium;
        public final ForgeConfigSpec.BooleanValue genUranium;
        public final ForgeConfigSpec.DoubleValue generatorMultiplier;
        public final ForgeConfigSpec.DoubleValue machineSpeedMultiplier;
        public final ForgeConfigSpec.DoubleValue baseEnergyUse;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("worldgen");
            genBauxite = builder.comment("Генерация бокситовой руды").define("genBauxite", true);
            genTitanium = builder.comment("Генерация титановой руды").define("genTitanium", true);
            genUranium = builder.comment("Генерация урановой руды").define("genUranium", true);
            builder.pop();

            builder.push("balance");
            generatorMultiplier = builder.comment("Множитель энергии генераторов")
                    .defineInRange("generatorMultiplier", 1.0, 0.1, 10.0);
            machineSpeedMultiplier = builder.comment("Множитель скорости машин")
                    .defineInRange("machineSpeedMultiplier", 1.0, 0.1, 10.0);
            baseEnergyUse = builder.comment("Базовое потребление энергии (FE/t) машинами")
                    .defineInRange("baseEnergyUse", 40.0, 1.0, 100000.0);
            builder.pop();
        }
    }

    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new Common(builder);
        COMMON_SPEC = builder.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
    }
}
