package com.pipimod.network;

import com.pipimod.bladder.BladderProvider;
import com.pipimod.util.TemporaryItemManager;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/** Network packet informing the server that the player is peeing. */
public class PeePacket {
    public PeePacket() {}

    public PeePacket(PacketBuffer buf) {}

    public void toBytes(PacketBuffer buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) {
                return;
            }

            player.getCapability(BladderProvider.BLADDER_CAPABILITY).ifPresent(b -> {
                if (b.getLevel() > 0) {
                    b.drain(2);
                    ServerWorld world = player.getLevel();
                    Vector3d start = new Vector3d(player.getX(), player.getEyeY(), player.getZ());
                    Vector3d velocity = player.getLookAngle().scale(0.4);
                    TemporaryItemManager.spawn(world, start, new ItemStack(Blocks.YELLOW_CONCRETE), velocity, 60);
                    world.playSound(null, player.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundCategory.PLAYERS, 0.5F, 1.0F);
                } else {
                    player.sendMessage(new TranslationTextComponent("message.bladder.empty"), player.getUUID());
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
