package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;

import java.util.Optional;

public class HostileWolf extends Monster {

	private static final EntityDataAccessor<Holder<WolfVariant>> VARIANT = SynchedEntityData.defineId(HostileWolf.class, EntityDataSerializers.WOLF_VARIANT);

	public HostileWolf(EntityType<? extends HostileWolf> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new LeapGoal(this, 0.4F));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, HostileWolf.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Animal.class, false, Wolf.PREY_SELECTOR));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT, this.registryAccess().lookupOrThrow(Registries.WOLF_VARIANT).getOrThrow(WolfVariants.PALE));
	}

	public Identifier getTexture() {
		WolfVariant wolfvariant = this.getVariant().value();
		WolfVariant.AssetInfo assetInfo = this.isBaby() ? wolfvariant.babyInfo() : wolfvariant.adultInfo();
		return this.isAggressive() ? assetInfo.angry().texturePath() : assetInfo.wild().texturePath();
	}

	public Holder<WolfVariant> getVariant() {
		return this.getEntityData().get(VARIANT);
	}

	public void setVariant(Holder<WolfVariant> variant) {
		this.getEntityData().set(VARIANT, variant);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput tag) {
		super.addAdditionalSaveData(tag);
		tag.putString("variant", this.getVariant().unwrapKey().orElse(WolfVariants.PALE).identifier().toString());
	}

	@Override
	public void readAdditionalSaveData(ValueInput tag) {
		super.readAdditionalSaveData(tag);
		tag.read("variant", WolfVariant.CODEC).ifPresent(this::setVariant);
	}

	@Override
	public void setTarget(@Nullable LivingEntity entity) {
		if (entity != null && entity != this.getTarget())
			this.playSound(this.getTargetSound(), 4F, this.getVoicePitch());
		super.setTarget(entity);
	}

	public static boolean checkWolfSpawnRules(EntityType<? extends HostileWolf> entity, ServerLevelAccessor accessor, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		return accessor.getDifficulty() != Difficulty.PEACEFUL && isValidLightLevel(accessor, pos, random) && checkMobSpawnRules(entity, accessor, reason, pos, random);
	}

	public static boolean isValidLightLevel(ServerLevelAccessor accessor, BlockPos pos, RandomSource random) {
		int chunkX = Mth.floor(pos.getX()) >> 4;
		int chunkZ = Mth.floor(pos.getZ()) >> 4;
		// We're allowed to spawn in bright light only in hedge mazes.
		return LegacyLandmarkPlacements.pickLandmarkForChunk(chunkX, chunkZ, accessor) == TFStructures.HEDGE_MAZE || Monster.isDarkEnoughToSpawn(accessor, pos, random);
	}

	protected SoundEvent getTargetSound() {
		return TFSounds.HOSTILE_WOLF_TARGET.get();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.HOSTILE_WOLF_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.HOSTILE_WOLF_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.HOSTILE_WOLF_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SoundEvents.WOLF_STEP.value(), 0.15F, 1.0F);
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	public float getTailAngle() {
		if (this.getTarget() != null) {
			return 1.5393804F;
		} else {
			return ((float) Math.PI / 5F);
		}
	}

	//add aggressive flags so its face doesnt turn passive when it jumps
	public static class LeapGoal extends LeapAtTargetGoal {

		private final Mob mob;

		public LeapGoal(Mob mob, float jump) {
			super(mob, jump);
			this.mob = mob;
		}

		@Override
		public void start() {
			super.start();
			this.mob.setAggressive(true);
		}

		@Override
		public void stop() {
			super.stop();
			this.mob.setAggressive(false);
		}
	}
}
