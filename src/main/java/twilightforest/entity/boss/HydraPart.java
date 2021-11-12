package twilightforest.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.TFPart;

public abstract class HydraPart extends TFPart<Hydra> {

	private static final EntityDataAccessor<Boolean> DATA_SIZEACTIVE = SynchedEntityData.defineId(HydraPart.class, EntityDataSerializers.BOOLEAN);

	final float maxHealth = 1000F;
	float health = maxHealth;

	private EntityDimensions cacheSize;

	public HydraPart(Hydra hydra) {
		super(hydra);
	}

	@Override
	protected void defineSynchedData() {
		fireImmune();
		entityData.define(DATA_SIZEACTIVE, true);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		super.onSyncedDataUpdated(pKey);
		if (pKey == DATA_SIZEACTIVE) {
			if (entityData.get(DATA_SIZEACTIVE))
				activate();
			else
				deactivate();
		}
	}

	// [VanillaCopy] from MobEntity
	public boolean canEntityBeSeen(Entity entityIn) {
		Vec3 vector3d = new Vec3(this.getX(), this.getEyeY(), this.getZ());
		Vec3 vector3d1 = new Vec3(entityIn.getX(), entityIn.getEyeY(), entityIn.getZ());
		return this.level.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
	}

	public HydraPart(Hydra parent, float width, float height) {
		this(parent);
		setSize(EntityDimensions.scalable(width, height));
		this.refreshDimensions();
	}

	@Override
	protected void setSize(EntityDimensions size) {
		super.setSize(size);
		cacheSize = size;
	}

	@Override
	public void tick() {
		clearFire();
		super.tick();

		if(hurtTime > 0)
			hurtTime--;

		if(health <= 0F)
			deathTime++;

	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return getParent() != null && getParent().attackEntityFromPart(this, source, amount);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	public boolean is(Entity entity) {
		return this == entity || getParent() == entity;
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

	public void activate() {
		dimensions = cacheSize;
		entityData.set(DATA_SIZEACTIVE, true);
	}

	public void deactivate() {
		dimensions = EntityDimensions.scalable(0, 0);
		entityData.set(DATA_SIZEACTIVE, false);
	}
}
