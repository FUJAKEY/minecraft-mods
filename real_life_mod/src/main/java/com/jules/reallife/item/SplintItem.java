package com.jules.reallife.item;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.network.PacketHandler;
import com.jules.reallife.network.SyncBodyStatsPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class SplintItem extends Item {
    public SplintItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_MISC));
        setRegistryName("splint");
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (!worldIn.isClientSide) {
            playerIn.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                if (cap.isLegBroken()) {
                    cap.setLegBroken(false);
                    playerIn.removeEffect(Effects.MOVEMENT_SLOWDOWN);

                    itemstack.shrink(1);

                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerIn),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
                }
            });
        }

        return ActionResult.success(itemstack);
    }
}
