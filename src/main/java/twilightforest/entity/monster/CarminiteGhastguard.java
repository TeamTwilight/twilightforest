package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.ai.goal.GhastguardAttackGoal;
import twilightforest.entity.ai.goal.GhastguardHomedFlightGoal;
import twilightforest.entity.ai.goal.GhastguardRandomFlyGoal;
import twilightforest.entity.boss.UrGhast;
import twilightforest.init.TFSounds;

import java.util.Optional;

public class CarminiteGhastguard extends Ghast implements EnforcedHomePoint {
	// 0 = idle, 1 = eyes open / tracking player, 2 = shooting fireball
	private static final EntityDataAccessor<Byte> ATTACK_STATUS = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> ATTACK_TIMER = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> ATTACK_PREVTIMER = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

	private GhastguardAttackGoal attackAI;
	protected float wanderFactor;
	private int inTrapCounter;

	public CarminiteGhastguard(EntityType<? extends CarminiteGhastguard> type, Level world) {
		super(type, world);

		this.wanderFactor = 16.0F;
		this.inTrapCounter = 0;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(ATTACK_STATUS, (byte) 0);
		this.getEntityData().define(ATTACK_TIMER, (byte) 0);
		this.getEntityData().define(ATTACK_PREVTIMER, (byte) 0);
		this.getEntityData().define(HOME_POINT, Optional.empty());
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(5, new GhastguardHomedFlightGoal(this));
		if (!(this instanceof UrGhast)) this.goalSelector.addGoal(5, new GhastguardRandomFlyGoal(this));
		this.goalSelector.addGoal(7, new Ghast.GhastLookGoal(this));
		this.goalSelector.addGoal(7, attackAI = new GhastguardAttackGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public float getWanderFactor() {
		return this.wanderFactor;
	}

	public void setInTrap() {
		this.inTrapCounter = 10;
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
	protected void customServerAiStep() {

		if (this.inTrapCounter > 0) {
			this.inTrapCounter--;
			this.setTarget(null);
		}

		int status = getTarget() != null && this.shouldAttack(this.getTarget()) ? 1 : 0;

		this.getEntityData().set(ATTACK_STATUS, (byte) status);
		this.getEntityData().set(ATTACK_TIMER, (byte) attackAI.attackTimer);
		this.getEntityData().set(ATTACK_PREVTIMER, (byte) attackAI.prevAttackTimer);
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

	public void spitFireball() {
		Vec3 vec3d = this.getViewVector(1.0F);
		double d2 = this.getTarget().getX() - (this.getX() + vec3d.x() * 4.0D);
		double d3 = this.getTarget().getBoundingBox().minY + this.getTarget().getBbHeight() / 2.0F - (0.5D + this.getY() + this.getBbHeight() / 2.0F);
		double d4 = this.getTarget().getZ() - (this.getZ() + vec3d.z() * 4.0D);
		LargeFireball entitylargefireball = new LargeFireball(this.level(), this, d2, d3, d4, this.getExplosionPower());
		entitylargefireball.setPos(this.getX() + vec3d.x() * 4.0D, this.getY() + this.getBbHeight() / 2.0F + 0.5D, this.getZ() + vec3d.z() * 4.0D);
		this.level().addFreshEntity(entitylargefireball);

		// when we attack, there is a 1-in-6 chance we decide to stop attacking
		if (this.getRandom().nextInt(6) == 0) {
			this.setTarget(null);
		}
	}

	public static boolean ghastSpawnHandler(EntityType<? extends CarminiteGhastguard> entityType, LevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource random) {
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
		return entity.blockPosition().getY() > this.level().getMinBuildHeight() + 64 &&
				entity.blockPosition().getY() < this.level().getMaxBuildHeight() - 64 &&
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

