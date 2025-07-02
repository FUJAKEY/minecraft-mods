package com.pipimod.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MetalFillerScreen extends ContainerScreen<MetalFillerContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnergyMod.MODID, "textures/gui/metal_filler_screen.png");

    public MetalFillerScreen(MetalFillerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
        super.renderLabels(stack, mouseX, mouseY);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        if (isHovering(8, 22, 14, 14, mouseX, mouseY) && this.menu.getCarbon() > 0) {
            this.renderTooltip(stack, new net.minecraft.util.text.TranslationTextComponent(
                    "tooltip.energymod.carbon", this.menu.getCarbon()), mouseX, mouseY);
        }
        if (isHovering(154, 22, 14, 14, mouseX, mouseY) && this.menu.getEnergy() > 0) {
            this.renderTooltip(stack, new net.minecraft.util.text.TranslationTextComponent(
                    "tooltip.energymod.energy", this.menu.getEnergy()), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        blit(stack, x, y, 0, 0, this.imageWidth, this.imageHeight);

        int k = this.menu.getProgress() * 24 / 100;
        blit(stack, x + 100, y + 34, 176, 14, k + 1, 16);

        int carbon = this.menu.getCarbon() * 14 / 500;
        if (carbon > 0) {
            blit(stack, x + 8, y + 36 - carbon, 176, 0, 14, carbon);
        }

        int energy = Math.min(14, this.menu.getEnergy() * 14 / 10000);
        if (energy > 0) {
            blit(stack, x + 154, y + 36 - energy, 176, 0, 14, energy);
        }
    }
}
