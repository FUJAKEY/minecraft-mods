package com.pipimod.pipimod.system;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class PeePhysics {
    private static final Random RANDOM = new Random();

    private PeePhysics() {
    }

    public static void simulateSpray(ServerPlayerEntity player, float pressure) {
        ServerWorld world = (ServerWorld) player.level;
        Vector3d start = player.getEyePosition(1f).subtract(0, 0.2, 0);
        Vector3d look = player.getLookAngle();
        Vector3d velocity = look.scale(0.8f * pressure).add(randomSpread(0.1f));

        boolean playedSound = false;
        for (int i = 0; i < 18; i++) {
            start = start.add(velocity);
            velocity = velocity.add(0, -0.06f, 0).scale(0.92f);

            spawnDroplet(world, start);
            if (handleCollision(world, player, start)) {
                if (!playedSound) {
                    world.playSound(null, start.x, start.y, start.z, SoundEvents.BUCKET_EMPTY, SoundCategory.PLAYERS, 0.25f, 1.2f);
                    playedSound = true;
                }
                break;
            }
        }
    }

    private static boolean handleCollision(ServerWorld world, ServerPlayerEntity player, Vector3d position) {
        BlockPos pos = new BlockPos(position);
        BlockState state = world.getBlockState(pos);
        if (!state.getMaterial().isReplaceable() && state.getMaterial().isSolid()) {
            dampenBlock(world, state, pos);
            influenceEntities(world, player, position);
            return true;
        }
        influenceEntities(world, player, position);
        return false;
    }

    private static void dampenBlock(ServerWorld world, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof FireBlock) {
            world.removeBlock(pos, false);
        }
        if (state.getBlock() instanceof CampfireBlock && state.hasProperty(CampfireBlock.LIT)) {
            world.setBlock(pos, state.setValue(CampfireBlock.LIT, Boolean.FALSE), 3);
        }
        if (state.getBlock() instanceof FarmlandBlock && state.hasProperty(FarmlandBlock.MOISTURE)) {
            world.setBlock(pos, state.setValue(FarmlandBlock.MOISTURE, 7), 2);
        }
        world.sendParticles(ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5, 4, 0.15, 0.1, 0.15, 0.01);
    }

    private static void influenceEntities(ServerWorld world, ServerPlayerEntity player, Vector3d position) {
        AxisAlignedBB area = new AxisAlignedBB(position.x - 0.5, position.y - 0.5, position.z - 0.5, position.x + 0.5, position.y + 0.5, position.z + 0.5);
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, area, e -> e != player);
        for (LivingEntity target : entities) {
            target.clearFire();
            target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 40, 1, false, true));
            if (RANDOM.nextFloat() < 0.15f) {
                target.hurt(DamageSource.indirectMagic(player, player), 0.5f);
            }
        }
    }

    private static void spawnDroplet(ServerWorld world, Vector3d position) {
        world.sendParticles(ParticleTypes.FALLING_WATER, position.x, position.y, position.z, 3, 0.01, 0.01, 0.01, 0.0);
    }

    private static Vector3d randomSpread(float strength) {
        double dx = (RANDOM.nextDouble() - 0.5) * strength;
        double dy = (RANDOM.nextDouble() - 0.5) * strength * 0.5;
        double dz = (RANDOM.nextDouble() - 0.5) * strength;
        return new Vector3d(dx, dy, dz);
    }
}
