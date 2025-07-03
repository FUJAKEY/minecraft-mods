package com.pipimod.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

import java.util.*;

/**
 * Utility class to build and cache wire networks per tick.
 */
final class EnergyNetwork {
    private static final Map<WireBlockEntity, Set<WireBlockEntity>> cache = new HashMap<>();
    private static long cacheTick = -1;

    static Set<WireBlockEntity> getNetwork(WireBlockEntity start) {
        if (start.getLevel() == null) {
            return Collections.singleton(start);
        }
        long tick = start.getLevel().getGameTime();
        if (tick != cacheTick) {
            cache.clear();
            cacheTick = tick;
        }
        return cache.computeIfAbsent(start, w -> {
            Set<WireBlockEntity> set = new HashSet<>();
            Queue<WireBlockEntity> q = new ArrayDeque<>();
            q.add(w);
            while (!q.isEmpty()) {
                WireBlockEntity wb = q.poll();
                if (!set.add(wb)) continue;
                for (Direction d : Direction.values()) {
                    TileEntity te = wb.getLevel().getBlockEntity(wb.getBlockPos().relative(d));
                    if (te instanceof WireBlockEntity) {
                        q.add((WireBlockEntity) te);
                    }
                }
            }
            for (WireBlockEntity wb : set) {
                cache.put(wb, set);
            }
            return set;
        });
    }

    private EnergyNetwork() {}
}
