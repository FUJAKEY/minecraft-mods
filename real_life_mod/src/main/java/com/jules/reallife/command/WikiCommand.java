package com.jules.reallife.command;

import com.jules.reallife.RealLifeMod;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "reallife")
public class WikiCommand {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("wiki")
            .executes(context -> {
                if (context.getSource().getEntity() instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) context.getSource().getEntity();
                    // Give book
                    if (RealLifeMod.RegistryEvents.WIKI_BOOK != null) {
                        ItemStack book = new ItemStack(RealLifeMod.RegistryEvents.WIKI_BOOK);
                        if (!player.inventory.add(book)) {
                            player.drop(book, false);
                        }
                        player.sendMessage(new StringTextComponent("Here is your guide."), player.getUUID());
                    }
                    return 1;
                }
                return 0;
            }));
    }
}
