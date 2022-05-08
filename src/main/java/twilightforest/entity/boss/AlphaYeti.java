package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFSounds;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.TFEntities;
import twilightforest.entity.ai.StayNearHomeGoal;
import twilightforest.entity.ai.ThrowRiderGoal;
import twilightforest.entity.ai.YetiRampageGoal;
import twilightforest.entity.ai.YetiTiredGoal;
import twilightforest.entity.projectile.FallingIce;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.loot.TFTreasure;
import twilightforest.util.EntityUtil;
import twilightforest.util.WorldUtil;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AlphaYeti extends Monster implements RangedAttackMob, IHostileMount {

	private static final EntityDataAccessor<Byte> RAMPAGE_FLAG = SynchedEntityData.defineId(AlphaYeti.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> TIRED_FLAG = SynchedEntityData.defineId(AlphaYeti.class, EntityDataSerializers.BYTE);
	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
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
		this.goalSelector.addGoal(2, new StayNearHomeGoal(this, 2.0F));
		this.goalSelector.addGoal(3, new YetiRampageGoal(this, 10, 180));
		this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0D, 40, 40, 40.0F) {
			@Override
			public boolean canUse() {
				return getRandom().nextInt(50) > 0 && getTarget() != null && distanceToSqr(getTarget()) >= 16D && super.canUse(); // Give us a chance to move to the next AI
			}
		});
		this.goalSelector.addGoal(4, new ThrowRiderGoal(this, 1.0D, false) {
			@Override
			protected void checkAndPerformAttack(LivingEntity victim, double p_190102_2_) {
				super.checkAndPerformAttack(victim, p_190102_2_);
				if (!getPassengers().isEmpty())
					playSound(TFSounds.ALPHAYETI_GRAB, 4F, 0.75F + getRandom().nextFloat() * 0.25F);
			}

			@Override
			public void stop() {
				if (!getPassengers().isEmpty())
					playSound(TFSounds.ALPHAYETI_THROW, 4F, 0.75F + getRandom().nextFloat() * 0.25F);
				super.stop();
			}
		});
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 2.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(RAMPAGE_FLAG, (byte) 0);
		entityData.define(TIRED_FLAG, (byte) 0);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 200.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.38D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 40.0D);
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

		if (!level.isClientSide) {
			bossInfo.setProgress(getHealth() / getMaxHealth());

			if (this.horizontalCollision || this.verticalCollision) { //collided does not exist, but this is an equal?
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
				this.animationSpeed += 0.6;
			}

			if (this.isTired()) {
				for (int i = 0; i < 20; i++) {
					this.level.addParticle(ParticleTypes.SPLASH, this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 0.5, this.getY() + this.getEyeHeight(), this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 0.5, (random.nextFloat() - 0.5F) * 0.75F, 0, (random.nextFloat() - 0.5F) * 0.75F);
				}
			}
		}
	}

	private void addSnowEffect(float rotation, float hgt) {
		double px = 3F * Math.cos(rotation);
		double py = hgt % 5F;
		double pz = 3F * Math.sin(rotation);

		level.addParticle(TFParticleType.SNOW.get(), this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
	}

	@Override
	public void setTarget(@Nullable LivingEntity entity) {
		if (entity != null && entity != getTarget())
			playSound(TFSounds.ALPHAYETI_ALERT, 4F, 0.5F + getRandom().nextFloat() * 0.5F);
		super.setTarget(entity);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		// no arrow damage when in ranged mode
		if (!this.canRampage && !this.isTired() && source.isProjectile()) {
			return false;
		}

		this.canRampage = true;
		if(source.getEntity() instanceof ServerPlayer player && !hurtBy.contains(player)) {
			hurtBy.add(player);
		}
		return super.hurt(source, amount);
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ALPHAYETI_GROWL;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.ALPHAYETI_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ALPHAYETI_DEATH;
	}

	@Override
	public float getVoicePitch() {
		return 0.5F + getRandom().nextFloat() * 0.5F;
	}

	@Override
	protected float getSoundVolume() {
		return 4F;
	}

	@Override
	public void positionRider(Entity passenger) {
		Vec3 riderPos = this.getRiderPosition();
		passenger.setPos(riderPos.x, riderPos.y, riderPos.z);
	}

	@Override
	public double getPassengersRidingOffset() {
		return 5.75D;
	}

	/**
	 * Used to both get a rider position and to push out of blocks
	 */
	private Vec3 getRiderPosition() {
		if (isVehicle()) {
			float distance = 0.4F;

			double dx = Math.cos((this.getYRot() + 90) * Math.PI / 180.0D) * distance;
			double dz = Math.sin((this.getYRot() + 90) * Math.PI / 180.0D) * distance;

			return new Vec3(this.getX() + dx, this.getY() + this.getPassengersRidingOffset() + this.getPassengers().get(0).getMyRidingOffset(), this.getZ() + dz);
		} else {
			return new Vec3(this.getX(), this.getY(), this.getZ());
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	public void destroyBlocksInAABB(AABB box) {
		if (ForgeEventFactory.getMobGriefingEvent(level, this)) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				if (EntityUtil.canDestroyBlock(level, pos, this)) {
					level.destroyBlock(pos, false);
				}
			}
		}
	}

	public void makeRandomBlockFall() {
		// begin turning blocks into falling blocks
		makeRandomBlockFall(30);
	}

	private void makeRandomBlockFall(int range) {
		// find a block nearby
		int bx = Mth.floor(this.getX()) + this.getRandom().nextInt(range) - this.getRandom().nextInt(range);
		int bz = Mth.floor(this.getZ()) + this.getRandom().nextInt(range) - this.getRandom().nextInt(range);
		int by = Mth.floor(this.getY() + this.getEyeHeight());

		makeBlockFallAbove(new BlockPos(bx, bz, by));
	}

	private void makeBlockFallAbove(BlockPos pos) {
		if (level.isEmptyBlock(pos)) {
			for (int i = 1; i < 30; i++) {
				BlockPos up = pos.above(i);
				if (!level.isEmptyBlock(up)) {
					makeBlockFall(up);
					break;
				}
			}
		}
	}

	public void makeNearbyBlockFall() {
		makeRandomBlockFall(15);
	}

	public void makeBlockAboveTargetFall() {
		if (this.getTarget() != null) {

			int bx = Mth.floor(this.getTarget().getX());
			int bz = Mth.floor(this.getTarget().getZ());
			int by = Mth.floor(this.getTarget().getY() + this.getTarget().getEyeHeight());

			makeBlockFallAbove(new BlockPos(bx, bz, by));
		}

	}

	private void makeBlockFall(BlockPos pos) {
		level.setBlockAndUpdate(pos, Blocks.PACKED_ICE.defaultBlockState());
		level.levelEvent(2001, pos, Block.getId(Blocks.PACKED_ICE.defaultBlockState()));

		FallingIce ice = new FallingIce(level, pos.getX(), pos.getY() - 3, pos.getZ());
		level.addFreshEntity(ice);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		if (!this.canRampage) {
			IceBomb ice = new IceBomb(TFEntities.THROWN_ICE.get(), this.level, this);

			// [VanillaCopy] Part of EntitySkeleton.attackEntityWithRangedAttack
			double d0 = target.getX() - this.getX();
			double d1 = target.getBoundingBox().minY + target.getBbHeight() / 3.0F - ice.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
			ice.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 14 - this.level.getDifficulty().getId() * 4);

			this.playSound(TFSounds.ALPHAYETI_ICE, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.level.addFreshEntity(ice);
		}
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			if (getRestrictCenter() != BlockPos.ZERO) {
				level.setBlockAndUpdate(getRestrictCenter(), TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get().defaultBlockState());
			}
			remove(RemovalReason.DISCARDED);
		} else {
			super.checkDespawn();
		}
	}

	public boolean canRampage() {
		return this.canRampage;
	}

	public void setRampaging(boolean rampaging) {
		entityData.set(RAMPAGE_FLAG, (byte) (rampaging ? 1 : 0));
	}

	public boolean isRampaging() {
		return entityData.get(RAMPAGE_FLAG) == 1;
	}

	public void setTired(boolean tired) {
		entityData.set(TIRED_FLAG, (byte) (tired ? 1 : 0));
		this.canRampage = false;
	}

	public boolean isTired() {
		return entityData.get(TIRED_FLAG) == 1;
	}

	@Override
	public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {

		if (!this.level.isClientSide && isRampaging()) {
			this.playSound(TFSounds.ALPHAYETI_ICE, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			hitNearbyEntities();
		}

		//TODO: Return value?
		return super.causeFallDamage(distance, multiplier, source);
	}

	private void hitNearbyEntities() {
		for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5, 0, 5))) {
			if (entity != this && entity.hurt(DamageSource.mobAttack(this), 5F)) {
				entity.push(0, 0.4, 0);
			}
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the lair as defeated
		if (!level.isClientSide) {
			TFGenerationSettings.markStructureConquered(level, new BlockPos(this.blockPosition()), TFFeature.YETI_CAVE);
			for(ServerPlayer player : hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}

			TFTreasure.entityDropsIntoContainer(this, this.createLootContext(true, cause).create(LootContextParamSets.ENTITY), TFBlocks.CANOPY_CHEST.get().defaultBlockState(), new BlockPos(this.position()));
		}
	}

	@Override
	protected boolean shouldDropLoot() {
		// Invoked the mob's loot during die, this will avoid duplicating during the actual drop phase
		return false;
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
		BlockPos home = this.getRestrictCenter();
		compound.put("Home", newDoubleList(home.getX(), home.getY(), home.getZ()));
		compound.putBoolean("HasHome", this.hasRestriction());
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Home", 9)) {
			ListTag nbttaglist = compound.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			this.restrictTo(new BlockPos(hx, hy, hz), 30);
		}
		if (!compound.getBoolean("HasHome")) {
			this.hasRestriction();
		}
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
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
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}
}
