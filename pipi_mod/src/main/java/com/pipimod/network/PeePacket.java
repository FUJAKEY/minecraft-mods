package com.pipimod.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PeePacket {
    public PeePacket() {
    }

    public PeePacket(PacketBuffer buf) {
    }

    public void toBytes(PacketBuffer buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                player.getCapability(com.pipimod.bladder.BladderProvider.BLADDER_CAPABILITY).ifPresent(b -> b.empty());
                player.level.playSound(null, player.blockPosition(), net.minecraft.util.SoundEvents.EXPERIENCE_ORB_PICKUP, net.minecraft.util.SoundCategory.PLAYERS, 1.0F, 1.0F);
                // spawn simple particles
                for (int i = 0; i < 20; i++) {
                    player.level.addParticle(net.minecraft.particles.ParticleTypes.FALLING_WATER, player.getX(), player.getY() + 0.5, player.getZ(), player.getLookAngle().x * 0.5, 0.1, player.getLookAngle().z * 0.5);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
