package twilightforest.entity.boss;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;

public class EntityTFHydraPart extends PartEntity<EntityTFHydra> {

	final float maxHealth = 1000F;
	float health = maxHealth;

	int deathTime;
	int hurtTime;

	public EntityTFHydraPart(EntityTFHydra hydra) {
		super(hydra);
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
		this.size = EntitySize.flexible(width, height);
		this.recalculateSize();
	}

	@Override
	public void tick() {
		super.baseTick();

		if(hurtTime > 0)
			hurtTime--;

		if(health <= 0F)
			deathTime++;

		lastTickPosX = getPosX();
		lastTickPosY = getPosY();
		lastTickPosZ = getPosZ();

		// FIXME
		/*if (this.newPosRotationIncrements > 0) {
			double x = this.getPosX() + (this.interpTargetX - this.getPosX()) / this.newPosRotationIncrements;
			double y = this.getPosY() + (this.interpTargetY - this.getPosY()) / this.newPosRotationIncrements;
			double z = this.getPosZ() + (this.interpTargetZ - this.getPosZ()) / this.newPosRotationIncrements;
			double yawDelta = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
			this.rotationYaw = (float) (this.rotationYaw + yawDelta / this.newPosRotationIncrements);
			this.rotationPitch = (float) (this.rotationPitch + (this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPosition(x, y, z);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		this.rotationYawHead = this.rotationYaw;
		this.prevRotationYawHead = this.prevRotationYaw;

		while (rotationYaw - prevRotationYaw < -180F) prevRotationYaw -= 360F;
		while (rotationYaw - prevRotationYaw >= 180F) prevRotationYaw += 360F;

		while (renderYawOffset - prevRenderYawOffset < -180F) prevRenderYawOffset -= 360F;
		while (renderYawOffset - prevRenderYawOffset >= 180F) prevRenderYawOffset += 360F;

		while (rotationPitch - prevRotationPitch < -180F) prevRotationPitch -= 360F;
		while (rotationPitch - prevRotationPitch >= 180F) prevRotationPitch += 360F;

		while (rotationYawHead - prevRotationYawHead < -180F) prevRotationYawHead -= 360F;
		while (rotationYawHead - prevRotationYawHead >= 180F) prevRotationYawHead += 360F;*/
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
	protected void registerData() {

	}

	@Override
	protected void setRotation(float yaw, float pitch) {
		this.rotationYaw = yaw % 360.0F;
		this.rotationPitch = pitch % 360.0F;
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	public void setWidth(float width) {
		setWidthAndHeight(width, size.height);
	}

	public void setHeight(float height) {
		setWidthAndHeight(size.width, height);
	}

	public void setWidthAndHeight(float value) {
		setWidthAndHeight(value, value);
	}

	public void setWidthAndHeight(float width, float height) {
		size = EntitySize.flexible(width, height);
		recalculateSize();
	}
}
