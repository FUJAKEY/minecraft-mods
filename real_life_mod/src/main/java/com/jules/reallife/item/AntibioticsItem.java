package com.jules.reallife.item;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.network.PacketHandler;
import com.jules.reallife.network.SyncBodyStatsPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class AntibioticsItem extends Item {
    public AntibioticsItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_MISC).stacksTo(16));
        setRegistryName("antibiotics");
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (!worldIn.isClientSide) {
            playerIn.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                if (cap.isSick()) {
                    cap.setSick(false);
                    cap.setImmunity(50.0f); // Restore some immunity
                    itemstack.shrink(1);
                    playerIn.sendMessage(new StringTextComponent("§aYou feel better."), playerIn.getUUID());
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerIn),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
                } else {
                    playerIn.sendMessage(new StringTextComponent("You are not sick."), playerIn.getUUID());
                }
            });
        }
        return ActionResult.success(itemstack);
    }
}
