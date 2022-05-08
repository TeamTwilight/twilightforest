package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.TFSounds;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.AbstractLightableBlock;
import twilightforest.block.TFBlocks;
import twilightforest.entity.TFEntities;
import twilightforest.entity.ai.AlwaysWatchTargetGoal;
import twilightforest.entity.ai.LichMinionsGoal;
import twilightforest.entity.ai.LichPopMobsGoal;
import twilightforest.entity.ai.LichShadowsGoal;
import twilightforest.entity.monster.LichMinion;
import twilightforest.entity.projectile.LichBolt;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.loot.TFTreasure;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lich extends Monster {

	private static final EntityDataAccessor<Boolean> DATA_ISCLONE = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Byte> DATA_SHIELDSTRENGTH = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> DATA_MINIONSLEFT = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> DATA_ATTACKTYPE = SynchedEntityData.defineId(Lich.class, EntityDataSerializers.BYTE);

	public static final int MAX_SHADOW_CLONES = 2;
	public static final int INITIAL_SHIELD_STRENGTH = 6;
	public static final int MAX_ACTIVE_MINIONS = 3;
	public static final int INITIAL_MINIONS_TO_SUMMON = 9;
	public static final int MAX_HEALTH = 100;

	private Lich masterLich;
	private int attackCooldown;
	private int popCooldown;
	private int spawnTime;
	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.NOTCHED_6);
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public Lich(EntityType<? extends Lich> type, Level world) {
		super(type, world);

		setShadowClone(false);
		this.masterLich = null;
		this.fireImmune();
		this.xpReward = 217;
	}

	public Lich(Level world, Lich otherLich) {
		this(TFEntities.LICH.get(), world);

		setShadowClone(true);
		this.masterLich = otherLich;
	}

	public Lich getMasterLich(){
		return masterLich;
	}

	public int getAttackCooldown() {
		return attackCooldown;
	}

	public void setAttackCooldown(int cooldown) {
		attackCooldown = cooldown;
	}

	public int getPopCooldown() {
		return popCooldown;
	}

	public void setPopCooldown(int cooldown) {
		popCooldown = cooldown;
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new AlwaysWatchTargetGoal(this));
		this.goalSelector.addGoal(1, new LichPopMobsGoal(this));
		this.goalSelector.addGoal(2, new LichShadowsGoal(this));
		this.goalSelector.addGoal(3, new LichMinionsGoal(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 0.75D, true) {
			@Override
			public boolean canUse() {
				return getPhase() == 3 && super.canUse();
			}

			@Override
			public void start() {
				super.start();
				setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
			}
		});

		this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
			@Override
			public boolean canUse() {
				if (this.mob instanceof Lich main && this.mob.getLastHurtByMob() instanceof Lich lich && lich.masterLich == main.masterLich) {
					return false;
				}
				return super.canUse();
			}
		});
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_ISCLONE, false);
		entityData.define(DATA_SHIELDSTRENGTH, (byte) INITIAL_SHIELD_STRENGTH);
		entityData.define(DATA_MINIONSLEFT, (byte) INITIAL_MINIONS_TO_SUMMON);
		entityData.define(DATA_ATTACKTYPE, (byte) 0);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.45000001788139344D); // Same speed as an angry enderman
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
	public boolean removeWhenFarAway(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (level.getDifficulty() == Difficulty.PEACEFUL && !isShadowClone()) {
			if (hasRestriction()) {
				level.setBlockAndUpdate(getRestrictCenter(), TFBlocks.LICH_BOSS_SPAWNER.get().defaultBlockState());
			}
			discard();
		} else {
			super.checkDespawn();
		}
	}

	/**
	 * What phase of the fight are we on?
	 * <p>
	 * 1 - reflecting bolts, shield up
	 * 2 - summoning minions
	 * 3 - melee
	 */
	public int getPhase() {
		if (isShadowClone() || getShieldStrength() > 0) {
			return 1;
		} else if (getMinionsToSummon() > 0 || countMyMinions() > 0) {
			return 2;
		} else {
			return 3;
		}
	}

	public void setExtinguishTimer() {
		spawnTime = 20;
	}

	@Override
	public void aiStep() {
		// determine the hand position
		float angle = ((yBodyRot * 3.141593F) / 180F);

		double dx = getX() + (Mth.cos(angle) * 0.65);
		double dy = getY() + (getBbHeight() * 0.94);
		double dz = getZ() + (Mth.sin(angle) * 0.65);


		// add particles!

		// how many particles do we want to add?!
		int factor = (80 - attackCooldown) / 10;
		int particles = factor > 0 ? random.nextInt(factor) : 1;


		for (int j1 = 0; j1 < particles; j1++) {
			float sparkle = 1.0F - (attackCooldown + 1.0F) / 60.0F;
			sparkle *= sparkle;

			float red = 0.37F * sparkle;
			float grn = 0.99F * sparkle;
			float blu = 0.89F * sparkle;

			// change color for fireball
			if (this.getNextAttackType() != 0) {
				red = 0.99F * sparkle;
				grn = 0.47F * sparkle;
				blu = 0.00F * sparkle;
			}

			level.addParticle(ParticleTypes.ENTITY_EFFECT, dx + (random.nextGaussian() * 0.025), dy + (random.nextGaussian() * 0.025), dz + (random.nextGaussian() * 0.025), red, grn, blu);
		}

		if (this.getPhase() == 3)
			level.addParticle(ParticleTypes.ANGRY_VILLAGER,
				this.getX() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
				this.getY() + 1.0D + this.random.nextFloat() * this.getBbHeight(),
				this.getZ() + this.random.nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth(),
				this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);

		if (!level.isClientSide) {
			if (this.getPhase() == 1) {
				bossInfo.setProgress((float) (getShieldStrength()) / (float) (INITIAL_SHIELD_STRENGTH));
			} else {
				bossInfo.setOverlay(BossEvent.BossBarOverlay.PROGRESS);
				bossInfo.setProgress(getHealth() / getMaxHealth());
				if (this.getPhase() == 2)
					bossInfo.setColor(BossEvent.BossBarColor.PURPLE);
				else
					bossInfo.setColor(BossEvent.BossBarColor.RED);
			}
		}

		super.aiStep();
	}

	@Override
	public boolean hurt(DamageSource src, float damage) {
		// if we're in a wall, teleport for gosh sakes
		if ("inWall".equals(src.getMsgId()) && getTarget() != null) {
			teleportToSightOfEntity(getTarget());
		}

		if (isShadowClone() && src != DamageSource.OUT_OF_WORLD) {
			playSound(TFSounds.LICH_CLONE_HURT, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			return false;
		}

		// ignore all bolts that are not reflected
		if (src.getEntity() instanceof Lich) {
			return false;
		}

		// if our shield is up, ignore any damage that can be blocked.
		if (src != DamageSource.OUT_OF_WORLD && getShieldStrength() > 0) {
			if (src.isMagic() && damage > 2) {
				// reduce shield for magic damage greater than 1 heart
				if (getShieldStrength() > 0) {
					setShieldStrength(getShieldStrength() - 1);
					playSound(TFSounds.SHIELD_BREAK, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				}
			} else {
				playSound(TFSounds.SHIELD_BREAK, 1.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				if (src.getEntity() instanceof LivingEntity) {
					setLastHurtByMob((LivingEntity) src.getEntity());
				}
			}

			return false;
		}

		if (super.hurt(src, damage)) {
			if (this.getPhase() < 3 || random.nextInt(4) == 0) {
				this.teleportToSightOfEntity(this.getTarget());
			}

			if(src.getEntity() instanceof ServerPlayer player && !hurtBy.contains(player)) {
				hurtBy.add(player);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		if (attackCooldown > 0 && spawnTime <= 0) {
			attackCooldown--;
		}

		if (popCooldown > 0) {
			popCooldown--;
		}

		if(this.getTarget() != null) {
			if (spawnTime > 0 && hasLineOfSight(getTarget())) {
				spawnTime--;
				if (spawnTime <= 0) {
					extinguishNearbyCandles();
				}
			}
		}
	}

	private void extinguishNearbyCandles() {
		AABB box = this.getBoundingBox().inflate(10.0D);
		for(BlockPos pos : BlockPos.betweenClosed(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ), Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ))) {
			if(level.getBlockState(pos).getBlock() instanceof AbstractCandleBlock && level.getBlockState(pos).getValue(BlockStateProperties.LIT)) {
				level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(BlockStateProperties.LIT, false));
				level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 2.0F, 1.0F);
			} else if(level.getBlockState(pos).getBlock() instanceof AbstractLightableBlock && level.getBlockState(pos).getValue(AbstractLightableBlock.LIGHTING) == AbstractLightableBlock.Lighting.NORMAL) {
				level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(AbstractLightableBlock.LIGHTING, AbstractLightableBlock.Lighting.OMINOUS));
				level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 2.0F, 0.75F);
			}
		}
	}

	public void launchBoltAt() {
		float bodyFacingAngle = ((yBodyRot * 3.141593F) / 180F);
		double sx = getX() + (Mth.cos(bodyFacingAngle) * 0.65);
		double sy = getY() + (getBbHeight() * 0.82);
		double sz = getZ() + (Mth.sin(bodyFacingAngle) * 0.65);

		double tx = Objects.requireNonNull(getTarget()).getX() - sx;
		double ty = (getTarget().getBoundingBox().minY + getTarget().getBbHeight() / 2.0F) - (getY() + getBbHeight() / 2.0F);
		double tz = getTarget().getZ() - sz;

		playSound(TFSounds.LICH_SHOOT, getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

		LichBolt projectile = new LichBolt(TFEntities.LICH_BOLT.get(), level, this);
		projectile.moveTo(sx, sy, sz, getYRot(), getXRot());
		projectile.shoot(tx, ty, tz, 0.5F, 1.0F);

		level.addFreshEntity(projectile);
	}

	public void launchBombAt() {
		float bodyFacingAngle = ((yBodyRot * 3.141593F) / 180F);
		double sx = getX() + (Mth.cos(bodyFacingAngle) * 0.65);
		double sy = getY() + (getBbHeight() * 0.82);
		double sz = getZ() + (Mth.sin(bodyFacingAngle) * 0.65);

		double tx = Objects.requireNonNull(getTarget()).getX() - sx;
		double ty = (getTarget().getBoundingBox().minY + getTarget().getBbHeight() / 2.0F) - (getY() + getBbHeight() / 2.0F);
		double tz = getTarget().getZ() - sz;

		playSound(TFSounds.LICH_SHOOT, getSoundVolume(), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

		LichBomb projectile = new LichBomb(TFEntities.LICH_BOMB.get(), level, this);
		projectile.moveTo(sx, sy, sz, getYRot(), getXRot());
		projectile.shoot(tx, ty, tz, 0.35F, 1.0F);

		level.addFreshEntity(projectile);
	}

	public boolean wantsNewClone(Lich clone) {
		return clone.isShadowClone() && countMyClones() < Lich.MAX_SHADOW_CLONES;
	}

	public void setMaster(Lich lich) {
		masterLich = lich;
	}

	public int countMyClones() {
		// check if there are enough clones.  we check a 32x16x32 area
		int count = 0;

		for (Lich nearbyLich : getNearbyLiches()) {
			if (nearbyLich.isShadowClone() && nearbyLich.getMasterLich() == this) {
				count++;
			}
		}

		return count;
	}

	public List<? extends Lich> getNearbyLiches() {
		return level.getEntitiesOfClass(getClass(), new AABB(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1).inflate(32.0D, 16.0D, 32.0D));
	}

	public boolean wantsNewMinion() {
		return countMyMinions() < Lich.MAX_ACTIVE_MINIONS;
	}

	public int countMyMinions() {
		return (int) level.getEntitiesOfClass(LichMinion.class, new AABB(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1).inflate(32.0D, 16.0D, 32.0D))
				.stream()
				.filter(m -> m.master == this)
				.count();
	}

	public void teleportToSightOfEntity(@Nullable Entity entity) {
		Vec3 dest = findVecInLOSOf(entity);
		double srcX = getX();
		double srcY = getY();
		double srcZ = getZ();

		if (dest != null && entity != null) {
			teleportToNoChecks(dest.x, dest.y, dest.z);
			this.getLookControl().setLookAt(entity, 100F, 100F);
			this.yBodyRot = this.getYRot();

			if (!this.getSensing().hasLineOfSight(entity)) {
				teleportToNoChecks(srcX, srcY, srcZ);
			}
		}
	}

	/**
	 * Returns coords that would be good to teleport to.
	 * Returns null if we can't find anything
	 */
	@Nullable
	public Vec3 findVecInLOSOf(@Nullable Entity targetEntity) {
		if (targetEntity == null) return null;
		double origX = getX();
		double origY = getY();
		double origZ = getZ();

		int tries = 100;
		for (int i = 0; i < tries; i++) {
			// we abuse LivingEntity.attemptTeleport, which does all the collision/ground checking for us, then teleport back to our original spot
			double tx = targetEntity.getX() + random.nextGaussian() * 16D;
			double ty = targetEntity.getY();
			double tz = targetEntity.getZ() + random.nextGaussian() * 16D;

			boolean destClear = randomTeleport(tx, ty, tz, true);
			boolean canSeeTargetAtDest = hasLineOfSight(targetEntity); // Don't use senses cache because we're in a temporary position
			teleportTo(origX, origY, origZ);

			if (destClear && canSeeTargetAtDest) {
				return new Vec3(tx, ty, tz);
			}
		}

		return null;
	}

	/**
	 * Does not check that the teleport destination is valid, we just go there
	 */
	private void teleportToNoChecks(double destX, double destY, double destZ) {
		// save original position
		double srcX = getX();
		double srcY = getY();
		double srcZ = getZ();

		// change position
		teleportTo(destX, destY, destZ);

		makeTeleportTrail(srcX, srcY, srcZ, destX, destY, destZ);
		this.level.playSound(null, srcX, srcY, srcZ, TFSounds.LICH_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
		this.playSound(TFSounds.LICH_TELEPORT, 1.0F, 1.0F);

		// sometimes we need to do this
		this.jumping = false;
	}

	public void makeTeleportTrail(double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
		// make particle trail
		int particles = 128;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = (random.nextFloat() - 0.5F) * 0.2F;
			float f1 = (random.nextFloat() - 0.5F) * 0.2F;
			float f2 = (random.nextFloat() - 0.5F) * 0.2F;
			double tx = srcX + (destX - srcX) * trailFactor + (random.nextDouble() - 0.5D) * getBbWidth() * 2D;
			double ty = srcY + (destY - srcY) * trailFactor + random.nextDouble() * getBbHeight();
			double tz = srcZ + (destZ - srcZ) * trailFactor + (random.nextDouble() - 0.5D) * getBbWidth() * 2D;
			level.addParticle(ParticleTypes.EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	public void makeRedMagicTrail(BlockPos source, BlockPos target) {
		int particles = 60;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = 1.0F;
			float f1 = 0.5F;
			float f2 = 0.5F;
			double tx = source.getX() + (target.getX() - source.getX()) * trailFactor + random.nextGaussian() * 0.005;
			double ty = source.getY() + (target.getY() - source.getY()) * trailFactor + random.nextGaussian() * 0.005;
			double tz = source.getZ() + (target.getZ() - source.getZ()) * trailFactor + random.nextGaussian() * 0.005;
			level.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	public void makeBlackMagicTrail(BlockPos source, BlockPos target) {
		// make particle trail
		int particles = 60;
		for (int i = 0; i < particles; i++) {
			double trailFactor = i / (particles - 1.0D);
			float f = 0.2F;
			float f1 = 0.2F;
			float f2 = 0.2F;
			double tx = source.getX() + (target.getX() - source.getX()) * trailFactor + random.nextGaussian() * 0.005;
			double ty = source.getY() + (target.getY() - source.getY()) * trailFactor + random.nextGaussian() * 0.005;
			double tz = source.getZ() + (target.getZ() - source.getZ()) * trailFactor + random.nextGaussian() * 0.005;
			level.addParticle(ParticleTypes.ENTITY_EFFECT, tx, ty, tz, f, f1, f2);
		}
	}

	public boolean isShadowClone() {
		return entityData.get(DATA_ISCLONE);
	}

	public void setShadowClone(boolean shadowClone) {
		bossInfo.setVisible(!shadowClone);
		entityData.set(DATA_ISCLONE, shadowClone);
	}

	public byte getShieldStrength() {
		return entityData.get(DATA_SHIELDSTRENGTH);
	}

	public void setShieldStrength(int shieldStrength) {
		entityData.set(DATA_SHIELDSTRENGTH, (byte) shieldStrength);
	}

	public byte getMinionsToSummon() {
		return entityData.get(DATA_MINIONSLEFT);
	}

	public void setMinionsToSummon(int minionsToSummon) {
		entityData.set(DATA_MINIONSLEFT, (byte) minionsToSummon);
	}

	public byte getNextAttackType() {
		return entityData.get(DATA_ATTACKTYPE);
	}

	public void setNextAttackType(int attackType) {
		entityData.set(DATA_ATTACKTYPE, (byte) attackType);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.LICH_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.LICH_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.LICH_DEATH;
	}

	@Override
	public ResourceLocation getDefaultLootTable() {
		return !isShadowClone() ? super.getDefaultLootTable() : null;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putBoolean("ShadowClone", isShadowClone());
		compound.putByte("ShieldStrength", getShieldStrength());
		compound.putByte("MinionsToSummon", getMinionsToSummon());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		setShadowClone(compound.getBoolean("ShadowClone"));
		setShieldStrength(compound.getByte("ShieldStrength"));
		setMinionsToSummon(compound.getByte("MinionsToSummon"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the tower as defeated
		if (!level.isClientSide && !this.isShadowClone()) {
			TFGenerationSettings.markStructureConquered(level, new BlockPos(this.blockPosition()), TFFeature.LICH_TOWER);
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
	public MobType getMobType() {
		return MobType.UNDEAD;
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
