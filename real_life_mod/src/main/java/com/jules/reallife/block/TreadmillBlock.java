package com.jules.reallife.block;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.network.PacketHandler;
import com.jules.reallife.network.SyncBodyStatsPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class TreadmillBlock extends Block {
    public TreadmillBlock() {
        super(Block.Properties.of(Material.METAL).strength(2.0f));
        setRegistryName("treadmill");
    }

    @Override
    public void stepOn(World worldIn, BlockPos pos, BlockState state, Entity entityIn) {
        super.stepOn(worldIn, pos, state, entityIn);

        if (!worldIn.isClientSide && entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            // Only train if sprinting or moving fast?
            if (Math.abs(player.getDeltaMovement().x) > 0.1 || Math.abs(player.getDeltaMovement().z) > 0.1) {
                player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                    cap.addLungCapacity(0.05f); // Fast lung training
                    cap.consumeStamina(0.5f);

                    if (player.tickCount % 20 == 0) {
                         PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
                    }
                });
            }
        }
    }
}
