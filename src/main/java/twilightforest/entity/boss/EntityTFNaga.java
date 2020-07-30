package twilightforest.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.PacketDistributor;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.entity.IEntityMultiPart;
import twilightforest.entity.MultiPartEntityPart;
import twilightforest.entity.TFEntities;
import twilightforest.enums.BossVariant;
import twilightforest.network.PacketThrowPlayer;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.EntityUtil;
import twilightforest.world.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityTFNaga extends MonsterEntity implements IEntityMultiPart {

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

	private final ServerBossInfo bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.NOTCHED_10);

	private final AttributeModifier slowSpeed = new AttributeModifier("Naga Slow Speed", 0.25F, AttributeModifier.Operation.ADDITION);
	private final AttributeModifier fastSpeed = new AttributeModifier("Naga Fast Speed", 0.50F, AttributeModifier.Operation.ADDITION);

	private static final DataParameter<Boolean> DATA_DAZE = EntityDataManager.createKey(EntityTFNaga.class, DataSerializers.BOOLEAN);

	public EntityTFNaga(EntityType<? extends EntityTFNaga> type, World world) {
		super(type, world);
		this.stepHeight = 2;
		this.healthPerSegment = getMaxHealth() / 10;
		this.experienceValue = 217;
		this.ignoreFrustumCheck = true;

		for (int i = 0; i < bodySegments.length; i++) {
			bodySegments[i] = TFEntities.naga_segment.create(world);
		}

		this.goNormal();
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(DATA_DAZE, false);
	}

	public boolean isDazed() {
		return dataManager.get(DATA_DAZE);
	}

	protected void setDazed(boolean daze) {
		dataManager.set(DATA_DAZE, daze);
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
	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public boolean canDespawn(double p_213397_1_) {
		return false;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new AIAttack(this));
		this.goalSelector.addGoal(3, new AISmash(this));
		this.goalSelector.addGoal(4, movementAI = new AIMovementPattern(this));
		this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1, 1) {
			@Override
			public void startExecuting() {
				EntityTFNaga.this.goNormal();
				super.startExecuting();
			}

			@Override
			protected Vector3d getPosition() {
				return RandomPositionGenerator.findRandomTarget(this.creature, 30, 7);
			}
		});
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));

		this.moveController = new NagaMoveHelper(this);
	}

	// Similar to MeleeAttackGoal but simpler (no pathfinding)
	static class AIAttack extends Goal {

		private final EntityTFNaga taskOwner;
		private int attackTick = 20;

		AIAttack(EntityTFNaga taskOwner) {
			this.taskOwner = taskOwner;
		}

		@Override
		public boolean shouldExecute() {
			LivingEntity target = taskOwner.getAttackTarget();

			return target != null
					&& target.getBoundingBox().maxY > taskOwner.getBoundingBox().minY - 2.5
					&& target.getBoundingBox().minY < taskOwner.getBoundingBox().maxY + 2.5
					&& taskOwner.getDistanceSq(target) <= 4.0D
					&& taskOwner.getEntitySenses().canSee(target);

		}

		@Override
		public void tick() {
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

	static class AISmash extends Goal {

		private final EntityTFNaga taskOwner;

		AISmash(EntityTFNaga taskOwner) {
			this.taskOwner = taskOwner;
		}

		@Override
		public boolean shouldExecute() {
			return /*taskOwner.getAttackTarget() != null &&*/ taskOwner.collidedHorizontally && ForgeEventFactory.getMobGriefingEvent(taskOwner.world, taskOwner);
		}

		@Override
		public void startExecuting() {
			// NAGA SMASH!
			if (taskOwner.world.isRemote) return;

			AxisAlignedBB bb = taskOwner.getBoundingBox();

			int minx = MathHelper.floor(bb.minX - 0.75D);
			int miny = MathHelper.floor(bb.minY + 1.01D);
			int minz = MathHelper.floor(bb.minZ - 0.75D);
			int maxx = MathHelper.floor(bb.maxX + 0.75D);
			int maxy = MathHelper.floor(bb.maxY + 0.0D);
			int maxz = MathHelper.floor(bb.maxZ + 0.75D);

			BlockPos min = new BlockPos(minx, miny, minz);
			BlockPos max = new BlockPos(maxx, maxy, maxz);

			if (taskOwner.world.isAreaLoaded(min, max)) {
				for (BlockPos pos : BlockPos.getAllInBoxMutable(min, max)) {
					if (EntityUtil.canDestroyBlock(taskOwner.world, pos, taskOwner)) {
						taskOwner.world.destroyBlock(pos, true);
					}
				}
			}
		}
	}

	enum MovementState {
		INTIMIDATE,
		CRUMBLE,
		CHARGE,
		CIRCLE,
		DAZE
	}

	static class AIMovementPattern extends Goal {

		private final EntityTFNaga taskOwner;
		private MovementState movementState;
		private int stateCounter;
		private boolean clockwise;

		AIMovementPattern(EntityTFNaga taskOwner) {
			this.taskOwner = taskOwner;
			setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
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
		public void tick() {
			if (!taskOwner.getNavigator().noPath()) {
				// If we still have an uncompleted path don't run yet
				// This isn't in shouldExecute/shouldContinueExecuting because we don't want to reset the task
				// todo 1.10 there's a better way to do this I think
				taskOwner.setDazed(false); // Since we have a path, we shouldn't be dazed anymore.
				return;
			}

			switch (movementState) {
				case INTIMIDATE: {
					taskOwner.getNavigator().clearPath();
					taskOwner.getLookController().setLookPositionWithEntity(taskOwner.getAttackTarget(), 30F, 30F);
					taskOwner.faceEntity(taskOwner.getAttackTarget(), 30F, 30F);
					taskOwner.moveForward = 0.1f;
					break;
				}
				case CRUMBLE: {
					taskOwner.getNavigator().clearPath();
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
				case DAZE: {
					taskOwner.setDazed(true);
					break;
				}
			}

			stateCounter--;
			if (stateCounter <= 0) {
				transitionState();
			}
		}

		private void transitionState() {
			taskOwner.setDazed(false);
			switch (movementState) {
				case INTIMIDATE: {
					clockwise = !clockwise;

					if (taskOwner.getAttackTarget().getBoundingBox().minY > taskOwner.getBoundingBox().maxY) {
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
				case DAZE:
					doCircle();
					break;
			}
		}

		private void doDaze() {
			movementState = MovementState.DAZE;
			taskOwner.getNavigator().clearPath();
			stateCounter = 60 + taskOwner.rand.nextInt(40);
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
	public void livingTick() {

		super.livingTick();

		if (world.isRemote || !ForgeEventFactory.getMobGriefingEvent(world, this)) return;

		AxisAlignedBB bb = this.getBoundingBox();

		int minx = MathHelper.floor(bb.minX - 0.75D);
		int miny = MathHelper.floor(bb.minY + 1.01D);
		int minz = MathHelper.floor(bb.minZ - 0.75D);
		int maxx = MathHelper.floor(bb.maxX + 0.75D);
		int maxy = MathHelper.floor(bb.maxY + 0.0D);
		int maxz = MathHelper.floor(bb.maxZ + 0.75D);

		BlockPos min = new BlockPos(minx, miny, minz);
		BlockPos max = new BlockPos(maxx, maxy, maxz);

		if (world.isAreaLoaded(min, max)) {
			for (BlockPos pos : BlockPos.getAllInBoxMutable(min, max)) {
				BlockState state = world.getBlockState(pos);
				if (state.getMaterial() == Material.LEAVES && EntityUtil.canDestroyBlock(world, pos, state, this)) {
					world.destroyBlock(pos, true);
				}
			}
		}
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.func_233815_a_(Attributes.MAX_HEALTH, /*getMaxHealthPerDifficulty()*/ 200) //TODO: We're static now
				.func_233815_a_(Attributes.MOVEMENT_SPEED, DEFAULT_SPEED)
				.func_233815_a_(Attributes.ATTACK_DAMAGE, 5.0D)
				.func_233815_a_(Attributes.FOLLOW_RANGE, 80.0D);
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
			this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(newSpeed);
		}
	}

	@Override
	public boolean isSteppingCarefully() {
		return false;
	}

	@Override
	public boolean isInLava() {
		return false;
	}

	@Override
	public void tick() {
		if (deathTime > 0) {
			for (int k = 0; k < 5; k++) {
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				world.addParticle((rand.nextBoolean() ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION), (getPosX() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(), getPosY() + rand.nextFloat() * getHeight(), (getPosZ() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(), d, d1, d2);
			}
		}

		// update health
		this.ticksSinceDamaged++;

		if (!this.world.isRemote && this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 20 == 0) {
			this.heal(1);
		}

		setSegmentsPerHealth();

		super.tick();

		// update bodySegments parts
		if (this.world instanceof ServerWorld && isAlive()) {
			ServerWorld serverWorld = (ServerWorld) this.world;
			for (EntityTFNagaSegment segment : bodySegments) {
				if (!segment.isAddedToWorld()) {
					segment.setParentUUID(this.getUniqueID());
					segment.setParentId(this.getEntityId());
					serverWorld.addEntity(segment);
				}

			}
		}

		moveSegments();
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		if (getAttackTarget() != null && (getDistanceSq(getAttackTarget()) > 80 * 80 || !this.isEntityWithinHomeArea(getAttackTarget()))) {
			setAttackTarget(null);
		}

		// if we are very close to the path point, go to the next point, unless the path is finished
		// TODO 1.10 this runs after the path navigator runs, is that okay?
		double d = getWidth() * 4.0F;
		Vector3d vec3d = hasPath() ? getNavigator().getPath().getPosition(this) : null;

		while (vec3d != null && vec3d.squareDistanceTo(getPosX(), vec3d.y, getPosZ()) < d * d) {
			getNavigator().getPath().incrementPathIndex();

			if (getNavigator().getPath().isFinished()) {
				vec3d = null;
			} else {
				vec3d = getNavigator().getPath().getPosition(this);
			}
		}

		if (!isWithinHomeDistanceCurrentPosition()) {
			setAttackTarget(null);
			getNavigator().setPath(getNavigator().getPathToPos(getHomePosition(), 0), 1.0F);
		}

		// BOSS BAR!
		this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
	}

	static class NagaMoveHelper extends MovementController {

		public NagaMoveHelper(MobEntity naga) {
			super(naga);
		}

		@Override
		public void tick() {
			// TF - slither!
			MovementState currentState = ((EntityTFNaga) mob).movementAI.movementState;
			if (currentState == MovementState.DAZE) {
				this.mob.moveStrafing = 0F;
			} else if (currentState != MovementState.CHARGE && currentState != MovementState.INTIMIDATE) {
				this.mob.moveStrafing = MathHelper.cos(this.mob.ticksExisted * 0.3F) * 0.6F;
			} else {
				this.mob.moveStrafing *= 0.8F;
			}

			super.tick();
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

	private void crumbleBelowTarget(int range) {
		if (!ForgeEventFactory.getMobGriefingEvent(world, this)) return;

		int floor = (int) getBoundingBox().minY;
		int targetY = (int) getAttackTarget().getBoundingBox().minY;

		if (targetY > floor) {
			int dx = (int) getAttackTarget().getPosX() + rand.nextInt(range) - rand.nextInt(range);
			int dz = (int) getAttackTarget().getPosZ() + rand.nextInt(range) - rand.nextInt(range);
			int dy = targetY - rand.nextInt(range) + rand.nextInt(range > 1 ? range - 1 : range);

			if (dy <= floor) {
				dy = targetY;
			}

			BlockPos pos = new BlockPos(dx, dy, dz);

			if (EntityUtil.canDestroyBlock(world, pos, this)) {
				// todo limit what can be broken
				world.destroyBlock(pos, true);

				// sparkle!!
				for (int k = 0; k < 20; k++) {
					double d = rand.nextGaussian() * 0.02D;
					double d1 = rand.nextGaussian() * 0.02D;
					double d2 = rand.nextGaussian() * 0.02D;

					world.addParticle(ParticleTypes.CRIT, (getPosX() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(), getPosY() + rand.nextFloat() * getHeight(), (getPosZ() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(), d, d1, d2);
				}
			}
		}
	}

	/**
	 * Sets the naga to move slowly, such as when he is intimidating the player
	 */
	private void goSlow() {
		this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(slowSpeed); // if we apply this twice, we crash, but we can always remove it
		this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(fastSpeed);
		this.getAttribute(Attributes.MOVEMENT_SPEED).func_233767_b_(slowSpeed);
	}

	/**
	 * Normal speed, like when he is circling
	 */
	private void goNormal() {
		this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(slowSpeed);
		this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(fastSpeed);
	}

	/**
	 * Fast, like when he is charging
	 */
	private void goFast() {
		this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(slowSpeed);
		this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(fastSpeed);
		this.getAttribute(Attributes.MOVEMENT_SPEED).func_233767_b_(fastSpeed);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	/**
	 * Finds a point that allows us to circle the target clockwise.
	 */
	private BlockPos findCirclePoint(boolean clockwise, double radius, double rotation) {
		LivingEntity toCircle = getAttackTarget();

		// compute angle
		double vecx = getPosX() - toCircle.getPosX();
		double vecz = getPosZ() - toCircle.getPosZ();
		float rangle = (float) (Math.atan2(vecz, vecx));

		// add a little, so he circles (clockwise)
		rangle += clockwise ? rotation : -rotation;

		// figure out where we're headed from the target angle
		double dx = MathHelper.cos(rangle) * radius;
		double dz = MathHelper.sin(rangle) * radius;

		double dy = Math.min(getBoundingBox().minY, toCircle.getPosY());

		// add that to the target entity's position, and we have our destination
		return new BlockPos(toCircle.getPosX() + dx, dy, toCircle.getPosZ() + dz);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource src) {
		return src.getTrueSource() != null && !this.isEntityWithinHomeArea(src.getTrueSource()) // reject damage from outside of our home radius
				|| src.getImmediateSource() != null && !this.isEntityWithinHomeArea(src.getImmediateSource())
				|| src.isFireDamage() || src.isExplosion() || super.isInvulnerableTo(src);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source != DamageSource.FALL && super.attackEntityFrom(source, amount)) {
			this.ticksSinceDamaged = 0;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity toAttack) {
		if (movementAI.movementState == MovementState.CHARGE && toAttack instanceof LivingEntity && ((LivingEntity) toAttack).isActiveItemStackBlocking()) {
			Vector3d motion = this.getMotion();
			toAttack.addVelocity(motion.x * 1.25D, 0.5D, motion.z * 1.25D);
			this.setMotion(motion.x * -1.5D, motion.y + 0.5D, motion.z * -1.5D);
			if (toAttack instanceof ServerPlayerEntity)
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) toAttack), new PacketThrowPlayer((float) toAttack.getMotion().getX(), (float) toAttack.getMotion().getY(), (float) toAttack.getMotion().getZ()));
			attackEntityFrom(DamageSource.GENERIC, 4F);
			world.playSound(null, toAttack.func_233580_cy_(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0F, 0.8F + this.world.rand.nextFloat() * 0.4F);
			movementAI.doDaze();
			return false;
		}
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

	@Override
	public void checkDespawn() {
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			if (getHomePosition() != BlockPos.ZERO) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner.get().getDefaultState().with(BlockTFBossSpawner.VARIANT, BossVariant.NAGA));
			}
			remove();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public void remove() {
		super.remove();
		if (this.world instanceof ServerWorld) {
			ServerWorld world = (ServerWorld) this.world;
			for (EntityTFNagaSegment seg : bodySegments) {
				// must use this instead of setDead
				// since multiparts are not added to the world tick list which is what checks isDead
				// TODO: Is this code sufficient?
				seg.remove();
				// this.world.removeEntityDangerously(seg);
			}
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
		return isWithinHomeDistanceFromPosition(entity.func_233580_cy_());
	}

	private void activateBodySegments() {
		for (int i = 0; i < currentSegmentCount; i++) {
			EntityTFNagaSegment segment = bodySegments[i];
			segment.activate();
			segment.setLocationAndAngles(getPosX() + 0.1 * i, getPosY() + 0.5D, getPosZ() + 0.1 * i, rand.nextFloat() * 360F, 0.0F);
			for (int j = 0; j < 20; j++) {
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.addParticle(ParticleTypes.EXPLOSION,
						segment.getPosX() + (double) (this.rand.nextFloat() * segment.getWidth() * 2.0F) - (double) segment.getWidth() - d0 * 10.0D,
						segment.getPosY() + (double) (this.rand.nextFloat() * segment.getHeight()) - d1 * 10.0D,
						segment.getPosZ() + (double) (this.rand.nextFloat() * segment.getWidth() * 2.0F) - (double) segment.getWidth() - d2 * 10.0D,
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
			double followX = leader.getPosX();
			double followY = leader.getPosY();
			double followZ = leader.getPosZ();

			// also weight the position so that the segments straighten out a little bit, and the front ones straighten more
			float angle = (((leader.rotationYaw + 180) * 3.141593F) / 180F);


			double straightenForce = 0.05D + (1.0 / (float) (i + 1)) * 0.5D;

			double idealX = -MathHelper.sin(angle) * straightenForce;
			double idealZ = MathHelper.cos(angle) * straightenForce;


			Vector3d diff = new Vector3d(bodySegments[i].getPosX() - followX, bodySegments[i].getPosY() - followY, bodySegments[i].getPosZ() - followZ);
			diff = diff.normalize();

			// weight so segments drift towards their ideal position
			diff = diff.add(idealX, 0, idealZ).normalize();

			double f = 2.0D;

			double destX = followX + f * diff.x;
			double destY = followY + f * diff.y;
			double destZ = followZ + f * diff.z;

			bodySegments[i].setPosition(destX, destY, destZ);

			double distance = (double) MathHelper.sqrt(diff.x * diff.x + diff.z * diff.z);

			if (i == 0) {
				// tilt segment next to head up towards head
				diff = diff.add(0, -0.15, 0);
			}

			bodySegments[i].setRotation((float) (Math.atan2(diff.z, diff.x) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(diff.y, distance) * 180.0D / Math.PI));
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		if (getHomePosition() != BlockPos.ZERO) {
			BlockPos home = this.getHomePosition();
			compound.put("Home", new IntArrayNBT(new int[]{home.getX(), home.getY(), home.getZ()}));
		}

		super.writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);

		if (compound.contains("Home", Constants.NBT.TAG_INT_ARRAY)) {
			int[] home = compound.getIntArray("Home");
			this.setHomePosAndDistance(new BlockPos(home[0], home[1], home[2]), 20);
		} else {
			this.detachHome();
		}

		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		// mark the courtyard as defeated
		if (!world.isRemote) {
			TFGenerationSettings.markStructureConquered(world, new BlockPos(this.func_233580_cy_()), TFFeature.NAGA_COURTYARD);
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
	public void addTrackingPlayer(ServerPlayerEntity player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(ServerPlayerEntity player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
