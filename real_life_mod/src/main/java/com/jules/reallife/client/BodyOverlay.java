package com.jules.reallife.client;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
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
                int x = 10;
                int y = height / 2 - 50;

                // Helper to draw text with shadow
                mc.font.drawShadow(ms, new net.minecraft.util.text.TranslationTextComponent("reallife.overlay.title").getString(), x, y - 10, Color.WHITE.getRGB());

                mc.font.drawShadow(ms, new net.minecraft.util.text.TranslationTextComponent("reallife.overlay.strength", String.format("%.1f", cap.getStrength())).getString(), x, y + 10, Color.RED.getRGB());
                mc.font.drawShadow(ms, new net.minecraft.util.text.TranslationTextComponent("reallife.overlay.endurance", String.format("%.1f", cap.getEndurance())).getString(), x, y + 20, Color.GREEN.getRGB());
                mc.font.drawShadow(ms, new net.minecraft.util.text.TranslationTextComponent("reallife.overlay.lungs", String.format("%.1f", cap.getLungCapacity())).getString(), x, y + 30, Color.BLUE.getRGB());

                // Stamina Bar
                mc.font.drawShadow(ms, new net.minecraft.util.text.TranslationTextComponent("reallife.overlay.stamina", String.format("%.0f", cap.getStamina()), String.format("%.0f", cap.getMaxStamina())).getString(), x, y + 40, Color.YELLOW.getRGB());

                // Organ Health
                mc.font.drawShadow(ms, new net.minecraft.util.text.TranslationTextComponent("reallife.overlay.heart", String.format("%.0f", cap.getHeartHealth())).getString(), x, y + 60, 0x8B0000);
                mc.font.drawShadow(ms, new net.minecraft.util.text.TranslationTextComponent("reallife.overlay.muscles", String.format("%.0f", cap.getMuscleIntegrity())).getString(), x, y + 70, 0xFFA07A);
            });
        }
    }
}
