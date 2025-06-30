package com.pipimod.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.command.Commands;

@Mod.EventBusSubscriber(modid = EnergyMod.MODID)
public class ModCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(LiteralArgumentBuilder.<CommandSource>literal("wiki")
            .requires(cs -> cs.hasPermission(0))
            .executes(ctx -> {
                ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
                ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
                CompoundNBT tag = new CompoundNBT();
                tag.putString("title", "Energy Mod Wiki");
                tag.putString("author", "EnergyMod");
                ListNBT pages = new ListNBT();
                pages.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent("Use energy cells and wires to store and transfer FE. Generators produce power."))));
                tag.put("pages", pages);
                book.setTag(tag);
                if (!player.addItem(book)) {
                    player.drop(book, false);
                }
                return 1;
            }));
    }
}
