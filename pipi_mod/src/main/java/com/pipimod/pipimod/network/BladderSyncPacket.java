package com.pipimod.pipimod.network;

import com.pipimod.pipimod.client.ClientBladderData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BladderSyncPacket {
    private final float level;
    private final float capacity;
    private final boolean urinating;
    private final int discomfortStage;

    public BladderSyncPacket(float level, float capacity, boolean urinating, int discomfortStage) {
        this.level = level;
        this.capacity = capacity;
        this.urinating = urinating;
        this.discomfortStage = discomfortStage;
    }

    public static void encode(BladderSyncPacket packet, PacketBuffer buffer) {
        buffer.writeFloat(packet.level);
        buffer.writeFloat(packet.capacity);
        buffer.writeBoolean(packet.urinating);
        buffer.writeInt(packet.discomfortStage);
    }

    public static BladderSyncPacket decode(PacketBuffer buffer) {
        return new BladderSyncPacket(buffer.readFloat(), buffer.readFloat(), buffer.readBoolean(), buffer.readInt());
    }

    public static void handle(BladderSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            ctx.get().enqueueWork(() -> ClientBladderData.update(packet.level, packet.capacity, packet.urinating, packet.discomfortStage));
        }
        ctx.get().setPacketHandled(true);
    }
}
