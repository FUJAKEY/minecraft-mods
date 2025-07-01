package com.pipimod.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.util.Direction;
import com.pipimod.energy.GeneratorTileEntity;


@Mod.EventBusSubscriber(modid = EnergyMod.MODID, value = Dist.CLIENT)
public class EnergyHudOverlay {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.hitResult instanceof BlockRayTraceResult) {
            BlockRayTraceResult ray = (BlockRayTraceResult) mc.hitResult;
            FontRenderer font = mc.font;
            MatrixStack stack = event.getMatrixStack();
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            net.minecraft.tileentity.TileEntity tile = mc.level.getBlockEntity(ray.getBlockPos());
            if (tile instanceof EnergyInfoProvider && tile instanceof net.minecraftforge.energy.IEnergyStorage) {
                net.minecraftforge.energy.IEnergyStorage cap = (net.minecraftforge.energy.IEnergyStorage) tile;
                EnergyInfoProvider info = (EnergyInfoProvider) tile;
                String text = new net.minecraft.util.text.TranslationTextComponent("message.energymod.energy", cap.getEnergyStored(), cap.getMaxEnergyStored()).getString();
                int y = height / 2 - 10;
                font.draw(stack, text, width / 2f - font.width(text) / 2f, y, 0xFFFFFF);
                y += 12;
                if (tile instanceof GeneratorTileEntity) {
                    GeneratorTileEntity gen = (GeneratorTileEntity) tile;
                    String eff = new net.minecraft.util.text.TranslationTextComponent("message.energymod.efficiency", gen.getEfficiency()).getString();
                    font.draw(stack, eff, width / 2f - font.width(eff) / 2f, y, 0xFFFFFF);
                    y += 12;
                    if (info.getLastGeneration() > 0) {
                        String g = new net.minecraft.util.text.TranslationTextComponent("message.energymod.gen", info.getLastGeneration()).getString();
                        font.draw(stack, g, width / 2f - font.width(g) / 2f, y, 0xFFFFFF);
                    }
                } else {
                    if (info.getLastInput() > 0) {
                        String in = new net.minecraft.util.text.TranslationTextComponent("message.energymod.input", info.getLastInput()).getString();
                        font.draw(stack, in, width / 2f - font.width(in) / 2f, y, 0xFFFFFF);
                        y += 12;
                    }
                    if (info.getLastOutput() > 0) {
                        String out = new net.minecraft.util.text.TranslationTextComponent("message.energymod.output", info.getLastOutput()).getString();
                        font.draw(stack, out, width / 2f - font.width(out) / 2f, y, 0xFFFFFF);
                    }
                }
            } else if (mc.player != null && (mc.player.getMainHandItem().getItem() instanceof WireToolItem || mc.player.getOffhandItem().getItem() instanceof WireToolItem) && mc.level.getBlockEntity(ray.getBlockPos()) instanceof WireBlockEntity) {
            } else if (mc.player != null && (mc.player.getMainHandItem().getItem() instanceof WireToolItem || mc.player.getOffhandItem().getItem() instanceof WireToolItem) && mc.level.getBlockEntity(ray.getBlockPos()) instanceof WireBlockEntity) {
                WireBlockEntity wire = (WireBlockEntity) mc.level.getBlockEntity(ray.getBlockPos());
                WireMode mode = wire.getMode(ray.getDirection());
                String modeStr = new net.minecraft.util.text.TranslationTextComponent("wiremode." + mode.name().toLowerCase()).getString();
                String text = new net.minecraft.util.text.TranslationTextComponent("message.energymod.wireinfo", modeStr, wire.getDisplayEnergy(), wire.getMaxEnergyStored()).getString();
                font.draw(stack, text, width / 2f - font.width(text) / 2f, height / 2f, 0xFFFFFF);
            }
        }
    }
}
