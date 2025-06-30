package com.pipimod.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class WindTurbineRenderer extends TileEntityRenderer<WindTurbineTileEntity> {
    private final ResourceLocation ROTOR_MODEL = new ResourceLocation(EnergyMod.MODID, "block/wind_turbine_rotor");

    public WindTurbineRenderer(TileEntityRendererDispatcher d) {
        super(d);
    }

    @Override
    public void render(WindTurbineTileEntity tile, float partialTicks, MatrixStack stack,
                        IRenderTypeBuffer buffer, int light, int overlay) {
        IVertexBuilder vb = buffer.getBuffer(RenderType.solid());
        IBakedModel rotor = Minecraft.getInstance().getModelManager().getModel(ROTOR_MODEL);
        float angle = tile.getRotation() + partialTicks * 9f;
        stack.pushPose();
        stack.translate(0.5, 2.0625, 0.5);
        stack.mulPose(Vector3f.YP.rotationDegrees(angle));
        stack.translate(-0.5, -2.0625, -0.5);
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(stack.last(), vb, null, rotor, 1,1,1, light, overlay);
        stack.popPose();
    }
}
