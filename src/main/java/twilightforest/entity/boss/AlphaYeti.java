package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.TFConfig;
import twilightforest.init.TFAdvancements;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.ai.goal.ThrowRiderGoal;
import twilightforest.entity.ai.goal.YetiRampageGoal;
import twilightforest.entity.ai.goal.YetiTiredGoal;
import twilightforest.entity.projectile.FallingIce;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.init.*;
import twilightforest.loot.TFLootTables;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.WorldUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlphaYeti extends Monster implements RangedAttackMob, IHostileMount, EnforcedHomePoint {

	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(AlphaYeti.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);
	private static final EntityDataAccessor<Byte> RAMPAGE_FLAG = SynchedEntityData.defineId(AlphaYeti.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> TIRED_FLAG = SynchedEntityData.defineId(AlphaYeti.class, EntityDataSerializers.BYTE);
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
	private int collisionCounter;
	private boolean canRampage;
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public AlphaYeti(EntityType<? extends AlphaYeti> type, Level world) {
		super(type, world);
		this.xpReward = 317;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new YetiTiredGoal(this, 100));
		this.goalSelector.addGoal(3, new YetiRampageGoal(this, 10, 180));
		this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0D, 40, 40, 40.0F) {
			@Override
			public boolean canUse() {
				return AlphaYeti.this.getRandom().nextInt(50) > 0 && AlphaYeti.this.getTarget() != null && AlphaYeti.this.distanceToSqr(AlphaYeti.this.getTarget()) >= 16D && super.canUse(); // Give us a chance to move to the next AI
			}
		});
		this.goalSelector.addGoal(4, new ThrowRiderGoal(this, 1.0D, false) {
			@Override
			protected void checkAndPerformAttack(LivingEntity victim) {
				super.checkAndPerformAttack(victim);
				if (!AlphaYeti.this.getPassengers().isEmpty())
					AlphaYeti.this.playSound(TFSounds.ALPHA_YETI_GRAB.get(), 4F, 0.75F + AlphaYeti.this.getRandom().nextFloat() * 0.25F);
			}

			@Override
			public void stop() {
				if (!AlphaYeti.this.getPassengers().isEmpty())
					AlphaYeti.this.playSound(TFSounds.ALPHA_YETI_THROW.get(), 4F, 0.75F + AlphaYeti.this.getRandom().nextFloat() * 0.25F);
				super.stop();
			}
		});
		this.addRestrictionGoals(this, this.goalSelector);
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 2.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(RAMPAGE_FLAG, (byte) 0);
		this.getEntityData().define(TIRED_FLAG, (byte) 0);
		this.getEntityData().define(HOME_POINT, Optional.empty());
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 200.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.38D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 40.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
	}

	@Override
	public void aiStep() {
		if (!this.getPassengers().isEmpty() && this.getPassengers().get(0).isShiftKeyDown()) {
			this.getPassengers().get(0).setShiftKeyDown(false);
		}

		super.aiStep();

		if (this.isVehicle()) {
			this.getLookControl().setLookAt(getPassengers().get(0), 100F, 100F);
		}

		if (!this.level().isClientSide()) {
			this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());

			if (this.isRampaging() && (this.horizontalCollision || this.verticalCollision)) { //collided does not exist, but this is an equal?
				this.collisionCounter++;
			}

			if (this.collisionCounter >= 15) {
				this.destroyBlocksInAABB(this.getBoundingBox());
				this.collisionCounter = 0;
			}
		} else {
			if (this.isRampaging()) {
				float rotation = this.tickCount / 10F;

				for (int i = 0; i < 20; i++) {
					addSnowEffect(rotation + (i * 50), i + rotation);
				}

				// also swing limbs
				this.walkAnimation.setSpeed(this.walkAnimation.speed() + 0.6F);
			}

			if (this.isTired()) {
				for (int i = 0; i < 20; i++) {
					this.level().addParticle(ParticleTypes.SPLASH, this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 0.5, this.getY() + this.getEyeHeight(), this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 0.5, (random.nextFloat() - 0.5F) * 0.75F, 0, (random.nextFloat() - 0.5F) * 0.75F);
				}
			}
		}
	}

	private void addSnowEffect(float rotation, float hgt) {
		double px = 3F * Math.cos(rotation);
		double py = hgt % 5F;
		double pz = 3F * Math.sin(rotation);

		this.level().addParticle(TFParticleType.SNOW.get(), this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
	}

	@Override
	public void setTarget(@Nullable LivingEntity entity) {
		if (entity != null && entity != this.getTarget())
			this.playSound(TFSounds.ALPHA_YETI_ALERT.get(), 4F, 0.5F + this.getRandom().nextFloat() * 0.5F);
		super.setTarget(entity);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		// no arrow damage when in ranged mode
		if (!this.canRampage && !this.isTired() && source.is(DamageTypeTags.IS_PROJECTILE)) {
			return false;
		}

		this.canRampage = true;
		if (source.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
			this.hurtBy.add(player);
		}
		return super.hurt(source, amount);
	}

	@Override
	public void lavaHurt() {
		if (!this.fireImmune()) {
			this.setSecondsOnFire(5);
			if (this.hurt(this.damageSources().lava(), 4F)) {
				this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
				EntityUtil.killLavaAround(this);
			}
		}
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ALPHA_YETI_GROWL.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ALPHA_YETI_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ALPHA_YETI_DEATH.get();
	}

	@Override
	public float getVoicePitch() {
		return 0.5F + this.getRandom().nextFloat() * 0.5F;
	}

	@Override
	protected float getSoundVolume() {
		return 4.0F;
	}

	@Override
	protected Vector3f getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float yRot) {
		return new Vector3f(0.0F, dimensions.height, 0.4F);
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	public void destroyBlocksInAABB(AABB box) {
		if (EventHooks.getMobGriefingEvent(this.level(), this)) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				if (EntityUtil.canDestroyBlock(this.level(), pos, this)) {
					this.level().destroyBlock(pos, false);
				}
			}
		}
	}

	public void makeRandomBlockFall(int range, int hangTime) {
		if (EventHooks.getMobGriefingEvent(this.level(), this)) {
			// find a block nearby
			int bx = Mth.floor(this.getX()) + this.getRandom().nextInt(range) - this.getRandom().nextInt(range);
			int bz = Mth.floor(this.getZ()) + this.getRandom().nextInt(range) - this.getRandom().nextInt(range);
			int by = Mth.floor(this.getY() + this.getEyeHeight());

			this.makeBlockFallAbove(new BlockPos(bx, by, bz), hangTime);
		}
	}

	private void makeBlockFallAbove(BlockPos pos, int hangTime) {
		for (int i = 1; i < 25; i++) {
			BlockPos up = pos.above(i);
			if (this.level().getBlockState(up).is(BlockTags.ICE) && this.level().getBlockState(up.below()).isAir()) {
				this.makeBlockFall(up, hangTime);
				break;
			}
		}
	}

	public void makeBlockAboveTargetFall() {
		if (this.getTarget() != null) {
			this.makeBlockFallAbove(this.getTarget().blockPosition(), 40);
		}
	}

	private void makeBlockFall(BlockPos pos, int hangTime) {
		FallingIce ice = new FallingIce(this.level(), pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, this.level().getBlockState(pos), hangTime);
		this.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		this.level().addFreshEntity(ice);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		if (!this.canRampage) {
			IceBomb ice = new IceBomb(TFEntities.THROWN_ICE.get(), this.level(), this);

			// [VanillaCopy] Part of Skeleton.performRangedAttack
			double d0 = target.getX() - this.getX();
			double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - ice.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
			ice.shoot(d0, d1 + d3 * 0.2D, d2, 1.6F, 14 - this.level().getDifficulty().getId() * 4);

			this.playSound(TFSounds.ALPHA_YETI_ICE.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.gameEvent(GameEvent.PROJECTILE_SHOOT);
			this.level().addFreshEntity(ice);
		}
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.isRestrictionPointValid(this.level().dimension()) && this.level().isLoaded(this.getRestrictionPoint().pos())) {
				this.level().setBlockAndUpdate(this.getRestrictionPoint().pos(), TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	public boolean canRampage() {
		return this.canRampage;
	}

	public void setRampaging(boolean rampaging) {
		getEntityData().set(RAMPAGE_FLAG, (byte) (rampaging ? 1 : 0));
	}

	public boolean isRampaging() {
		return getEntityData().get(RAMPAGE_FLAG) == 1;
	}

	public void setTired(boolean tired) {
		this.getEntityData().set(TIRED_FLAG, (byte) (tired ? 1 : 0));
		this.canRampage = false;
	}

	public boolean isTired() {
		return this.getEntityData().get(TIRED_FLAG) == 1;
	}

	@Override
	public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {

		if (!this.level().isClientSide() && isRampaging()) {
			this.playSound(TFSounds.ALPHA_YETI_ICE.get(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.hitNearbyEntities();
		}

		return super.causeFallDamage(distance, multiplier, source);
	}

	private void hitNearbyEntities() {
		for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5, 0, 5))) {
			if (entity != this && entity.hurt(this.damageSources().mobAttack(this), 5F)) {
				entity.push(0, 0.4, 0);
			}
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the lair as defeated
		if (!this.level().isClientSide()) {
			this.bossInfo.setProgress(0.0F);
			LandmarkUtil.markStructureConquered(this.level(), this, TFStructures.YETI_CAVE, true);
			for (ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.get().trigger(player, this);
			}

			TFLootTables.entityDropsIntoContainer(this,cause, TFBlocks.CANOPY_CHEST.get().defaultBlockState(), EntityUtil.bossChestLocation(this));
		}
	}

	@Override
	protected boolean shouldDropLoot() {
		return !TFConfig.COMMON_CONFIG.bossDropChests.get();
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
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
	public void addAdditionalSaveData(CompoundTag compound) {
		this.saveHomePointToNbt(compound);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.loadHomePointFromNbt(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
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
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
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
		return 30;
	}
}
