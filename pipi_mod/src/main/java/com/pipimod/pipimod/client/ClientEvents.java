package com.pipimod.pipimod.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.pipimod.pipimod.network.PipiNetwork;
import com.pipimod.pipimod.network.TogglePeePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {
    private static boolean lastKeyState = false;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ClientSetup.keyBindingHolder == null) {
            return;
        }
        boolean current = ClientSetup.keyBindingHolder.peeKey().isDown();
        if (current != lastKeyState) {
            lastKeyState = current;
            PipiNetwork.CHANNEL.sendToServer(new TogglePeePacket(current));
        }
        // Emergency stop if key released due to window focus changes
        if (event.getAction() == GLFW.GLFW_RELEASE && event.getKey() == ClientSetup.keyBindingHolder.peeKey().getKey().getValue()) {
            lastKeyState = false;
            PipiNetwork.CHANNEL.sendToServer(new TogglePeePacket(false));
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        MatrixStack stack = event.getMatrixStack();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int barWidth = 120;
        int barHeight = 12;
        int x = screenWidth / 2 - barWidth / 2;
        int y = screenHeight - 55;

        float ratio = ClientBladderData.getFillRatio();
        int filled = (int) (barWidth * Math.min(1f, ratio));

        int baseColor = 0xAA0A0A0A;
        int fillColor = 0xFFE5C200;
        int overflowColor = 0xFFFF6B3B;
        AbstractGui.fill(stack, x, y, x + barWidth, y + barHeight, baseColor);
        AbstractGui.fill(stack, x + 1, y + 1, x + 1 + filled - 2, y + barHeight - 1, ratio > 1.01f ? overflowColor : fillColor);

        String label = String.format("Мочевой пузырь: %.0f%%", ratio * 100f);
        String stageLabel;
        switch (ClientBladderData.getDiscomfortStage()) {
            case 1:
                stageLabel = "Легкая тяга";
                break;
            case 2:
                stageLabel = "Сильное давление";
                break;
            case 3:
                stageLabel = "Почти авария";
                break;
            case 4:
                stageLabel = "Потеря контроля";
                break;
            default:
                stageLabel = ClientBladderData.isUrinating() ? "Облегчение" : "Комфорт";
        }
        mc.font.drawShadow(stack, label, x, y - 10, 0xF6EEC7);
        mc.font.drawShadow(stack, stageLabel, x, y + barHeight + 2, 0xFFE57F);

        if (ClientBladderData.isUrinating()) {
            mc.font.drawShadow(stack, "Писаем...", x + barWidth + 6, y, 0xFFB347);
        }
    }
}
