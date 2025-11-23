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
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class DumbbellItem extends Item {
    public DumbbellItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_MISC).durability(100));
        setRegistryName("dumbbell");
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (!worldIn.isClientSide) {
            playerIn.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                if (cap.getStamina() >= 10) {
                    cap.consumeStamina(10);
                    cap.addStrength(0.1f); // Significant strength gain
                    cap.damageMuscles(1.0f); // Training hurts muscles slightly

                    // Sync
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerIn),
                        new SyncBodyStatsPacket(cap.serializeNBT()));

                    itemstack.hurtAndBreak(1, playerIn, (p) -> p.broadcastBreakEvent(handIn));
                } else {
                   // Maybe send a message "Too tired"
                }
            });
        }

        return ActionResult.success(itemstack);
    }
}
