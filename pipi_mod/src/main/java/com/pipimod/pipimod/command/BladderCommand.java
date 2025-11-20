package com.pipimod.pipimod.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pipimod.pipimod.PipiMod;
import com.pipimod.pipimod.capability.BladderCapability;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PipiMod.MOD_ID)
public class BladderCommand {
    @SubscribeEvent
    public static void onRegister(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSource> root = Commands.literal("bladder")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("set")
                        .then(Commands.argument("percent", FloatArgumentType.floatArg(0.0f, 300.0f))
                                .executes(BladderCommand::setBladderPercent)));
        event.getDispatcher().register(root);
    }

    private static int setBladderPercent(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        float percent = FloatArgumentType.getFloat(ctx, "percent");
        ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
        player.getCapability(BladderCapability.BLADDER).ifPresent(cap -> {
            float targetLevel = cap.getCapacity() * (percent / 100f);
            cap.setLevel(targetLevel);
            ctx.getSource().sendSuccess(new StringTextComponent(
                    String.format("Bladder level set to %.1f%% (%.1f / %.1f)", percent, cap.getLevel(), cap.getCapacity())), true);
        });
        return 1;
    }
}
