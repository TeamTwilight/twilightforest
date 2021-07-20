package twilightforest.dispenser;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import twilightforest.TFSounds;

import java.util.List;
import java.util.Random;

public class FeatherFanDispenseBehavior extends DefaultDispenseItemBehavior {
    boolean fired = false;
    @Override
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();
        BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
        int damage = stack.getMaxDamage() - stack.getDamage();
        if(!world.isRemote) {
            List<LivingEntity> thingsToPush = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(blockpos).grow(3), EntityPredicates.NOT_SPECTATING);
            if (!(thingsToPush.size() >= damage)) {
                for (Entity entity : thingsToPush) {
                    Vector3i lookVec = world.getBlockState(source.getBlockPos()).get(DispenserBlock.FACING).getDirectionVec();

                    if (entity.canBePushed() || entity instanceof ItemEntity) {
                        entity.setMotion(lookVec.getX(), lookVec.getY(), lookVec.getZ());
                    }

                    if (stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity) null)) {
                        stack.setCount(0);
                    }
                }
                fired = true;
            }
        }
        return stack;
    }

    @Override
    protected void playDispenseSound(IBlockSource source) {
        if (fired) {
            Random random = source.getWorld().getRandom();
            source.getWorld().playSound(null, source.getBlockPos(), TFSounds.FAN_WOOSH, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
            fired = false;
        } else {
            source.getWorld().playEvent(1001, source.getBlockPos(), 0);
        }
    }

    //Particle woooosh
    //[VanillaCopy] of WorldRender.playEvent(case 2000), but with further range and a different particle
    @Override
    protected void spawnDispenseParticles(IBlockSource source, Direction direction) {
        BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
        World world = source.getWorld();
        Random random = world.getRandom();

        int j1 = direction.getXOffset();
        int j2 = direction.getYOffset();
        int k2 = direction.getZOffset();
        double d18 = (double) blockpos.getX() + (double) j1 * 0.6D + 0.5D;
        double d24 = (double) blockpos.getY() + (double) j2 * 0.6D + 0.5D;
        double d28 = (double) blockpos.getZ() + (double) k2 * 0.6D + 0.5D;

        for (int i = 0; i < 30; ++i) {
            double d4 = random.nextDouble() * 0.2D + 0.01D;
            double d6 = d18 + (double) j1 * 0.01D + (random.nextDouble() - 0.5D) * (double) k2 * 0.5D;
            double d8 = d24 + (double) j2 * 0.01D + (random.nextDouble() - 0.5D) * (double) j2 * 0.5D;
            double d30 = d28 + (double) k2 * 0.01D + (random.nextDouble() - 0.5D) * (double) j1 * 0.5D;
            double d9 = (double) j1 * d4 + random.nextGaussian() * 0.01D;
            double d10 = (double) j2 * d4 + random.nextGaussian() * 0.01D;
            double d11 = (double) k2 * d4 + random.nextGaussian() * 0.01D;
            world.addParticle(ParticleTypes.CLOUD, d6, d8, d30, d9, d10, d11);
        }
    }
}
