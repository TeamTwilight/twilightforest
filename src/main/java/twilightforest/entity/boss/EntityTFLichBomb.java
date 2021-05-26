package twilightforest.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.projectile.EntityTFThrowable;
import twilightforest.util.TFDamageSources;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityTFLichBomb extends EntityTFThrowable implements IRendersAsItem {

	public EntityTFLichBomb(EntityType<? extends EntityTFLichBomb> type, World world) {
		super(type, world);
	}

	public EntityTFLichBomb(EntityType<? extends EntityTFLichBomb> type, World world, LivingEntity thrower) {
		super(type, world, thrower);
	}

	@Override
	public void tick() {
		super.tick();
		makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 1; i++) {
			double sx = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.getMotion().getX();
			double sy = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.getMotion().getY();
			double sz = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.getMotion().getZ();

			double dx = getPosX() + sx;
			double dy = getPosY() + sy;
			double dz = getPosZ() + sz;

			world.addParticle(ParticleTypes.FLAME, dx, dy, dz, sx * -0.25, sy * -0.25, sz * -0.25);
		}
	}

	@Override
	public boolean isBurning() {
		return true;
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
	public boolean attackEntityFrom(DamageSource source, float amount) {
		super.attackEntityFrom(source, amount);

		if (source.getImmediateSource() != null) {
			if (!source.isExplosion())
				explode();
			return true;
		} else {
			return false;
		}
	}

	private void explode() {
		if (!this.world.isRemote) {
			this.world.createExplosion(this, TFDamageSources.LICH_BOMB, null, this.getPosX(), this.getPosY(), this.getPosZ(), 2F, false, Explosion.Mode.NONE);
			this.remove();
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0.001F;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result instanceof EntityRayTraceResult) {
			if (((EntityRayTraceResult)result).getEntity() instanceof EntityTFLichBolt
					|| ((EntityRayTraceResult)result).getEntity() instanceof EntityTFLichBomb
					|| ((EntityRayTraceResult)result).getEntity() instanceof EntityTFLich) {
				return;
			}
		}

		explode();
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Items.MAGMA_CREAM);
	}
}
