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

public class BandageItem extends Item {
    public BandageItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_MISC));
        setRegistryName("bandage");
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (!worldIn.isClientSide) {
            playerIn.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                if (cap.isBleeding()) {
                    cap.setBleeding(false);
                    // Heal muscles a bit too?
                    cap.setMuscleIntegrity(cap.getMuscleIntegrity() + 5.0f);

                    itemstack.shrink(1);

                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerIn),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
                }
            });
        }

        return ActionResult.success(itemstack);
    }
}
