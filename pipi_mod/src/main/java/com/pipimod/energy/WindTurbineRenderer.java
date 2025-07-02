package com.pipimod.energy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class WindTurbineRenderer extends TileEntityRenderer<WindTurbineTileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnergyMod.MODID, "textures/block/wind_turbine_turbne.png");
    private final ModelRenderer blade1;
    private final ModelRenderer blade2;

    public WindTurbineRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.blade1 = new ModelRenderer(64, 64, 0, 0);
        this.blade1.addBox(-12.0F, -1.0F, -1.0F, 24, 2, 2, 0.0F);
        this.blade2 = new ModelRenderer(64, 64, 0, 4);
        this.blade2.addBox(-1.0F, -1.0F, -12.0F, 2, 2, 24, 0.0F);
    }

    @Override
    public void render(WindTurbineTileEntity tile, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
        ms.pushPose();
        ms.translate(0.5D, 3.0D, 0.5D);
        float angle = tile.getRotation();
        ms.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
        ms.mulPose(Vector3f.XP.rotationDegrees(angle));
        IVertexBuilder vb = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
        blade1.render(ms, vb, light, OverlayTexture.NO_OVERLAY);
        blade2.render(ms, vb, light, OverlayTexture.NO_OVERLAY);
        ms.popPose();
    }
}
