package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.entity.ai.EntityAITFFindEntityNearestPlayer;
import twilightforest.entity.boss.EntityTFUrGhast;

import java.util.Random;

public class EntityTFTowerGhast extends EntityGhast {
	// 0 = idle, 1 = eyes open / tracking player, 2 = shooting fireball
	private static final DataParameter<Byte> ATTACK_STATUS = EntityDataManager.createKey(EntityTFTowerGhast.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> ATTACK_TIMER = EntityDataManager.createKey(EntityTFTowerGhast.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> ATTACK_PREVTIMER = EntityDataManager.createKey(EntityTFTowerGhast.class, DataSerializers.BYTE);

	private AIAttack attackAI;
	protected float wanderFactor;
	private int inTrapCounter;

	public EntityTFTowerGhast(World par1World) {
		super(par1World);
		this.setSize(4.0F, 6.0F);

		this.wanderFactor = 16.0F;
		this.inTrapCounter = 0;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ATTACK_STATUS, (byte) 0);
		this.dataManager.register(ATTACK_TIMER, (byte) 0);
		this.dataManager.register(ATTACK_PREVTIMER, (byte) 0);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(5, new AIHomedFly(this));
		if (!(this instanceof EntityTFUrGhast)) this.tasks.addTask(5, new AIRandomFly(this));
		this.tasks.addTask(7, new EntityGhast.AILookAround(this));
		this.tasks.addTask(7, attackAI = new AIAttack(this));
		this.targetTasks.addTask(1, new EntityAITFFindEntityNearestPlayer(this));
	}

	// [VanillaCopy] from EntityGhast but we use wanderFactor instead, we also stop moving when we have a target
	public static class AIRandomFly extends EntityAIBase {
		private final EntityTFTowerGhast parentEntity;

		public AIRandomFly(EntityTFTowerGhast ghast) {
			this.parentEntity = ghast;
			this.setMutexBits(1);
		}

		@Override
		public boolean shouldExecute() {
			EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
			if (!entitymovehelper.isUpdating()) {
				return parentEntity.getAttackTarget() == null;
			} else {
				double d0 = entitymovehelper.getX() - this.parentEntity.posX;
				double d1 = entitymovehelper.getY() - this.parentEntity.posY;
				double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
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
			double d0 = this.parentEntity.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			double d1 = this.parentEntity.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			double d2 = this.parentEntity.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
		}
	}

	// [VanillaCopy]-ish mixture of EntityGhast.AIFly and EntityAIStayNearHome
	public static class AIHomedFly extends EntityAIBase {
		private final EntityTFTowerGhast parentEntity;

		AIHomedFly(EntityTFTowerGhast ghast) {
			this.parentEntity = ghast;
			setMutexBits(1);
		}

		// From AIFly, but with extra condition from AIStayNearHome
		@Override
		public boolean shouldExecute() {
			EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

			if (!entitymovehelper.isUpdating()) {
				return !this.parentEntity.isWithinHomeDistanceCurrentPosition();
			} else {
				double d0 = entitymovehelper.getX() - this.parentEntity.posX;
				double d1 = entitymovehelper.getY() - this.parentEntity.posY;
				double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
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
			double d0 = this.parentEntity.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			double d1 = this.parentEntity.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			double d2 = this.parentEntity.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
			this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);

			if (this.parentEntity.getDistanceSq(this.parentEntity.getHomePosition()) > 256.0D) {
				Vec3d vecToHome = new Vec3d(this.parentEntity.getHomePosition()).subtract(this.parentEntity.getPositionVector()).normalize();

				double targetX = this.parentEntity.posX + vecToHome.x * parentEntity.wanderFactor + (double) ((this.parentEntity.rand.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
				double targetY = this.parentEntity.posY + vecToHome.y * parentEntity.wanderFactor + (double) ((this.parentEntity.rand.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);
				double targetZ = this.parentEntity.posZ + vecToHome.z * parentEntity.wanderFactor + (double) ((this.parentEntity.rand.nextFloat() * 2.0F - 1.0F) * parentEntity.wanderFactor);

				this.parentEntity.getMoveHelper().setMoveTo(targetX, targetY, targetZ, 1.0D);
			} else {
				this.parentEntity.getMoveHelper().setMoveTo(this.parentEntity.getHomePosition().getX() + 0.5D, this.parentEntity.getHomePosition().getY(), this.parentEntity.getHomePosition().getZ() + 0.5D, 1.0D);
			}
		}
	}

	// [VanillaCopy] EntityGhast.AIFireballAttack, edits noted
	public static class AIAttack extends EntityAIBase {
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
		public void updateTask() {
			EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();

			if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < 4096.0D && this.parentEntity.getEntitySenses().canSee(entitylivingbase)) {
				World world = this.parentEntity.world;
				this.prevAttackTimer = attackTimer;
				++this.attackTimer;

				// TF face our target at all times
				this.parentEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10F, this.parentEntity.getVerticalFaceSpeed());

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

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
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
	public void onLivingUpdate() {
		// age when in light, like mobs
		if (getBrightness() > 0.5F) {
			this.idleTime += 2;
		}

		if (this.rand.nextBoolean()) {
			this.world.spawnParticle(EnumParticleTypes.REDSTONE, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0, 0, 0);
		}

		super.onLivingUpdate();
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

	protected boolean shouldAttack(EntityLivingBase living) {
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
		Vec3d vec3d = this.getLook(1.0F);
		double d2 = getAttackTarget().posX - (this.posX + vec3d.x * 4.0D);
		double d3 = getAttackTarget().getEntityBoundingBox().minY + (double) (getAttackTarget().height / 2.0F) - (0.5D + this.posY + (double) (this.height / 2.0F));
		double d4 = getAttackTarget().posZ - (this.posZ + vec3d.z * 4.0D);
		EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, this, d2, d3, d4);
		entitylargefireball.explosionPower = this.getFireballStrength();
		entitylargefireball.posX = this.posX + vec3d.x * 4.0D;
		entitylargefireball.posY = this.posY + (double) (this.height / 2.0F) + 0.5D;
		entitylargefireball.posZ = this.posZ + vec3d.z * 4.0D;
		world.spawnEntity(entitylargefireball);

		// when we attack, there is a 1-in-6 chance we decide to stop attacking
		if (rand.nextInt(6) == 0) {
			setAttackTarget(null);
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		return this.world.checkNoEntityCollision(getEntityBoundingBox())
				&& this.world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty()
				&& !this.world.containsAnyLiquid(getEntityBoundingBox())
				&& this.world.getDifficulty() != EnumDifficulty.PEACEFUL
				&& this.isValidLightLevel();
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	protected boolean isValidLightLevel() {
		return true;
	}

	private void findHome() {
		if (!this.hasHome()) {
			int chunkX = MathHelper.floor(this.posX) >> 4;
			int chunkZ = MathHelper.floor(this.posZ) >> 4;

			TFFeature nearFeature = TFFeature.getFeatureForRegion(chunkX, chunkZ, this.world);

			if (nearFeature != TFFeature.darkTower) {
				this.detachHome();
				this.idleTime += 5;
			} else {
				BlockPos cc = TFFeature.getNearestCenterXYZ(chunkX, chunkZ, world);
				this.setHomePosAndDistance(cc.up(128), 64);
			}
		}
	}

	public void setInTrap() {
		this.inTrapCounter = 10;
	}

	// [VanillaCopy] Home fields and methods from EntityCreature, changes noted
	private BlockPos homePosition = BlockPos.ORIGIN;
	private float maximumHomeDistance = -1.0F;

	public boolean isWithinHomeDistanceCurrentPosition() {
		return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
	}

	public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
		// TF - restrict valid y levels
		// Towers are so large, a simple radius doesn't really work, so we make it more of a cylinder
		return this.maximumHomeDistance == -1.0F
				? true
				: pos.getY() > 64 && pos.getY() < 210 && this.homePosition.distanceSq(pos) < (double) (this.maximumHomeDistance * this.maximumHomeDistance);
	}

	public void setHomePosAndDistance(BlockPos pos, int distance) {
		this.homePosition = pos;
		this.maximumHomeDistance = (float) distance;
	}

	public BlockPos getHomePosition() {
		return this.homePosition;
	}

	public float getMaximumHomeDistance() {
		return this.maximumHomeDistance;
	}

	public void detachHome() {
		this.maximumHomeDistance = -1.0F;
	}

	public boolean hasHome() {
		return this.maximumHomeDistance != -1.0F;
	}
	// End copy
}

