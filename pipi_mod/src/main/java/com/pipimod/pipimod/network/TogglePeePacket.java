package com.pipimod.pipimod.network;

import com.pipimod.pipimod.capability.BladderCapability;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TogglePeePacket {
    private final boolean urinating;

    public TogglePeePacket(boolean urinating) {
        this.urinating = urinating;
    }

    public static void encode(TogglePeePacket packet, PacketBuffer buffer) {
        buffer.writeBoolean(packet.urinating);
    }

    public static TogglePeePacket decode(PacketBuffer buffer) {
        return new TogglePeePacket(buffer.readBoolean());
    }

    public static void handle(TogglePeePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (!(ctx.get().getSender() instanceof ServerPlayerEntity)) {
                return;
            }
            ServerPlayerEntity player = ctx.get().getSender();
            player.getCapability(BladderCapability.BLADDER).ifPresent(cap -> {
                if (packet.urinating && cap.getLevel() <= 0.1f) {
                    cap.setUrinating(false);
                } else {
                    cap.setUrinating(packet.urinating);
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
