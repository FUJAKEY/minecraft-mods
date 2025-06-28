package com.pipimod.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "pipimod")
public class TemporaryBlockManager {
    private static final Map<ServerWorld, Map<BlockPos, Integer>> BLOCKS = new HashMap<>();

    public static void place(ServerWorld world, BlockPos pos, BlockState state, int ticks) {
        world.setBlock(pos, state, 3);
        BLOCKS.computeIfAbsent(world, w -> new HashMap<>()).put(pos, ticks);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        for (Iterator<Map.Entry<ServerWorld, Map<BlockPos, Integer>>> it = BLOCKS.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<ServerWorld, Map<BlockPos, Integer>> entry = it.next();
            ServerWorld world = entry.getKey();
            Map<BlockPos, Integer> map = entry.getValue();
            Iterator<Map.Entry<BlockPos, Integer>> posIt = map.entrySet().iterator();
            while (posIt.hasNext()) {
                Map.Entry<BlockPos, Integer> e = posIt.next();
                int remaining = e.getValue() - 1;
                if (remaining <= 0) {
                    world.setBlockAndUpdate(e.getKey(), Blocks.AIR.defaultBlockState());
                    posIt.remove();
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
