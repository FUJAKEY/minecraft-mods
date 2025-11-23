package com.jules.reallife.item;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.network.PacketHandler;
import com.jules.reallife.network.SyncBodyStatsPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class ProteinShakeItem extends Item {
    public ProteinShakeItem() {
        super(new Item.Properties()
            .tab(ItemGroup.TAB_FOOD)
            .food(new Food.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build()));
        setRegistryName("protein_shake");
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide && entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                cap.addProtein(30.0f);
                cap.setWater(Math.min(100, cap.getWater() + 20.0f));

                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
            });
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
}
