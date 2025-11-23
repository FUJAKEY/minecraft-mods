package com.jules.reallife.block;

import com.jules.reallife.capability.BodyCapabilityProvider;
import com.jules.reallife.network.PacketHandler;
import com.jules.reallife.network.SyncBodyStatsPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class BenchPressBlock extends Block {
    public BenchPressBlock() {
        super(Block.Properties.of(Material.METAL).strength(2.0f));
        setRegistryName("bench_press");
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide && handIn == Hand.MAIN_HAND) {
            player.getCapability(BodyCapabilityProvider.BODY_CAPABILITY).ifPresent(cap -> {
                if (cap.getStamina() > 20) {
                    cap.consumeStamina(20.0f);
                    cap.addStrength(0.5f); // Huge strength gain
                    cap.damageMuscles(2.0f);

                    player.sendMessage(new StringTextComponent("You lift the weights! Strength +0.5"), player.getUUID());

                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new SyncBodyStatsPacket(cap.serializeNBT()));
                } else {
                    player.sendMessage(new StringTextComponent("Too tired..."), player.getUUID());
                }
            });
        }
        return ActionResultType.SUCCESS;
    }
}
