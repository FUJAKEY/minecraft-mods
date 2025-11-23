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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class EnergyDrinkItem extends Item {
    public EnergyDrinkItem() {
        super(new Item.Properties()
            .tab(ItemGroup.TAB_FOOD)
            .food(new Food.Builder().nutrition(1).saturationMod(0.1f).alwaysEat().build()));
        setRegistryName("energy_drink");
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide && entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                cap.setFatigue(Math.max(0, cap.getFatigue() - 30.0f));
                cap.restoreStamina(50.0f);

                // Crash later? For now just speed
                player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 600, 1));

                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
            });
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
}
