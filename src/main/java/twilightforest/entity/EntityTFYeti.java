package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.TFBiomes;
import twilightforest.entity.ai.EntityAITFThrowRider;

import javax.annotation.Nullable;

public class EntityTFYeti extends EntityMob implements IHostileMount {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/yeti");
	private static final DataParameter<Boolean> ANGER_FLAG = EntityDataManager.createKey(EntityTFYeti.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier ANGRY_MODIFIER = new AttributeModifier("Angry follow range boost", 24, 0).setSaved(false);

	public EntityTFYeti(World world) {
		super(world);
		this.setSize(1.4F, 2.4F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAITFThrowRider(this, 1.0D, false) {
			@Override
			protected void checkAndPerformAttack(EntityLivingBase p_190102_1_, double p_190102_2_) {
				super.checkAndPerformAttack(p_190102_1_, p_190102_2_);
				if (!getPassengers().isEmpty())
					playSound(TFSounds.ALPHAYETI_GRAB, 1F, 1.25F + getRNG().nextFloat() * 0.5F);
			}

			@Override
			public void resetTask() {
				if (!getPassengers().isEmpty())
					playSound(TFSounds.ALPHAYETI_THROW, 1F, 1.25F + getRNG().nextFloat() * 0.5F);
				super.resetTask();
			}
		});
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.38D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(4.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(ANGER_FLAG, false);
	}

	@Override
	public void onLivingUpdate() {
		if (!this.getPassengers().isEmpty()) {
			// stop player sneaking so that they can't dismount!
			if (this.getPassengers().get(0).isSneaking()) {
				this.getPassengers().get(0).setSneaking(false);
			}
		}

		super.onLivingUpdate();

		// look at things in our jaws
		if (!this.getPassengers().isEmpty()) {
			this.getLookHelper().setLookPositionWithEntity(getPassengers().get(0), 100F, 100F);

			// push out of user in wall
			Vec3d riderPos = this.getRiderPosition(getPassengers().get(0));
			this.pushOutOfBlocks(riderPos.x, riderPos.y, riderPos.z);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source.getTrueSource() != null) {
			// become angry
			this.setAngry(true);
		}

		return super.attackEntityFrom(source, amount);
	}

	public boolean isAngry() {
		return dataManager.get(ANGER_FLAG);
	}

	public void setAngry(boolean anger) {
		dataManager.set(ANGER_FLAG, anger);

		if (!world.isRemote) {
			if (anger) {
				if (!getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).hasModifier(ANGRY_MODIFIER)) {
					this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(ANGRY_MODIFIER);
				}
			} else {
				this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).removeModifier(ANGRY_MODIFIER);
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setBoolean("Angry", this.isAngry());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setAngry(compound.getBoolean("Angry"));
	}

	/**
	 * Put the player out in front of where we are
	 */
	@Override
	public void updatePassenger(Entity passenger) {
		Vec3d riderPos = this.getRiderPosition(passenger);
		passenger.setPosition(riderPos.x, riderPos.y, riderPos.z);
	}

	/**
	 * Returns the Y offset from the entity's position for any entity riding this one.
	 */
	@Override
	public double getMountedYOffset() {
		return 2.25D;
	}

	/**
	 * Used to both get a rider position and to push out of blocks
	 */
	private Vec3d getRiderPosition(@Nullable Entity passenger) {
		if (passenger != null) {
			float distance = 0.4F;

			double dx = Math.cos((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;
			double dz = Math.sin((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;

			return new Vec3d(this.posX + dx, this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ + dz);
		} else {
			return new Vec3d(this.posX, this.posY, this.posZ);
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public boolean getCanSpawnHere() {
		// don't check light level in the snow
		if (world.getBiome(new BlockPos(this)) == TFBiomes.snowy_forest) {
			return world.checkNoEntityCollision(getEntityBoundingBox()) && world.getCollisionBoxes(this, getEntityBoundingBox()).size() == 0;
		} else {
			// normal EntityMob spawn check, checks light level
			return super.getCanSpawnHere();
		}
	}

	@Override
	protected boolean isValidLightLevel() {
		return world.getBiome(new BlockPos(this)) == TFBiomes.snowy_forest || super.isValidLightLevel();
	}

	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() + 0.55F;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ALPHAYETI_GROWL;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.ALPHAYETI_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ALPHAYETI_DIE;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

}
