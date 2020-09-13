package twilightforest.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.entity.ai.TFNearestPlayerGoal;
import twilightforest.entity.boss.EntityTFUrGhast;

import java.util.EnumSet;
import java.util.Random;

public class EntityTFTowerGhast extends GhastEntity {
	// 0 = idle, 1 = eyes open / tracking player, 2 = shooting fireball
	private static final DataParameter<Byte> ATTACK_STATUS = EntityDataManager.createKey(EntityTFTowerGhast.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> ATTACK_TIMER = EntityDataManager.createKey(EntityTFTowerGhast.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> ATTACK_PREVTIMER = EntityDataManager.createKey(EntityTFTowerGhast.class, DataSerializers.BYTE);

	private AIAttack attackAI;
	protected float wanderFactor;
	private int inTrapCounter;

	public EntityTFTowerGhast(EntityType<? extends EntityTFTowerGhast> type, World world) {
		super(type, world);

		this.wanderFactor = 16.0F;
		this.inTrapCounter = 0;
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(ATTACK_STATUS, (byte) 0);
		this.dataManager.register(ATTACK_TIMER, (byte) 0);
		this.dataManager.register(ATTACK_PREVTIMER, (byte) 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(5, new AIHomedFly(this));
		if (!(this instanceof EntityTFUrGhast)) this.goalSelector.addGoal(5, new AIRandomFly(this));
		this.goalSelector.addGoal(7, new GhastEntity.LookAroundGoal(this));
		this.goalSelector.addGoal(7, attackAI = new AIAttack(this));
		this.targetSelector.addGoal(1, new TFNearestPlayerGoal(this));
	}
	
	protected SoundEvent getAmbientSound() {
	      return TFSounds.GHASTGUARD_AMBIENT;
	   }

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return TFSounds.GHASTGUARD_HURT;
	   }

	protected SoundEvent getDeathSound() {
	      return TFSounds.GHASTGUARD_DEATH;
	   }

	// [VanillaCopy] from EntityGhast but we use wanderFactor instead, we also stop moving when we have a target
	public static class AIRandomFly extends Goal {
		private final EntityTFTowerGhast parentEntity;

		public AIRandomFly(EntityTFTowerGhast ghast) {
			this.parentEntity = ghast;
			this.setMutexFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean shouldExecute() {
			MovementController entitymovehelper = this.parentEntity.getMoveHelper();
			if (!entitymovehelper.isUpdating()) {
				return parentEntity.getAttackTarget() == null;
			} else {
				double d0 = entitymovehelper.getX() - this.parentEntity.getPosX();
				double d1 = entitymovehelper.getY() - this.parentEntity.getPosY();
				double d2 = entitymovehelper.getZ() - this.parentEntity.getPosZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return parentEntity.getAttackTarget() == null && (d3 < 1.0D || d3 > 3600.0D);
			}
		}

		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}

		@Override
		public void startExecuting() {
			Random random = this.parentEntity.getRNG();
			double d0 = this.parentEntity.getPosX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			double d1 = this.parentEntity.getPosY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			double d2 = this.parentEntity.getPosZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
		}
	}

	// [VanillaCopy]-ish mixture of EntityGhast.AIFly and EntityAIStayNearHome
	public static class AIHomedFly extends Goal {
		private final EntityTFTowerGhast parentEntity;

		AIHomedFly(EntityTFTowerGhast ghast) {
			this.parentEntity = ghast;
			setMutexFlags(EnumSet.of(Flag.MOVE));
		}

		// From AIFly, but with extra condition from AIStayNearHome
		@Override
		public boolean shouldExecute() {
			MovementController entitymovehelper = this.parentEntity.getMoveHelper();

			if (!entitymovehelper.isUpdating()) {
				return !this.parentEntity.isWithinHomeDistanceCurrentPosition();
			} else {
				double d0 = entitymovehelper.getX() - this.parentEntity.getPosX();
				double d1 = entitymovehelper.getY() - this.parentEntity.getPosY();
				double d2 = entitymovehelper.getZ() - this.parentEntity.getPosZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return (d3 < 1.0D || d3 > 3600.0D)
						&& !this.parentEntity.isWithinHomeDistanceCurrentPosition();
			}
		}

		// From AIFly
		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}

		// From AIStayNearHome but use move helper instead of PathNavigate
		@Override
		public void startExecuting() {
			Random random = this.parentEntity.getRNG();
			double d0 = this.parentEntity.getPosX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			double d1 = this.parentEntity.getPosY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			double d2 = this.parentEntity.getPosZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);

			if (this.parentEntity.getDistanceSq(Vector3d.copy(this.parentEntity.getHomePosition())) > 256.0D) {
				Vector3d vecToHome = Vector3d.copy(this.parentEntity.getHomePosition()).subtract(this.parentEntity.getPositionVec()).normalize();

				double targetX = this.parentEntity.getPosX() + vecToHome.x * parentEntity.wanderFactor + (double) ((this.parentEntity.rand.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
				double targetY = this.parentEntity.getPosY() + vecToHome.y * parentEntity.wanderFactor + (double) ((this.parentEntity.rand.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
				double targetZ = this.parentEntity.getPosZ() + vecToHome.z * parentEntity.wanderFactor + (double) ((this.parentEntity.rand.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);

				this.parentEntity.getMoveHelper().setMoveTo(targetX, targetY, targetZ, 1.0D);
			} else {
				this.parentEntity.getMoveHelper().setMoveTo(this.parentEntity.getHomePosition().getX() + 0.5D, this.parentEntity.getHomePosition().getY(), this.parentEntity.getHomePosition().getZ() + 0.5D, 1.0D);
			}
		}
	}

	// [VanillaCopy] EntityGhast.AIFireballAttack, edits noted
	public static class AIAttack extends Goal {
		private final EntityTFTowerGhast parentEntity;
		public int attackTimer;
		public int prevAttackTimer; // TF - add for renderer

		public AIAttack(EntityTFTowerGhast ghast) {
			this.parentEntity = ghast;
		}

		@Override
		public boolean shouldExecute() {
			return this.parentEntity.getAttackTarget() != null && parentEntity.shouldAttack(parentEntity.getAttackTarget());
		}

		@Override
		public void startExecuting() {
			this.attackTimer = this.prevAttackTimer = 0;
		}

		@Override
		public void resetTask() {
			this.parentEntity.setAttacking(false);
		}

		@Override
		public void tick() {
			LivingEntity entitylivingbase = this.parentEntity.getAttackTarget();

			if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D && this.parentEntity.getEntitySenses().canSee(entitylivingbase)) {
				World world = this.parentEntity.world;
				this.prevAttackTimer = attackTimer;
				++this.attackTimer;

				// TF face our target at all times
				this.parentEntity.getLookController().setLookPositionWithEntity(entitylivingbase, 10F, this.parentEntity.getVerticalFaceSpeed());

				if (this.attackTimer == 10) {
					parentEntity.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, parentEntity.getSoundPitch());
				}

				if (this.attackTimer == 20) {
					if (this.parentEntity.shouldAttack(entitylivingbase)) {
						// TF - call custom method
						parentEntity.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 10.0F, parentEntity.getSoundPitch());
						this.parentEntity.spitFireball();
						this.prevAttackTimer = attackTimer;
					}
					this.attackTimer = -40;
				}
			} else if (this.attackTimer > 0) {
				this.prevAttackTimer = attackTimer;
				--this.attackTimer;
			}

			this.parentEntity.setAttacking(this.attackTimer > 10);
		}
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return GhastEntity.func_234290_eH_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 30.0D)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 64.0D);
	}

