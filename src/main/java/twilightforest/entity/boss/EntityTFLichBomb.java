package twilightforest.entity.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityTFLichBomb extends EntityThrowable {
	public EntityTFLichBomb(World par1World) {
		super(par1World);
	}

	public EntityTFLichBomb(World par1World, EntityLivingBase par2EntityLiving) {
		super(par1World, par2EntityLiving);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 1; i++) {
			double sx = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.motionX;
			double sy = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.motionY;
			double sz = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.motionZ;


			double dx = posX + sx;
			double dy = posY + sy;
			double dz = posZ + sz;

			world.spawnParticle(EnumParticleTypes.FLAME, dx, dy, dz, sx * -0.25, sy * -0.25, sz * -0.25);
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
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		super.attackEntityFrom(damagesource, i);

		if (damagesource.getImmediateSource() != null) {
			if (!damagesource.isExplosion())
				explode();
			return true;
		} else {
			return false;
		}
	}

	private void explode() {
		if (!this.world.isRemote) {
			this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2F, false, false);
			this.setDead();
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0.001F;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.entityHit instanceof EntityTFLichBolt
				|| result.entityHit instanceof EntityTFLichBomb
				|| result.entityHit instanceof EntityTFLich) {
			return;
		}

		explode();
	}
}
