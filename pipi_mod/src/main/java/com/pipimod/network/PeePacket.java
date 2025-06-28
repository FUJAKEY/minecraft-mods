package com.pipimod.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import com.pipimod.util.TemporaryBlockManager;
import com.pipimod.bladder.BladderProvider;

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
                player.getCapability(BladderProvider.BLADDER_CAPABILITY).ifPresent(b -> b.drain(5));
                ServerWorld world = player.getLevel();
                BlockPos pos = player.blockPosition().relative(player.getDirection());
                TemporaryBlockManager.place(world, pos, Blocks.YELLOW_CONCRETE.defaultBlockState(), 60);
                world.playSound(null, pos, net.minecraft.util.SoundEvents.BUCKET_EMPTY, net.minecraft.util.SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
