package twilightforest.entity.boss;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFAdvancements;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.ai.control.NoClipMoveControl;
import twilightforest.entity.ai.goal.PhantomAttackStartGoal;
import twilightforest.entity.ai.goal.PhantomThrowWeaponGoal;
import twilightforest.entity.ai.goal.PhantomUpdateFormationAndMoveGoal;
import twilightforest.entity.ai.goal.PhantomWatchAndAttackGoal;
import twilightforest.init.*;
import twilightforest.loot.TFLootTables;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class KnightPhantom extends FlyingMob implements Enemy, EnforcedHomePoint {

	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(KnightPhantom.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);
	private static final EntityDataAccessor<Boolean> FLAG_CHARGING = SynchedEntityData.defineId(KnightPhantom.class, EntityDataSerializers.BOOLEAN);
	private static final AttributeModifier CHARGING_MODIFIER = new AttributeModifier("Charging attack boost", 7, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier NON_CHARGING_ARMOR_MODIFIER = new AttributeModifier("Inactive Armor boost", 4.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);

	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);

	private int number;
	private int totalKnownKnights = 1;
	private int ticksProgress;
	private Formation currentFormation;
	private BlockPos chargePos = BlockPos.ZERO;
	private final List<ServerPlayer> hurtBy = new ArrayList<>();
	private final EntityDimensions invisibleSize = EntityDimensions.fixed(1.25F, 2.5F);
	private final EntityDimensions visibleSize = EntityDimensions.fixed(1.75F, 4.0F);

	public KnightPhantom(EntityType<? extends KnightPhantom> type, Level world) {
		super(type, world);
		this.noPhysics = true;
		this.currentFormation = Formation.HOVER;
		this.xpReward = 93;
		this.moveControl = new NoClipMoveControl(this);
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		if (getNumber() == 0)
			this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		SpawnGroupData data = super.finalizeSpawn(accessor, difficulty, reason, spawnDataIn, dataTag);
		this.populateDefaultEquipmentSlots(accessor.getRandom(), difficulty);
		this.populateDefaultEquipmentEnchantments(accessor.getRandom(), difficulty);
		this.getAttribute(Attributes.ARMOR).addTransientModifier(NON_CHARGING_ARMOR_MODIFIER);
		return data;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_SWORD.get()));
		this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(TFItems.PHANTOM_CHESTPLATE.get()));
		this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(TFItems.PHANTOM_HELMET.get()));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(FLAG_CHARGING, false);
		this.getEntityData().define(HOME_POINT, Optional.empty());
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new PhantomWatchAndAttackGoal(this));
		this.goalSelector.addGoal(1, new PhantomUpdateFormationAndMoveGoal(this));
		this.goalSelector.addGoal(2, new PhantomAttackStartGoal(this));
		this.goalSelector.addGoal(3, new PhantomThrowWeaponGoal(this));

		this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 35.0D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D);
	}

	public Formation getCurrentFormation() {
		return this.currentFormation;
	}

	public BlockPos getChargePos() {
		return this.chargePos;
	}

	public void setChargePos(BlockPos pos) {
		this.chargePos = pos;
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_) {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource src) {
		return src.is(DamageTypes.IN_WALL) || super.isInvulnerableTo(src);
	}

	@Override
	public void checkDespawn() {
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.getRestrictionPoint() != null && this.getNumber() == 0) {
				this.level().setBlockAndUpdate(this.getRestrictionPoint().pos(), TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!this.level().isClientSide() && this.getNumber() == 0) {
			float health = 0F;
			float maxHealth = 0F;
			int amount = 0;
			for (KnightPhantom nearbyKnight : this.getNearbyKnights()) {
				health += nearbyKnight.getHealth();
				maxHealth += nearbyKnight.getMaxHealth();
				amount++;
			}
			int remaining = totalKnownKnights - amount;
			if (remaining > 0) {
				maxHealth += (this.getMaxHealth() * (float) remaining);
			}
			this.bossInfo.setProgress(health / maxHealth);
		}

		if (this.isChargingAtPlayer()) {
			// make particles
			for (int i = 0; i < 4; ++i) {
				Item particleID = this.getRandom().nextBoolean() ? TFItems.PHANTOM_HELMET.get() : TFItems.KNIGHTMETAL_SWORD.get();

				this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(particleID)), this.getX() + (this.getRandom().nextFloat() - 0.5D) * this.getBbWidth(), this.getY() + this.getRandom().nextFloat() * (this.getBbHeight() - 0.75D) + 0.5D, this.getZ() + (this.getRandom().nextFloat() - 0.5D) * this.getBbWidth(), 0, -0.1, 0);
				this.level().addParticle(ParticleTypes.SMOKE, this.getX() + (this.getRandom().nextFloat() - 0.5D) * this.getBbWidth(), this.getY() + this.getRandom().nextFloat() * (this.getBbHeight() - 0.75D) + 0.5D, this.getZ() + (this.getRandom().nextFloat() - 0.5D) * this.getBbWidth(), 0, 0.1, 0);
			}
		}
	}

	@Override
	protected void tickDeath() {
		super.tickDeath();

		for (int i = 0; i < 20; ++i) {
			double d0 = this.getRandom().nextGaussian() * 0.02D;
			double d1 = this.getRandom().nextGaussian() * 0.02D;
			double d2 = this.getRandom().nextGaussian() * 0.02D;
			this.level().addParticle(ParticleTypes.EXPLOSION, this.getX() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), this.getY() + this.getRandom().nextFloat() * this.getBbHeight(), this.getZ() + this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(), d0, d1, d2);
		}
	}

	@Override
	public void die(DamageSource cause) {

		super.die(cause);

		if (this.level() instanceof ServerLevel serverLevel) {
			List<KnightPhantom> knights = this.getNearbyKnights();
			if (!knights.isEmpty()) {
				knights.forEach(KnightPhantom::updateMyNumber);
			} else if (!cause.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
				this.bossInfo.setProgress(0.0F);

				BlockPos treasurePos = this.getRestrictionPoint() != null ? serverLevel.getBlockState(this.getRestrictionPoint().pos().below()).canBeReplaced() ? this.getRestrictionPoint().pos().below() : this.getRestrictionPoint().pos() : this.blockPosition();

				// make treasure for killing the last knight
				// This one won't receive the same loot treatment like the other bosses because this chest is supposed to reward for all of them instead of just the last one killed
				TFLootTables.STRONGHOLD_BOSS.generateLootContainer(serverLevel, treasurePos, TFBlocks.DARK_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH), 2, this.getLootTableSeed());

				//trigger criteria for killing every phantom in a group
				if (cause.getEntity() instanceof ServerPlayer player) {
					TFAdvancements.KILL_ALL_PHANTOMS.get().trigger(player);
				}

				// mark the stronghold as defeated
				LandmarkUtil.markStructureConquered(this.level(), this, TFStructures.KNIGHT_STRONGHOLD, true);

				for (ServerPlayer player : this.hurtBy) {
					TFAdvancements.HURT_BOSS.get().trigger(player, this);
				}

				for (ServerPlayer player : this.level().getEntitiesOfClass(ServerPlayer.class, new AABB(treasurePos).inflate(32.0D))) {
					TFAdvancements.HURT_BOSS.get().trigger(player, this);
				}
			}
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isDamageSourceBlocked(source)) {
			this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level().getRandom().nextFloat() * 0.4F);
		}

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

	@Override
	public boolean doHurtTarget(Entity entity) {
		return EntityUtil.properlyApplyCustomDamageSource(this, entity, TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.HAUNT, this));
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	public void knockback(double damage, double xRatio, double zRatio) {
		this.hasImpulse = true;
		float f = Mth.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
		float distance = 0.2F;
		this.setDeltaMovement(new Vec3(this.getDeltaMovement().x() / 2.0D, this.getDeltaMovement().y() / 2.0D, this.getDeltaMovement().z() / 2.0D));
		this.setDeltaMovement(new Vec3(
				this.getDeltaMovement().x() - xRatio / f * distance,
				this.getDeltaMovement().y() + distance,
				this.getDeltaMovement().z() - zRatio / f * distance));

		if (this.getDeltaMovement().y() > 0.4D) {
			this.setDeltaMovement(this.getDeltaMovement().x(), 0.4D, this.getDeltaMovement().z());
		}
	}

	public List<KnightPhantom> getNearbyKnights() {
		return this.level().getEntitiesOfClass(KnightPhantom.class, this.getBoundingBox().inflate(64.0D), LivingEntity::isAlive);
	}

	private void updateMyNumber() {
		List<Integer> nums = Lists.newArrayList();
		List<KnightPhantom> knights = getNearbyKnights();
		for (KnightPhantom knight : knights) {
			if (knight == this || !knight.isAlive())
				continue;
			nums.add(knight.getNumber());
			if (knight.getNumber() == 0)
				this.setRestrictionPoint(knight.getRestrictionPoint());
		}
		if (nums.isEmpty()) {
			this.setNumber(0);
			return;
		}
		int[] n = Ints.toArray(nums);
		Arrays.sort(n);
		int smallest = n[0];
		int largest = knights.size() + 1;
		int smallestUnused = largest + 1;
		if (smallest > 0) {
			smallestUnused = 0;
		} else {
			for (int i = 1; i < largest; i++) {
				if (Arrays.binarySearch(n, i) < 0) {
					smallestUnused = i;
					break;
				}
			}
		}
		if (this.totalKnownKnights < largest)
			this.totalKnownKnights = largest;
		if (this.number > smallestUnused || nums.contains(this.number))
			this.setNumber(smallestUnused);
	}

	public boolean isChargingAtPlayer() {
		return this.getEntityData().get(FLAG_CHARGING);
	}

	private void setChargingAtPlayer(boolean flag) {
		this.getEntityData().set(FLAG_CHARGING, flag);
		if (!this.level().isClientSide()) {
			if (flag) {
				if (!this.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(CHARGING_MODIFIER)) {
					this.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(CHARGING_MODIFIER);
				}
				if (this.getAttribute(Attributes.ARMOR).hasModifier(NON_CHARGING_ARMOR_MODIFIER)) {
					this.getAttribute(Attributes.ARMOR).removeModifier(NON_CHARGING_ARMOR_MODIFIER.getId());
				}
			} else {
				this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(CHARGING_MODIFIER.getId());
				if (!this.getAttribute(Attributes.ARMOR).hasModifier(NON_CHARGING_ARMOR_MODIFIER)) {
					this.getAttribute(Attributes.ARMOR).addTransientModifier(NON_CHARGING_ARMOR_MODIFIER);
				}
			}
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
		if (FLAG_CHARGING.equals(accessor)) {
			this.refreshDimensions();
		}
		super.onSyncedDataUpdated(accessor);
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.isChargingAtPlayer() ? this.visibleSize : this.invisibleSize;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.KNIGHT_PHANTOM_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.KNIGHT_PHANTOM_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.KNIGHT_PHANTOM_DEATH.get();
	}

	private void switchToFormationByNumber(int formationNumber) {
		this.currentFormation = Formation.values()[formationNumber];
		this.ticksProgress = 0;
	}

	public void switchToFormation(Formation formation) {
		this.currentFormation = formation;
		this.ticksProgress = 0;
		this.updateMyNumber();
		this.setChargingAtPlayer(this.currentFormation == Formation.ATTACK_PLAYER_START || this.currentFormation == Formation.ATTACK_PLAYER_ATTACK);

	}

	private int getFormationAsNumber() {
		return this.currentFormation.ordinal();
	}

	public int getTicksProgress() {
		return this.ticksProgress;
	}

	public void setTicksProgress(int ticksProgress) {
		this.ticksProgress = ticksProgress;
	}

	public int getMaxTicksForFormation() {
		return this.currentFormation.duration;
	}

	public boolean isSwordKnight() {
		return this.getMainHandItem().is(TFItems.KNIGHTMETAL_SWORD);
	}

	public boolean isAxeKnight() {
		return this.getMainHandItem().is(TFItems.KNIGHTMETAL_AXE);
	}

	public boolean isPickKnight() {
		return this.getMainHandItem().is(TFItems.KNIGHTMETAL_PICKAXE);
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
		if (number == 0)
			this.level().getEntitiesOfClass(ServerPlayer.class, this.getBoundingBox().inflate(64.0D)).forEach(this::startSeenByPlayer);

		// set weapon per number
		switch (number % 3) {
			case 0 -> this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_SWORD.get()));
			case 1 -> this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_AXE.get()));
			case 2 -> this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_PICKAXE.get()));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		this.saveHomePointToNbt(compound);
		compound.putInt("MyNumber", this.getNumber());
		compound.putInt("Formation", this.getFormationAsNumber());
		compound.putInt("TicksProgress", this.getTicksProgress());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);

		this.loadHomePointFromNbt(compound);
		this.setNumber(compound.getInt("MyNumber"));
		this.switchToFormationByNumber(compound.getInt("Formation"));
		this.setTicksProgress(compound.getInt("TicksProgress"));

		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public @Nullable GlobalPos getRestrictionPoint() {
		return this.getEntityData().get(HOME_POINT).orElse(null);
	}

	@Override
	public void setRestrictionPoint(@Nullable GlobalPos pos) {
		this.getEntityData().set(HOME_POINT, Optional.ofNullable(pos));
		// set the chargePos here as well, so we don't go off flying in some direction when we first spawn
		if (pos != null) {
			this.chargePos = pos.pos();
		}
	}

	@Override
	public int getHomeRadius() {
		return 30;
	}

	public enum Formation {

		HOVER(90),

		LARGE_CLOCKWISE(180),

		SMALL_CLOCKWISE(90),

		LARGE_ANTICLOCKWISE(180),

		SMALL_ANTICLOCKWISE(90),

		CHARGE_PLUSX(180),

		CHARGE_MINUSX(180),

		CHARGE_PLUSZ(180),

		CHARGE_MINUSZ(180),

		WAITING_FOR_LEADER(10),

		ATTACK_PLAYER_START(50),

		ATTACK_PLAYER_ATTACK(50);

		final int duration;

		Formation(int duration) {
			this.duration = duration;
		}
	}

	@Override
	protected boolean canRide(Entity entityIn) {
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
}
