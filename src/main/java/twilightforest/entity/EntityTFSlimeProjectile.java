package twilightforest.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.projectile.EntityTFThrowable;

public class EntityTFSlimeProjectile extends EntityTFThrowable implements IRendersAsItem {

	public EntityTFSlimeProjectile(EntityType<? extends EntityTFSlimeProjectile> type, World world) {
		super(type, world);
	}

	public EntityTFSlimeProjectile(EntityType<? extends EntityTFSlimeProjectile> type, World world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	@Override
	protected float getGravityVelocity() {
		return 0.006F;
	}

	private void makeTrail() {
		for (int i = 0; i < 2; i++) {
			double dx = getX() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = getY() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = getZ() + 0.5 * (rand.nextDouble() - rand.nextDouble());
			world.addParticle(ParticleTypes.ITEM_SLIME, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		super.attackEntityFrom(source, amount);
		die();
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(ParticleTypes.ITEM_SLIME, this.getX(), this.getY(), this.getZ(), rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult target) {
		// only damage living things
		if (target instanceof EntityRayTraceResult) {
			if (!world.isRemote && ((EntityRayTraceResult)target).getEntity() instanceof LivingEntity) {
				((EntityRayTraceResult)target).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 8);
				// TODO: damage armor?
			}
		}

		die();
	}

	private void die() {
		if (!this.world.isRemote) {
			this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
			this.remove();
			this.world.setEntityState(this, (byte) 3);
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.SLIME_BALL);
	}
}
