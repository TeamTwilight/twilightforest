package twilightforest.entity.boss;

import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import twilightforest.entity.TFPartEntity;

public abstract class EntityTFHydraPart extends TFPartEntity<EntityTFHydra> {

	final float maxHealth = 1000F;
	float health = maxHealth;

	private EntitySize cacheSize;

	public EntityTFHydraPart(EntityTFHydra hydra) {
		super(hydra);
	}

	@Override
	protected void registerData() {
		isImmuneToFire();
	}

	// [VanillaCopy] from MobEntity
	public boolean canEntityBeSeen(Entity entityIn) {
		Vector3d vector3d = new Vector3d(this.getPosX(), this.getPosYEye(), this.getPosZ());
		Vector3d vector3d1 = new Vector3d(entityIn.getPosX(), entityIn.getPosYEye(), entityIn.getPosZ());
		return this.world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() == RayTraceResult.Type.MISS;
	}

	public EntityTFHydraPart(EntityTFHydra parent, float width, float height) {
		this(parent);
		setSize(EntitySize.flexible(width, height));
		this.recalculateSize();
	}

	@Override
	protected void setSize(EntitySize size) {
		super.setSize(size);
		cacheSize = size;
	}

	@Override
	public void tick() {
		extinguish();
		super.tick();

		if(hurtTime > 0)
			hurtTime--;

		if(health <= 0F)
			deathTime++;

	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return getParent() != null && getParent().attackEntityFromPart(this, source, amount);
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {

	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {

	}

	@Override
	public boolean isEntityEqual(Entity entity) {
		return this == entity || getParent() == entity;
	}

	@Override
	protected void setRotation(float yaw, float pitch) {
		this.rotationYaw = yaw % 360.0F;
		this.rotationPitch = pitch % 360.0F;
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimension() {
		return false;
	}

	public void activate() {
		size = cacheSize;
		recalculateSize();
	}

	public void deactivate() {
		size = EntitySize.flexible(0, 0);
		recalculateSize();
	}
}
