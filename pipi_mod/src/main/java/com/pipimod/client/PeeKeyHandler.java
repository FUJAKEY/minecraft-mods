package com.pipimod.client;

import org.lwjgl.glfw.GLFW;
import com.pipimod.network.NetworkHandler;
import com.pipimod.network.PeePacket;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "pipimod", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PeeKeyHandler {
    public static KeyBinding PEE_KEY;

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        PEE_KEY = new KeyBinding("key.pipimod.pee", GLFW.GLFW_KEY_P, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(PEE_KEY);
    }

    @Mod.EventBusSubscriber(modid = "pipimod", value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onKey(InputEvent.KeyInputEvent event) {
            if (PEE_KEY != null && PEE_KEY.isDown()) {
                NetworkHandler.CHANNEL.sendToServer(new PeePacket());
            }
        }
    }
}
