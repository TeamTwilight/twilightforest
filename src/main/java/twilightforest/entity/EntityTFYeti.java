package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.TFBiomes;
import twilightforest.entity.ai.EntityAITFThrowRider;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityTFYeti extends MonsterEntity implements IHostileMount {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/yeti");
	private static final DataParameter<Boolean> ANGER_FLAG = EntityDataManager.createKey(EntityTFYeti.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier ANGRY_MODIFIER = new AttributeModifier("Angry follow range boost", 24, AttributeModifier.Operation.ADDITION).setSaved(false);

	public EntityTFYeti(EntityType<? extends EntityTFYeti> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new EntityAITFThrowRider(this, 1.0D, false) {
			@Override
			protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
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
		this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.38D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.0D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(4.0D);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(ANGER_FLAG, false);
	}

	@Override
	public void livingTick() {
		if (!this.getPassengers().isEmpty()) {
			// stop player sneaking so that they can't dismount!
			if (this.getPassengers().get(0).isSneaking()) {
				this.getPassengers().get(0).setSneaking(false);
			}
		}

		super.livingTick();

		// look at things in our jaws
		if (!this.getPassengers().isEmpty()) {
			this.getLookController().setLookPositionWithEntity(getPassengers().get(0), 100F, 100F);

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
				if (!getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).hasModifier(ANGRY_MODIFIER)) {
					this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(ANGRY_MODIFIER);
				}
			} else {
				this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).removeModifier(ANGRY_MODIFIER);
			}
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("Angry", this.isAngry());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
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

			return new Vec3d(this.getX() + dx, this.getY() + this.getMountedYOffset() + passenger.getYOffset(), this.getZ() + dz);
		} else {
			return new Vec3d(this.getX(), this.getY(), this.getZ());
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	public static boolean yetiSnowyForestSpawnHandler(EntityType<? extends EntityTFYeti> entityType, IWorld world, SpawnReason p_223324_2_, BlockPos pos, Random random) {
		if (world.getBiome(new BlockPos(pos)) == TFBiomes.snowy_forest.get()) {
			return canSpawnOn(entityType, world, p_223324_2_, pos, random);
		} else {
			// normal EntityMob spawn check, checks light level
			return normalYetiSpawnHandler(entityType, world, p_223324_2_, pos, random);
		}
	}

	public static boolean normalYetiSpawnHandler(EntityType<? extends MonsterEntity> p_223325_0_, IWorld p_223325_1_, SpawnReason p_223325_2_, BlockPos p_223325_3_, Random p_223325_4_) {
		return p_223325_1_.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(p_223325_1_, p_223325_3_, p_223325_4_) && canSpawnOn(p_223325_0_, p_223325_1_, p_223325_2_, p_223325_3_, p_223325_4_);
	}


	public static boolean isValidLightLevel(IWorld world, BlockPos blockPos, Random p_223323_2_) {
		if (world.getLightLevel(LightType.SKY, blockPos) > p_223323_2_.nextInt(32)) {
			return world.getBiome(blockPos) == TFBiomes.snowy_forest.get();
		} else {
			int i = world.getWorld().isThundering() ? world.getNeighborAwareLightSubtracted(blockPos, 10) : world.getLight(blockPos);
			return i <= p_223323_2_.nextInt(8) || world.getBiome(blockPos) == TFBiomes.snowy_forest.get();
		}
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
