package com.pipimod.client;

import com.pipimod.bladder.BladderProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pipimod", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BladderOverlay {
    @SubscribeEvent
    public static void onRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getInstance();
        mc.player.getCapability(BladderProvider.BLADDER_CAPABILITY).ifPresent(b -> {
            int percent = b.getPercent();
            MatrixStack stack = event.getMatrixStack();
            int width = event.getWindow().getGuiScaledWidth();
            int barWidth = 182;
            int filled = barWidth * percent / 100;
            int x = width / 2 - 91;
            int y = event.getWindow().getGuiScaledHeight() - 49;
            AbstractGui.fill(stack, x, y, x + barWidth, y + 5, 0x90000000);
            if (filled > 0) {
                AbstractGui.fill(stack, x, y, x + filled, y + 5, 0xFFFFD800);
            }
        });
    }
}