	@Override
	protected float getSoundVolume() {
		return 0.5F;
	}

	@Override
	public int getTalkInterval() {
		return 160;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	@Override
	public void livingTick() {
		// age when in light, like mobs
		if (getBrightness() > 0.5F) {
			this.idleTime += 2;
		}

		if (this.rand.nextBoolean()) {
			this.world.addParticle(RedstoneParticleData.REDSTONE_DUST, this.getPosX() + (this.rand.nextDouble() - 0.5D) * (double) this.getWidth(), this.getPosY() + this.rand.nextDouble() * (double) this.getHeight() - 0.25D, this.getPosZ() + (this.rand.nextDouble() - 0.5D) * (double) this.getWidth(), 0, 0, 0);
		}

		super.livingTick();
	}

	@Override
	protected void updateAITasks() {
		findHome();

		if (this.inTrapCounter > 0) {
			this.inTrapCounter--;
			setAttackTarget(null);
		}

		int status = getAttackTarget() != null && shouldAttack(getAttackTarget()) ? 1 : 0;

		dataManager.set(ATTACK_STATUS, (byte) status);
		dataManager.set(ATTACK_TIMER, (byte) attackAI.attackTimer);
		dataManager.set(ATTACK_PREVTIMER, (byte) attackAI.prevAttackTimer);
	}

	public int getAttackStatus() {
		return dataManager.get(ATTACK_STATUS);
	}

	public int getAttackTimer() {
		return dataManager.get(ATTACK_TIMER);
	}

	public int getPrevAttackTimer() {
		return dataManager.get(ATTACK_PREVTIMER);
	}

	protected boolean shouldAttack(LivingEntity living) {
		return true;
	}

	/**
	 * Something is deeply wrong with the calculations based off of this value, so let's set it high enough that it's ignored.
	 */
	@Override
	public int getVerticalFaceSpeed() {
		return 500;
	}

	protected void spitFireball() {
		Vector3d vec3d = this.getLook(1.0F);
		double d2 = getAttackTarget().getPosX() - (this.getPosX() + vec3d.x * 4.0D);
		double d3 = getAttackTarget().getBoundingBox().minY + (double) (getAttackTarget().getHeight() / 2.0F) - (0.5D + this.getPosY() + (double) (this.getHeight() / 2.0F));
		double d4 = getAttackTarget().getPosZ() - (this.getPosZ() + vec3d.z * 4.0D);
		FireballEntity entitylargefireball = new FireballEntity(world, this, d2, d3, d4);
		entitylargefireball.explosionPower = this.getFireballStrength();
//		entitylargefireball.getPosX() = this.getPosX() + vec3d.x * 4.0D;
//		entitylargefireball.getY() = this.getY() + (double) (this.getHeight() / 2.0F) + 0.5D;
//		entitylargefireball.getPosZ() = this.getPosZ() + vec3d.z * 4.0D;
		entitylargefireball.setPosition(this.getPosX() + vec3d.x * 4.0D, this.getPosY() + (double) (this.getHeight() / 2.0F) + 0.5D, this.getPosZ() + vec3d.z * 4.0D);
		world.addEntity(entitylargefireball);

		// when we attack, there is a 1-in-6 chance we decide to stop attacking
		if (rand.nextInt(6) == 0) {
			setAttackTarget(null);
		}
	}

	public static boolean ghastSpawnHandler(EntityType<? extends EntityTFTowerGhast> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && canSpawnOn(entityType, world, reason, pos, random);
	}

	public boolean isNotColliding(IWorldReader world) {
		return world.checkNoEntityCollision(this) && !world.containsAnyLiquid(this.getBoundingBox()); //TODO: Verify
	}

	private void findHome() {
		if (!this.hasHome()) {
			int chunkX = MathHelper.floor(this.getPosX()) >> 4;
			int chunkZ = MathHelper.floor(this.getPosZ()) >> 4;

			TFFeature nearFeature = TFFeature.getFeatureForRegion(chunkX, chunkZ, (ServerWorld) this.world);

			if (nearFeature != TFFeature.DARK_TOWER) {
				this.detachHome();
				this.idleTime += 5;
			} else {
				BlockPos cc = TFFeature.getNearestCenterXYZ(chunkX, chunkZ);
				this.setHomePosAndDistance(cc.up(128), 64);
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
	public boolean isWithinHomeDistanceCurrentPosition() {
		return this.isWithinHomeDistanceFromPosition(getPosition());
	}

	@Override
	public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
		// TF - restrict valid y levels
		// Towers are so large, a simple radius doesn't really work, so we make it more of a cylinder
		return this.maximumHomeDistance == -1.0F
				? true
				: pos.getY() > 64 && pos.getY() < 210 && this.homePosition.distanceSq(pos) < (double) (this.maximumHomeDistance * this.maximumHomeDistance);
	}

	@Override
	public void setHomePosAndDistance(BlockPos pos, int distance) {
		this.homePosition = pos;
		this.maximumHomeDistance = (float) distance;
	}

	@Override
	public BlockPos getHomePosition() {
		return this.homePosition;
	}

	@Override
	public float getMaximumHomeDistance() {
		return this.maximumHomeDistance;
	}

	@Override
	public boolean detachHome() {
		this.maximumHomeDistance = -1.0F;
		return false;
	}

	public boolean hasHome() {
		return this.maximumHomeDistance != -1.0F;
	}
	// End copy
}

