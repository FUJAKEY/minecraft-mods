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
    private final ModelRenderer hub;
    private final ModelRenderer arm;
    
    public WindTurbineRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        // Single thin blade rendered three times with rotation
        this.blade = new ModelRenderer(64, 64, 0, 0);
        // Extend blade length to 20px
        this.blade.addBox(-0.5F, -10.0F, 0.0F, 1, 20, 2, 0.0F);

        // Central hub that rotates with the blades
        this.hub = new ModelRenderer(64, 64, 0, 22);
        this.hub.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);

        // Static arm connecting the hub to the tower
        this.arm = new ModelRenderer(64, 64, 8, 22);
        this.arm.addBox(-0.5F, -0.5F, -3.0F, 1, 1, 3, 0.0F);
    }

    @Override
    public void render(WindTurbineTileEntity tile, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
        ms.pushPose();
        Direction facing = tile.getBlockState().getValue(WindTurbineBlock.FACING);
        float angle = tile.getRotation();

        // Position rotor in front of the top section
        ms.translate(0.5D, 2.9375D, 0.5D);
        ms.mulPose(Vector3f.YP.rotationDegrees(facing.toYRot()));
        ms.translate(0.0D, 0.0D, -0.5D);

        IVertexBuilder vb = buffer.getBuffer(RenderType.entityCutout(TEXTURE));

        // Render static arm that connects rotor to tower
        arm.render(ms, vb, light, OverlayTexture.NO_OVERLAY);

        // Rotate blades around the facing axis
        ms.mulPose(Vector3f.ZP.rotationDegrees(angle));

        // Render hub
        hub.render(ms, vb, light, OverlayTexture.NO_OVERLAY);

        for (int i = 0; i < 3; i++) {
            ms.pushPose();
            ms.mulPose(Vector3f.ZP.rotationDegrees(i * 120f));
            blade.render(ms, vb, light, OverlayTexture.NO_OVERLAY);
            ms.popPose();
        }
        ms.popPose();
    }
}
