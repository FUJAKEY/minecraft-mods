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

public class SoapItem extends Item {
    public SoapItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_MISC).durability(10));
        setRegistryName("soap");
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        // Require water nearby? Simpler to just allow using it.
        if (!worldIn.isClientSide) {
            if (playerIn.isInWater() || playerIn.level.isRainingAt(playerIn.blockPosition())) {
                playerIn.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                    cap.setHygiene(100.0f);
                    itemstack.hurtAndBreak(1, playerIn, (p) -> p.broadcastBreakEvent(handIn));
                    playerIn.sendMessage(new StringTextComponent("§bYou are clean now."), playerIn.getUUID());
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerIn),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
                });
            } else {
                playerIn.sendMessage(new StringTextComponent("You need water to use soap."), playerIn.getUUID());
            }
        }
        return ActionResult.success(itemstack);
    }
}
