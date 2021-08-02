package net.minecraft.world.level.block;

import net.minecraft.advancements.CriterionTriggers;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.particles.ParticleParamBlock;
import net.minecraft.core.particles.Particles;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import net.minecraft.world.entity.vehicle.EntityBoat;
import net.minecraft.world.entity.vehicle.EntityMinecartAbstract;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.VoxelShapeCollision;

public class BlockHoney extends BlockHalfTransparent {

    private static final double SLIDE_STARTS_WHEN_VERTICAL_SPEED_IS_AT_LEAST = 0.13D;
    private static final double MIN_FALL_SPEED_TO_BE_CONSIDERED_SLIDING = 0.08D;
    private static final double THROTTLE_SLIDE_SPEED_TO = 0.05D;
    private static final int SLIDE_ADVANCEMENT_CHECK_INTERVAL = 20;
    protected static final VoxelShape SHAPE = Block.a(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public BlockHoney(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    private static boolean c(Entity entity) {
        return entity instanceof EntityLiving || entity instanceof EntityMinecartAbstract || entity instanceof EntityTNTPrimed || entity instanceof EntityBoat;
    }

    @Override
    public VoxelShape c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockHoney.SHAPE;
    }

    @Override
    public void fallOn(World world, IBlockData iblockdata, BlockPosition blockposition, Entity entity, float f) {
        entity.playSound(SoundEffects.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
        if (!world.isClientSide) {
            world.broadcastEntityEffect(entity, (byte) 54);
        }

        if (entity.a(f, 0.2F, DamageSource.FALL)) {
            entity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
        }

    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (this.a(blockposition, entity)) {
            this.a(entity, blockposition);
            this.d(entity);
            this.a(world, entity);
        }

        super.a(iblockdata, world, blockposition, entity);
    }

    private boolean a(BlockPosition blockposition, Entity entity) {
        if (entity.isOnGround()) {
            return false;
        } else if (entity.locY() > (double) blockposition.getY() + 0.9375D - 1.0E-7D) {
            return false;
        } else if (entity.getMot().y >= -0.08D) {
            return false;
        } else {
            double d0 = Math.abs((double) blockposition.getX() + 0.5D - entity.locX());
            double d1 = Math.abs((double) blockposition.getZ() + 0.5D - entity.locZ());
            double d2 = 0.4375D + (double) (entity.getWidth() / 2.0F);

            return d0 + 1.0E-7D > d2 || d1 + 1.0E-7D > d2;
        }
    }

    private void a(Entity entity, BlockPosition blockposition) {
        if (entity instanceof EntityPlayer && entity.level.getTime() % 20L == 0L) {
            CriterionTriggers.HONEY_BLOCK_SLIDE.a((EntityPlayer) entity, entity.level.getType(blockposition));
        }

    }

    private void d(Entity entity) {
        Vec3D vec3d = entity.getMot();

        if (vec3d.y < -0.13D) {
            double d0 = -0.05D / vec3d.y;

            entity.setMot(new Vec3D(vec3d.x * d0, -0.05D, vec3d.z * d0));
        } else {
            entity.setMot(new Vec3D(vec3d.x, -0.05D, vec3d.z));
        }

        entity.fallDistance = 0.0F;
    }

    private void a(World world, Entity entity) {
        if (c(entity)) {
            if (world.random.nextInt(5) == 0) {
                entity.playSound(SoundEffects.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
            }

            if (!world.isClientSide && world.random.nextInt(5) == 0) {
                world.broadcastEntityEffect(entity, (byte) 53);
            }
        }

    }

    public static void a(Entity entity) {
        a(entity, 5);
    }

    public static void b(Entity entity) {
        a(entity, 10);
    }

    private static void a(Entity entity, int i) {
        if (entity.level.isClientSide) {
            IBlockData iblockdata = Blocks.HONEY_BLOCK.getBlockData();

            for (int j = 0; j < i; ++j) {
                entity.level.addParticle(new ParticleParamBlock(Particles.BLOCK, iblockdata), entity.locX(), entity.locY(), entity.locZ(), 0.0D, 0.0D, 0.0D);
            }

        }
    }
}
