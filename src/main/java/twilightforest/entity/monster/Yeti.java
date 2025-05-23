package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.ai.goal.ThrowRiderGoal;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFSounds;

import java.util.Objects;
import java.util.Optional;

public class Yeti extends Monster implements IHostileMount {

	private static final EntityDataAccessor<Boolean> ANGER_FLAG = SynchedEntityData.defineId(Yeti.class, EntityDataSerializers.BOOLEAN);
	private static final AttributeModifier ANGRY_MODIFIER = new AttributeModifier(TwilightForestMod.prefix("anger_follow_boost"), 8, AttributeModifier.Operation.ADD_VALUE);

	public Yeti(EntityType<? extends Yeti> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new ThrowRiderGoal(this, 1.0D, false) {
			@Override
			protected void checkAndPerformAttack(LivingEntity victim) {
				super.checkAndPerformAttack(victim);
				if (!getPassengers().isEmpty())
					playSound(TFSounds.YETI_GRAB.get(), 1F, 1.25F + getRandom().nextFloat() * 0.5F);
			}

			@Override
			public void stop() {
				if (!getPassengers().isEmpty())
					playSound(TFSounds.YETI_THROW.get(), 1F, 1.25F + getRandom().nextFloat() * 0.5F);
				super.stop();
			}
		});
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 20.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.38D)
			.add(Attributes.ATTACK_DAMAGE, 0.0D)
			.add(Attributes.FOLLOW_RANGE, 4.0D);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ANGER_FLAG, false);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		// look at things in our jaws
		if (!this.getPassengers().isEmpty()) {
			this.getLookControl().setLookAt(this.getPassengers().get(0), 100F, 100F);
		}
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (source.getEntity() != null && !source.isCreativePlayer()) {
			// become angry
			this.setAngry(true);
		}

		return super.hurtServer(level, source, amount);
	}

	public boolean isAngry() {
		return this.getEntityData().get(ANGER_FLAG);
	}

	public void setAngry(boolean anger) {
		this.getEntityData().set(ANGER_FLAG, anger);

		if (!this.level().isClientSide()) {
			if (anger) {
				if (!Objects.requireNonNull(getAttribute(Attributes.FOLLOW_RANGE)).hasModifier(ANGRY_MODIFIER.id())) {
					Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).addTransientModifier(ANGRY_MODIFIER);
				}
			} else {
				Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).removeModifier(ANGRY_MODIFIER.id());
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("Angry", this.isAngry());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setAngry(compound.getBoolean("Angry"));
	}

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float yRot) {
		return new Vec3(0.0F, dimensions.height(), 0.4F);
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	public static boolean checkYetiSpawnRules(EntityType<? extends Yeti> entityType, ServerLevelAccessor accessor, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		if (accessor.getDifficulty() != Difficulty.PEACEFUL) {
			if (accessor.getBiome(pos).is(TFBiomes.SNOWY_FOREST)) {
				return checkMobSpawnRules(entityType, accessor, reason, pos, random);
			} else {
				// normal Mob spawn check, checks light level
				return checkMonsterSpawnRules(entityType, accessor, reason, pos, random);
			}
		}
		return false;
	}

	public static boolean isValidLightLevel(ServerLevelAccessor accessor, BlockPos blockPos, RandomSource random) {
		Optional<ResourceKey<Biome>> key = accessor.getBiome(blockPos).unwrapKey();
		if (accessor.getBrightness(LightLayer.SKY, blockPos) > random.nextInt(32)) {
			return Objects.equals(key, Optional.of(TFBiomes.SNOWY_FOREST));
		} else {
			int i = accessor.getLevel().isThundering() ? accessor.getMaxLocalRawBrightness(blockPos, 10) : accessor.getMaxLocalRawBrightness(blockPos);
			return i <= random.nextInt(8) || Objects.equals(key, Optional.of(TFBiomes.SNOWY_FOREST));
		}
	}

	@Override
	public float getVoicePitch() {
		return super.getVoicePitch() + 0.55F;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.YETI_GROWL.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.YETI_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.YETI_DEATH.get();
	}
}
