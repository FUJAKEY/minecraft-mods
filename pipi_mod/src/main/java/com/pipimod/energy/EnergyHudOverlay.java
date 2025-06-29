package com.pipimod.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnergyMod.MODID, value = Dist.CLIENT)
public class EnergyHudOverlay {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.hitResult instanceof BlockRayTraceResult) {
            BlockRayTraceResult ray = (BlockRayTraceResult) mc.hitResult;
            if (mc.level.getBlockEntity(ray.getBlockPos()) instanceof EnergyCellTileEntity) {
                EnergyCellTileEntity cell = (EnergyCellTileEntity) mc.level.getBlockEntity(ray.getBlockPos());
                String text = cell.getEnergyStored() + "/" + cell.getMaxEnergyStored() + " FE";
                FontRenderer font = mc.font;
                MatrixStack stack = event.getMatrixStack();
                int width = mc.getWindow().getGuiScaledWidth();
                int height = mc.getWindow().getGuiScaledHeight();
                font.draw(stack, text, width / 2f - font.width(text) / 2f, height / 2f, 0xFFFFFF);
            }
        }
    }
}
