package com.jules.reallife.client;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.Color;

@Mod.EventBusSubscriber(modid = "reallife", value = Dist.CLIENT)
public class BodyOverlay extends AbstractGui {

    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            if (player == null) return;

            MatrixStack ms = event.getMatrixStack();

            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                int width = mc.getWindow().getGuiScaledWidth();
                int height = mc.getWindow().getGuiScaledHeight();

                // Use a cleaner layout. Left-middle screen.
                int startX = 10;
                int startY = height / 2 - 80;
                int barWidth = 60;
                int barHeight = 4;
                int gap = 12;

                // Background box
                fill(ms, startX - 5, startY - 5, startX + barWidth + 5, startY + (gap * 9) + 5, 0x80000000);

                // Title
                mc.font.drawShadow(ms, "Body Status", startX, startY - 15, 0xFFFFFF);

                int currentY = startY;

                // --- PHYSICAL ---
                drawStat(ms, mc, startX, currentY, "Str", cap.getStrength(), 100.0f, 0xFFFF5555);
                currentY += gap;
                drawStat(ms, mc, startX, currentY, "End", cap.getEndurance(), 100.0f, 0xFF55FF55);
                currentY += gap;
                drawStat(ms, mc, startX, currentY, "Lung", cap.getLungCapacity(), 100.0f, 0xFF5555FF);
                currentY += gap;
                drawStat(ms, mc, startX, currentY, "Stam", cap.getStamina(), cap.getMaxStamina(), 0xFFFFFF55);
                currentY += gap;

                // --- NEEDS ---
                drawStat(ms, mc, startX, currentY, "H2O", cap.getWater(), 100.0f, 0xFF00BFFF);
                currentY += gap;

                // Temperature is special
                String tempText = String.format("Temp: %.1f°C", cap.getTemperature());
                int tempColor = cap.getTemperature() < 10 ? 0xFF00FFFF : (cap.getTemperature() > 30 ? 0xFFFF4500 : 0xFFFFA500);
                mc.font.drawShadow(ms, tempText, startX, currentY, tempColor);
                currentY += gap;

                // --- HARDCORE ---
                drawStat(ms, mc, startX, currentY, "Sanity", cap.getSanity(), 100.0f, 0xFFAA00AA);
                currentY += gap;
                drawStat(ms, mc, startX, currentY, "Fatigue", cap.getFatigue(), 100.0f, 0xFF888888);
                currentY += gap;
                drawStat(ms, mc, startX, currentY, "Hyg", cap.getHygiene(), 100.0f, 0xFFE0FFFF);
                currentY += gap;

                // --- ALERTS ---
                int alertX = startX + barWidth + 10;
                int alertY = startY;

                if (cap.isBleeding()) {
                    mc.font.drawShadow(ms, "BLEEDING!", alertX, alertY, 0xFFFF0000);
                    alertY += 10;
                }
                if (cap.isLegBroken()) {
                    mc.font.drawShadow(ms, "BROKEN LEG!", alertX, alertY, 0xFFFF4500);
                    alertY += 10;
                }
                if (cap.isSick()) {
                    mc.font.drawShadow(ms, "SICK!", alertX, alertY, 0xFF32CD32);
                    alertY += 10;
                }
                if (cap.isFoodPoisoned()) {
                    mc.font.drawShadow(ms, "POISONED!", alertX, alertY, 0xFF006400);
                    alertY += 10;
                }
                if (cap.getSanity() < 20) {
                     mc.font.drawShadow(ms, "INSANE", alertX, alertY, 0xFF800080);
                     alertY += 10;
                }
                if (cap.getWater() < 10) {
                     mc.font.drawShadow(ms, "THIRSTY!", alertX, alertY, 0xFF0000FF);
                     alertY += 10;
                }
            });
        }
    }

    private static void drawStat(MatrixStack ms, Minecraft mc, int x, int y, String label, double current, double max, int color) {
        // Draw label
        mc.font.draw(ms, label, x, y, color);

        // Draw bar
        int barX = x + 35;
        int barY = y + 2;
        int barWidth = 40;
        int barHeight = 4;

        float pct = (float)(current / max);
        if (pct > 1.0f) pct = 1.0f;
        if (pct < 0.0f) pct = 0.0f;

        int filledWidth = (int)(barWidth * pct);

        fill(ms, barX, barY, barX + barWidth, barY + barHeight, 0xFF404040); // Background
        fill(ms, barX, barY, barX + filledWidth, barY + barHeight, color);   // Foreground
    }
}
