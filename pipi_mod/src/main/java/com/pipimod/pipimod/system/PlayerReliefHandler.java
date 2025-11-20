package com.pipimod.pipimod.system;

import com.pipimod.pipimod.capability.BladderCapability;
import com.pipimod.pipimod.capability.IBladderData;
import com.pipimod.pipimod.network.BladderSyncPacket;
import com.pipimod.pipimod.network.PipiNetwork;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class PlayerReliefHandler {
    private static final int PASSIVE_FILL_INTERVAL_TICKS = 20 * 30; // 30 секунд
    private static final float PASSIVE_FILL_PERCENT = 0.01f;
    private static final float ACTIVE_FILL_PER_TICK = 0.02f;
    private static final float UNDERWATER_FILL_PER_TICK = 0.005f;
    private static final float DRINK_GAIN_PERCENT = 0.15f;
    private static final float DRAIN_PER_TICK = 1.6f;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        PlayerEntity player = event.player;
        player.getCapability(BladderCapability.BLADDER).ifPresent(cap -> {
            boolean serverSide = !player.level.isClientSide;
            if (serverSide) {
                tickServer(player, cap);
            }
            applyStatusEffects(player, cap);
        });
    }

    @SubscribeEvent
    public static void onDrinkFinished(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntityLiving() instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (player.level.isClientSide) {
            return;
        }
        ItemStack stack = event.getItem();
        Item item = stack.getItem();
        boolean drankPotion = item == Items.POTION && isDrinkablePotion(stack);
        boolean drankMilk = item == Items.MILK_BUCKET;
        if (!drankPotion && !drankMilk) {
            return;
        }
        player.getCapability(BladderCapability.BLADDER).ifPresent(cap -> {
            cap.addLevel(cap.getCapacity() * DRINK_GAIN_PERCENT);
            cap.setTicksSinceSync(Math.max(cap.getTicksSinceSync(), 9));
        });
    }

    private static void tickServer(PlayerEntity player, IBladderData cap) {
        float ratio = cap.getLevel() / cap.getCapacity();
        boolean shouldForce = ratio > 1.05f;
        boolean canRest = player.isSleeping();
        if (!cap.isUrinating() && !canRest) {
            accumulatePassiveFill(cap);
            float gain = player.isSprinting() ? ACTIVE_FILL_PER_TICK : 0f;
            gain += player.isUnderWater() ? UNDERWATER_FILL_PER_TICK : 0f;
            if (gain > 0) {
                cap.addLevel(gain);
            }
        }
        if (shouldForce) {
            cap.setUrinating(true);
        }

        if (cap.isUrinating()) {
            cap.addLevel(-DRAIN_PER_TICK);
            float pressure = MathHelper.clamp(0.55f + ratio, 0.55f, 1.75f);
            PeePhysics.simulateSpray((ServerPlayerEntity) player, pressure);
            if (cap.getLevel() <= 0.05f || canRest) {
                cap.setUrinating(false);
            }
        }

        syncToClient(player, cap);
    }

    private static void accumulatePassiveFill(IBladderData cap) {
        int ticks = cap.getPassiveFillTicks() + 1;
        if (ticks >= PASSIVE_FILL_INTERVAL_TICKS) {
            int steps = ticks / PASSIVE_FILL_INTERVAL_TICKS;
            float gain = cap.getCapacity() * PASSIVE_FILL_PERCENT * steps;
            cap.addLevel(gain);
            ticks = ticks % PASSIVE_FILL_INTERVAL_TICKS;
        }
        cap.setPassiveFillTicks(ticks);
    }

    private static void syncToClient(PlayerEntity player, IBladderData cap) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }
        cap.setTicksSinceSync(cap.getTicksSinceSync() + 1);
        if (cap.getTicksSinceSync() >= 10) {
            cap.setTicksSinceSync(0);
            PipiNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                    new BladderSyncPacket(cap.getLevel(), cap.getCapacity(), cap.isUrinating(), cap.getDiscomfortStage()));
        }
    }

    private static void applyStatusEffects(PlayerEntity player, IBladderData cap) {
        int stage = cap.getDiscomfortStage();
        if (stage >= 1) {
            player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 40, 0, false, false));
        }
        if (stage >= 2) {
            player.addEffect(new EffectInstance(Effects.WEAKNESS, 40, 0, false, false));
        }
        if (stage >= 3) {
            player.addEffect(new EffectInstance(Effects.BLINDNESS, 20, 0, false, false));
        }
        if (stage >= 4) {
            player.addEffect(new EffectInstance(Effects.CONFUSION, 60, 0, false, false));
            cap.setUrinating(true);
        }
    }

    private static boolean isDrinkablePotion(ItemStack stack) {
        Potion potion = PotionUtils.getPotion(stack);
        return potion != Potions.EMPTY;
    }
}
