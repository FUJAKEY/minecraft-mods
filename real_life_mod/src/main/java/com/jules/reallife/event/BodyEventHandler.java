package com.jules.reallife.event;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.capability.IBodyStats;
import com.jules.reallife.network.PacketHandler;
import com.jules.reallife.network.SyncBodyStatsPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = "reallife")
public class BodyEventHandler {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            if (!event.getObject().getCapability(BodyCapabilityProvider.BODY_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation("reallife", "body_stats"), new BodyCapabilityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerEntity original = event.getOriginal();
        PlayerEntity newItem = event.getPlayer();
        original.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(oldCap -> {
            newItem.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(newCap -> {
                newCap.copyFrom(oldCap);
            });
        });
    }

    @SubscribeEvent
    public static void onPlayerTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.END) {
            PlayerEntity player = event.player;
            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                boolean changed = false;

                // Stamina Logic
                if (player.isSprinting()) {
                    cap.consumeStamina(0.5f / cap.getEndurance()); // Better endurance = less stamina drain
                    // Training: Running trains Lungs and Endurance
                    if (player.tickCount % 200 == 0) { // Every 10 seconds of sprinting
                         cap.addLungCapacity(0.1f);
                         cap.addEndurance(0.01f);
                         changed = true;
                    }
                    if (cap.getStamina() <= 0) {
                        player.setSprinting(false);
                    }
                    changed = true;
                } else {
                    if (cap.getStamina() < cap.getMaxStamina()) {
                        cap.restoreStamina(0.2f * cap.getLungCapacity() / 100.0f); // Better lungs = faster regen
                        changed = true;
                    }
                }

                // Stats Effects
                // Speed modification based on muscle/stamina?
                // Note: Modifying attributes every tick is expensive. Usually done on change.
                // For now, we just track stats.

                // Sync to client
                if (changed && !player.level.isClientSide && player instanceof ServerPlayerEntity) {
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
                }
            });
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        if (!player.level.isClientSide) {
            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                if (cap.getStamina() > 5) {
                    cap.consumeStamina(5.0f);
                    // Train Strength
                    cap.addStrength(0.05f);

                    // Bonus Damage based on strength?
                    // This is hard to inject directly here without AttributeModifiers,
                    // but we are simulating the "Training" part.
                } else {
                    event.setCanceled(true); // Too tired to attack
                }
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                    new SyncBodyStatsPacket(cap.serializeNBT()));
            });
        }
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (!player.level.isClientSide) {
                player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                    float damage = event.getDistance();
                    if (damage > 3) {
                        cap.damageMuscles(damage * 2); // Falling hurts muscles/bones
                        // Taking damage trains durability?
                        cap.addEndurance(0.02f);
                    }
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
                });
            }
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
             PlayerEntity player = (PlayerEntity) event.getEntityLiving();
             if (!player.level.isClientSide) {
                 player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                     cap.damageHeart(event.getAmount()); // Damage hurts heart health
                     PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                         new SyncBodyStatsPacket(cap.serializeNBT()));
                 });
             }
        }
    }
}
