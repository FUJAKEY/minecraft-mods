package com.example.industrialsynergy.blocks.machines;

import com.example.industrialsynergy.IndustrialSynergy;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import com.mojang.blaze3d.matrix.MatrixStack;

/**
 * Простой экран: отображает энергию и прогресс.
 */
public class CrusherScreen extends ContainerScreen<CrusherContainer> {
    private static final ResourceLocation GUI = new ResourceLocation(IndustrialSynergy.MODID, "textures/gui/crusher.png");

    public CrusherScreen(CrusherContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().bindTexture(GUI);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
        int progressWidth = (int) (24 * ((float)container.getProgress() / container.getMaxProgress()));
        blit(matrixStack, guiLeft + 79, guiTop + 34, 176, 0, progressWidth, 16);
    }
}
