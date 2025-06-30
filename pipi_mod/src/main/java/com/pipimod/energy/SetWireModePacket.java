package com.pipimod.energy;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SetWireModePacket {
    private final BlockPos pos;
    private final Direction side;
    private final WireMode mode;

    public SetWireModePacket(BlockPos pos, Direction side, WireMode mode) {
        this.pos = pos;
        this.side = side;
        this.mode = mode;
    }

    public static void encode(SetWireModePacket msg, PacketBuffer buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeEnum(msg.side);
        buf.writeEnum(msg.mode);
    }

    public static SetWireModePacket decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        Direction side = buf.readEnum(Direction.class);
        WireMode mode = buf.readEnum(WireMode.class);
        return new SetWireModePacket(pos, side, mode);
    }

    public static void handle(SetWireModePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) return;
            if (ctx.get().getSender().level.getBlockEntity(msg.pos) instanceof WireBlockEntity) {
                WireBlockEntity wire = (WireBlockEntity) ctx.get().getSender().level.getBlockEntity(msg.pos);
                wire.setMode(msg.side, msg.mode);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
