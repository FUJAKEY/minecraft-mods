package com.pipimod.client;

import com.pipimod.bladder.BladderProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "pipimod", value = Dist.CLIENT)
public class BladderOverlay {
    @SubscribeEvent
    public static void onRender(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getInstance();
        mc.player.getCapability(BladderProvider.BLADDER_CAPABILITY).ifPresent(b -> {
            int percent = b.getPercent();
            MatrixStack stack = event.getMatrixStack();
            int width = event.getWindow().getGuiScaledWidth();
            int barWidth = 100;
            int filled = barWidth * percent / 100;
            int x = (width - barWidth) / 2;
            int y = event.getWindow().getGuiScaledHeight() - 50;
            AbstractGui.fill(stack, x, y, x + barWidth, y + 5, 0xFF555555);
            AbstractGui.fill(stack, x, y, x + filled, y + 5, 0xFFFFFF00);
        });
    }
}
