package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFSounds;
import twilightforest.entity.boss.UrGhast;
import twilightforest.world.registration.TFFeature;

import java.util.EnumSet;
import java.util.Random;

public class CarminiteGhastguard extends Ghast {
	// 0 = idle, 1 = eyes open / tracking player, 2 = shooting fireball
	private static final EntityDataAccessor<Byte> ATTACK_STATUS = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> ATTACK_TIMER = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> ATTACK_PREVTIMER = SynchedEntityData.defineId(CarminiteGhastguard.class, EntityDataSerializers.BYTE);

	private AIAttack attackAI;
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
		this.entityData.define(ATTACK_STATUS, (byte) 0);
		this.entityData.define(ATTACK_TIMER, (byte) 0);
		this.entityData.define(ATTACK_PREVTIMER, (byte) 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(5, new AIHomedFly(this));
		if (!(this instanceof UrGhast)) this.goalSelector.addGoal(5, new AIRandomFly(this));
		this.goalSelector.addGoal(7, new Ghast.GhastLookGoal(this));
		this.goalSelector.addGoal(7, attackAI = new AIAttack(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
	      return TFSounds.GHASTGUARD_AMBIENT;
	   }

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return TFSounds.GHASTGUARD_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.GHASTGUARD_DEATH;
	   }

	// [VanillaCopy] from EntityGhast but we use wanderFactor instead, we also stop moving when we have a target
	public static class AIRandomFly extends Goal {
		private final CarminiteGhastguard parentEntity;

		public AIRandomFly(CarminiteGhastguard ghast) {
			this.parentEntity = ghast;
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			MoveControl entitymovehelper = this.parentEntity.getMoveControl();
			if (!entitymovehelper.hasWanted()) {
				return parentEntity.getTarget() == null;
			} else {
				double d0 = entitymovehelper.getWantedX() - this.parentEntity.getX();
				double d1 = entitymovehelper.getWantedY() - this.parentEntity.getY();
				double d2 = entitymovehelper.getWantedZ() - this.parentEntity.getZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return parentEntity.getTarget() == null && (d3 < 1.0D || d3 > 3600.0D);
			}
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void start() {
			Random random = this.parentEntity.getRandom();
			double d0 = this.parentEntity.getX() + (random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor;
			double d1 = this.parentEntity.getY() + (random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor;
			double d2 = this.parentEntity.getZ() + (random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor;
			this.parentEntity.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
		}
	}

	// [VanillaCopy]-ish mixture of EntityGhast.AIFly and EntityAIStayNearHome
	public static class AIHomedFly extends Goal {
		private final CarminiteGhastguard parentEntity;

		AIHomedFly(CarminiteGhastguard ghast) {
			this.parentEntity = ghast;
			setFlags(EnumSet.of(Flag.MOVE));
		}

		// From AIFly, but with extra condition from AIStayNearHome
		@Override
		public boolean canUse() {
			MoveControl entitymovehelper = this.parentEntity.getMoveControl();

			if (!entitymovehelper.hasWanted()) {
				return !this.parentEntity.isWithinRestriction();
			} else {
				double d0 = entitymovehelper.getWantedX() - this.parentEntity.getX();
				double d1 = entitymovehelper.getWantedY() - this.parentEntity.getY();
				double d2 = entitymovehelper.getWantedZ() - this.parentEntity.getZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return (d3 < 1.0D || d3 > 3600.0D)
						&& !this.parentEntity.isWithinRestriction();
			}
		}

		// From AIFly
		@Override
		public boolean canContinueToUse() {
			return false;
		}

		// From AIStayNearHome but use move helper instead of PathNavigate
		@Override
		public void start() {
			Random random = this.parentEntity.getRandom();
			double d0 = this.parentEntity.getX() + (random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor;
			double d1 = this.parentEntity.getY() + (random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor;
			double d2 = this.parentEntity.getZ() + (random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor;
			this.parentEntity.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);

			if (this.parentEntity.distanceToSqr(Vec3.atLowerCornerOf(this.parentEntity.getRestrictCenter())) > 256.0D) {
				Vec3 vecToHome = Vec3.atLowerCornerOf(this.parentEntity.getRestrictCenter()).subtract(this.parentEntity.position()).normalize();

				double targetX = this.parentEntity.getX() + vecToHome.x * parentEntity.wanderFactor + (this.parentEntity.random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor;
				double targetY = this.parentEntity.getY() + vecToHome.y * parentEntity.wanderFactor + (this.parentEntity.random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor;
				double targetZ = this.parentEntity.getZ() + vecToHome.z * parentEntity.wanderFactor + (this.parentEntity.random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor;

				this.parentEntity.getMoveControl().setWantedPosition(targetX, targetY, targetZ, 1.0D);
			} else {
				this.parentEntity.getMoveControl().setWantedPosition(this.parentEntity.getRestrictCenter().getX() + 0.5D, this.parentEntity.getRestrictCenter().getY(), this.parentEntity.getRestrictCenter().getZ() + 0.5D, 1.0D);
			}
		}
	}

	// [VanillaCopy] EntityGhast.AIFireballAttack, edits noted
	public static class AIAttack extends Goal {
		private final CarminiteGhastguard parentEntity;
		public int attackTimer;
		public int prevAttackTimer; // TF - add for renderer

		public AIAttack(CarminiteGhastguard ghast) {
			this.parentEntity = ghast;
		}

		@Override
		public boolean canUse() {
			return this.parentEntity.getTarget() != null && parentEntity.shouldAttack(parentEntity.getTarget());
		}

		@Override
		public void start() {
			this.attackTimer = this.prevAttackTimer = 0;
		}

		@Override
		public void stop() {
			this.parentEntity.setCharging(false);
		}

		@Override
		public void tick() {
			LivingEntity entitylivingbase = this.parentEntity.getTarget();

			if (entitylivingbase.distanceToSqr(this.parentEntity) < 4096.0D && this.parentEntity.getSensing().hasLineOfSight(entitylivingbase)) {
				this.prevAttackTimer = attackTimer;
				++this.attackTimer;

				// TF face our target at all times
				this.parentEntity.getLookControl().setLookAt(entitylivingbase, 10F, this.parentEntity.getMaxHeadXRot());

				if (this.attackTimer == 10) {
					parentEntity.playSound(parentEntity.getWarnSound(), 10.0F, parentEntity.getVoicePitch());
				}

				if (this.attackTimer == 20) {
					if (this.parentEntity.shouldAttack(entitylivingbase)) {
						// TF - call custom method
						parentEntity.playSound(parentEntity.getFireSound(), 10.0F, parentEntity.getVoicePitch());
						this.parentEntity.spitFireball();
						this.prevAttackTimer = attackTimer;
					}
					this.attackTimer = -40;
				}
			} else if (this.attackTimer > 0) {
				this.prevAttackTimer = attackTimer;
				--this.attackTimer;
			}

			this.parentEntity.setCharging(this.attackTimer > 10);
		}
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Ghast.createAttributes()
				.add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	public SoundEvent getFireSound() {
		return TFSounds.GHASTGUARD_SHOOT;
	}

	public SoundEvent getWarnSound() {
		return TFSounds.GHASTGUARD_WARN;
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
		// age when in light, like mobs
		if (getBrightness() > 0.5F) {
			this.noActionTime += 2;
		}

		if (this.random.nextBoolean()) {
			this.level.addParticle(DustParticleOptions.REDSTONE, this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(), this.getY() + this.random.nextDouble() * this.getBbHeight() - 0.25D, this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(), 0, 0, 0);
		}

		super.aiStep();
	}

	@Override
	protected void customServerAiStep() {
		findHome();

		if (this.inTrapCounter > 0) {
			this.inTrapCounter--;
			setTarget(null);
		}

		int status = getTarget() != null && shouldAttack(getTarget()) ? 1 : 0;

		entityData.set(ATTACK_STATUS, (byte) status);
		entityData.set(ATTACK_TIMER, (byte) attackAI.attackTimer);
		entityData.set(ATTACK_PREVTIMER, (byte) attackAI.prevAttackTimer);
	}

	public int getAttackStatus() {
		return entityData.get(ATTACK_STATUS);
	}

	public int getAttackTimer() {
		return entityData.get(ATTACK_TIMER);
	}

	public int getPrevAttackTimer() {
		return entityData.get(ATTACK_PREVTIMER);
	}

	protected boolean shouldAttack(LivingEntity living) {
		return true;
	}

	/**
	 * Something is deeply wrong with the calculations based off of this value, so let's set it high enough that it's ignored.
	 */
	@Override
	public int getMaxHeadXRot() {
		return 500;
	}

	protected void spitFireball() {
		Vec3 vec3d = this.getViewVector(1.0F);
		double d2 = getTarget().getX() - (this.getX() + vec3d.x * 4.0D);
		double d3 = getTarget().getBoundingBox().minY + getTarget().getBbHeight() / 2.0F - (0.5D + this.getY() + this.getBbHeight() / 2.0F);
		double d4 = getTarget().getZ() - (this.getZ() + vec3d.z * 4.0D);
		LargeFireball entitylargefireball = new LargeFireball(level, this, d2, d3, d4, this.getExplosionPower());
		entitylargefireball.setPos(this.getX() + vec3d.x * 4.0D, this.getY() + this.getBbHeight() / 2.0F + 0.5D, this.getZ() + vec3d.z * 4.0D);
		level.addFreshEntity(entitylargefireball);

		// when we attack, there is a 1-in-6 chance we decide to stop attacking
		if (random.nextInt(6) == 0) {
			setTarget(null);
		}
	}

	public static boolean ghastSpawnHandler(EntityType<? extends CarminiteGhastguard> entityType, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(entityType, world, reason, pos, random);
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader world) {
		return world.isUnobstructed(this) && !world.containsAnyLiquid(this.getBoundingBox()); //TODO: Verify
	}

	private void findHome() {
		if (!this.hasHome()) {
			int chunkX = Mth.floor(this.getX()) >> 4;
			int chunkZ = Mth.floor(this.getZ()) >> 4;

			TFFeature nearFeature = TFFeature.getFeatureForRegion(chunkX, chunkZ, (ServerLevel) this.level);

			if (nearFeature != TFFeature.DARK_TOWER) {
				this.hasRestriction();
				this.noActionTime += 5;
			} else {
				BlockPos cc = TFFeature.getNearestCenterXYZ(chunkX, chunkZ);
				this.restrictTo(cc.above(128), 64);
			}
		}
	}

	public void setInTrap() {
		this.inTrapCounter = 10;
	}

	// [VanillaCopy] Home fields and methods from CreatureEntity, changes noted
	private BlockPos homePosition = BlockPos.ZERO;
	private float maximumHomeDistance = -1.0F;

	@Override
	public boolean isWithinRestriction() {
		return this.isWithinRestriction(blockPosition());
	}

	@Override
	public boolean isWithinRestriction(BlockPos pos) {
		// TF - restrict valid y levels
		// Towers are so large, a simple radius doesn't really work, so we make it more of a cylinder
		return this.maximumHomeDistance == -1.0F
				? true
				: pos.getY() > 64 && pos.getY() < 210 && this.homePosition.distSqr(pos) < this.maximumHomeDistance * this.maximumHomeDistance;
	}

	@Override
	public void restrictTo(BlockPos pos, int distance) {
		this.homePosition = pos;
		this.maximumHomeDistance = distance;
	}

	@Override
	public BlockPos getRestrictCenter() {
		return this.homePosition;
	}

	@Override
	public float getRestrictRadius() {
		return this.maximumHomeDistance;
	}

	@Override
	public boolean hasRestriction() {
		this.maximumHomeDistance = -1.0F;
		return false;
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	public boolean hasHome() {
		return this.maximumHomeDistance != -1.0F;
	}
	// End copy
}

