package twilightforest.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFWorld;

public class EntityTFNaga extends EntityMob implements IEntityMultiPart {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/naga");
	private static final int TICKS_BEFORE_HEALING = 600;
	private static final int MAX_SEGMENTS = 12;
	private static final int LEASH_X = 46;
	private static final int LEASH_Y = 7;
	private static final int LEASH_Z = 46;
	private static final double DEFAULT_SPEED = 0.3;

	private int currentSegmentCount = 0; // not including head
	private final float healthPerSegment;
	private final EntityTFNagaSegment[] bodySegments = new EntityTFNagaSegment[MAX_SEGMENTS];
	private AIMovementPattern movementAI;
	private int ticksSinceDamaged = 0;

	private final BossInfoServer bossInfo = new BossInfoServer(getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.NOTCHED_10);

	private final AttributeModifier slowSpeed = new AttributeModifier("Naga Slow Speed", 0.25F, 0).setSaved(false);
	private final AttributeModifier fastSpeed = new AttributeModifier("Naga Fast Speed", 0.50F, 0).setSaved(false);


	public EntityTFNaga(World world) {
		super(world);
		this.setSize(1.75f, 3.0f);
		this.stepHeight = 2;
		this.healthPerSegment = getMaxHealth() / 10;
		this.experienceValue = 217;
		this.ignoreFrustumCheck = true;

		for (int i = 0; i < bodySegments.length; i++) {
			bodySegments[i] = new EntityTFNagaSegment(this, i);
		}

		this.goNormal();
	}

