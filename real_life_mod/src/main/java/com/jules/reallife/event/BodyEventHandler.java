package com.jules.reallife.event;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.network.PacketHandler;
import com.jules.reallife.network.SyncBodyStatsPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
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
            if (player.level.isClientSide) return;

            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                boolean changed = false;

                // --- Stamina & Training ---
                if (player.isSprinting()) {
                    cap.consumeStamina(0.5f / cap.getEndurance());
                    // Training: Running trains Lungs and Endurance
                    if (player.tickCount % 200 == 0) {
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
                        float regen = 0.2f * (cap.getLungCapacity() / 100.0f);
                        if (cap.getCarbs() < 10) regen *= 0.5f; // Low carbs = slow regen
                        cap.restoreStamina(regen);
                        changed = true;
                    }
                }

                // --- Metabolism (Every 5 seconds) ---
                if (player.tickCount % 100 == 0) {
                    float biomeTemp = player.level.getBiome(player.blockPosition()).getTemperature(player.blockPosition());

                    // Thirst
                    float waterLoss = 0.2f;
                    if (biomeTemp > 1.2f) waterLoss *= 2.0f; // Hot biome
                    cap.consumeWater(waterLoss);

                    // Nutrients
                    cap.addCarbs(-0.1f);
                    cap.addProtein(-0.05f); // Protein decays slower

                    // Temperature Regulation
                    float targetTemp = 36.6f;
                    if (biomeTemp < 0.2) targetTemp = 35.0f; // Cold
                    if (biomeTemp > 1.2) targetTemp = 38.0f; // Hot

                    // Move current temp towards target
                    float currentTemp = cap.getTemperature();
                    if (Math.abs(currentTemp - targetTemp) > 0.1f) {
                        cap.setTemperature(currentTemp + (targetTemp - currentTemp) * 0.1f);
                    }

                    // Muscle Growth (if protein high)
                    if (cap.getProtein() > 80.0f && cap.getStrength() < 10.0f) {
                        cap.addStrength(0.001f);
                    }

                    changed = true;
                }

                // --- Effects ---
                // Bleeding
                if (cap.isBleeding() && player.tickCount % 80 == 0) {
                    player.hurt(DamageSource.GENERIC, 1.0f);
                }

                // Broken Leg
                if (cap.isLegBroken()) {
                    player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 40, 3, false, false, false));
                }

                // Thirst
                if (cap.getWater() < 10.0f) {
                     player.addEffect(new EffectInstance(Effects.CONFUSION, 100, 0, false, false, false));
                     player.addEffect(new EffectInstance(Effects.WEAKNESS, 100, 1, false, false, false));
                }

                // Sync to client
                // We sync periodically or on major change.
                if (changed || player.tickCount % 200 == 0) {
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
                    cap.addStrength(0.05f);

                    // Bonus Damage logic could go here if using attributes
                } else {
                    event.setCanceled(true);
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
                        cap.damageMuscles(damage * 2);
                        cap.addEndurance(0.02f);

                        // Break Leg Chance
                        if (damage > 5.0f && Math.random() < 0.5) {
                            cap.setLegBroken(true);
                            player.sendMessage(new net.minecraft.util.text.StringTextComponent("§cYou broke your leg!"), player.getUUID());
                        }
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
                     cap.damageHeart(event.getAmount());

                     // Bleeding Chance
                     DamageSource src = event.getSource();
                     if (src == DamageSource.CACTUS || src == DamageSource.SWEET_BERRY_BUSH || (src.getEntity() != null)) {
                         if (Math.random() < 0.3) {
                             cap.setBleeding(true);
                             player.sendMessage(new net.minecraft.util.text.StringTextComponent("§cYou are bleeding!"), player.getUUID());
                         }
                     }

                     PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                         new SyncBodyStatsPacket(cap.serializeNBT()));
                 });
             }
        }
    }
}
