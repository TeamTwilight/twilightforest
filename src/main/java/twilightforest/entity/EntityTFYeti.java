package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import twilightforest.TFSounds;
import twilightforest.worldgen.biomes.BiomeKeys;
import twilightforest.entity.ai.EntityAITFThrowRider;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class EntityTFYeti extends MonsterEntity implements IHostileMount {

	private static final DataParameter<Boolean> ANGER_FLAG = EntityDataManager.createKey(EntityTFYeti.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier ANGRY_MODIFIER = new AttributeModifier("Angry follow range boost", 24, AttributeModifier.Operation.ADDITION);

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
					playSound(TFSounds.YETI_GRAB, 1F, 1.25F + getRNG().nextFloat() * 0.5F);
			}

			@Override
			public void resetTask() {
				if (!getPassengers().isEmpty())
					playSound(TFSounds.YETI_THROW, 1F, 1.25F + getRNG().nextFloat() * 0.5F);
				super.resetTask();
			}
		});
		this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.38D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 0.0D)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 4.0D);
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
			Vector3d riderPos = this.getRiderPosition(getPassengers().get(0));
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
				if (!getAttribute(Attributes.FOLLOW_RANGE).hasModifier(ANGRY_MODIFIER)) {
					this.getAttribute(Attributes.FOLLOW_RANGE).applyNonPersistentModifier(ANGRY_MODIFIER);
				}
			} else {
				this.getAttribute(Attributes.FOLLOW_RANGE).removeModifier(ANGRY_MODIFIER);
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
		Vector3d riderPos = this.getRiderPosition(passenger);
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
	private Vector3d getRiderPosition(@Nullable Entity passenger) {
		if (passenger != null) {
			float distance = 0.4F;

			double dx = Math.cos((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;
			double dz = Math.sin((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;

			return new Vector3d(this.getPosX() + dx, this.getPosY() + this.getMountedYOffset() + passenger.getYOffset(), this.getPosZ() + dz);
		} else {
			return new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ());
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	public static boolean yetiSnowyForestSpawnHandler(EntityType<? extends EntityTFYeti> entityType, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
		Optional<RegistryKey<Biome>> key = world.func_242406_i(pos);
		if (Objects.equals(key, Optional.of(BiomeKeys.SNOWY_FOREST))) {
			return canSpawnOn(entityType, world, reason, pos, random);
		} else {
			// normal EntityMob spawn check, checks light level
			return normalYetiSpawnHandler(entityType, world, reason, pos, random);
		}
	}

	public static boolean normalYetiSpawnHandler(EntityType<? extends MonsterEntity> entity, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(world, pos, random) && canSpawnOn(entity, world, reason, pos, random);
	}

	public static boolean isValidLightLevel(IServerWorld world, BlockPos blockPos, Random random) {
		Optional<RegistryKey<Biome>> key = world.func_242406_i(blockPos);
		if (world.getLightFor(LightType.SKY, blockPos) > random.nextInt(32)) {
			return Objects.equals(key, Optional.of(BiomeKeys.SNOWY_FOREST));
		} else {
			int i = world.getWorld().isThundering() ? world.getNeighborAwareLightSubtracted(blockPos, 10) : world.getLight(blockPos);
			return i <= random.nextInt(8) || Objects.equals(key, Optional.of(BiomeKeys.SNOWY_FOREST));
		}
	}

	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() + 0.55F;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.YETI_GROWL;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.YETI_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.YETI_DIE;
	}
}
