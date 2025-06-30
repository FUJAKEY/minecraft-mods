package com.pipimod.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MetalFillerScreen extends ContainerScreen<MetalFillerContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/hopper.png");

    public MetalFillerScreen(MetalFillerContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
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
        blit(stack, x + 8, y + 36 - carbon, 176, 0, 14, carbon);

        int energy = Math.min(14, this.menu.getEnergy() * 14 / 10000);
        blit(stack, x + 154, y + 36 - energy, 176, 0, 14, energy);
    }
}
