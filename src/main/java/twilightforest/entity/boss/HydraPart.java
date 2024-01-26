package twilightforest.entity.boss;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.TFPart;

public abstract class HydraPart extends TFPart<Hydra> {

	private static final EntityDataAccessor<Boolean> DATA_SIZEACTIVE = SynchedEntityData.defineId(HydraPart.class, EntityDataSerializers.BOOLEAN);

	boolean markedDead;
	private EntityDimensions cacheSize;

	public HydraPart(Hydra parent, float width, float height) {
		super(parent);
		this.setSize(EntityDimensions.scalable(width, height));
		this.refreshDimensions();
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(DATA_SIZEACTIVE, true);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		super.onSyncedDataUpdated(accessor);
		if (accessor == DATA_SIZEACTIVE) {
			this.setSize(this.getDimensions(Pose.STANDING));
			//reset death markers so things render again
			if (this.isActive()) {
				this.markedDead = false;
				this.deathTime = 0;
			}
		}
	}

	// [VanillaCopy] from MobEntity
	public boolean canEntityBeSeen(Entity entity) {
		Vec3 vector3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
		Vec3 vector3d1 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
		return this.level().clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
	}

	@Override
	protected void setSize(EntityDimensions size) {
		super.setSize(size);
		this.cacheSize = size;
	}

	@Override
	public void tick() {
		this.clearFire();
		super.tick();

		if (this.hurtTime > 0)
			this.hurtTime--;

		if (this.markedDead)
			this.deathTime++;

		if (this.markedDead && this.isActive() && this.level().isClientSide()) {
			float width = this.getBbWidth();
			float height = this.getBbHeight();
			for (int k = 0; k < 10; k++) {
				this.level().addParticle(this.random.nextInt(5) == 0 ? ParticleTypes.EXPLOSION : ParticleTypes.POOF,
						(this.getX() + this.random.nextFloat() * width),
						this.getY() + this.random.nextFloat() * height,
						(this.getZ() + this.random.nextFloat() * width),
						this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
			}
		}

		if (this.deathTime == 20) {
			this.deactivate();
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean flag = this.getParent() != null && this.getParent().attackEntityFromPart(this, source, amount);
		if (flag) {
			this.gameEvent(GameEvent.ENTITY_DAMAGE);
		}
		return flag;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	public boolean is(Entity entity) {
		return this == entity || this.getParent() == entity;
	}

	@Override
	protected void setRot(float yaw, float pitch) {
		this.setYRot(yaw % 360.0F);
		this.setXRot(pitch % 360.0F);
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	public boolean isActive() {
		return this.getEntityData().get(DATA_SIZEACTIVE);
	}

	public void activate() {
		this.dimensions = this.cacheSize;
		this.getEntityData().set(DATA_SIZEACTIVE, true);
	}

	public void deactivate() {
		this.dimensions = EntityDimensions.scalable(0, 0);
		this.getEntityData().set(DATA_SIZEACTIVE, false);
	}
}
