package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.PacketDistributor;
import twilightforest.TFSounds;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFPart;
import twilightforest.loot.TFTreasure;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.ThrowPlayerPacket;
import twilightforest.util.EntityUtil;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Naga extends Monster {

	private static final int TICKS_BEFORE_HEALING = 600;
	private static final int MAX_SEGMENTS = 12;
	private static final int LEASH_X = 46;
	private static final int LEASH_Y = 7;
	private static final int LEASH_Z = 46;
	private static final double DEFAULT_SPEED = 0.3;

	private int currentSegmentCount = 0; // not including head
	private final float healthPerSegment;
	private final NagaSegment[] bodySegments = new NagaSegment[MAX_SEGMENTS];
	private AIMovementPattern movementAI;
	private int ticksSinceDamaged = 0;
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_10);

	private final AttributeModifier slowSpeed = new AttributeModifier("Naga Slow Speed", 0.25F, AttributeModifier.Operation.ADDITION);
	private final AttributeModifier fastSpeed = new AttributeModifier("Naga Fast Speed", 0.50F, AttributeModifier.Operation.ADDITION);

	private static final EntityDataAccessor<Boolean> DATA_DAZE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_CHARGE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);

	public Naga(EntityType<? extends Naga> type, Level world) {
		super(type, world);
		this.maxUpStep = 2;
		this.healthPerSegment = getMaxHealth() / 10;
		this.xpReward = 217;
		this.noCulling = true;

		for (int i = 0; i < bodySegments.length; i++) {
			bodySegments[i] = new NagaSegment(this);
		}

		this.goNormal();
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_DAZE, false);
		entityData.define(DATA_CHARGE, false);
	}

	public boolean isDazed() {
		return entityData.get(DATA_DAZE);
	}

	protected void setDazed(boolean daze) {
		entityData.set(DATA_DAZE, daze);
	}

	public boolean isCharging() {
		return entityData.get(DATA_CHARGE);
	}

	protected void setCharging(boolean charge) {
		entityData.set(DATA_CHARGE, charge);
	}

	private float getMaxHealthPerDifficulty() {
		return switch (level.getDifficulty()) {
			case EASY -> 120;
			case HARD -> 250;
			default -> 200;
		};
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_) {
		return false;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new AIAttack(this));
		this.goalSelector.addGoal(3, new AISmash(this));
		this.goalSelector.addGoal(4, movementAI = new AIMovementPattern(this));
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1, 1) {
			@Override
			public void start() {
				Naga.this.goNormal();
				super.start();
			}

			@Override
			protected Vec3 getPosition() {
				return DefaultRandomPos.getPos(this.mob, 30, 7);
			}
		});
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));

		this.moveControl = new NagaMoveHelper(this);
	}

	// Similar to MeleeAttackGoal but simpler (no pathfinding)
	static class AIAttack extends Goal {

		private final Naga taskOwner;
		private int attackTick = 20;

		AIAttack(Naga taskOwner) {
			this.taskOwner = taskOwner;
		}

		@Override
		public boolean canUse() {
			LivingEntity target = taskOwner.getTarget();

			return target != null
					&& target.getBoundingBox().maxY > taskOwner.getBoundingBox().minY - 2.5
					&& target.getBoundingBox().minY < taskOwner.getBoundingBox().maxY + 2.5
					&& taskOwner.distanceToSqr(target) <= 4.0D
					&& taskOwner.getSensing().hasLineOfSight(target);

		}

		@Override
		public void tick() {
			if (attackTick > 0) {
				attackTick--;
			}
		}

		@Override
		public void stop() {
			attackTick = 20;
		}

		@Override
		public void start() {
			taskOwner.doHurtTarget(taskOwner.getTarget());
			attackTick = 20;
		}
	}

	static class AISmash extends Goal {

		private final Naga taskOwner;

		AISmash(Naga taskOwner) {
			this.taskOwner = taskOwner;
		}

		@Override
		public boolean canUse() {
			return /*taskOwner.getAttackTarget() != null &&*/ taskOwner.horizontalCollision && ForgeEventFactory.getMobGriefingEvent(taskOwner.level, taskOwner);
		}

		@Override
		public void start() {
			// NAGA SMASH!
			if (taskOwner.level.isClientSide) return;

			AABB bb = taskOwner.getBoundingBox();

			int minx = Mth.floor(bb.minX - 0.75D);
			int miny = Mth.floor(bb.minY + 1.01D);
			int minz = Mth.floor(bb.minZ - 0.75D);
			int maxx = Mth.floor(bb.maxX + 0.75D);
			int maxy = Mth.floor(bb.maxY + 0.0D);
			int maxz = Mth.floor(bb.maxZ + 0.75D);

			BlockPos min = new BlockPos(minx, miny, minz);
			BlockPos max = new BlockPos(maxx, maxy, maxz);

			if (taskOwner.level.hasChunksAt(min, max)) {
				for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
					if (EntityUtil.canDestroyBlock(taskOwner.level, pos, taskOwner)) {
						taskOwner.level.destroyBlock(pos, true);
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

		private final Naga taskOwner;
		private MovementState movementState;
		private int stateCounter;
		private boolean clockwise;

		AIMovementPattern(Naga taskOwner) {
			this.taskOwner = taskOwner;
			setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
			stop();
		}

		@Override
		public boolean canUse() {
			return taskOwner.getTarget() != null;
		}

		@Override
		public void stop() {
			movementState = MovementState.CIRCLE;
			stateCounter = 15;
			clockwise = false;
		}

		@Override
		public void tick() {
			if (!taskOwner.getNavigation().isDone()) {
				// If we still have an uncompleted path don't run yet
				// This isn't in shouldExecute/shouldContinueExecuting because we don't want to reset the task
				// todo 1.10 there's a better way to do this I think
				taskOwner.setDazed(false); // Since we have a path, we shouldn't be dazed anymore.
				return;
			}

			switch (movementState) {
				case INTIMIDATE -> {
					taskOwner.getNavigation().stop();
					taskOwner.getLookControl().setLookAt(taskOwner.getTarget(), 30F, 30F);
					taskOwner.lookAt(taskOwner.getTarget(), 30F, 30F);
					taskOwner.zza = 0.1f;
				}
				case CRUMBLE -> {
					taskOwner.getNavigation().stop();
					taskOwner.crumbleBelowTarget(2);
					taskOwner.crumbleBelowTarget(3);
				}
				case CHARGE -> {
					BlockPos tpoint = taskOwner.findCirclePoint(clockwise, 14, Math.PI);
					taskOwner.getNavigation().moveTo(tpoint.getX(), tpoint.getY(), tpoint.getZ(), 1); // todo 1.10 check speed
					taskOwner.setCharging(true);
				}
				case CIRCLE -> {
					// normal radius is 13
					double radius = stateCounter % 2 == 0 ? 12.0 : 14.0;
					double rotation = 1; // in radians

					// hook out slightly before circling
					if (stateCounter == 2) {
						radius = 16;
					}

					// head almost straight at the player at the end
					if (stateCounter == 1) {
						rotation = 0.1;
					}

					BlockPos tpoint = taskOwner.findCirclePoint(clockwise, radius, rotation);
					taskOwner.getNavigation().moveTo(tpoint.getX(), tpoint.getY(), tpoint.getZ(), 1); // todo 1.10 check speed
				}
				case DAZE -> {
					taskOwner.setDazed(true);
					taskOwner.setCharging(false);
				}
			}

			stateCounter--;
			if (stateCounter <= 0) {
				transitionState();
			}
		}

		private void transitionState() {
			taskOwner.setDazed(false);
			taskOwner.setCharging(false);
			switch (movementState) {
				case INTIMIDATE -> {
					clockwise = !clockwise;

					if (taskOwner.getTarget().getBoundingBox().minY > taskOwner.getBoundingBox().maxY) {
						doCrumblePlayer();
					} else {
						doCharge();
					}
				}
				case CRUMBLE -> doCharge();
				case CHARGE, DAZE -> doCircle();
				case CIRCLE -> doIntimidate();
			}
		}

		private void doDaze() {
			movementState = MovementState.DAZE;
			taskOwner.getNavigation().stop();
			stateCounter = 60 + taskOwner.random.nextInt(40);
		}

		private void doCircle() {
			movementState = MovementState.CIRCLE;
			stateCounter += 10 + taskOwner.random.nextInt(10);
			taskOwner.goNormal();
		}

		private void doCrumblePlayer() {
			movementState = MovementState.CRUMBLE;
			stateCounter = 20 + taskOwner.random.nextInt(20);
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
			taskOwner.playSound(TFSounds.NAGA_RATTLE, taskOwner.getSoundVolume() * 4F, taskOwner.getVoicePitch());

			stateCounter += 15 + taskOwner.random.nextInt(10);
			taskOwner.goSlow();
		}
	}

	@Override
	public void aiStep() {

		super.aiStep();

		if (level.isClientSide || !ForgeEventFactory.getMobGriefingEvent(level, this)) return;

		AABB bb = this.getBoundingBox();

		int minx = Mth.floor(bb.minX - 0.75D);
		int miny = Mth.floor(bb.minY + 1.01D);
		int minz = Mth.floor(bb.minZ - 0.75D);
		int maxx = Mth.floor(bb.maxX + 0.75D);
		int maxy = Mth.floor(bb.maxY + 0.0D);
		int maxz = Mth.floor(bb.maxZ + 0.75D);

		BlockPos min = new BlockPos(minx, miny, minz);
		BlockPos max = new BlockPos(maxx, maxy, maxz);

		if (level.hasChunksAt(min, max)) {
			for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
				BlockState state = level.getBlockState(pos);
				if (state.getMaterial() == Material.LEAVES && EntityUtil.canDestroyBlock(level, pos, state, this)) {
					level.destroyBlock(pos, true);
				}
			}
		}
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, /*getMaxHealthPerDifficulty()*/ 200) //TODO: We're static now
				.add(Attributes.MOVEMENT_SPEED, DEFAULT_SPEED)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.FOLLOW_RANGE, 80.0D);
	}

	/**
	 * Determine how many segments, from 2-12, the naga should have, dependent on its current health
	 */
	private void setSegmentsPerHealth() {
		int oldSegments = this.currentSegmentCount;
		int newSegments = Mth.clamp((int) ((this.getHealth() / healthPerSegment) + (getHealth() > 0 ? 2 : 0)), 0, MAX_SEGMENTS);
		this.currentSegmentCount = newSegments;
		if (newSegments < oldSegments) {
			for (int i = newSegments; i < oldSegments; i++) {
				bodySegments[i].selfDestruct();
			}
		} else if (newSegments > oldSegments) {
			this.activateBodySegments();
		}

		if (!level.isClientSide) {
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
				double d = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				level.addParticle((random.nextBoolean() ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION), (getX() + random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(), getY() + random.nextFloat() * getBbHeight(), (getZ() + random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(), d, d1, d2);
			}
		}

		// update health
		this.ticksSinceDamaged++;

		if (!this.level.isClientSide && this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 20 == 0) {
			this.heal(1);
		}

		setSegmentsPerHealth();

		super.tick();

		moveSegments();
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		if (getTarget() != null && (distanceToSqr(getTarget()) > 80 * 80 || !this.isEntityWithinHomeArea(getTarget()))) {
			setTarget(null);
		}

		// if we are very close to the path point, go to the next point, unless the path is finished
		// TODO 1.10 this runs after the path navigator runs, is that okay?
		double d = getBbWidth() * 4.0F;
		Vec3 vec3d = isPathFinding() ? getNavigation().getPath().getNextEntityPos(this) : null;

		while (vec3d != null && vec3d.distanceToSqr(getX(), vec3d.y, getZ()) < d * d) {
			getNavigation().getPath().advance();

			if (getNavigation().getPath().isDone()) {
				vec3d = null;
			} else {
				vec3d = getNavigation().getPath().getNextEntityPos(this);
			}
		}

		if (!isWithinRestriction()) {
			setTarget(null);
			getNavigation().moveTo(getNavigation().createPath(getRestrictCenter(), 0), 1.0F);
		}

		// BOSS BAR!
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}

	static class NagaMoveHelper extends MoveControl {

		public NagaMoveHelper(Mob naga) {
			super(naga);
		}

		@Override
		public void tick() {
			// TF - slither!
			MovementState currentState = ((Naga) mob).movementAI.movementState;
			if (currentState == MovementState.DAZE) {
				this.mob.xxa = 0F;
			} else if (currentState != MovementState.CHARGE && currentState != MovementState.INTIMIDATE) {
				this.mob.xxa = Mth.cos(this.mob.tickCount * 0.3F) * 0.6F;
			} else {
				this.mob.xxa *= 0.8F;
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
		if (!ForgeEventFactory.getMobGriefingEvent(level, this)) return;

		int floor = (int) getBoundingBox().minY;
		int targetY = (int) getTarget().getBoundingBox().minY;

		if (targetY > floor) {
			int dx = (int) getTarget().getX() + random.nextInt(range) - random.nextInt(range);
			int dz = (int) getTarget().getZ() + random.nextInt(range) - random.nextInt(range);
			int dy = targetY - random.nextInt(range) + random.nextInt(range > 1 ? range - 1 : range);

			if (dy <= floor) {
				dy = targetY;
			}

			BlockPos pos = new BlockPos(dx, dy, dz);

			if (EntityUtil.canDestroyBlock(level, pos, this)) {
				// todo limit what can be broken
				level.destroyBlock(pos, true);

				// sparkle!!
				for (int k = 0; k < 20; k++) {
					double d = random.nextGaussian() * 0.02D;
					double d1 = random.nextGaussian() * 0.02D;
					double d2 = random.nextGaussian() * 0.02D;

					level.addParticle(ParticleTypes.CRIT, (getX() + random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(), getY() + random.nextFloat() * getBbHeight(), (getZ() + random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(), d, d1, d2);
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
		this.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(slowSpeed);
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
		this.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(fastSpeed);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	/**
	 * Finds a point that allows us to circle the target clockwise.
	 */
	private BlockPos findCirclePoint(boolean clockwise, double radius, double rotation) {
		LivingEntity toCircle = getTarget();

		// compute angle
		double vecx = getX() - toCircle.getX();
		double vecz = getZ() - toCircle.getZ();
		float rangle = (float) (Math.atan2(vecz, vecx));

		// add a little, so he circles (clockwise)
		rangle += clockwise ? rotation : -rotation;

		// figure out where we're headed from the target angle
		double dx = Mth.cos(rangle) * radius;
		double dz = Mth.sin(rangle) * radius;

		double dy = Math.min(getBoundingBox().minY, toCircle.getY());

		// add that to the target entity's position, and we have our destination
		return new BlockPos(toCircle.getX() + dx, dy, toCircle.getZ() + dz);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource src) {
		return src.getEntity() != null && !this.isEntityWithinHomeArea(src.getEntity()) // reject damage from outside of our home radius
				|| src.getDirectEntity() != null && !this.isEntityWithinHomeArea(src.getDirectEntity())
				|| src.isFire() || src.isExplosion() || super.isInvulnerableTo(src);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source != DamageSource.FALL && super.hurt(source, amount)) {
			this.ticksSinceDamaged = 0;
			if(source.getEntity() instanceof ServerPlayer player && !hurtBy.contains(player)) {
				hurtBy.add(player);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean doHurtTarget(Entity toAttack) {
		if (movementAI.movementState == MovementState.CHARGE && toAttack instanceof LivingEntity && ((LivingEntity) toAttack).isBlocking()) {
			Vec3 motion = this.getDeltaMovement();
			toAttack.push(motion.x * 1.25D, 0.5D, motion.z * 1.25D);
			this.setDeltaMovement(motion.x * -1.5D, motion.y + 0.5D, motion.z * -1.5D);
			if (toAttack instanceof ServerPlayer)
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) toAttack), new ThrowPlayerPacket((float) toAttack.getDeltaMovement().x(), (float) toAttack.getDeltaMovement().y(), (float) toAttack.getDeltaMovement().z()));
			hurt(DamageSource.GENERIC, 4F);
			level.playSound(null, toAttack.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F + this.level.random.nextFloat() * 0.4F);
			movementAI.doDaze();
			return false;
		}

		if (!this.isDazed()) {
			boolean result = super.doHurtTarget(toAttack);

			if (result) {
				// charging, apply extra pushback
				toAttack.push(-Mth.sin((getYRot() * 3.141593F) / 180F) * 2.0F, 0.4F, Mth.cos((getYRot() * 3.141593F) / 180F) * 2.0F);
			}

			return result;
		}
		return false;
	}

	@Override
	public float getWalkTargetValue(BlockPos pos) {
		if (!this.isWithinRestriction(pos)) {
			return Float.MIN_VALUE;
		} else {
			return 0.0F;
		}
	}

	@Override
	public void checkDespawn() {
		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			if (getRestrictCenter() != BlockPos.ZERO) {
				level.setBlockAndUpdate(getRestrictCenter(), TFBlocks.NAGA_BOSS_SPAWNER.get().defaultBlockState());
			}
			discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		if (this.level instanceof ServerLevel) {
			for (NagaSegment seg : bodySegments) {
				// must use this instead of setDead
				// since multiparts are not added to the world tick list which is what checks isDead
				// TODO: Is this code sufficient?
				seg.remove(RemovalReason.KILLED);
				// this.world.removeEntityDangerously(seg);
			}
		}
	}

	@Override
	public boolean isWithinRestriction(BlockPos pos) {
		if (this.getRestrictRadius() == -1) {
			return true;
		} else {
			int distX = Math.abs(this.getRestrictCenter().getX() - pos.getX());
			int distY = Math.abs(this.getRestrictCenter().getY() - pos.getY());
			int distZ = Math.abs(this.getRestrictCenter().getZ() - pos.getZ());

			return distX <= LEASH_X && distY <= LEASH_Y && distZ <= LEASH_Z;
		}
	}

	private boolean isEntityWithinHomeArea(Entity entity) {
		return isWithinRestriction(entity.blockPosition());
	}

	private void activateBodySegments() {
		for (int i = 0; i < currentSegmentCount; i++) {
			NagaSegment segment = bodySegments[i];
			segment.activate();
			segment.moveTo(getX() + 0.1 * i, getY() + 0.5D, getZ() + 0.1 * i, random.nextFloat() * 360F, 0.0F);
			for (int j = 0; j < 20; j++) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level.addParticle(ParticleTypes.EXPLOSION,
						segment.getX() + this.random.nextFloat() * segment.getBbWidth() * 2.0F - segment.getBbWidth() - d0 * 10.0D,
						segment.getY() + this.random.nextFloat() * segment.getBbHeight() - d1 * 10.0D,
						segment.getZ() + this.random.nextFloat() * segment.getBbWidth() * 2.0F - segment.getBbWidth() - d2 * 10.0D,
						d0, d1, d2);
			}
		}
	}

	/**
	 * Sets the heading (ha ha) of the bodySegments segments
	 */
	private void moveSegments() {
		for (int i = 0; i < this.bodySegments.length; i++) {
			bodySegments[i].tick();
			Entity leader = i == 0 ? this : this.bodySegments[i - 1];
			double followX = leader.getX();
			double followY = leader.getY();
			double followZ = leader.getZ();

			// also weight the position so that the segments straighten out a little bit, and the front ones straighten more
			float angle = (((leader.getYRot() + 180) * 3.141593F) / 180F);


			double straightenForce = 0.05D + (1.0 / (i + 1)) * 0.5D;

			double idealX = -Mth.sin(angle) * straightenForce;
			double idealZ = Mth.cos(angle) * straightenForce;

			double groundY = bodySegments[i].isInWall() ? followY + 2F : followY;
			double idealY = (groundY - followY) * straightenForce;

			Vec3 diff = new Vec3(bodySegments[i].getX() - followX, bodySegments[i].getY() - followY, bodySegments[i].getZ() - followZ);
			diff = diff.normalize();

			// weight so segments drift towards their ideal position
			diff = diff.add(idealX, idealY, idealZ).normalize();

			double f = 2.0D;

			double destX = followX + f * diff.x;
			double destY = followY + f * diff.y;
			double destZ = followZ + f * diff.z;

			bodySegments[i].setPos(destX, destY, destZ);

			double distance = Mth.sqrt((float) (diff.x * diff.x + diff.z * diff.z));

			if (i == 0) {
				// tilt segment next to head up towards head
				diff = diff.add(0, -0.15, 0);
			}

			bodySegments[i].setRot((float) (Math.atan2(diff.z, diff.x) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(diff.y, distance) * 180.0D / Math.PI));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		if (getRestrictCenter() != BlockPos.ZERO) {
			BlockPos home = this.getRestrictCenter();
			compound.put("Home", new IntArrayTag(new int[]{home.getX(), home.getY(), home.getZ()}));
		}

		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);

		if (compound.contains("Home", Tag.TAG_INT_ARRAY)) {
			int[] home = compound.getIntArray("Home");
			this.restrictTo(new BlockPos(home[0], home[1], home[2]), 20);
		} else {
			this.hasRestriction();
		}

		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the courtyard as defeated
		if (!level.isClientSide) {
			TFGenerationSettings.markStructureConquered(level, new BlockPos(this.blockPosition()), TFFeature.NAGA_COURTYARD);
			for(ServerPlayer player : hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}

			TFTreasure.entityDropsIntoContainer(this, this.createLootContext(true, cause).create(LootContextParamSets.ENTITY), this.random.nextBoolean() ? TFBlocks.TWILIGHT_OAK_CHEST.get().defaultBlockState() : TFBlocks.CANOPY_CHEST.get().defaultBlockState(), new BlockPos(this.position()));
		}
	}

	@Override
	protected boolean shouldDropLoot() {
		// Invoked the mob's loot during die, this will avoid duplicating during the actual drop phase
		return false;
	}

	@Override
	public boolean isMultipartEntity() {
		return true;
	}

	@Override
	public void recreateFromPacket(ClientboundAddMobPacket p_147206_) {
		super.recreateFromPacket(p_147206_);
		TFPart.assignPartIDs(this);
	}

	@Nullable
	@Override
	public PartEntity<?>[] getParts() {
		return bodySegments;
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected float getWaterSlowDown() {
		return 1.0F;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}
}
