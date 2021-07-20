package twilightforest.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.storage.ServerWorldInfo;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.BlockTFGhastTrap;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.EntityTFMiniGhast;
import twilightforest.entity.EntityTFTowerGhast;
import twilightforest.entity.NoClipMoveHelper;
import twilightforest.entity.TFEntities;
import twilightforest.enums.BossVariant;
import twilightforest.loot.TFTreasure;
import twilightforest.util.TFDamageSources;
import twilightforest.world.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class EntityTFUrGhast extends EntityTFTowerGhast {

	private static final DataParameter<Boolean> DATA_TANTRUM = EntityDataManager.createKey(EntityTFUrGhast.class, DataSerializers.BOOLEAN);

	//private static final int CRUISING_ALTITUDE = 235; // absolute cruising altitude
	private static final int HOVER_ALTITUDE = 20; // how far, relatively, do we hover over ghast traps?

	private List<BlockPos> trapLocations;
	private int nextTantrumCry;

	private float damageUntilNextPhase = 10; // how much damage can we take before we toggle tantrum mode
	private boolean noTrapMode; // are there no traps nearby?  just float around
	private final ServerBossInfo bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);

	public EntityTFUrGhast(EntityType<? extends EntityTFUrGhast> type, World world) {
		super(type, world);
		this.wanderFactor = 32.0F;
		this.noClip = true;
		this.setInTantrum(false);
		this.experienceValue = 317;
		this.moveController = new NoClipMoveHelper(this);
	}

	@Override
	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return EntityTFTowerGhast.registerAttributes()
				.createMutableAttribute(Attributes.MAX_HEALTH, 250)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(DATA_TANTRUM, false);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		trapLocations = new ArrayList<BlockPos>();
		this.goalSelector.goals.removeIf(e -> e.getGoal() instanceof EntityTFTowerGhast.AIHomedFly);
		this.goalSelector.addGoal(5, new AIWaypointFly(this));
	}

	static class AIWaypointFly extends Goal {
		private final EntityTFUrGhast taskOwner;

		private final List<BlockPos> pointsToVisit;
		private int currentPoint = 0;

		AIWaypointFly(EntityTFUrGhast ghast) {
			this.taskOwner = ghast;
			pointsToVisit = createPath();
			setMutexFlags(EnumSet.of(Flag.MOVE));
		}

		// [VanillaCopy] EntityGhast.AIRandomFly
		@Override
		public boolean shouldExecute() {
			MovementController entitymovehelper = this.taskOwner.getMoveHelper();

			if (!entitymovehelper.isUpdating()) {
				return true;
			} else {
				double d0 = entitymovehelper.getX() - this.taskOwner.getPosX();
				double d1 = entitymovehelper.getY() - this.taskOwner.getPosY();
				double d2 = entitymovehelper.getZ() - this.taskOwner.getPosZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return d3 < 1.0D || d3 > 3600.0D;
			}
		}

		@Override
		public boolean shouldContinueExecuting() {
			return false;
		}

		@Override
		public void startExecuting() {
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
				taskOwner.getMoveHelper().setMoveTo(x, y, z, 1.0F);
				this.currentPoint++;

				// we have reached cruising altitude, time to turn noClip off
				taskOwner.noClip = false;
			}
		}

		private List<BlockPos> createPath() {
			List<BlockPos> potentialPoints = new ArrayList<>();
			BlockPos pos = new BlockPos(this.taskOwner.getPosition());

			if (!this.taskOwner.noTrapMode) {
				// make a copy of the trap locations list
				potentialPoints.addAll(this.taskOwner.trapLocations);
			} else {
				potentialPoints.add(pos.add(20, -HOVER_ALTITUDE, 0));
				potentialPoints.add(pos.add(0, -HOVER_ALTITUDE, -20));
				potentialPoints.add(pos.add(-20, -HOVER_ALTITUDE, 0));
				potentialPoints.add(pos.add(0, -HOVER_ALTITUDE, 20));
			}

			Collections.shuffle(potentialPoints);

			if (this.taskOwner.noTrapMode) {
				// if in no trap mode, head back to the middle when we're done
				potentialPoints.add(pos.down(HOVER_ALTITUDE));
			}

			return potentialPoints;
		}
	}

	@Override
	public boolean canDespawn(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			if (hasHome()) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner_ur_ghast.get().getDefaultState());
			}
			remove();
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
	public void livingTick() {
		super.livingTick();

		if (!world.isRemote) {
			bossInfo.setPercent(getHealth() / getMaxHealth());
		} else {
			if (this.isInTantrum()) {
				world.addParticle(TFParticleType.BOSS_TEAR.get(),
						this.getPosX() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 0.75D,
						this.getPosY() + this.rand.nextDouble() * this.getHeight() * 0.5D,
						this.getPosZ() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 0.75D,
						0, 0, 0
				);
			}

			// extra death explosions
			if (deathTime > 0) {
				for (int k = 0; k < 5; k++) {

					double d = rand.nextGaussian() * 0.02D;
					double d1 = rand.nextGaussian() * 0.02D;
					double d2 = rand.nextGaussian() * 0.02D;

					world.addParticle(rand.nextBoolean() ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION,
							(getPosX() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(),
							getPosY() + rand.nextFloat() * getHeight(),
							(getPosZ() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(),
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
	public void applyKnockback(float strength, double xRatio, double zRatio) {
		// Don't take knockback
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		// in tantrum mode take only 1/10 damage
		if (this.isInTantrum()) {
			damage /= 10;
		}

		float oldHealth = getHealth();
		boolean attackSuccessful;

		if ("fireball".equals(source.getDamageType()) && source.getTrueSource() instanceof PlayerEntity) {
			// 'hide' fireball attacks so that we don't take 1000 damage.
			attackSuccessful = super.attackEntityFrom(DamageSource.causeThrownDamage(source.getTrueSource(), source.getImmediateSource()), damage);
		} else {
			attackSuccessful = super.attackEntityFrom(source, damage);
		}

		float lastDamage = oldHealth - getHealth();

		if (!world.isRemote) {
			if (this.hurtTime == this.maxHurtTime) {
				this.damageUntilNextPhase -= lastDamage;

				TwilightForestMod.LOGGER.debug("Urghast Attack successful, {} damage until phase switch.", this.damageUntilNextPhase);

				if (this.damageUntilNextPhase <= 0) {
					this.switchPhase();
				}
			} else {
				TwilightForestMod.LOGGER.debug("Urghast Attack fail with {} type attack for {} damage", source.damageType, damage);
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

		// start raining
		int rainTime = 300 * 20;

		ServerWorldInfo worldInfo = (ServerWorldInfo) world.getServer().getWorld(World.OVERWORLD).getWorldInfo(); // grab the overworld to set weather properly

		worldInfo.setClearWeatherTime(0);
		worldInfo.setRainTime(rainTime);
		worldInfo.setThunderTime(rainTime);
		worldInfo.setRaining(true);
		worldInfo.setThundering(true);

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
		LightningBoltEntity bolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
		bolt.setPosition(x, y + 4, z);
		bolt.setEffectOnly(true);
		this.world.addEntity(bolt);

		for (int i = 0; i < tries; i++) {
			EntityTFMiniGhast minion = new EntityTFMiniGhast(TFEntities.mini_ghast, world);

			double sx = x + ((rand.nextDouble() - rand.nextDouble()) * rangeXZ);
			double sy = y + (rand.nextDouble() * rangeY);
			double sz = z + ((rand.nextDouble() - rand.nextDouble()) * rangeXZ);

			minion.setLocationAndAngles(sx, sy, sz, this.world.rand.nextFloat() * 360.0F, 0.0F);
			minion.makeBossMinion();

			if (minion.canSpawn(world, SpawnReason.MOB_SUMMONED)) {
				this.world.addEntity(minion);
				minion.spawnExplosionParticle();
			}

			if (++spawns >= maxSpawns) {
				break;
			}
		}
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		this.detachHome();

		// despawn mini ghasts that are in our AABB
		for (EntityTFMiniGhast ghast : world.getEntitiesWithinAABB(EntityTFMiniGhast.class, this.getBoundingBox().grow(1, 1, 1))) {
			ghast.spawnExplosionParticle();
			ghast.remove();
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
			setAttackTarget(null);

			// cry?
			if (--this.nextTantrumCry <= 0) {
				this.playSound(getHurtSound(null), this.getSoundVolume(), this.getSoundPitch());
				this.nextTantrumCry = 20 + rand.nextInt(30);
			}

			if (this.ticksExisted % 10 == 0) {
				doTantrumDamageEffects();
			}
		}
	}

	private void doTantrumDamageEffects() {
		// harm player below
		AxisAlignedBB below = this.getBoundingBox().offset(0, -16, 0).grow(0, 16, 0);

		for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class, below)) {
			if (world.canBlockSeeSky(player.getPosition())) {
				player.attackEntityFrom(TFDamageSources.GHAST_TEAR, 3);
			}
		}

		// also suck up mini ghasts
		for (EntityTFMiniGhast ghast : world.getEntitiesWithinAABB(EntityTFMiniGhast.class, below)) {
			ghast.addVelocity(0, 1, 0);
		}
	}

	/**
	 * Check if there are at least 4 ghasts near at least 2 traps.  Return false if not.
	 */
	private boolean checkGhastsAtTraps() {
		int trapsWithEnoughGhasts = 0;

		for (BlockPos trap : this.trapLocations) {
			AxisAlignedBB aabb = new AxisAlignedBB(trap, trap.add(1, 1, 1)).grow(8D, 16D, 8D);

			List<EntityTFMiniGhast> nearbyGhasts = world.getEntitiesWithinAABB(EntityTFMiniGhast.class, aabb);

			if (nearbyGhasts.size() >= 4) {
				trapsWithEnoughGhasts++;
			}
		}

		return trapsWithEnoughGhasts >= 1;
	}

	@Override
	protected void spitFireball() {
		double offsetX = this.getAttackTarget().getPosX() - this.getPosX();
		double offsetY = this.getAttackTarget().getBoundingBox().minY + this.getAttackTarget().getHeight() / 2.0F - (this.getPosY() + this.getHeight() / 2.0F);
		double offsetZ = this.getAttackTarget().getPosZ() - this.getPosZ();

		EntityTFUrGhastFireball entityFireball = new EntityTFUrGhastFireball(this.world, this, offsetX, offsetY, offsetZ);
		entityFireball.explosionPower = 1;
		double shotSpawnDistance = 8.5D;
		Vector3d lookVec = this.getLook(1.0F);
		entityFireball.setPosition(
				this.getPosX() + lookVec.x * shotSpawnDistance,
				this.getPosY() + this.getHeight() / 2.0F + lookVec.y * shotSpawnDistance,
				this.getPosZ() + lookVec.z * shotSpawnDistance
		);
		this.world.addEntity(entityFireball);

		for (int i = 0; i < 2; i++) {
			entityFireball = new EntityTFUrGhastFireball(this.world, this, offsetX + (rand.nextFloat() - rand.nextFloat()) * 8, offsetY, offsetZ + (rand.nextFloat() - rand.nextFloat()) * 8);
			entityFireball.explosionPower = 1;
			entityFireball.setPosition(
					this.getPosX() + lookVec.x * shotSpawnDistance,
					this.getPosY() + this.getHeight() / 2.0F + lookVec.y * shotSpawnDistance,
					this.getPosZ() + lookVec.z * shotSpawnDistance
			);
			this.world.addEntity(entityFireball);
		}
	}

	/**
	 * Scan a few chunks around us for ghast trap blocks and if we find any, add them to our list
	 */
	private void scanForTrapsTwice() {
		int scanRangeXZ = 48;
		int scanRangeY = 32;

		scanForTraps(scanRangeXZ, scanRangeY, getPosition());

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
					BlockPos trapCoords = pos.add(sx, sy, sz);
					if (isTrapAt(trapCoords)) {
						trapLocations.add(trapCoords);
					}
				}
			}
		}
	}

	private boolean isTrapAt(BlockPos pos) {
		BlockState inactive = TFBlocks.ghast_trap.get().getDefaultState().with(BlockTFGhastTrap.ACTIVE, false);
		BlockState active = TFBlocks.ghast_trap.get().getDefaultState().with(BlockTFGhastTrap.ACTIVE, true);
		return world.isBlockLoaded(pos)
				&& (world.getBlockState(pos) == inactive || world.getBlockState(pos) == active);
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
	public boolean isBurning() {
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	public boolean isInTantrum() {
		return dataManager.get(DATA_TANTRUM);
	}

	public void setInTantrum(boolean inTantrum) {
		dataManager.set(DATA_TANTRUM, inTantrum);
		resetDamageUntilNextPhase();
	}

	@Override
	protected float getSoundVolume() {
		return 16F;
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putBoolean("inTantrum", this.isInTantrum());
		super.writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setInTantrum(compound.getBoolean("inTantrum"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();
		if (this.deathTime == 20 && !world.isRemote) {
			TFTreasure.darktower_boss.generateChest(world, findChestCoords(), Direction.NORTH, false);
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		// mark the tower as defeated
		if (!world.isRemote) {
			TFGenerationSettings.markStructureConquered(world, findChestCoords(), TFFeature.DARK_TOWER);
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
			return new BlockPos(this.getPosition());
		}
	}

	// Don't attack (or even think about attacking) things while we're throwing a tantrum
	@Override
	protected boolean shouldAttack(LivingEntity living) {
		return !this.isInTantrum();
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}

	@Override
	public boolean canChangeDimension() {
		return false;
	}
}
