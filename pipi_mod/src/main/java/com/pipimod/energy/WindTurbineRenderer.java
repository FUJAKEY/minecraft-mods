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
import net.minecraft.util.Direction;

public class WindTurbineRenderer extends TileEntityRenderer<WindTurbineTileEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(EnergyMod.MODID, "textures/block/wind_turbine_turbne.png");
    private final ModelRenderer blade;
    
    public WindTurbineRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        // Single thin blade rendered three times with rotation
        this.blade = new ModelRenderer(64, 64, 0, 0);
        this.blade.addBox(-0.5F, -8.0F, 0.0F, 1, 16, 2, 0.0F);
    }

    @Override
    public void render(WindTurbineTileEntity tile, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
        ms.pushPose();
        Direction facing = tile.getBlockState().getValue(WindTurbineBlock.FACING);
        float angle = tile.getRotation();

        // Position rotor in front of the top section
        ms.translate(0.5D, 2.9375D, 0.5D);
        ms.mulPose(Vector3f.YP.rotationDegrees(facing.toYRot()));
        ms.translate(0.0D, 0.0D, -0.75D);

        // Rotate blades around the facing axis
        ms.mulPose(Vector3f.ZP.rotationDegrees(angle));
        IVertexBuilder vb = buffer.getBuffer(RenderType.entityCutout(TEXTURE));

        for (int i = 0; i < 3; i++) {
            ms.pushPose();
            ms.mulPose(Vector3f.ZP.rotationDegrees(i * 120f));
            blade.render(ms, vb, light, OverlayTexture.NO_OVERLAY);
            ms.popPose();
        }
        ms.popPose();
    }
}
