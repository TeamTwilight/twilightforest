package twilightforest.entity.boss;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerBossEvent;
import twilightforest.advancements.TFAdvancements;
import twilightforest.entity.projectile.UrGhastFireball;
import twilightforest.world.registration.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.GhastTrapBlock;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.entity.monster.CarminiteGhastguard;
import twilightforest.entity.NoClipMoveHelper;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;
import twilightforest.util.TFDamageSources;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class UrGhast extends CarminiteGhastguard {

	private static final EntityDataAccessor<Boolean> DATA_TANTRUM = SynchedEntityData.defineId(UrGhast.class, EntityDataSerializers.BOOLEAN);

	//private static final int CRUISING_ALTITUDE = 235; // absolute cruising altitude
	private static final int HOVER_ALTITUDE = 20; // how far, relatively, do we hover over ghast traps?

	private List<BlockPos> trapLocations;
	private int nextTantrumCry;

	private float damageUntilNextPhase = 10; // how much damage can we take before we toggle tantrum mode
	private boolean noTrapMode; // are there no traps nearby?  just float around
	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public UrGhast(EntityType<? extends UrGhast> type, Level world) {
		super(type, world);
		this.wanderFactor = 32.0F;
		this.noPhysics = true;
		this.setInTantrum(false);
		this.xpReward = 317;
		this.moveControl = new NoClipMoveHelper(this);
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return CarminiteGhastguard.registerAttributes()
				.add(Attributes.MAX_HEALTH, 250)
				.add(Attributes.FOLLOW_RANGE, 128.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_TANTRUM, false);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		trapLocations = new ArrayList<BlockPos>();
		this.goalSelector.availableGoals.removeIf(e -> e.getGoal() instanceof CarminiteGhastguard.AIHomedFly);
		this.goalSelector.addGoal(5, new AIWaypointFly(this));
	}

	static class AIWaypointFly extends Goal {
		private final UrGhast taskOwner;

		private final List<BlockPos> pointsToVisit;
		private int currentPoint = 0;

		AIWaypointFly(UrGhast ghast) {
			this.taskOwner = ghast;
			pointsToVisit = createPath();
			setFlags(EnumSet.of(Flag.MOVE));
		}

		// [VanillaCopy] EntityGhast.AIRandomFly
		@Override
		public boolean canUse() {
			MoveControl entitymovehelper = this.taskOwner.getMoveControl();

			if (!entitymovehelper.hasWanted()) {
				return true;
			} else {
				double d0 = entitymovehelper.getWantedX() - this.taskOwner.getX();
				double d1 = entitymovehelper.getWantedY() - this.taskOwner.getY();
				double d2 = entitymovehelper.getWantedZ() - this.taskOwner.getZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return d3 < 1.0D || d3 > 3600.0D;
			}
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void start() {
			if (this.pointsToVisit.isEmpty()) {
				pointsToVisit.addAll(createPath());
			} else {
				if (this.currentPoint >= pointsToVisit.size()) {
					this.currentPoint = 0;

					// when we're in tantrum mode, this is a good time to check if we need to spawn more ghasts
					if (!taskOwner.checkGhastsAtTraps()) {
						taskOwner.spawnGhastsAtTraps();
					}
				}

				// TODO reintrodue wanderFactor somehow? Would need to change move helper or add extra fields here

				double x = pointsToVisit.get(currentPoint).getX();
				double y = pointsToVisit.get(currentPoint).getY() + HOVER_ALTITUDE;
				double z = pointsToVisit.get(currentPoint).getZ();
				taskOwner.getMoveControl().setWantedPosition(x, y, z, 1.0F);
				this.currentPoint++;

				// we have reached cruising altitude, time to turn noClip off
				taskOwner.noPhysics = false;
			}
		}

		private List<BlockPos> createPath() {
			List<BlockPos> potentialPoints = new ArrayList<>();
			BlockPos pos = new BlockPos(this.taskOwner.blockPosition());

			if (!this.taskOwner.noTrapMode) {
				// make a copy of the trap locations list
				potentialPoints.addAll(this.taskOwner.trapLocations);
			} else {
				potentialPoints.add(pos.offset(20, -HOVER_ALTITUDE, 0));
				potentialPoints.add(pos.offset(0, -HOVER_ALTITUDE, -20));
				potentialPoints.add(pos.offset(-20, -HOVER_ALTITUDE, 0));
				potentialPoints.add(pos.offset(0, -HOVER_ALTITUDE, 20));
			}

			Collections.shuffle(potentialPoints);

			if (this.taskOwner.noTrapMode) {
				// if in no trap mode, head back to the middle when we're done
				potentialPoints.add(pos.below(HOVER_ALTITUDE));
			}

			return potentialPoints;
		}
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			if (hasHome()) {
				level.setBlockAndUpdate(getRestrictCenter(), TFBlocks.UR_GHAST_BOSS_SPAWNER.get().defaultBlockState());
			}
			discard();
		} else {
			super.checkDespawn();
		}
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
	      return TFSounds.URGHAST_AMBIENT;
	   }

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return TFSounds.URGHAST_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.URGHAST_DEATH;
	   }

	@Override
	public SoundEvent getFireSound() {
		return TFSounds.URGHAST_SHOOT;
	}

	@Override
	public SoundEvent getWarnSound() {
		return TFSounds.URGHAST_WARN;
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!level.isClientSide) {
			bossInfo.setProgress(getHealth() / getMaxHealth());
		} else {
			if (this.isInTantrum()) {
				level.addParticle(TFParticleType.BOSS_TEAR.get(),
						this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 0.75D,
						this.getY() + this.random.nextDouble() * this.getBbHeight() * 0.5D,
						this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 0.75D,
						0, 0, 0
				);
			}

			// extra death explosions
			if (deathTime > 0) {
				for (int k = 0; k < 5; k++) {

					double d = random.nextGaussian() * 0.02D;
					double d1 = random.nextGaussian() * 0.02D;
					double d2 = random.nextGaussian() * 0.02D;

					level.addParticle(random.nextBoolean() ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION,
							(getX() + random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(),
							getY() + random.nextFloat() * getBbHeight(),
							(getZ() + random.nextFloat() * getBbWidth() * 2.0F) - getBbWidth(),
							d, d1, d2
					);
				}
			}
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource src) {
		return src == DamageSource.IN_WALL || src == DamageSource.IN_FIRE || src == DamageSource.ON_FIRE || super.isInvulnerableTo(src);
	}

	@Override
	public void knockback(double strength, double xRatio, double zRatio) {
		// Don't take knockback
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		// in tantrum mode take only 1/10 damage
		if (this.isInTantrum()) {
			damage /= 10;
		}

		float oldHealth = getHealth();
		boolean attackSuccessful;

		if ("fireball".equals(source.getMsgId()) && source.getEntity() instanceof Player) {
			// 'hide' fireball attacks so that we don't take 1000 damage.
			attackSuccessful = super.hurt(DamageSource.thrown(source.getEntity(), source.getDirectEntity()), damage);
		} else {
			attackSuccessful = super.hurt(source, damage);
		}

		float lastDamage = oldHealth - getHealth();

		if(source.getEntity() instanceof ServerPlayer player && !hurtBy.contains(player)) {
			hurtBy.add(player);
		}

		if (!level.isClientSide) {
			if (this.hurtTime == this.hurtDuration) {
				this.damageUntilNextPhase -= lastDamage;

				TwilightForestMod.LOGGER.debug("Urghast Attack successful, {} damage until phase switch.", this.damageUntilNextPhase);

				if (this.damageUntilNextPhase <= 0) {
					this.switchPhase();
				}
			} else {
				TwilightForestMod.LOGGER.debug("Urghast Attack fail with {} type attack for {} damage", source.msgId, damage);
			}
		}

		return attackSuccessful;
	}

	private void switchPhase() {
		if (this.isInTantrum()) {
			this.setInTantrum(false);
		} else {
			this.startTantrum();
		}

		resetDamageUntilNextPhase();
	}

	public void resetDamageUntilNextPhase() {
		damageUntilNextPhase = 18;
	}

	private void startTantrum() {
		this.setInTantrum(true);

		// FIXME Use custom rain rendering instead like we do for blocking-off biomes, this is ridiculous especially for multiplayer
		// start raining
		//int rainTime = 300 * 20;

		//PrimaryLevelData worldInfo = (PrimaryLevelData) level.getServer().getLevel(Level.OVERWORLD).getLevelData(); // grab the overworld to set weather properly

		//worldInfo.setClearWeatherTime(0);
		//worldInfo.setRainTime(rainTime);
		//worldInfo.setThunderTime(rainTime);
		//worldInfo.setRaining(true);
		//worldInfo.setThundering(true);

		spawnGhastsAtTraps();
	}

	/**
	 * Spawn ghasts at two of the traps
	 */
	private void spawnGhastsAtTraps() {
		// spawn ghasts around two of the traps
		List<BlockPos> ghastSpawns = new ArrayList<BlockPos>(this.trapLocations);
		Collections.shuffle(ghastSpawns);

		int numSpawns = Math.min(2, ghastSpawns.size());

		for (int i = 0; i < numSpawns; i++) {
			BlockPos spawnCoord = ghastSpawns.get(i);
			spawnMinionGhastsAt(spawnCoord.getX(), spawnCoord.getY(), spawnCoord.getZ());
		}
	}

	/**
	 * Spawn up to 6 minon ghasts around the indicated area
	 */
	private void spawnMinionGhastsAt(int x, int y, int z) {
		int tries = 24;
		int spawns = 0;
		int maxSpawns = 6;

		int rangeXZ = 4;
		int rangeY = 8;

		// lightning strike
		LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
		bolt.setPos(x, y + 4, z);
		bolt.setVisualOnly(true);
		this.level.addFreshEntity(bolt);

		for (int i = 0; i < tries; i++) {
			CarminiteGhastling minion = new CarminiteGhastling(TFEntities.CARMINITE_GHASTLING, level);

			double sx = x + ((random.nextDouble() - random.nextDouble()) * rangeXZ);
			double sy = y + (random.nextDouble() * rangeY);
			double sz = z + ((random.nextDouble() - random.nextDouble()) * rangeXZ);

			minion.moveTo(sx, sy, sz, this.level.random.nextFloat() * 360.0F, 0.0F);
			minion.makeBossMinion();

			if (minion.checkSpawnRules(level, MobSpawnType.MOB_SUMMONED)) {
				this.level.addFreshEntity(minion);
				minion.spawnAnim();
			}

			if (++spawns >= maxSpawns) {
				break;
			}
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		this.hasRestriction();

		// despawn mini ghasts that are in our AABB
		for (CarminiteGhastling ghast : level.getEntitiesOfClass(CarminiteGhastling.class, this.getBoundingBox().inflate(1, 1, 1))) {
			ghast.spawnAnim();
			ghast.discard();
			this.heal(2);
		}

		// trap locations?
		if (this.trapLocations.isEmpty() && !this.noTrapMode) {
			this.scanForTrapsTwice();

			if (this.trapLocations.isEmpty()) {
				this.noTrapMode = true;
			}
		}

		if (this.isInTantrum()) {
			setTarget(null);

			// cry?
			if (--this.nextTantrumCry <= 0) {
				this.playSound(getHurtSound(null), this.getSoundVolume(), this.getVoicePitch());
				this.nextTantrumCry = 20 + random.nextInt(30);
			}

			if (this.tickCount % 10 == 0) {
				doTantrumDamageEffects();
			}
		}
	}

	private void doTantrumDamageEffects() {
		// harm player below
		AABB below = this.getBoundingBox().move(0, -16, 0).inflate(0, 16, 0);

		for (Player player : level.getEntitiesOfClass(Player.class, below)) {
			if (level.canSeeSkyFromBelowWater(player.blockPosition())) {
				player.hurt(TFDamageSources.GHAST_TEAR, 3);
			}
		}

		// also suck up mini ghasts
		for (CarminiteGhastling ghast : level.getEntitiesOfClass(CarminiteGhastling.class, below)) {
			ghast.push(0, 1, 0);
		}
	}

	/**
	 * Check if there are at least 4 ghasts near at least 2 traps.  Return false if not.
	 */
	private boolean checkGhastsAtTraps() {
		int trapsWithEnoughGhasts = 0;

		for (BlockPos trap : this.trapLocations) {
			AABB aabb = new AABB(trap, trap.offset(1, 1, 1)).inflate(8D, 16D, 8D);

			List<CarminiteGhastling> nearbyGhasts = level.getEntitiesOfClass(CarminiteGhastling.class, aabb);

			if (nearbyGhasts.size() >= 4) {
				trapsWithEnoughGhasts++;
			}
		}

		return trapsWithEnoughGhasts >= 1;
	}

	@Override
	protected void spitFireball() {
		double offsetX = this.getTarget().getX() - this.getX();
		double offsetY = this.getTarget().getBoundingBox().minY + this.getTarget().getBbHeight() / 2.0F - (this.getY() + this.getBbHeight() / 2.0F);
		double offsetZ = this.getTarget().getZ() - this.getZ();

		UrGhastFireball entityFireball = new UrGhastFireball(this.level, this, offsetX, offsetY, offsetZ, 1);
		double shotSpawnDistance = 8.5D;
		Vec3 lookVec = this.getViewVector(1.0F);
		entityFireball.setPos(
				this.getX() + lookVec.x * shotSpawnDistance,
				this.getY() + this.getBbHeight() / 2.0F + lookVec.y * shotSpawnDistance,
				this.getZ() + lookVec.z * shotSpawnDistance
		);
		this.level.addFreshEntity(entityFireball);

		for (int i = 0; i < 2; i++) {
			entityFireball = new UrGhastFireball(this.level, this, offsetX + (random.nextFloat() - random.nextFloat()) * 8, offsetY, offsetZ + (random.nextFloat() - random.nextFloat()) * 8, 1);
			entityFireball.setPos(
					this.getX() + lookVec.x * shotSpawnDistance,
					this.getY() + this.getBbHeight() / 2.0F + lookVec.y * shotSpawnDistance,
					this.getZ() + lookVec.z * shotSpawnDistance
			);
			this.level.addFreshEntity(entityFireball);
		}
	}

	/**
	 * Scan a few chunks around us for ghast trap blocks and if we find any, add them to our list
	 */
	private void scanForTrapsTwice() {
		int scanRangeXZ = 48;
		int scanRangeY = 32;

		scanForTraps(scanRangeXZ, scanRangeY, blockPosition());

		if (trapLocations.size() > 0) {
			// average the location of the traps we've found, and scan again from there
			int ax = 0, ay = 0, az = 0;

			for (BlockPos trapCoords : trapLocations) {
				ax += trapCoords.getX();
				ay += trapCoords.getY();
				az += trapCoords.getZ();
			}

			ax /= trapLocations.size();
			ay /= trapLocations.size();
			az /= trapLocations.size();

			scanForTraps(scanRangeXZ, scanRangeY, new BlockPos(ax, ay, az));
		}
	}

	private void scanForTraps(int scanRangeXZ, int scanRangeY, BlockPos pos) {
		for (int sx = -scanRangeXZ; sx <= scanRangeXZ; sx++) {
			for (int sz = -scanRangeXZ; sz <= scanRangeXZ; sz++) {
				for (int sy = -scanRangeY; sy <= scanRangeY; sy++) {
					BlockPos trapCoords = pos.offset(sx, sy, sz);
					if (isTrapAt(trapCoords)) {
						trapLocations.add(trapCoords);
					}
				}
			}
		}
	}

	private boolean isTrapAt(BlockPos pos) {
		BlockState inactive = TFBlocks.GHAST_TRAP.get().defaultBlockState().setValue(GhastTrapBlock.ACTIVE, false);
		BlockState active = TFBlocks.GHAST_TRAP.get().defaultBlockState().setValue(GhastTrapBlock.ACTIVE, true);
		return level.hasChunkAt(pos)
				&& (level.getBlockState(pos) == inactive || level.getBlockState(pos) == active);
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
	public boolean isOnFire() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	public boolean isInTantrum() {
		return entityData.get(DATA_TANTRUM);
	}

	public void setInTantrum(boolean inTantrum) {
		entityData.set(DATA_TANTRUM, inTantrum);
		resetDamageUntilNextPhase();
	}

	@Override
	protected float getSoundVolume() {
		return 16F;
	}

	@Override
	public float getVoicePitch() {
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean("inTantrum", this.isInTantrum());
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setInTantrum(compound.getBoolean("inTantrum"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	protected void tickDeath() {
		super.tickDeath();
		if (this.deathTime == 20 && level instanceof ServerLevel serverLevel) {
			TFTreasure.DARKTOWER_BOSS.generateChest(serverLevel, findChestCoords(), Direction.NORTH, false);
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the tower as defeated
		if (!level.isClientSide) {
			TFGenerationSettings.markStructureConquered(level, findChestCoords(), TFFeature.DARK_TOWER);
			for(ServerPlayer player : hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}
		}
	}

	private BlockPos findChestCoords() {
		if (trapLocations.size() > 0) {
			// average the location of the traps we've found, and scan again from there
			int ax = 0, ay = 0, az = 0;

			for (BlockPos trapCoords : trapLocations) {
				ax += trapCoords.getX();
				ay += trapCoords.getY();
				az += trapCoords.getZ();
			}

			ax /= trapLocations.size();
			ay /= trapLocations.size();
			az /= trapLocations.size();


			return new BlockPos(ax, ay + 2, az);
		} else {
			return new BlockPos(this.blockPosition());
		}
	}

	// Don't attack (or even think about attacking) things while we're throwing a tantrum
	@Override
	protected boolean shouldAttack(LivingEntity living) {
		return !this.isInTantrum();
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
