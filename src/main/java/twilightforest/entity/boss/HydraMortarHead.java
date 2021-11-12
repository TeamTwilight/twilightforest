package twilightforest.entity.boss;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import twilightforest.TwilightForestMod;

public class HydraMortarHead extends ThrowableProjectile {

	private static final int BURN_FACTOR = 5;
	private static final int DIRECT_DAMAGE = 18;

	public int fuse = 80;
	private boolean megaBlast = false;

	public HydraMortarHead(EntityType<? extends HydraMortarHead> type, Level world) {
		super(type, world);
	}

	public HydraMortarHead(EntityType<? extends HydraMortarHead> type, Level world, HydraHead head) {
		super(type, head.getParent(), world);

		Vec3 vector = head.getLookAngle();

		double dist = 3.5;
		double px = head.getX() + vector.x * dist;
		double py = head.getY() + 1 + vector.y * dist;
		double pz = head.getZ() + vector.z * dist;

		moveTo(px, py, pz, 0, 0);
		// these are being set to extreme numbers when we get here, why?
		head.setDeltaMovement(new Vec3(0, 0, 0));
		shootFromRotation(head, head.getXRot(), head.getYRot(), -20.0F, 0.5F, 1F);

		TwilightForestMod.LOGGER.debug("Launching mortar! Current head motion is {}, {}", head.getDeltaMovement().x(), head.getDeltaMovement().z());
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public void tick() {
		super.tick();

		//this.pushOutOfBlocks(this.getPosX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getPosZ());

		if (this.isOnGround()) {
			this.getDeltaMovement().multiply(0.9D, 0.9D, 0.9D);

			if (!level.isClientSide && this.fuse-- <= 0) {
				detonate();
			}
		}
	}

	public void setToBlasting() {
		this.megaBlast = true;
	}

	@Override
	protected void onHit(HitResult ray) {
		if (ray instanceof EntityHitResult) {
			if (!level.isClientSide &&

					(!(((EntityHitResult)ray).getEntity() instanceof HydraMortarHead) || ((HydraMortarHead) ((EntityHitResult)ray).getEntity()).getOwner() != getOwner()) &&

					((EntityHitResult)ray).getEntity() != getOwner() &&

					!isPartOfHydra(((EntityHitResult)ray).getEntity())) {
				detonate();
			}
		} else if (!megaBlast) {
			// we hit the ground
			this.setDeltaMovement(this.getDeltaMovement().x(), 0.0D, this.getDeltaMovement().z());
			this.onGround = true;
		} else
			detonate();
	}

	private boolean isPartOfHydra(Entity entity) {
		return (getOwner() instanceof Hydra && entity instanceof HydraPart && ((HydraPart) entity).getParent() == getOwner());
	}

	@Override
	public float getBlockExplosionResistance(Explosion explosion, BlockGetter world, BlockPos pos, BlockState state, FluidState fluid, float p_180428_6_) {
		float resistance = super.getBlockExplosionResistance(explosion, world, pos, state, fluid, p_180428_6_);

		if (this.megaBlast && state.getBlock() != Blocks.BEDROCK && state.getBlock() != Blocks.END_PORTAL && state.getBlock() != Blocks.END_PORTAL_FRAME) {
			resistance = Math.min(0.8F, resistance);
		}

		return resistance;
	}

	private void detonate() {
		float explosionPower = megaBlast ? 4.0F : 0.1F;
		boolean flag = ForgeEventFactory.getMobGriefingEvent(level, this);
		Explosion.BlockInteraction flag1 = flag ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE;
		this.level.explode(this, this.getX(), this.getY(), this.getZ(), explosionPower, flag, flag1);

		DamageSource src = new IndirectEntityDamageSource("onFire", this, getOwner()).setIsFire().setProjectile();

		for (Entity nearby : this.level.getEntities(this, this.getBoundingBox().inflate(1.0D, 1.0D, 1.0D))) {
			if (nearby.hurt(src, DIRECT_DAMAGE) && !nearby.fireImmune()) {
				nearby.setSecondsOnFire(BURN_FACTOR);
			}
		}

		this.discard();
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		super.hurt(source, amount);

		if (source.getEntity() != null && !this.level.isClientSide) {
			Vec3 vec3d = source.getEntity().getLookAngle();
			if (vec3d != null) {
				// reflect faster and more accurately
				this.shoot(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);  // reflect faster and more accurately
				this.onGround = false;
				this.fuse += 20;
			}

			if (source.getEntity() instanceof LivingEntity) {
				this.setOwner(source.getEntity());
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isOnFire() {
		return true;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	/**
	 * We need to set this so that the player can attack and reflect the bolt
	 */
	@Override
	public float getPickRadius() {
		return 1.5F;
	}

	@Override
	protected float getGravity() {
		return 0.05F;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
