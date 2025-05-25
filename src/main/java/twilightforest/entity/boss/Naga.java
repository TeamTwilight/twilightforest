package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;
import twilightforest.entity.ai.control.NagaMoveControl;
import twilightforest.entity.ai.goal.AttemptToGoHomeGoal;
import twilightforest.entity.ai.goal.NagaMovementPattern;
import twilightforest.entity.ai.goal.NagaSmashGoal;
import twilightforest.entity.ai.goal.SimplifiedAttackGoal;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.network.MovePlayerPacket;
import twilightforest.util.entities.EntityUtil;

import java.util.Objects;
import java.util.UUID;

public class Naga extends BaseTFBoss {
	private static final int DEATH_ANIMATION_DURATION = 24;
	private static final int DEATH_PARTICLES_DURATION = 100;

	private static final int TICKS_BEFORE_HEALING = 600;
	private static final int MAX_SEGMENTS = 12;
	private static final int XZ_HOME_BOUNDS = 46;
	private static final int Y_HOME_BOUNDS = 7;
	private static final double DEFAULT_SPEED = 0.5D;

	private int currentSegmentCount = 0; // not including head
	private final float healthPerSegment;
	private final NagaSegment[] bodySegments = new NagaSegment[MAX_SEGMENTS];
	private NagaMovementPattern movementPattern;
	private int ticksSinceDamaged = 0;
	private int damageDuringCurrentStun = 0;
	public float stunlessRedOverlayProgress = 0.0F;

	private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("1fe84ad2-3b63-4922-ade7-546aae84a9e1");
	private static final EntityDataAccessor<Boolean> DATA_DAZE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_CHARGE = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_STUNLESS = SynchedEntityData.defineId(Naga.class, EntityDataSerializers.BOOLEAN);