	private float getMaxHealthPerDifficulty() {
		switch (world.getDifficulty()) {
			case EASY:
				return 120;
			default:
			case NORMAL:
				return 200;
			case HARD:
				return 250;
		}
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new AIAttack(this));
		this.tasks.addTask(3, new AISmash(this));
		this.tasks.addTask(4, movementAI = new AIMovementPattern(this));
		this.tasks.addTask(8, new EntityAIWander(this, 1, 1) {
			@Override
			public void startExecuting() {
				EntityTFNaga.this.goNormal();
				super.startExecuting();
			}
			@Override
			protected Vec3d getPosition()
			{
				return RandomPositionGenerator.findRandomTarget(this.entity, 30, 7);
			}
		});
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));

		this.moveHelper = new NagaMoveHelper(this);
	}

	// Similar to EntityAIAttackMelee but simpler (no pathfinding)
	static class AIAttack extends EntityAIBase {
		private final EntityTFNaga taskOwner;
		private int attackTick = 20;

		AIAttack(EntityTFNaga taskOwner) {
			this.taskOwner = taskOwner;
		}

		@Override
		public boolean shouldExecute() {
			EntityLivingBase target = taskOwner.getAttackTarget();

			return target != null
					&& target.getEntityBoundingBox().maxY > taskOwner.getEntityBoundingBox().minY - 2.5
					&& target.getEntityBoundingBox().minY < taskOwner.getEntityBoundingBox().maxY + 2.5
					&& taskOwner.getDistanceSqToEntity(target) <= 4.0D
					&& taskOwner.getEntitySenses().canSee(target);

		}

		@Override
		public void updateTask() {
			if (attackTick > 0) {
				attackTick--;
			}
		}

		@Override
		public void resetTask() {
			attackTick = 20;
		}

		@Override
		public void startExecuting() {
			taskOwner.attackEntityAsMob(taskOwner.getAttackTarget());
			attackTick = 20;
		}
	}

	static class AISmash extends EntityAIBase {
		private final EntityTFNaga taskOwner;

		AISmash(EntityTFNaga taskOwner) {
			this.taskOwner = taskOwner;
		}

		@Override
		public boolean shouldExecute() {
			return taskOwner.world.getGameRules().getBoolean("mobGriefing") && taskOwner.getAttackTarget() != null && taskOwner.isCollidedHorizontally;
		}

		@Override
		public void startExecuting() {
			// NAGA SMASH!
			AxisAlignedBB bb = taskOwner.getEntityBoundingBox();
			int minx = MathHelper.floor(bb.minX - 0.5D);
			int miny = MathHelper.floor(bb.minY + 1.01D);
			int minz = MathHelper.floor(bb.minZ - 0.5D);
			int maxx = MathHelper.floor(bb.maxX + 0.5D);
			int maxy = MathHelper.floor(bb.maxY + 0.001D);
			int maxz = MathHelper.floor(bb.maxZ + 0.5D);
			if (taskOwner.getWorld().isAreaLoaded(new BlockPos(minx, miny, minz), new BlockPos(maxx, maxy, maxz))) {
				for (int dx = minx; dx <= maxx; dx++) {
					for (int dy = miny; dy <= maxy; dy++) {
						for (int dz = minz; dz <= maxz; dz++) {
							BlockPos pos = new BlockPos(dx, dy, dz);
							if (taskOwner.getWorld().getBlockState(pos).getBlockHardness(taskOwner.getWorld(), pos) >= 0)
								taskOwner.getWorld().destroyBlock(pos, true);
						}
					}
				}
			}
		}
	}

	enum MovementState {
		INTIMIDATE,
		CRUMBLE,
		CHARGE,
		CIRCLE
	}

	static class AIMovementPattern extends EntityAIBase {
		private final EntityTFNaga taskOwner;
		private MovementState movementState;
		private int stateCounter;
		private boolean clockwise;

		AIMovementPattern(EntityTFNaga taskOwner) {
			this.taskOwner = taskOwner;
			setMutexBits(3);
			resetTask();
		}

		@Override
		public boolean shouldExecute() {
			return taskOwner.getAttackTarget() != null;
		}

		@Override
		public void resetTask() {
			movementState = MovementState.CIRCLE;
			stateCounter = 15;
			clockwise = false;
		}

		@Override
		public void updateTask() {
			if (!taskOwner.getNavigator().noPath()) {
				// If we still have an uncompleted path don't run yet
				// This isn't in shouldExecute/shouldContinueExecuting because we don't want to reset the task
				// todo 1.10 there's a better way to do this I think
				return;
			}

			switch (movementState) {
				case INTIMIDATE: {
					taskOwner.getNavigator().clearPathEntity();
					taskOwner.getLookHelper().setLookPositionWithEntity(taskOwner.getAttackTarget(), 30F, 30F);
					taskOwner.faceEntity(taskOwner.getAttackTarget(), 30F, 30F);
					taskOwner.moveForward = 0.1f;
					break;
				}
				case CRUMBLE: {
					taskOwner.getNavigator().clearPathEntity();
					taskOwner.crumbleBelowTarget(2);
					taskOwner.crumbleBelowTarget(3);
					break;
				}
				case CHARGE: {
					BlockPos tpoint = taskOwner.findCirclePoint(clockwise, 14, Math.PI);
					taskOwner.getNavigator().tryMoveToXYZ(tpoint.getX(), tpoint.getY(), tpoint.getZ(), 1); // todo 1.10 check speed
					break;
				}
				case CIRCLE: {
					// normal radius is 13
					double radius = stateCounter % 2 == 0 ? 12.0 : 14.0;
					double rotation = 1; // in radians

					// hook out slightly before circling
					if (stateCounter > 1 && stateCounter < 3) {
						radius = 16;
					}

					// head almost straight at the player at the end
					if (stateCounter == 1) {
						rotation = 0.1;
					}

					BlockPos tpoint = taskOwner.findCirclePoint(clockwise, radius, rotation);
					taskOwner.getNavigator().tryMoveToXYZ(tpoint.getX(), tpoint.getY(), tpoint.getZ(), 1); // todo 1.10 check speed
					break;
				}
			}

			stateCounter--;
			if (stateCounter <= 0) {
				transitionState();
			}
		}

		private void transitionState() {
			switch (movementState) {
				case INTIMIDATE: {
					clockwise = !clockwise;

					if (taskOwner.getAttackTarget().getEntityBoundingBox().minY > taskOwner.getEntityBoundingBox().maxY) {
						doCrumblePlayer();
					} else {
						doCharge();
					}

					break;
				}
				case CRUMBLE:
					doCharge();
					break;
				case CHARGE:
					doCircle();
					break;
				case CIRCLE:
					doIntimidate();
					break;
			}
		}

		private void doCircle() {
			movementState = MovementState.CIRCLE;
			stateCounter += 10 + taskOwner.rand.nextInt(10);
			taskOwner.goNormal();
		}

		private void doCrumblePlayer() {
			movementState = MovementState.CRUMBLE;
			stateCounter = 20 + taskOwner.rand.nextInt(20);
			taskOwner.goSlow();
		}

		/**
		 * Charge the player.  Although the count is 3, we actually charge only 2 times.
		 */
		private void doCharge() {
			movementState = MovementState.CHARGE;
			stateCounter = 3;
			taskOwner.goFast();
		}

		private void doIntimidate() {
			movementState = MovementState.INTIMIDATE;
			taskOwner.playSound(TFSounds.NAGA_RATTLE, taskOwner.getSoundVolume() * 4F, taskOwner.getSoundPitch());

			stateCounter += 15 + taskOwner.rand.nextInt(10);
			taskOwner.goSlow();
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMaxHealthPerDifficulty());
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(DEFAULT_SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(80.0D);
	}

	/**
	 * Determine how many segments, from 2-12, the naga should have, dependent on its current health
	 */
	private void setSegmentsPerHealth() {
		int oldSegments = this.currentSegmentCount;
		int newSegments = MathHelper.clamp((int) ((this.getHealth() / healthPerSegment) + (getHealth() > 0 ? 2 : 0)), 0, MAX_SEGMENTS);
		this.currentSegmentCount = newSegments;
		if (newSegments < oldSegments) {
			for (int i = newSegments; i < oldSegments; i++) {
				bodySegments[i].selfDestruct();
			}
		} else if (newSegments > oldSegments) {
			this.activateBodySegments();
		}

		if (!world.isRemote) {
			double newSpeed = DEFAULT_SPEED - newSegments * (-0.2F / 12F);
			if (newSpeed < 0)
				newSpeed = 0;
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(newSpeed);
		}
	}

	@Override
	public boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean isInLava() {
		return false;
	}

	@Override
	public void onUpdate() {
		despawnIfPeaceful();

		if (deathTime > 0) {
			for (int k = 0; k < 5; k++) {
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				EnumParticleTypes explosionType = rand.nextBoolean() ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_NORMAL;

				world.spawnParticle(explosionType, (posX + rand.nextFloat() * width * 2.0F) - width, posY + rand.nextFloat() * height, (posZ + rand.nextFloat() * width * 2.0F) - width, d, d1, d2);
			}
		}

		// update health
		this.ticksSinceDamaged++;

		if (!this.world.isRemote && this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 20 == 0) {
			this.heal(1);
		}

		setSegmentsPerHealth();

		super.onUpdate();

		// update bodySegments parts
		for (EntityTFNagaSegment segment : bodySegments) {
			this.world.updateEntityWithOptionalForce(segment, true);
		}

		moveSegments();
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		if (getAttackTarget() != null &&
				(getDistanceSqToEntity(getAttackTarget()) > 80 * 80 || !this.isEntityWithinHomeArea(getAttackTarget()))) {
			setAttackTarget(null);
		}

		// if we are very close to the path point, go to the next point, unless the path is finished
		// TODO 1.10 this runs after the path navigator runs, is that okay?
		double d = width * 4.0F;
		Vec3d vec3d = hasPath() ? getNavigator().getPath().getPosition(this) : null;

		while (vec3d != null && vec3d.squareDistanceTo(posX, vec3d.y, posZ) < d * d) {
			getNavigator().getPath().incrementPathIndex();

			if (getNavigator().getPath().isFinished()) {
				vec3d = null;
			} else {
				vec3d = getNavigator().getPath().getPosition(this);
			}
		}

		if (!isWithinHomeDistanceCurrentPosition()) {
			setAttackTarget(null);
			getNavigator().setPath(getNavigator().getPathToPos(getHomePosition()), 1.0F);
		}

		// BOSS BAR!
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

	static class NagaMoveHelper extends EntityMoveHelper {
		public NagaMoveHelper(EntityLiving naga) {
			super(naga);
		}

		@Override
		public void onUpdateMoveHelper() {
			// TF - slither!
			MovementState currentState = ((EntityTFNaga) entity).movementAI.movementState;
			if (currentState != MovementState.CHARGE && currentState != MovementState.INTIMIDATE) {
				this.entity.moveStrafing = MathHelper.cos(this.entity.ticksExisted * 0.3F) * 0.6F;
			} else {
				this.entity.moveStrafing *= 0.8F;
			}

			super.onUpdateMoveHelper();
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.NAGA_HISS;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.NAGA_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.NAGA_HURT;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	private void crumbleBelowTarget(int range) {
		if (!world.getGameRules().getBoolean("mobGriefing"))
			return;

		int floor = (int) getEntityBoundingBox().minY;
		int targetY = (int) getAttackTarget().getEntityBoundingBox().minY;

		if (targetY > floor) {
			int dx = (int) getAttackTarget().posX + rand.nextInt(range) - rand.nextInt(range);
			int dz = (int) getAttackTarget().posZ + rand.nextInt(range) - rand.nextInt(range);
			int dy = targetY - rand.nextInt(range) + rand.nextInt(range > 1 ? range - 1 : range);

			if (dy <= floor) {
				dy = targetY;
			}

			BlockPos pos = new BlockPos(dx, dy, dz);

			if (!world.isAirBlock(pos)) {
				// todo limit what can be broken
				world.destroyBlock(pos, true);

				// sparkle!!
				for (int k = 0; k < 20; k++) {
					double d = rand.nextGaussian() * 0.02D;
					double d1 = rand.nextGaussian() * 0.02D;
					double d2 = rand.nextGaussian() * 0.02D;

					world.spawnParticle(EnumParticleTypes.CRIT, (posX + rand.nextFloat() * width * 2.0F) - width, posY + rand.nextFloat() * height, (posZ + rand.nextFloat() * width * 2.0F) - width, d, d1, d2);
				}
			}
		}
	}

	/**
	 * Sets the naga to move slowly, such as when he is intimidating the player
	 */
	private void goSlow() {
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(slowSpeed); // if we apply this twice, we crash, but we can always remove it
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(fastSpeed);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(slowSpeed);
	}

	/**
	 * Normal speed, like when he is circling
	 */
	private void goNormal() {
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(slowSpeed);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(fastSpeed);
	}

	/**
	 * Fast, like when he is charging
	 */
	private void goFast() {
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(slowSpeed);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(fastSpeed);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(fastSpeed);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	/**
	 * Finds a point that allows us to circle the target clockwise.
	 */
	private BlockPos findCirclePoint(boolean clockwise, double radius, double rotation) {
		EntityLivingBase toCircle = getAttackTarget();

		// compute angle
		double vecx = posX - toCircle.posX;
		double vecz = posZ - toCircle.posZ;
		float rangle = (float) (Math.atan2(vecz, vecx));

		// add a little, so he circles (clockwise)
		rangle += clockwise ? rotation : -rotation;

		// figure out where we're headed from the target angle
		double dx = MathHelper.cos(rangle) * radius;
		double dz = MathHelper.sin(rangle) * radius;

		double dy = Math.min(getEntityBoundingBox().minY, toCircle.posY);

		// add that to the target entity's position, and we have our destination
		return new BlockPos(toCircle.posX + dx, dy, toCircle.posZ + dz);
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource src) {
		return src.getTrueSource() != null && !this.isEntityWithinHomeArea(src.getTrueSource()) // reject damage from outside of our home radius
				|| src.getImmediateSource() != null && !this.isEntityWithinHomeArea(src.getImmediateSource())
				|| src.isFireDamage() || src.isExplosion() || super.isEntityInvulnerable(src);
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		if (super.attackEntityFrom(damagesource, i)) {
			this.ticksSinceDamaged = 0;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity toAttack) {
		boolean result = super.attackEntityAsMob(toAttack);

		if (result) {
			// charging, apply extra pushback
			toAttack.addVelocity(-MathHelper.sin((rotationYaw * 3.141593F) / 180F) * 2.0F, 0.4F, MathHelper.cos((rotationYaw * 3.141593F) / 180F) * 2.0F);
		}

		return result;
	}

	@Override
	public float getBlockPathWeight(BlockPos pos) {
		if (!this.isWithinHomeDistanceFromPosition(pos)) {
			return Float.MIN_VALUE;
		} else {
			return 0.0F;
		}
	}

	private void despawnIfPeaceful() {
		if (!world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			if (hasHome()) {
				BlockPos home = this.getHomePosition();
				world.setBlockState(home, TFBlocks.bossSpawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.NAGA));
			}

			setDead();
		}
	}

	@Override
	public void setDead() {
		super.setDead();
		for (EntityTFNagaSegment seg : bodySegments) {
			// must use this instead of setDead
			// since multiparts are not added to the world tick list which is what checks isDead
			this.world.removeEntityDangerously(seg);
		}
	}

	@Override
	public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
		if (this.getMaximumHomeDistance() == -1) {
			return true;
		} else {
			int distX = Math.abs(this.getHomePosition().getX() - pos.getX());
			int distY = Math.abs(this.getHomePosition().getY() - pos.getY());
			int distZ = Math.abs(this.getHomePosition().getZ() - pos.getZ());

			return distX <= LEASH_X && distY <= LEASH_Y && distZ <= LEASH_Z;
		}
	}

	private boolean isEntityWithinHomeArea(Entity entity) {
		return isWithinHomeDistanceFromPosition(new BlockPos(entity));
	}

	private void activateBodySegments() {
		for (int i = 0; i < currentSegmentCount; i++) {
			EntityTFNagaSegment segment = bodySegments[i];
			segment.activate();
			segment.setLocationAndAngles(posX + 0.1 * i, posY + 0.5D, posZ + 0.1 * i, rand.nextFloat() * 360F, 0.0F);
			for (int j = 0; j < 20; j++) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
						segment.posX + (double) (this.rand.nextFloat() * segment.width * 2.0F) - (double) segment.width - d0 * 10.0D,
						segment.posY + (double) (this.rand.nextFloat() * segment.height) - d1 * 10.0D,
						segment.posZ + (double) (this.rand.nextFloat() * segment.width * 2.0F) - (double) segment.width - d2 * 10.0D,
						d0, d1, d2);
			}
		}
	}

	/**
	 * Sets the heading (ha ha) of the bodySegments segments
	 */
	private void moveSegments() {
		for (int i = 0; i < this.bodySegments.length; i++) {
			Entity leader = i == 0 ? this : this.bodySegments[i - 1];
			double followX = leader.posX;
			double followY = leader.posY;
			double followZ = leader.posZ;

			// also weight the position so that the segments straighten out a little bit, and the front ones straighten more
			float angle = (((leader.rotationYaw + 180) * 3.141593F) / 180F);


			double straightenForce = 0.05D + (1.0 / (float) (i + 1)) * 0.5D;

			double idealX = -MathHelper.sin(angle) * straightenForce;
			double idealZ = MathHelper.cos(angle) * straightenForce;


			Vec3d diff = new Vec3d(bodySegments[i].posX - followX, bodySegments[i].posY - followY, bodySegments[i].posZ - followZ);
			diff = diff.normalize();

			// weight so segments drift towards their ideal position
			diff = diff.addVector(idealX, 0, idealZ).normalize();

			double f = 2.0D;

			double destX = followX + f * diff.x;
			double destY = followY + f * diff.y;
			double destZ = followZ + f * diff.z;

			bodySegments[i].setPosition(destX, destY, destZ);

			double distance = (double) MathHelper.sqrt(diff.x * diff.x + diff.z * diff.z);

			if (i == 0) {
				// tilt segment next to head up towards head
				diff = diff.addVector(0, -0.15, 0);
			}

			bodySegments[i].setRotation((float) (Math.atan2(diff.z, diff.x) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(diff.y, distance) * 180.0D / Math.PI));
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		if (hasHome()) {
			BlockPos home = this.getHomePosition();
			nbttagcompound.setTag("Home", new NBTTagIntArray(new int[]{home.getX(), home.getY(), home.getZ()}));
		}

		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);

		if (nbttagcompound.hasKey("Home", Constants.NBT.TAG_INT_ARRAY)) {
			int[] home = nbttagcompound.getIntArray("Home");
			this.setHomePosAndDistance(new BlockPos(home[0], home[1], home[2]), 20);
		} else {
			this.detachHome();
		}

		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		// mark the courtyard as defeated
		if (!world.isRemote && TFWorld.getChunkGenerator(world) instanceof ChunkGeneratorTwilightForest) {
			int dx = MathHelper.floor(this.posX);
			int dy = MathHelper.floor(this.posY);
			int dz = MathHelper.floor(this.posZ);

			ChunkGeneratorTwilightForest generator = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(world);
			TFFeature nearbyFeature = TFFeature.getFeatureAt(dx, dz, world);

			if (nearbyFeature == TFFeature.nagaCourtyard) {
				generator.setStructureConquered(dx, dy, dz, true);
			}
		}

	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource src, float damage) {
		return attackEntityFrom(src, damage);
	}

	@Override
	public Entity[] getParts() {
		return bodySegments;
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
