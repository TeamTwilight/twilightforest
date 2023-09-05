package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.advancements.TFAdvancements;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.TFPart;
import twilightforest.entity.ai.control.NagaMoveControl;
import twilightforest.entity.ai.goal.NagaMovementPattern;
import twilightforest.entity.ai.goal.NagaSmashGoal;
import twilightforest.entity.ai.goal.SimplifiedAttackGoal;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.network.ParticlePacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.network.ThrowPlayerPacket;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Naga extends Monster implements EnforcedHomePoint, IBossLootBuffer {

	private static final int TICKS_BEFORE_HEALING = 600;
	private static final int MAX_SEGMENTS = 12;
	private static final int LEASH_X = 46;
	private static final int LEASH_Y = 7;
	private static final int LEASH_Z = 46;
	private static final double DEFAULT_SPEED = 0.3;

	private int currentSegmentCount = 0; // not including head
	private final float healthPerSegment;
	private final NagaSegment[] bodySegments = new NagaSegment[MAX_SEGMENTS];
	private NagaMovementPattern movementAI;
	private int ticksSinceDamaged = 0;
	private final List<ServerPlayer> hurtBy = new ArrayList<>();
	private final NonNullList<ItemStack> dyingInventory = NonNullList.withSize(27, ItemStack.EMPTY);

	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_10);

	private final AttributeModifier slowSpeed = new AttributeModifier("Naga Slow Speed", 0.25F, AttributeModifier.Operation.ADDITION);
	private final AttributeModifier fastSpeed = new AttributeModifier("Naga Fast Speed", 0.50F, AttributeModifier.Operation.ADDITION);

	private static final EntityDataAccessor<Boolean> DATA_DAZE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_CHARGE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);

	public Naga(EntityType<? extends Naga> type, Level level) {
		super(type, level);
		this.xpReward = 217;
		this.noCulling = true;

		for (int i = 0; i < this.bodySegments.length; i++) {
			this.bodySegments[i] = new NagaSegment(this);
		}

		this.goNormal();
		this.healthPerSegment = this.getMaxHealth() / 10;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_DAZE, false);
		this.entityData.define(DATA_CHARGE, false);
	}

	public boolean isDazed() {
		return this.entityData.get(DATA_DAZE);
	}

	public void setDazed(boolean daze) {
		this.entityData.set(DATA_DAZE, daze);
	}

	public boolean isCharging() {
		return this.entityData.get(DATA_CHARGE);
	}

	public void setCharging(boolean charge) {
		this.entityData.set(DATA_CHARGE, charge);
	}

	public NagaMovementPattern getMovementAI() {
		return this.movementAI;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new SimplifiedAttackGoal(this));
		this.goalSelector.addGoal(3, new NagaSmashGoal(this));
		this.goalSelector.addGoal(4, this.movementAI = new NagaMovementPattern(this));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D) {
			@Override
			public void start() {
				Naga.this.setTarget(null);
				super.start();
			}
		});
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
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false) {
			@Override
			public boolean canUse() {
				return Naga.this.isWithinRestriction(Naga.this.blockPosition()) && super.canUse();
			}
		});

		this.moveControl = new NagaMoveControl(this);
	}

	@Override
	public void aiStep() {

		super.aiStep();

		if (this.getLevel().isClientSide() || !ForgeEventFactory.getMobGriefingEvent(this.getLevel(), this)) return;

		AABB bb = this.getBoundingBox();

		int minx = Mth.floor(bb.minX - 0.75D);
		int miny = Mth.floor(bb.minY + (this.shouldDestroyAllBlocks() ? 1.01F : 0.5F));
		int minz = Mth.floor(bb.minZ - 0.75D);
		int maxx = Mth.floor(bb.maxX + 0.75D);
		int maxy = Mth.floor(bb.maxY + 1.0D);
		int maxz = Mth.floor(bb.maxZ + 0.75D);

		BlockPos min = new BlockPos(minx, miny, minz);
		BlockPos max = new BlockPos(maxx, maxy, maxz);

		if (this.getLevel().hasChunksAt(min, max)) {
			for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
				BlockState state = this.getLevel().getBlockState(pos);
				if (state.is(BlockTags.LEAVES) || (this.shouldDestroyAllBlocks() && EntityUtil.canDestroyBlock(this.getLevel(), pos, this))) {
					this.getLevel().destroyBlock(pos, !state.is(BlockTags.LEAVES));
				}
			}
		}
	}

	public boolean shouldDestroyAllBlocks() {
		return this.isCharging() || !this.isWithinRestriction();
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 120)
				.add(Attributes.MOVEMENT_SPEED, DEFAULT_SPEED)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.FOLLOW_RANGE, 80.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
	}

	/**
	 * Determine how many segments, from 2-12, the naga should have, dependent on its current health
	 */
	private void setSegmentsPerHealth() {
		int oldSegments = this.currentSegmentCount;
		int newSegments = Mth.clamp((int) ((this.getHealth() / this.healthPerSegment) + (getHealth() > 0 ? 2 : 0)), 0, MAX_SEGMENTS);
		this.currentSegmentCount = newSegments;
		if (newSegments < oldSegments) {
			for (int i = newSegments; i < oldSegments; i++) {
				this.bodySegments[i].selfDestruct((oldSegments - i) * 12);
			}
		} else if (newSegments > oldSegments) {
			this.activateBodySegments();
		}

		if (!this.getLevel().isClientSide()) {
			double newSpeed = DEFAULT_SPEED - newSegments * (-0.2F / 12F);
			if (newSpeed < 0)
				newSpeed = 0;
			Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(newSpeed);
		}
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		if (this.getLevel().getDifficulty() != Difficulty.EASY && this.getAttribute(Attributes.MAX_HEALTH) != null) {
			boolean hard = this.level.getDifficulty() == Difficulty.HARD;
			AttributeModifier modifier = new AttributeModifier("Difficulty Health Boost", hard ? 130 : 80, AttributeModifier.Operation.ADDITION);
			if (!Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).hasModifier(modifier)) {
				Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(modifier);
				this.setHealth(this.getMaxHealth());
			}
		}
		return data;
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
		if (this.isDazed() && this.deathTime < 10) {
			for (int i = 0; i < 5; i++) {
				Vec3 pos = new Vec3(this.getX(), this.getY() + 2.15D, this.getZ()).add(new Vec3(1.5D, 0, 0).yRot((float) Math.toRadians(this.getRandom().nextInt(360))));
				this.getLevel().addParticle(ParticleTypes.CRIT, pos.x(), pos.y(), pos.z(), 0, 0, 0);
			}
		}

		// update health
		this.ticksSinceDamaged++;

		if (!this.getLevel().isClientSide() && this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 20 == 0) {
			this.heal(1);
		}

		this.setSegmentsPerHealth();

		super.tick();

		this.moveSegments();
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		if (this.getTarget() != null && (this.distanceToSqr(getTarget()) > 80 * 80 || !this.areSelfAndTargetInHome(this.getTarget()))) {
			this.setTarget(null);
		}

		// if we are very close to the path point, go to the next point, unless the path is finished
		double d = this.getBbWidth() * 4.0F;
		Vec3 vec3d = this.isPathFinding() ? Objects.requireNonNull(this.getNavigation().getPath()).getNextEntityPos(this) : null;

		while (vec3d != null && vec3d.distanceToSqr(this.getX(), vec3d.y(), this.getZ()) < d * d) {
			this.getNavigation().getPath().advance();

			if (this.getNavigation().getPath().isDone()) {
				vec3d = null;
			} else {
				vec3d = this.getNavigation().getPath().getNextEntityPos(this);
			}
		}

		// BOSS BAR!
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.NAGA_HISS.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.NAGA_HURT.get();
	}

	@Override
	public void playHurtSound(DamageSource pSource) {//Just made public
		super.playHurtSound(pSource);
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.NAGA_HURT.get();
	}

	/**
	 * Sets the naga to move slowly, such as when he is intimidating the player
	 */
	public void goSlow() {
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(slowSpeed); // if we apply this twice, we crash, but we can always remove it
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(fastSpeed);
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(slowSpeed);
	}

	/**
	 * Normal speed, like when he is circling
	 */
	public void goNormal() {
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(slowSpeed);
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(fastSpeed);
	}

	/**
	 * Fast, like when he is charging
	 */
	public void goFast() {
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(slowSpeed);
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(fastSpeed);
		Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(fastSpeed);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource src) {
		return src.getEntity() != null && !this.isEntityWithinHomeArea(src.getEntity()) // reject damage from outside of our home radius
				|| src.getDirectEntity() != null && !this.isEntityWithinHomeArea(src.getDirectEntity())
				|| src.is(DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(src);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (super.hurt(source, amount)) {
			this.ticksSinceDamaged = 0;
			if (source.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
				this.hurtBy.add(player);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean doHurtTarget(Entity toAttack) {
		if (this.movementAI.getState() == NagaMovementPattern.MovementState.CHARGE && toAttack instanceof LivingEntity living && living.isBlocking()) {
			Vec3 motion = this.getDeltaMovement();
			toAttack.push(motion.x() * 1.5D, 0.5D, motion.z() * 1.5D);
			this.push(motion.x() * -1.25D, 0.5D, motion.z() * -1.25D);
			if (toAttack instanceof ServerPlayer player) {
				player.getUseItem().hurtAndBreak(5, player, user -> user.broadcastBreakEvent(player.getUsedItemHand()));
				TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ThrowPlayerPacket(motion.x() * 3.0D,  motion.y() + 0.75D, motion.z() * 3.0D));
			}
			this.hurt(this.damageSources().generic(), 4.0F);
			this.getLevel().playSound(null, toAttack.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F + this.getLevel().getRandom().nextFloat() * 0.4F);
			this.movementAI.doDaze();
			return false;
		}

		if (!this.isDazed()) {
			boolean result = super.doHurtTarget(toAttack);

			if (result) {
				// charging, apply extra pushback
				toAttack.push(-Mth.sin((getYRot() * Mth.PI) / 180.0F) * 2.0F, 0.4F, Mth.cos((getYRot() * Mth.PI) / 180.0F) * 2.0F);
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
		if (this.getLevel().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.getRestrictCenter() != BlockPos.ZERO) {
				this.getLevel().setBlockAndUpdate(this.getRestrictCenter(), TFBlocks.NAGA_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		if (reason.equals(RemovalReason.KILLED) && this.level instanceof ServerLevel serverLevel) {
			IBossLootBuffer.depositDropsIntoChest(this, this.random.nextBoolean() ? TFBlocks.TWILIGHT_OAK_CHEST.get().defaultBlockState() : TFBlocks.CANOPY_CHEST.get().defaultBlockState(), EntityUtil.bossChestLocation(this), serverLevel);
		}
		super.remove(reason);
		if (this.getLevel() instanceof ServerLevel) {
			for (NagaSegment seg : this.bodySegments) {
				// must use this instead of setDead
				// since multiparts are not added to the world tick list which is what checks isDead
				seg.kill();
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

	public boolean isEntityWithinHomeArea(Entity entity) {
		return this.isWithinRestriction(entity.blockPosition());
	}

	public boolean areSelfAndTargetInHome(Entity entity) {
		return this.isWithinRestriction(this.blockPosition()) && this.isEntityWithinHomeArea(entity);
	}

	private void activateBodySegments() {
		for (int i = 0; i < this.currentSegmentCount; i++) {
			NagaSegment segment = this.bodySegments[i];
			segment.activate();
			segment.moveTo(getX() + 0.1 * i, getY() + 0.5D, getZ() + 0.1 * i, this.getRandom().nextFloat() * 360.0F, 0.0F);
			for (int j = 0; j < 20; j++) {
				double d0 = this.getRandom().nextGaussian() * 0.02D;
				double d1 = this.getRandom().nextGaussian() * 0.02D;
				double d2 = this.getRandom().nextGaussian() * 0.02D;
				this.getLevel().addParticle(ParticleTypes.EXPLOSION,
						segment.getX() + this.getRandom().nextFloat() * segment.getBbWidth() * 2.0F - segment.getBbWidth() - d0 * 10.0D,
						segment.getY() + this.getRandom().nextFloat() * segment.getBbHeight() - d1 * 10.0D,
						segment.getZ() + this.getRandom().nextFloat() * segment.getBbWidth() * 2.0F - segment.getBbWidth() - d2 * 10.0D,
						d0, d1, d2);
			}
		}
	}

	/**
	 * Sets the heading (ha ha) of the bodySegments segments
	 */
	private void moveSegments() {
		for (int i = 0; i < this.bodySegments.length; i++) {
			this.bodySegments[i].tick();
			Entity leader = i == 0 ? this : this.bodySegments[i - 1];
			double followX = leader.getX();
			double followY = leader.getY();
			double followZ = leader.getZ();

			// also weight the position so that the segments straighten out a little bit, and the front ones straighten more
			float angle = (((leader.getYRot() + 180) * Mth.PI) / 180.0F);


			double straightenForce = 0.05D + (1.0D / (i + 1)) * 0.5D;
			if (this.isDeadOrDying()) straightenForce = 0.0D; //Dead snakes don't move

			double idealX = -Mth.sin(angle) * straightenForce;
			double idealZ = Mth.cos(angle) * straightenForce;

			double groundY = this.bodySegments[i].isInWall() ? followY + 2.0F : followY;
			double idealY = (groundY - followY) * straightenForce;

			Vec3 diff = new Vec3(this.bodySegments[i].getX() - followX, this.bodySegments[i].getY() - followY, this.bodySegments[i].getZ() - followZ);
			diff = diff.normalize();

			// weight so segments drift towards their ideal position
			diff = diff.add(idealX, idealY, idealZ).normalize();

			double f = 2.0D;

			double destX = followX + f * diff.x();
			double destY = followY + f * diff.y();
			double destZ = followZ + f * diff.z();

			this.bodySegments[i].setPos(destX, destY, destZ);

			double distance = Mth.sqrt((float) (diff.x() * diff.x() + diff.z() * diff.z()));

			if (i == 0) {
				// tilt segment next to head up towards head
				diff = diff.add(0.0D, -0.15D, 0.0D);
			}

			this.bodySegments[i].setRot((float) (Math.atan2(diff.z(), diff.x()) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(diff.y(), distance) * 180.0D / Math.PI));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		this.saveHomePointToNbt(compound);
		this.addDeathItemsSaveData(compound);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.readDeathItemsSaveData(compound);
		this.loadHomePointFromNbt(compound, 20);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the courtyard as defeated
		if (this.getLevel() instanceof ServerLevel serverLevel) {
			this.bossInfo.setProgress(0.0F);
			LandmarkUtil.markStructureConquered(this.getLevel(), this, TFStructures.NAGA_COURTYARD, true);
			for (ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}

			IBossLootBuffer.saveDropsIntoBoss(this, this.createLootContext(true, cause).create(LootContextParamSets.ENTITY), serverLevel);
		}
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (!this.level.isClientSide && !this.isRemoved()) {
			int renderEnd = 24;
			int maxDeath = renderEnd + 120;
			if (this.deathTime >= renderEnd) {
				if (this.deathTime == renderEnd) {
					SoundEvent soundevent = this.getDeathSound();
					if (soundevent != null) {
						this.playSound(soundevent, this.getSoundVolume() * 1.2F, this.getVoicePitch() * 0.75F);
					}
					this.level.broadcastEntityEvent(this, (byte) 60);
				} else if (this.deathTime >= maxDeath) {
					this.remove(Entity.RemovalReason.KILLED);
				} else {
					Vec3 start = this.position().add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
					Vec3 end = Vec3.atCenterOf(EntityUtil.bossChestLocation(this));
					Vec3 diff = end.subtract(start);

					ParticlePacket particlePacket = new ParticlePacket();
					if (this.deathTime >= maxDeath - 3) {
						for (int i = 0; i < 40; i++) {
							double x = (this.random.nextDouble() - 0.5D) * 0.075D * i;
							double y = (this.random.nextDouble() - 0.5D) * 0.075D * i;
							double z = (this.random.nextDouble() - 0.5D) * 0.075D * i;
							particlePacket.queueParticle(ParticleTypes.POOF, false, end.add(x, y, z), Vec3.ZERO);
						}
					}

					double angle = Math.atan2(end.z - start.z, end.x - start.x) * Mth.RAD_TO_DEG + 180D;

					double xMul = angle % 180.0D;
					xMul = Math.min(xMul, 180.0D - xMul);
					xMul = Math.pow((xMul / 90.0D), 1.5D) * 2.0D;

					double zMul = (angle + 90.0D) % 180.0D;
					zMul = Math.min(zMul, 180.0D - zMul);
					zMul = Math.pow((zMul / 90.0D), 1.5D) * 2.0D;

					for (int p = 0; p < 4; p++) {
						int trailTime = (this.deathTime - renderEnd) - p + 1;//Plus one cuz the math makes it reach the correct spot at 120 ticks, but the method ends at 119
						if (trailTime < 0) continue;
						for (double d = 0.0D; d < 1.0D; d += 0.25D) {
							double preciseTime = trailTime - d;
							if (preciseTime < 0.0D) continue;
							double factor = preciseTime / (double) (maxDeath - renderEnd);
							Vec3 particlePos = start.add(diff.scale(factor)).add(Math.sin(preciseTime * Math.PI * 0.075D) * xMul, Math.sin(preciseTime * Math.PI * 0.025D) * 0.1D, Math.cos(preciseTime * Math.PI * 0.0625D) * zMul);//Some sine waves to make it slither-y;
							BlockHitResult blockhitresult = this.level.clip(new ClipContext(particlePos.add(0.0D, 2.0D, 0.0D), particlePos.subtract(0.0D, 3.0D, 0.0D), ClipContext.Block.COLLIDER, ClipContext.Fluid.WATER, null));
							particlePacket.queueParticle(ParticleTypes.COMPOSTER, false, blockhitresult.getLocation().add(0.0D, 0.15D, 0.0D), Vec3.ZERO);
						}
					}
					TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), particlePacket);
				}
			}
		}
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 60) {
			Vec3 pos = this.position();
			float width = this.getBbWidth();
			float height = this.getBbHeight();
			for (int k = 0; k < 20; k++) {
				this.getLevel().addParticle(random.nextBoolean() ? ParticleTypes.EXPLOSION : ParticleTypes.POOF,
						(pos.x + this.random.nextFloat() * width * 2.0F) - width,
						pos.y + this.random.nextFloat() * height,
						(pos.z + this.random.nextFloat() * width * 2.0F) - width,
						this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
			}
		}
		super.handleEntityEvent(id);
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
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		TFPart.assignPartIDs(this);
	}

	@Nullable
	@Override
	public PartEntity<?>[] getParts() {
		return this.bodySegments;
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
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public float getStepHeight() {
		return 2.0F;
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean isPushedByFluid(FluidType type) {
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

	@Override
	public BlockPos getRestrictionCenter() {
		return this.getRestrictCenter();
	}

	@Override
	public void setRestriction(BlockPos pos, int dist) {
		this.restrictTo(pos, dist);
	}

	@Override
	public NonNullList<ItemStack> getItemStacks() {
		return this.dyingInventory;
	}
}
