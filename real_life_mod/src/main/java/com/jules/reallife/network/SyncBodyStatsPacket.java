package com.jules.reallife.network;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.capability.IBodyStats;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncBodyStatsPacket {
    private final CompoundNBT data;

    public SyncBodyStatsPacket(CompoundNBT data) {
        this.data = data;
    }

    public static void encode(SyncBodyStatsPacket msg, PacketBuffer buf) {
        buf.writeNbt(msg.data);
    }

    public static SyncBodyStatsPacket decode(PacketBuffer buf) {
        return new SyncBodyStatsPacket(buf.readNbt());
    }

    public static void handle(SyncBodyStatsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Use DistExecutor to safely run client-side code
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                handleClient(msg);
                return null;
            });
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(SyncBodyStatsPacket msg) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                cap.deserializeNBT(msg.data);
            });
        }
    }
}
