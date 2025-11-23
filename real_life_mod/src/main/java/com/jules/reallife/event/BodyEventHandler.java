package com.jules.reallife.event;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.network.PacketHandler;
import com.jules.reallife.network.SyncBodyStatsPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.LightType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;

@Mod.EventBusSubscriber(modid = "reallife")
public class BodyEventHandler {
    private static final Random RANDOM = new Random();

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
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        if (!player.level.isClientSide) {
            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                // Assign Genetics if new
                if (cap.getGenetics() == 0) {
                    int trait = RANDOM.nextInt(3) + 1; // 1, 2, or 3
                    cap.setGenetics(trait);
                    String traitName = trait == 1 ? "Athlete" : (trait == 2 ? "Runner" : "Brittle Bones");
                    player.sendMessage(new StringTextComponent("§aYou were born with trait: " + traitName), player.getUUID());
                }
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
            });
        }
    }

    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        PlayerEntity player = event.getPlayer();
        if (!player.level.isClientSide) {
             player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                 cap.setFatigue(0.0f);
                 cap.setSanity(Math.min(100, cap.getSanity() + 20.0f));
                 PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
             });
        }
    }

    @SubscribeEvent
    public static void onEat(LivingEntityUseItemEvent.Finish event) {
        if (!event.getEntity().level.isClientSide && event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            Item item = event.getItem().getItem();

            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                 if (item == Items.ROTTEN_FLESH || item == Items.CHICKEN) {
                     if (RANDOM.nextFloat() < 0.3f) {
                         cap.setFoodPoisoned(true);
                         player.sendMessage(new StringTextComponent("§cYou feel sick... (Food Poisoning)"), player.getUUID());
                     }
                 }
                 PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.END) {
            PlayerEntity player = event.player;
            if (player.level.isClientSide) return;

            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                boolean changed = false;

                // --- Genetics Modifiers ---
                float lungMod = 1.0f;
                float strengthMod = 1.0f;
                if (cap.getGenetics() == 1) strengthMod = 1.2f; // Athlete
                if (cap.getGenetics() == 2) lungMod = 1.5f; // Runner

                // --- Stamina & Training ---
                if (player.isSprinting()) {
                    cap.consumeStamina(0.5f / (cap.getEndurance() * lungMod));
                    cap.changeHygiene(-0.02f); // Sweating
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
                        float regen = 0.2f * (cap.getLungCapacity() / 100.0f) * lungMod;
                        if (cap.getCarbs() < 10) regen *= 0.5f;
                        if (cap.isSick()) regen *= 0.7f;
                        cap.restoreStamina(regen);
                        changed = true;
                    }
                }

                // --- Metabolism (Every 5 seconds) ---
                if (player.tickCount % 100 == 0) {
                    float biomeTemp = player.level.getBiome(player.blockPosition()).getTemperature(player.blockPosition());

                    // Thirst
                    float waterLoss = 0.1f; // Reduced from 0.2
                    if (biomeTemp > 1.2f) waterLoss *= 2.0f;
                    if (cap.isFoodPoisoned()) waterLoss *= 1.5f;
                    cap.consumeWater(waterLoss);

                    // Nutrients
                    cap.addCarbs(-0.1f);
                    cap.addProtein(-0.05f);

                    // Temperature
                    float targetTemp = 36.6f;
                    if (biomeTemp < 0.2) targetTemp = 35.0f;
                    if (biomeTemp > 1.2) targetTemp = 38.0f;
                    float currentTemp = cap.getTemperature();
                    if (Math.abs(currentTemp - targetTemp) > 0.1f) {
                        cap.setTemperature(currentTemp + (targetTemp - currentTemp) * 0.1f);
                    }

                    // Immunity & Sickness
                    if (cap.getTemperature() < 35.5f) cap.setImmunity(cap.getImmunity() - 1.0f);
                    else if (cap.getImmunity() < 100.0f) cap.setImmunity(cap.getImmunity() + 0.5f);

                    if (cap.getImmunity() < 20.0f && !cap.isSick()) {
                        if (RANDOM.nextFloat() < 0.1f) {
                            cap.setSick(true);
                            player.sendMessage(new StringTextComponent("§cYou caught a cold!"), player.getUUID());
                        }
                    }

                    // Hygiene
                    if (player.isInWater()) cap.setHygiene(100.0f);
                    else cap.changeHygiene(-0.01f); // Slow natural decay

                    // Fatigue
                    cap.changeFatigue(0.02f); // Reduced from 0.05

                    // Sanity
                    int light = player.level.getBrightness(LightType.BLOCK, player.blockPosition());
                    if (light < 4) cap.changeSanity(-0.1f);
                    else if (light > 10) cap.changeSanity(0.1f);

                    changed = true;
                }

                // --- Effects & Sounds ---

                // Sickness Cough
                if (cap.isSick() && player.tickCount % 300 == 0) {
                    ((ServerPlayerEntity)player).connection.send(new SPlaySoundEffectPacket(
                        SoundEvents.PANDA_SNEEZE, SoundCategory.PLAYERS,
                        player.getX(), player.getY(), player.getZ(), 1.0f, 1.0f));
                }

                // Sanity Hallucinations
                if (cap.getSanity() < 30.0f && player.tickCount % 400 == 0) {
                    if (RANDOM.nextBoolean()) {
                        ((ServerPlayerEntity)player).connection.send(new SPlaySoundEffectPacket(
                            SoundEvents.TNT_PRIMED, SoundCategory.AMBIENT,
                            player.getX(), player.getY(), player.getZ(), 1.0f, 1.0f));
                    } else {
                        ((ServerPlayerEntity)player).connection.send(new SPlaySoundEffectPacket(
                            SoundEvents.ZOMBIE_AMBIENT, SoundCategory.AMBIENT,
                            player.getX(), player.getY(), player.getZ(), 1.0f, 1.0f));
                    }
                }

                // Fatigue
                if (cap.getFatigue() > 90.0f && player.tickCount % 200 == 0) {
                    player.addEffect(new EffectInstance(Effects.BLINDNESS, 40, 0, false, false, false));
                    player.sendMessage(new StringTextComponent("§7You are exhausted..."), player.getUUID());
                }

                // Bleeding
                if (cap.isBleeding() && player.tickCount % 80 == 0) {
                    player.hurt(DamageSource.GENERIC, 1.0f);
                }

                // Food Poisoning
                if (cap.isFoodPoisoned()) {
                    if (player.tickCount % 100 == 0) player.getFoodData().setFoodLevel(Math.max(0, player.getFoodData().getFoodLevel() - 1));
                    if (player.tickCount % 6000 == 0) cap.setFoodPoisoned(false); // Cures after 5 mins
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

                    float strengthGain = 0.05f;
                    if (cap.getGenetics() == 1) strengthGain *= 1.2f; // Athlete
                    cap.addStrength(strengthGain);

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

                        float breakChance = 0.5f;
                        if (cap.getGenetics() == 3) breakChance = 0.8f; // Brittle bones

                        if (damage > 5.0f && RANDOM.nextFloat() < breakChance) {
                            cap.setLegBroken(true);
                            player.sendMessage(new StringTextComponent("§cYou broke your leg!"), player.getUUID());
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

                     DamageSource src = event.getSource();
                     if (src == DamageSource.CACTUS || src == DamageSource.SWEET_BERRY_BUSH || (src.getEntity() != null)) {
                         if (RANDOM.nextFloat() < 0.3) {
                             cap.setBleeding(true);
                             player.sendMessage(new StringTextComponent("§cYou are bleeding!"), player.getUUID());
                         }
                     }

                     PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                         new SyncBodyStatsPacket(cap.serializeNBT()));
                 });
             }
        }
    }
}
