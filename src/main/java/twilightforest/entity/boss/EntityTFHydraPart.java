package twilightforest.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.entity.TFEntities;

public class EntityTFHydraPart extends MobEntity {

	private static final DataParameter<String> PART_NAME = EntityDataManager.createKey(EntityTFHydraPart.class, DataSerializers.STRING);

	public EntityTFHydra hydra;

	public EntityTFHydraPart(EntityTFHydra parent, World world, float width, float height) {
		super(TFEntities.hydra.get(), world);
		isImmuneToFire();
		this.hydra = parent;
		this.size = EntitySize.flexible(width, height);
		this.recalculateSize();
	}

	public EntityTFHydraPart(EntityTFHydra hydra, String name, float width, float height) {
		this(hydra, hydra.world, width, height);
		setPartName(name);
		//texture = TwilightForestMod.MODEL_DIR + "hydra4.png";
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(PART_NAME, "");
	}

	public String getPartName() {
		return dataManager.get(PART_NAME);
	}

	public void setPartName(String name) {
		dataManager.set(PART_NAME, name);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putString("PartName", getPartName());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		setPartName(compound.getString("PartName"));
	}

	@Override
	public void tick() {
		if (this.hydra != null && this.hydra.deathTime > 190) {
			remove();
		}

		//  just die if we've been alive 60 seconds and there's still no body
		if (this.hydra == null && this.ticksExisted > 1200) {
			remove();
		}

		super.baseTick();

		lastTickPosX = getX();
		lastTickPosY = getY();
		lastTickPosZ = getZ();

		if (this.newPosRotationIncrements > 0) {
			double x = this.getX() + (this.interpTargetX - this.getX()) / this.newPosRotationIncrements;
			double y = this.getY() + (this.interpTargetY - this.getY()) / this.newPosRotationIncrements;
			double z = this.getZ() + (this.interpTargetZ - this.getZ()) / this.newPosRotationIncrements;
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
		while (rotationYawHead - prevRotationYawHead >= 180F) prevRotationYawHead += 360F;
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000D);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return hydra != null && hydra.attackEntityFromPart(this, source, amount);
	}

	@Override
	public boolean isEntityEqual(Entity entity) {
		return this == entity || hydra == entity;
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

	@Override
	public boolean canDespawn(double p_213397_1_) {
		return hydra == null;
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
