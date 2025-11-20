package com.pipimod.pipimod.network;

import com.pipimod.pipimod.PipiMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PipiNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PipiMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetIndex = 0;

    public static void registerMessages() {
        CHANNEL.registerMessage(packetIndex++, TogglePeePacket.class, TogglePeePacket::encode, TogglePeePacket::decode, TogglePeePacket::handle);
        CHANNEL.registerMessage(packetIndex++, BladderSyncPacket.class, BladderSyncPacket::encode, BladderSyncPacket::decode, BladderSyncPacket::handle);
    }
}
