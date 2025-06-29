package com.pipimod.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Manages short-lived item entities used to represent pee projectiles. Items
 * are spawned with a custom lifetime and automatically removed after the time
 * expires.
 */
@Mod.EventBusSubscriber(modid = "pipimod")
public class TemporaryItemManager {
    private static final Map<ServerWorld, Map<ItemEntity, Integer>> ITEMS = new HashMap<>();

    public static void spawn(ServerWorld world, Vector3d pos, ItemStack stack, Vector3d velocity, int ticks) {
        ItemEntity item = new ItemEntity(world, pos.x, pos.y, pos.z, stack);
        item.setPickUpDelay(32767); // practically unpickupable
        item.setDeltaMovement(velocity);
        world.addFreshEntity(item);
        ITEMS.computeIfAbsent(world, w -> new HashMap<>()).put(item, ticks);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        for (Iterator<Map.Entry<ServerWorld, Map<ItemEntity, Integer>>> it = ITEMS.entrySet().iterator(); it.hasNext();) {
            Map.Entry<ServerWorld, Map<ItemEntity, Integer>> entry = it.next();
            ServerWorld world = entry.getKey();
            Map<ItemEntity, Integer> map = entry.getValue();
            Iterator<Map.Entry<ItemEntity, Integer>> itemIt = map.entrySet().iterator();
            while (itemIt.hasNext()) {
                Map.Entry<ItemEntity, Integer> e = itemIt.next();
                int remaining = e.getValue() - 1;
                if (remaining <= 0 || !e.getKey().isAlive()) {
                    e.getKey().remove();
                    itemIt.remove();
                } else {
                    e.setValue(remaining);
                }
            }
            if (map.isEmpty()) {
                it.remove();
            }
        }
    }
}
