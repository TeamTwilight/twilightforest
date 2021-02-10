package twilightforest.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TwilightForestMod;

public class EntityTFHydraMortar extends ThrowableEntity {

	private static final int BURN_FACTOR = 5;
	private static final int DIRECT_DAMAGE = 18;

	public int fuse = 80;
	private boolean megaBlast = false;

	public EntityTFHydraMortar(EntityType<? extends EntityTFHydraMortar> type, World world) {
		super(type, world);
	}

	public EntityTFHydraMortar(EntityType<? extends EntityTFHydraMortar> type, World world, EntityTFHydra head) {
		super(type, head, world);

		Vector3d vector = head.getLookVec();

		double dist = 3.5;
		double px = head.getPosX() + vector.x * dist;
		double py = head.getPosY() + 1 + vector.y * dist;
		double pz = head.getPosZ() + vector.z * dist;

		setLocationAndAngles(px, py, pz, 0, 0);
		// these are being set to extreme numbers when we get here, why?
		head.setMotion(new Vector3d(0, 0, 0));
		func_234612_a_(head, head.rotationPitch, head.rotationYaw, -20.0F, 0.5F, 1F);

		TwilightForestMod.LOGGER.debug("Launching mortar! Current head motion is {}, {}", head.getMotion().getX(), head.getMotion().getZ());
	}

	@Override
	protected void registerData() {

	}

	@Override
	public void tick() {
		super.tick();

		this.pushOutOfBlocks(this.getPosX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getPosZ());

		if (this.isOnGround()) {
//			this.motionX *= 0.9D;
//			this.motionY *= 0.9D;
//			this.motionZ *= 0.9D;
			this.getMotion().mul(0.9D, 0.9D, 0.9D);

			if (!world.isRemote && this.fuse-- <= 0) {
				detonate();
			}
		}
	}

	public void setToBlasting() {
		this.megaBlast = true;
	}

	@Override
	protected void onImpact(RayTraceResult ray) {
		if (ray instanceof EntityRayTraceResult) {
			if (((EntityRayTraceResult)ray).getEntity() == null && !megaBlast) {
				// we hit the ground
				this.setMotion(this.getMotion().getX(), 0.0D, this.getMotion().getZ());
				this.onGround = true;
			} else if (!world.isRemote && ((EntityRayTraceResult)ray).getEntity() != func_234616_v_() && !isPartOfHydra(((EntityRayTraceResult)ray).getEntity())) {
				detonate();
			}
		}
	}

	private boolean isPartOfHydra(Entity entity) {
		if (func_234616_v_() instanceof EntityTFHydra) {
			EntityTFHydra hydra = (EntityTFHydra) func_234616_v_();
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
	public float getExplosionResistance(Explosion explosion, IBlockReader world, BlockPos pos, BlockState state, FluidState fluid, float p_180428_6_) {
		float resistance = super.getExplosionResistance(explosion, world, pos, state, fluid, p_180428_6_);

		if (this.megaBlast && state.getBlock() != Blocks.BEDROCK && state.getBlock() != Blocks.END_PORTAL && state.getBlock() != Blocks.END_PORTAL_FRAME) {
			resistance = Math.min(0.8F, resistance);
		}

		return resistance;
	}

	private void detonate() {
		float explosionPower = megaBlast ? 4.0F : 0.1F;
		boolean flag = ForgeEventFactory.getMobGriefingEvent(world, this);
		Explosion.Mode flag1 = flag ? Explosion.Mode.BREAK : Explosion.Mode.NONE;
		this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), explosionPower, flag, flag1);

		DamageSource src = new IndirectEntityDamageSource("onFire", this, func_234616_v_()).setFireDamage().setProjectile();

		for (Entity nearby : this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(1.0D, 1.0D, 1.0D))) {
			if (nearby.attackEntityFrom(src, DIRECT_DAMAGE) && !nearby.isImmuneToFire()) {
				nearby.setFire(BURN_FACTOR);
			}
		}

		this.remove();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		super.attackEntityFrom(source, amount);

		if (source.getTrueSource() != null && !this.world.isRemote) {
			Vector3d vec3d = source.getTrueSource().getLookVec();
			if (vec3d != null) {
				// reflect faster and more accurately
				this.shoot(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);  // reflect faster and more accurately
				this.onGround = false;
				this.fuse += 20;
			}

			if (source.getTrueSource() instanceof LivingEntity) {
				this.setShooter(source.getTrueSource()); //TODO: Verify
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
