package twilightforest.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.block.TFBlocks;

public class EntityTFMoonwormShot extends EntityTFThrowable {

	public EntityTFMoonwormShot(EntityType<? extends EntityTFMoonwormShot> type, World world) {
		super(type, world);
	}

	public EntityTFMoonwormShot(EntityType<? extends EntityTFMoonwormShot> type, World world, LivingEntity thrower) {
		super(type, world, thrower);
		func_234612_a_(thrower, thrower.rotationPitch, thrower.rotationYaw, 0F, 1.5F, 1.0F);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	@Override
	public float getBrightness() {
		return 1.0F;
	}

	private void makeTrail() {
//		for (int i = 0; i < 5; i++) {
//			double dx = posX + 0.5 * (rand.nextDouble() - rand.nextDouble()); 
//			double dy = posY + 0.5 * (rand.nextDouble() - rand.nextDouble()); 
//			double dz = posZ + 0.5 * (rand.nextDouble() - rand.nextDouble()); 
//			
//			double s1 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.17F;
//			double s2 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.80F;
//			double s3 = ((rand.nextFloat() * 0.5F) + 0.5F) * 0.69F;
//
//			world.spawnParticle("mobSpell", dx, dy, dz, s1, s2, s3);
//		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public float getCollisionBorderSize() {
		return 1.0F;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.03F;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, TFBlocks.moonworm.get().getDefaultState()), false, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult ray) {
		if (!world.isRemote) {
			if (ray instanceof BlockRayTraceResult) {
				BlockRayTraceResult blockray = (BlockRayTraceResult) ray;
				BlockPos pos = blockray.getPos().offset(blockray.getFace());
				BlockState currentState = world.getBlockState(pos);

				DirectionalPlaceContext context = new DirectionalPlaceContext(world, pos, blockray.getFace(), ItemStack.EMPTY, blockray.getFace().getOpposite());
				if (currentState.isReplaceable(context)) {
					world.setBlockState(pos, TFBlocks.moonworm.get().getDefaultState().with(DirectionalBlock.FACING, ((BlockRayTraceResult) ray).getFace()));
					// todo sound
				}
			}

			if (ray instanceof EntityRayTraceResult) {
				if (((EntityRayTraceResult)ray).getEntity() != null) {
					((EntityRayTraceResult)ray).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 0);
				}
			}

			this.world.setEntityState(this, (byte) 3);
			this.remove();
		}
	}
}
