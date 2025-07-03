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

            if (mc.level.getBlockEntity(ray.getBlockPos()) instanceof EnergyCellTileEntity) {
                EnergyCellTileEntity cell = (EnergyCellTileEntity) mc.level.getBlockEntity(ray.getBlockPos());
                String energy = new net.minecraft.util.text.TranslationTextComponent(
                        "message.energymod.energy", cell.getEnergyStored(), cell.getMaxEnergyStored()).getString();
                String io = new net.minecraft.util.text.TranslationTextComponent(
                        "message.energymod.io", cell.getLastInput(), cell.getLastOutput()).getString();
                font.draw(stack, energy, width / 2f - font.width(energy) / 2f, height / 2f - 10, 0xFFFFFF);
                font.draw(stack, io, width / 2f - font.width(io) / 2f, height / 2f + 2, 0xFFFFFF);
            } else if (mc.level.getBlockEntity(ray.getBlockPos()) instanceof GeneratorTileEntity) {
                GeneratorTileEntity gen = (GeneratorTileEntity) mc.level.getBlockEntity(ray.getBlockPos());
                String energy = new net.minecraft.util.text.TranslationTextComponent(
                        "message.energymod.energy", gen.getEnergyStored(), gen.getMaxEnergyStored()).getString();
                String eff = new net.minecraft.util.text.TranslationTextComponent(
                        "message.energymod.efficiency", gen.getEfficiency()).getString();
                String genRate = new net.minecraft.util.text.TranslationTextComponent(
                        "message.energymod.generation", gen.getGenerationPerTick()).getString();
                font.draw(stack, energy, width / 2f - font.width(energy) / 2f, height / 2f - 18, 0xFFFFFF);
                font.draw(stack, eff, width / 2f - font.width(eff) / 2f, height / 2f - 6, 0xFFFFFF);
                font.draw(stack, genRate, width / 2f - font.width(genRate) / 2f, height / 2f + 6, 0xFFFFFF);
            } else if (mc.player != null && (mc.player.getMainHandItem().getItem() instanceof WireToolItem || mc.player.getOffhandItem().getItem() instanceof WireToolItem) && mc.level.getBlockEntity(ray.getBlockPos()) instanceof WireBlockEntity) {
                WireBlockEntity wire = (WireBlockEntity) mc.level.getBlockEntity(ray.getBlockPos());
                WireMode mode = wire.getMode(ray.getDirection());
                String modeStr = new net.minecraft.util.text.TranslationTextComponent("wiremode." + mode.name().toLowerCase()).getString();
                String text = new net.minecraft.util.text.TranslationTextComponent("message.energymod.wireinfo", modeStr, wire.getDisplayEnergy(), wire.getMaxEnergyStored()).getString();
                font.draw(stack, text, width / 2f - font.width(text) / 2f, height / 2f, 0xFFFFFF);

                java.util.Set<WireBlockEntity> network = EnergyNetwork.getNetwork(wire);
                int total = 0;
                for (WireBlockEntity w : network) {
                    total += w.getEnergyStored();
                }
                int capacity = network.size() * wire.getMaxEnergyStored();
                String netText = new net.minecraft.util.text.TranslationTextComponent(
                        "message.energymod.networkinfo", total, capacity, network.size()).getString();
                font.draw(stack, netText, width / 2f - font.width(netText) / 2f, height / 2f + 12, 0xFFFFFF);
            }
        }
    }
}
