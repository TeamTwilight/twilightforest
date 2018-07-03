package twilightforest.entity.boss;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFHydraMortar extends EntityThrowable {
	private static final int BURN_FACTOR = 5;
	private static final int DIRECT_DAMAGE = 18;

	public int fuse = 80;
	private boolean megaBlast = false;

	public EntityTFHydraMortar(World par1World) {
		super(par1World);
		this.setSize(0.75F, 0.75F);
	}

	public EntityTFHydraMortar(World par1World, EntityTFHydraHead head) {
		super(par1World, head);
		this.setSize(0.75F, 0.75F);

		Vec3d vector = head.getLookVec();

		double dist = 3.5;
		double px = head.posX + vector.x * dist;
		double py = head.posY + 1 + vector.y * dist;
		double pz = head.posZ + vector.z * dist;

		setLocationAndAngles(px, py, pz, 0, 0);
		// these are being set to extreme numbers when we get here, why?
		head.motionX = 0;
		head.motionY = 0;
		head.motionZ = 0;
		shoot(head, head.rotationPitch, head.rotationYaw, -20.0F, 0.5F, 1F);

		TwilightForestMod.LOGGER.info("Launching mortar! Current head motion is {}, {}", head.motionX, head.motionZ);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);

		if (this.onGround) {
			this.motionX *= 0.9D;
			this.motionY *= 0.9D;
			this.motionZ *= 0.9D;

			if (!world.isRemote && this.fuse-- <= 0) {
				detonate();
			}
		}
	}

	public void setToBlasting() {
		this.megaBlast = true;
	}

	@Override
	protected void onImpact(RayTraceResult mop) {
		if (mop.entityHit == null && !megaBlast) {
			// we hit the ground
			this.motionY = 0;
			this.onGround = true;
		} else if (!world.isRemote && mop.entityHit != thrower && !isPartOfHydra(mop.entityHit)) {
			detonate();
		}
	}

	private boolean isPartOfHydra(Entity entity) {
		if (thrower instanceof EntityTFHydraPart) {
			EntityTFHydra hydra = ((EntityTFHydraPart) thrower).hydraObj;
			if (hydra == null || hydra.getParts() == null)
				return false;
			if (entity == hydra)
				return true;
			for (Entity e : hydra.getParts())
				if (entity == e)
					return true;
			for (HydraHeadContainer container : hydra.hc)
				if (entity == container.headEntity)
					return true;
		}
		return false;
	}

	@Override
	public float getExplosionResistance(Explosion par1Explosion, World par2World, BlockPos pos, IBlockState state) {
		float var6 = super.getExplosionResistance(par1Explosion, par2World, pos, state);

		if (this.megaBlast && state.getBlock() != Blocks.BEDROCK && state.getBlock() != Blocks.END_PORTAL && state.getBlock() != Blocks.END_PORTAL_FRAME) {
			var6 = Math.min(0.8F, var6);
		}

		return var6;
	}

	private void detonate() {
		float explosionPower = megaBlast ? 4.0F : 0.1F;
		boolean flag = world.getGameRules().getBoolean("mobGriefing");
		this.world.newExplosion(this, this.posX, this.posY, this.posZ, explosionPower, flag, flag);

		DamageSource src = new EntityDamageSourceIndirect("onFire", this, getThrower()).setFireDamage().setProjectile();

		for (Entity nearby : this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(1.0D, 1.0D, 1.0D))) {
			if (nearby.attackEntityFrom(src, DIRECT_DAMAGE) && !nearby.isImmuneToFire()) {
				nearby.setFire(BURN_FACTOR);
			}
		}

		this.setDead();
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		super.attackEntityFrom(damagesource, i);

		if (damagesource.getTrueSource() != null && !this.world.isRemote) {
			Vec3d vec3d = damagesource.getTrueSource().getLookVec();
			if (vec3d != null) {
				// reflect faster and more accurately
				this.shoot(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);  // reflect faster and more accurately
				this.onGround = false;
				this.fuse += 20;
			}

			if (damagesource.getTrueSource() instanceof EntityLivingBase) {
				this.thrower = (EntityLivingBase) damagesource.getTrueSource();
			}
			return true;
		} else {
			return false;
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

	/**
	 * We need to set this so that the player can attack and reflect the bolt
	 */
	@Override
	public float getCollisionBorderSize() {
		return 1.5F;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.05F;
	}
}
