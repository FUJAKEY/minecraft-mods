package com.pipimod.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.pipimod.bladder.Bladder;
import com.pipimod.bladder.BladderProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pipimod")
public class BladderCommands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSource> root = Commands.literal("bladder")
                .requires(cs -> cs.hasPermission(2));

        root.then(Commands.literal("set")
                .then(Commands.argument("amount", IntegerArgumentType.integer(0, Bladder.MAX_LEVEL))
                        .executes(ctx -> setLevel(ctx.getSource().getPlayerOrException(),
                                IntegerArgumentType.getInteger(ctx, "amount"), ctx.getSource()))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> setLevel(EntityArgument.getPlayer(ctx, "target"),
                                        IntegerArgumentType.getInteger(ctx, "amount"), ctx.getSource())))));

        root.then(Commands.literal("add")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(ctx -> addLevel(ctx.getSource().getPlayerOrException(),
                                IntegerArgumentType.getInteger(ctx, "amount"), ctx.getSource()))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> addLevel(EntityArgument.getPlayer(ctx, "target"),
                                        IntegerArgumentType.getInteger(ctx, "amount"), ctx.getSource())))));

        root.then(Commands.literal("reduce")
                .then(Commands.argument("amount", IntegerArgumentType.integer())
                        .executes(ctx -> reduceLevel(ctx.getSource().getPlayerOrException(),
                                IntegerArgumentType.getInteger(ctx, "amount"), ctx.getSource()))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> reduceLevel(EntityArgument.getPlayer(ctx, "target"),
                                        IntegerArgumentType.getInteger(ctx, "amount"), ctx.getSource())))));

        event.getDispatcher().register(root);
    }

    private static int setLevel(ServerPlayerEntity player, int amount, CommandSource source) {
        player.getCapability(BladderProvider.BLADDER_CAPABILITY).ifPresent(b -> b.setLevel(amount));
        source.sendSuccess(new TranslationTextComponent("commands.bladder.set"), true);
        return 1;
    }

    private static int addLevel(ServerPlayerEntity player, int amount, CommandSource source) {
        player.getCapability(BladderProvider.BLADDER_CAPABILITY).ifPresent(b -> b.addLevel(amount));
        source.sendSuccess(new TranslationTextComponent("commands.bladder.add"), true);
        return 1;
    }

    private static int reduceLevel(ServerPlayerEntity player, int amount, CommandSource source) {
        player.getCapability(BladderProvider.BLADDER_CAPABILITY).ifPresent(b -> b.reduceLevel(amount));
        source.sendSuccess(new TranslationTextComponent("commands.bladder.reduce"), true);
        return 1;
    }
}
