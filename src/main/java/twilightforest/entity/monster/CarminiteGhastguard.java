package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.ai.goal.GhastguardAttackGoal;
import twilightforest.entity.ai.goal.GhastguardHomedFlightGoal;
import twilightforest.entity.ai.goal.GhastguardRandomFlyGoal;
import twilightforest.init.TFSounds;

import java.util.Optional;

public class CarminiteGhastguard extends Ghast implements EnforcedHomePoint {
	// 0 = idle, 1 = eyes open / tracking player, 2 = shooting fireball
	private static final EntityDataAccessor<Byte> ATTACK_STATUS = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> ATTACK_TIMER = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> ATTACK_PREVTIMER = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

	private GhastguardAttackGoal attackGoal;
	protected float wanderFactor;

	public CarminiteGhastguard(EntityType<? extends CarminiteGhastguard> type, Level level) {
		super(type, level);
		this.wanderFactor = 16.0F;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ATTACK_STATUS, (byte) 0);
		builder.define(ATTACK_TIMER, (byte) 0);
		builder.define(ATTACK_PREVTIMER, (byte) 0);
		builder.define(HOME_POINT, Optional.empty());
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(5, new GhastguardHomedFlightGoal(this));
		this.goalSelector.addGoal(5, new GhastguardRandomFlyGoal(this));
		this.goalSelector.addGoal(7, new Ghast.GhastLookGoal(this));
		this.goalSelector.addGoal(7, this.attackGoal = new GhastguardAttackGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public float getWanderFactor() {
		return this.wanderFactor;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Ghast.createAttributes()
			.add(Attributes.MAX_HEALTH, 30.0D)
			.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.CARMINITE_GHASTGUARD_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.CARMINITE_GHASTGUARD_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.CARMINITE_GHASTGUARD_DEATH.get();
	}

	public SoundEvent getFireSound() {
		return TFSounds.CARMINITE_GHASTGUARD_SHOOT.get();
	}

	public SoundEvent getWarnSound() {
		return TFSounds.CARMINITE_GHASTGUARD_WARN.get();
	}

	@Override
	protected float getSoundVolume() {
		return 0.5F;
	}

	@Override
	public int getAmbientSoundInterval() {
		return 160;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 8;
	}

	@Override
	public void aiStep() {
		if (this.getRandom().nextBoolean()) {
			this.level().addParticle(DustParticleOptions.REDSTONE, this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.getRandom().nextDouble() * this.getBbHeight() - 0.25D, this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth(), 0, 0, 0);
		}

		super.aiStep();
	}

	@Override
	protected void customServerAiStep(ServerLevel level) {
		int status = this.getTarget() != null && this.shouldAttack(this.getTarget()) ? 1 : 0;

		this.getEntityData().set(ATTACK_STATUS, (byte) status);
		this.getEntityData().set(ATTACK_TIMER, (byte) attackGoal.attackTimer);
		this.getEntityData().set(ATTACK_PREVTIMER, (byte) attackGoal.prevAttackTimer);
	}

	public int getAttackStatus() {
		return this.getEntityData().get(ATTACK_STATUS);
	}

	public int getAttackTimer() {
		return this.getEntityData().get(ATTACK_TIMER);
	}

	public int getPrevAttackTimer() {
		return this.getEntityData().get(ATTACK_PREVTIMER);
	}

	public boolean shouldAttack(LivingEntity living) {
		return true;
	}

	/**
	 * Something is deeply wrong with the calculations based off of this value, so let's set it high enough that it's ignored.
	 */
	@Override
	public int getMaxHeadXRot() {
		return 500;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	public static boolean checkGhastguardSpawnRules(EntityType<? extends CarminiteGhastguard> entityType, LevelAccessor accessor, EntitySpawnReason type, BlockPos pos, RandomSource random) {
		return accessor.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(entityType, accessor, type, pos, random);
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader world) {
		return world.isUnobstructed(this) && !world.containsAnyLiquid(this.getBoundingBox());
	}

	@Override
	public boolean isMobWithinHomeArea(Entity entity) {
		if (!this.isRestrictionPointValid(entity.level().dimension())) return true;
		// TF - restrict valid y levels
		// Towers are so large, a simple radius doesn't really work, so we make it more of a cylinder
		return entity.blockPosition().getY() > this.level().getMinY() + 64 &&
			entity.blockPosition().getY() < this.level().getMaxY() - 64 &&
			this.getRestrictionPoint().pos().distSqr(entity.blockPosition()) < (double) (this.getHomeRadius() * this.getHomeRadius());

	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		this.saveHomePointToNbt(compound);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.loadHomePointFromNbt(compound);
	}

	@Override
	public @Nullable GlobalPos getRestrictionPoint() {
		return this.getEntityData().get(HOME_POINT).orElse(null);
	}

	@Override
	public void setRestrictionPoint(@Nullable GlobalPos pos) {
		this.getEntityData().set(HOME_POINT, Optional.ofNullable(pos));
	}

	@Override
	public int getHomeRadius() {
		return 64;
	}
}