	public Naga(EntityType<? extends Naga> type, Level level) {
		super(type, level);
		this.xpReward = 217;

		for (int i = 0; i < this.bodySegments.length; i++) {
			this.bodySegments[i] = new NagaSegment(this);
		}

		this.healthPerSegment = this.getMaxHealth() / 10;
		this.moveControl = new NagaMoveControl(this);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_DAZE, false);
		builder.define(DATA_CHARGE, false);
		builder.define(DATA_STUNLESS, false);
	}

	public boolean isDazed() {
		return this.getEntityData().get(DATA_DAZE);
	}

	public void setDazed(boolean daze) {
		this.getEntityData().set(DATA_DAZE, daze);
	}

	public boolean isCharging() {
		return this.getEntityData().get(DATA_CHARGE);
	}

	public void setCharging(boolean charge) {
		this.getEntityData().set(DATA_CHARGE, charge);
		if (!charge) {
			this.getEntityData().set(DATA_STUNLESS, false);
		}
	}

	public boolean isStunlessCharging() {
		return this.getEntityData().get(DATA_STUNLESS);
	}

	public void setStunlessCharging(boolean charge) {
		this.getEntityData().set(DATA_STUNLESS, charge);
	}

	public NagaMovementPattern getMovementPattern() {
		return this.movementPattern;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new SimplifiedAttackGoal(this));
		this.goalSelector.addGoal(3, new NagaSmashGoal(this));
		this.goalSelector.addGoal(4, this.movementPattern = new NagaMovementPattern(this));
		this.goalSelector.addGoal(5, new AttemptToGoHomeGoal<>(this, 1.0D) {
			@Override
			public void start() {
				Naga.this.setTarget(null);
				super.start();
			}
		});
		this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1, 1) {

			@Override
			public boolean canUse() {
				return Naga.this.isMobWithinHomeArea(Naga.this) && Naga.this.getTarget() == null && super.canUse();
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
				return super.canUse() && Naga.this.areSelfAndTargetInHome(Naga.this.getTarget());
			}
		});
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 120)
			.add(Attributes.MOVEMENT_SPEED, DEFAULT_SPEED)
			.add(Attributes.ATTACK_DAMAGE, 5.0D)
			.add(Attributes.FOLLOW_RANGE, 80.0D)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
			.add(Attributes.STEP_HEIGHT, 2.0F);
	}

	/**
	 * Determine how many segments, from 2-12, the naga should have, dependent on its current health
	 */
	private void setSegmentsPerHealth() {
		int oldSegments = this.currentSegmentCount;
		int newSegments = Mth.clamp((int) ((this.getHealth() / this.healthPerSegment) + (this.getHealth() > 0 ? 2 : 0)), 0, MAX_SEGMENTS);
		this.currentSegmentCount = newSegments;
		if (newSegments < oldSegments) {
			for (int i = newSegments; i < oldSegments; i++) {
				this.bodySegments[i].selfDestruct((oldSegments - i) * 12);
			}
		} else if (newSegments > oldSegments) {
			this.activateBodySegments();
		}

		if (!this.level().isClientSide() && oldSegments != newSegments) {
			double speedMod = ((float) MAX_SEGMENTS / newSegments * 0.02F);
			AttributeModifier modifier = new AttributeModifier(TwilightForestMod.prefix("segment_speed_boost"), speedMod, AttributeModifier.Operation.ADD_VALUE);
			Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(modifier.id());
			Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(modifier);
		}
	}

	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, EntitySpawnReason type, @Nullable SpawnGroupData data) {
		if (this.level().getDifficulty() != Difficulty.EASY && this.getAttribute(Attributes.MAX_HEALTH) != null) {
			boolean hard = this.level().getDifficulty() == Difficulty.HARD;
			AttributeModifier modifier = new AttributeModifier(TwilightForestMod.prefix("difficulty_health_boost"), hard ? 130 : 80, AttributeModifier.Operation.ADD_VALUE);
			if (!Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).hasModifier(modifier.id())) {
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
		if (this.level().isClientSide()) {
			if (this.isDazed() && this.deathTime < 10) {
				for (int i = 0; i < 5; i++) {
					Vec3 pos = new Vec3(this.getX(), this.getY() + 2.15D, this.getZ()).add(new Vec3(1.5D, 0, 0).yRot((float) Math.toRadians(this.getRandom().nextInt(360))));
					this.level().addParticle(ParticleTypes.CRIT, pos.x(), pos.y(), pos.z(), 0, 0, 0);
				}
			}

			if (this.isStunlessCharging() && this.deathTime <= 0) {
				this.level().addParticle(ParticleTypes.ANGRY_VILLAGER, this.getRandomX(0.85F), this.blockPosition().getY() + 2.25F, this.getRandomZ(0.85F), 0, 0, 0);
			}

			if (this.isStunlessCharging()) {
				this.stunlessRedOverlayProgress = Math.min(0.65F, this.stunlessRedOverlayProgress + 0.01F);
			} else {
				this.stunlessRedOverlayProgress = Math.max(0.0F, this.stunlessRedOverlayProgress - 0.1F);
			}
		}

		this.setSegmentsPerHealth();
		super.tick();
		this.moveSegments();
	}

	@Override
	protected void customServerAiStep(ServerLevel level) {
		super.customServerAiStep(level);

		if (this.getTarget() != null && (this.distanceToSqr(this.getTarget()) > 80 * 80 || !this.areSelfAndTargetInHome(this.getTarget()))) {
			this.setTarget(null);
		}

		if (EventHooks.canEntityGrief(level, this)) {
			AABB bb = this.getBoundingBox();

			int minx = Mth.floor(bb.minX - 0.75D);
			int miny = Mth.floor(bb.minY + (this.shouldDestroyAllBlocks() ? 1.01F : 0.5F));
			int minz = Mth.floor(bb.minZ - 0.75D);
			int maxx = Mth.floor(bb.maxX + 0.75D);
			int maxy = Mth.floor(bb.maxY + 1.0D);
			int maxz = Mth.floor(bb.maxZ + 0.75D);

			BlockPos min = new BlockPos(minx, miny, minz);
			BlockPos max = new BlockPos(maxx, maxy, maxz);

			if (this.level().hasChunksAt(min, max)) {
				for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
					BlockState state = this.level().getBlockState(pos);
					if (state.is(BlockTags.LEAVES) || (this.shouldDestroyAllBlocks() && EntityUtil.canDestroyBlock(this.level(), pos, this))) {
						this.level().destroyBlock(pos, !state.is(BlockTags.LEAVES));
					}
				}
			}
		}

		//if we get stuck in a hole/cave send us home, dont even deal with trying to get out
		if (this.tickCount % 20 == 0 && this.isRestrictionPointValid(this.level().dimension()) && this.getY() < this.getRestrictionPoint().pos().getY() - 5) {
			this.teleportTo(this.getRestrictionPoint().pos().getX(), this.getRestrictionPoint().pos().getY(), this.getRestrictionPoint().pos().getZ());
			this.getNavigation().stop();
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

		// update health
		this.ticksSinceDamaged++;

		if (this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 20 == 0) {
			this.heal(1);
		}

		if (this.damageDuringCurrentStun > 15) {
			this.getMovementPattern().forceCircle();
			this.damageDuringCurrentStun = 0;
		}
	}

	public boolean shouldDestroyAllBlocks() {
		return this.isCharging() || !this.isMobWithinHomeArea(this);
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
	protected SoundEvent getDeathSound() {
		return TFSounds.NAGA_HURT.get();
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(ServerLevel level, DamageSource src) {
		return src.getEntity() != null && !this.isOtherEntityWithinHomeArea(src.getEntity()) // reject damage from outside of our home radius
			|| src.getDirectEntity() != null && !this.isOtherEntityWithinHomeArea(src.getDirectEntity())
			|| src.is(DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(level, src);
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		if (super.hurtServer(level, source, amount)) {
			this.ticksSinceDamaged = 0;
			if (this.isDazed()) {
				this.damageDuringCurrentStun += (int) amount;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean doHurtTarget(ServerLevel level, Entity toAttack) {
		if (toAttack instanceof LivingEntity living && living.isBlocking()) {
			if (this.getMovementPattern().getState() == NagaMovementPattern.MovementState.CHARGE) {
				Vec3 motion = this.getDeltaMovement();
				toAttack.push(motion.x() * 1.5D, 0.5D, motion.z() * 1.5D);
				this.push(motion.x() * -1.25D, 0.5D, motion.z() * -1.25D);
				if (toAttack instanceof ServerPlayer player) {
					player.getUseItem().hurtAndBreak(5, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
					PacketDistributor.sendToPlayer(player, new MovePlayerPacket(motion.x() * 3.0D, motion.y() + 0.75D, motion.z() * 3.0D));
				}
				this.hurtServer(level, this.damageSources().generic(), 2.0F);
				this.level().playSound(null, toAttack.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F + this.level().getRandom().nextFloat() * 0.4F);
				this.getMovementPattern().doDaze();
				return false;
			} else if (this.getMovementPattern().getState() == NagaMovementPattern.MovementState.STUNLESS_CHARGE) {
				if (toAttack instanceof ServerPlayer player) {
					player.getUseItem().hurtAndBreak(10, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
					player.getCooldowns().addCooldown(player.getUseItem(), 200);
					player.stopUsingItem();
					this.level().broadcastEntityEvent(player, (byte) 30);
				}
				living.hurtServer(level, this.damageSources().mobAttack(this), 4.0F);
				this.playSound(SoundEvents.FOX_BITE, 2.0F, 0.5F);
				this.getMovementPattern().doCircle();
				return false;
			}
		}
		if (!this.isDazed()) {
			boolean result = super.doHurtTarget(level, toAttack);

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
		if (!this.isMobWithinHomeArea(this)) {
			return Float.MIN_VALUE;
		} else {
			return 0.0F;
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		if (this.level() instanceof ServerLevel level) {
			for (NagaSegment seg : this.bodySegments) {
				// must use this instead of setDead
				// since multiparts are not added to the world tick list which is what checks isDead
				seg.kill(level);
			}
		}
	}

	@Override
	public boolean isMobWithinHomeArea(Entity entity) {
		if (!this.isRestrictionPointValid(this.level().dimension())) {
			return true;
		} else {
			double distX = Math.abs(this.getRestrictionPoint().pos().getX() - entity.blockPosition().getX());
			double distY = Math.abs(this.getRestrictionPoint().pos().getY() - entity.blockPosition().getY());
			double distZ = Math.abs(this.getRestrictionPoint().pos().getZ() - entity.blockPosition().getZ());

			return distX <= XZ_HOME_BOUNDS && distY <= Y_HOME_BOUNDS && distZ <= XZ_HOME_BOUNDS;
		}
	}

	public boolean isOtherEntityWithinHomeArea(Entity entity) {
		return this.isMobWithinHomeArea(entity);
	}

	public boolean areSelfAndTargetInHome(@Nullable Entity entity) {
		return this.isMobWithinHomeArea(this) && (entity == null || this.isOtherEntityWithinHomeArea(entity));
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
				this.level().addParticle(ParticleTypes.EXPLOSION,
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
			this.bodySegments[i].setRot((float) (Math.atan2(diff.z(), diff.x()) * 180.0D / Math.PI) + 90.0F, -(float) (Math.atan2(diff.y(), distance) * 180.0D / Math.PI));
		}
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
	public int getHomeRadius() {
		return 40;
	}

	@Override
	public ResourceKey<Structure> getHomeStructure() {
		return TFStructures.NAGA_COURTYARD;
	}

	@Override
	public Block getDeathContainer(RandomSource random) {
		return random.nextBoolean() ? TFBlocks.TWILIGHT_OAK_CHEST.get() : TFBlocks.CANOPY_CHEST.get();
	}

	@Override
	public Block getBossSpawner() {
		return TFBlocks.NAGA_BOSS_SPAWNER.get();
	}

	@Override
	public boolean isDeathAnimationFinished() {
		return this.deathTime >= DEATH_ANIMATION_DURATION + DEATH_PARTICLES_DURATION;
	}

	@Override
	public void tickDeathAnimation() {
		if (this.deathTime >= DEATH_ANIMATION_DURATION) {
            Vec3 start = this.position().add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
            Vec3 end = EntityUtil.bossChestLocation(this).getCenter();
            Vec3 diff = end.subtract(start);

            double angle = Math.atan2(end.z - start.z, end.x - start.x) * Mth.RAD_TO_DEG + 180D;

            double xMul = angle % 180.0D;
            xMul = Math.min(xMul, 180.0D - xMul);
            xMul = Math.pow((xMul / 90.0D), 1.5D) * 2.0D;

            double zMul = (angle + 90.0D) % 180.0D;
            zMul = Math.min(zMul, 180.0D - zMul);
            zMul = Math.pow((zMul / 90.0D), 1.5D) * 2.0D;

            for (int p = 1; p <= 4; p++) {
                int trailTime = (this.deathTime - DEATH_ANIMATION_DURATION) - p;
                if (trailTime < 0) continue;
                for (double d = 0.0D; d < 1.0D; d += 0.25D) {
                    double preciseTime = trailTime - d;
                    if (preciseTime < 0.0D) continue;
                    double factor = preciseTime / (double) DEATH_PARTICLES_DURATION;
                    Vec3 particlePos = start.add(diff.scale(factor)).add(Math.sin(preciseTime * Math.PI * 0.075D) * xMul, Math.sin(preciseTime * Math.PI * 0.025D) * 0.1D, Math.cos(preciseTime * Math.PI * 0.0625D) * zMul);//Some sine waves to make it slither-y;
                    BlockHitResult blockhitresult = this.level().clip(new ClipContext(particlePos.add(0.0D, 2.0D, 0.0D), particlePos.subtract(0.0D, 3.0D, 0.0D), ClipContext.Block.COLLIDER, ClipContext.Fluid.WATER, CollisionContext.empty()));
                    particlePos = blockhitresult.getLocation().add(0.0D, 0.15D, 0.0D);
					this.level().addParticle(ParticleTypes.COMPOSTER, particlePos.x(), particlePos.y(), particlePos.z(), 0.0D, 0.0D, 0.0D);
                }
            }
        }
	}

	@Override
	public void makePoofParticles() {
		if (this.getDeathSound() != null) this.playSound(this.getDeathSound(), this.getSoundVolume() * 1.25F, this.getVoicePitch() * 0.25F);
		this.makePoofAt(this.position());
	}

	// Made separate so that the NagaSegments can do it as well
	public void makePoofAt(Vec3 pos) {
		float width = this.getBbWidth();
		float height = this.getBbHeight();
		for (int k = 0; k < 20; k++) {
			this.level().addParticle(ParticleTypes.EXPLOSION,
				(pos.x() + this.getRandom().nextFloat() * width * 2.0F) - width,
				pos.y() + this.getRandom().nextFloat() * height,
				(pos.z() + this.getRandom().nextFloat() * width * 2.0F) - width,
				this.getRandom().nextGaussian() * 0.02D, this.getRandom().nextGaussian() * 0.02D, this.getRandom().nextGaussian() * 0.02D);
		}
	}

	@Override
	public BossEvent.BossBarOverlay getBossBarOverlay() {
		return BossEvent.BossBarOverlay.NOTCHED_10;
	}

	@Override
	public int getBossBarColor() {
		return 0x5E9916;
	}
}
