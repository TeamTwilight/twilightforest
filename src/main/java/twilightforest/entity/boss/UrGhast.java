package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.DustParticleOptions;
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
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.init.TFAdvancements;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.entity.ai.control.NoClipMoveControl;
import twilightforest.entity.ai.goal.GhastguardHomedFlightGoal;
import twilightforest.entity.ai.goal.UrGhastFlightGoal;
import twilightforest.entity.monster.CarminiteGhastguard;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.entity.projectile.UrGhastFireball;
import twilightforest.init.*;
import twilightforest.loot.TFLootTables;
import twilightforest.network.ParticlePacket;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//ghastguards already set home points so theres no need to here
public class UrGhast extends CarminiteGhastguard implements IBossLootBuffer {
	private static final Vec3 DYING_DECENT = new Vec3(0.0D, -0.03D, 0.0D);
	private static final EntityDataAccessor<Boolean> DATA_TANTRUM = SynchedEntityData.defineId(UrGhast.class, EntityDataSerializers.BOOLEAN);
	private final NonNullList<ItemStack> dyingInventory = NonNullList.withSize(27, ItemStack.EMPTY);

	private List<BlockPos> trapLocations;
	private int nextTantrumCry;

	private float damageUntilNextPhase = 10; // how much damage can we take before we toggle tantrum mode
	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public UrGhast(EntityType<? extends UrGhast> type, Level world) {
		super(type, world);
		this.wanderFactor = 32.0F;
		this.noPhysics = true;
		this.setInTantrum(false);
		this.xpReward = 317;
		this.moveControl = new NoClipMoveControl(this);
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return CarminiteGhastguard.registerAttributes()
				.add(Attributes.MAX_HEALTH, 250)
				.add(Attributes.FOLLOW_RANGE, 128.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(DATA_TANTRUM, false);
	}

	public List<BlockPos> getTrapLocations() {
		return this.trapLocations;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.trapLocations = new ArrayList<>();
		this.goalSelector.availableGoals.removeIf(e -> e.getGoal() instanceof GhastguardHomedFlightGoal);
		this.goalSelector.addGoal(5, new UrGhastFlightGoal(this));
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.isRestrictionPointValid(this.level().dimension()) && this.level().isLoaded(this.getRestrictionPoint().pos())) {
				this.level().setBlockAndUpdate(this.getRestrictionPoint().pos(), TFBlocks.UR_GHAST_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.UR_GHAST_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.UR_GHAST_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.UR_GHAST_DEATH.get();
	}

	@Override
	public SoundEvent getFireSound() {
		return TFSounds.UR_GHAST_SHOOT.get();
	}

	@Override
	public SoundEvent getWarnSound() {
		return TFSounds.UR_GHAST_WARN.get();
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (!this.level().isClientSide()) {
			this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
		} else {
			if (this.isInTantrum() && !this.isDeadOrDying()) {
				this.level().addParticle(TFParticleType.BOSS_TEAR.get(),
						this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 0.75D,
						this.getY() + this.getRandom().nextDouble() * this.getBbHeight() * 0.5D,
						this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 0.75D,
						0, 0, 0
				);
			}
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource src) {
		return src.is(DamageTypes.IN_WALL) || src.is(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(src);
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
			attackSuccessful = super.hurt(this.damageSources().thrown(source.getEntity(), source.getDirectEntity()), damage);
		} else {
			attackSuccessful = super.hurt(source, damage);
		}

		float lastDamage = oldHealth - this.getHealth();

		if (source.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
			this.hurtBy.add(player);
		}

		if (!this.level().isClientSide()) {
			if (this.hurtTime == this.hurtDuration && !this.isDeadOrDying()) {
				this.damageUntilNextPhase -= lastDamage;

				if (this.damageUntilNextPhase <= 0) {
					this.switchPhase();
				}
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

		this.resetDamageUntilNextPhase();
	}

	public void resetDamageUntilNextPhase() {
		damageUntilNextPhase = 18;
	}

	private void startTantrum() {
		this.setInTantrum(true);
		if (this.level() instanceof ServerLevel serverLevel) {
			LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
			if (lightningbolt != null) {
				BlockPos blockpos = serverLevel.findLightningTargetAround(BlockPos.containing(this.position().add(new Vec3(18, 0, 0).yRot((float) Math.toRadians(this.getRandom().nextInt(360))))));
				lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
				lightningbolt.setVisualOnly(true);
				serverLevel.addFreshEntity(lightningbolt);
			}
		}
		this.spawnGhastsAtTraps();
	}

	@Override
	public void tick() {
		if (this.level().isClientSide() && !this.isDeadOrDying() && this.isInTantrum()) TFWeatherRenderer.urGhastAlive = true;
		super.tick();
	}

	/**
	 * Spawn ghasts at two of the traps
	 */
	public void spawnGhastsAtTraps() {
		// spawn ghasts around two of the traps
		List<BlockPos> ghastSpawns = new ArrayList<>(this.trapLocations);
		Collections.shuffle(ghastSpawns);

		int numSpawns = Math.min(2, ghastSpawns.size());

		for (int i = 0; i < numSpawns; i++) {
			BlockPos spawnCoord = ghastSpawns.get(i);
			this.spawnMinionGhastsAt(spawnCoord.getX(), spawnCoord.getY(), spawnCoord.getZ());
		}
	}

	/**
	 * Spawn up to 6 minion ghasts around the indicated area
	 */
	private void spawnMinionGhastsAt(int x, int y, int z) {
		int tries = 24;
		int spawns = 0;
		int maxSpawns = 6;

		int rangeXZ = 4;
		int rangeY = 8;

		// lightning strike
		LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level());
		bolt.setPos(x, y + 4, z);
		bolt.setVisualOnly(true);
		this.level().addFreshEntity(bolt);

		for (int i = 0; i < tries; i++) {
			CarminiteGhastling minion = TFEntities.CARMINITE_GHASTLING.get().create(this.level());

			double sx = x + ((this.getRandom().nextDouble() - this.getRandom().nextDouble()) * rangeXZ);
			double sy = y + (this.getRandom().nextDouble() * rangeY);
			double sz = z + ((this.getRandom().nextDouble() - this.getRandom().nextDouble()) * rangeXZ);

			minion.moveTo(sx, sy, sz, this.level().getRandom().nextFloat() * 360.0F, 0.0F);
			minion.makeBossMinion();

			if (minion.checkSpawnRules(this.level(), MobSpawnType.MOB_SUMMONED)) {
				this.level().addFreshEntity(minion);
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

		// despawn mini ghasts that are in our AABB
		for (CarminiteGhastling ghast : this.level().getEntitiesOfClass(CarminiteGhastling.class, this.getBoundingBox().inflate(1.0D))) {
			ghast.spawnAnim();
			ghast.discard();
			this.heal(2);
		}

		if (this.tickCount % 60 == 0 && !this.getTrapLocations().isEmpty()) {
			//validate traps positions are still actually usable traps. If not, remove them
			this.getTrapLocations().removeIf(pos -> !this.level().getBlockState(pos).is(TFBlocks.GHAST_TRAP) || !this.level().canSeeSky(pos.above()));
		}

		if (this.firstTick || this.tickCount % 100 == 0) {
			List<BlockPos> addedPositions = this.scanForTraps((ServerLevel) this.level());
			addedPositions.removeIf(pos -> this.getTrapLocations().contains(pos));
			if (!addedPositions.isEmpty()) {
				this.getTrapLocations().addAll(addedPositions);
			}
		}

		if (this.isInTantrum()) {
			this.setTarget(null);

			// cry?
			if (--this.nextTantrumCry <= 0) {
				this.playSound(TFSounds.UR_GHAST_TANTRUM.get(), this.getSoundVolume(), this.getVoicePitch());
				this.ambientSoundTime = -this.getAmbientSoundInterval();
				this.nextTantrumCry = 20 + this.getRandom().nextInt(30);
			}

			if (this.tickCount % 10 == 0) {
				this.doTantrumDamageEffects();
			}
		}
	}

	//If we have a home position, use that for scanning, otherwise use our current position
	public BlockPos getLogicalScanPoint() {
		return !this.isRestrictionPointValid(this.level().dimension()) ? this.blockPosition() : this.getRestrictionPoint().pos();
	}

	private List<BlockPos> scanForTraps(ServerLevel level) {
		PoiManager poimanager = level.getPoiManager();
		Stream<PoiRecord> stream = poimanager.getInRange(type ->
				type.is(TFPOITypes.GHAST_TRAP.getKey()),
				this.getLogicalScanPoint(),
				this.getHomeRadius(),
				PoiManager.Occupancy.ANY);
		return stream.map(PoiRecord::getPos)
				.filter(trapPos -> level.canSeeSky(trapPos.above()))
				.sorted(Comparator.comparingDouble(trapPos -> trapPos.distSqr(this.getLogicalScanPoint())))
				.collect(Collectors.toList());
	}

	private void doTantrumDamageEffects() {
		// harm player below
		AABB below = this.getBoundingBox().move(0, -16, 0).inflate(0, 16, 0);

		for (Player player : this.level().getEntitiesOfClass(Player.class, below)) {
			if (this.level().canSeeSkyFromBelowWater(player.blockPosition())) {
				player.hurt(TFDamageTypes.getEntityDamageSource(this.level(), TFDamageTypes.GHAST_TEAR, this, TFEntities.UR_GHAST.get()), 3);
			}
		}

		// also suck up mini ghasts
		for (CarminiteGhastling ghast : this.level().getEntitiesOfClass(CarminiteGhastling.class, below)) {
			ghast.push(0, 1, 0);
		}
	}

	/**
	 * Check if there are at least 4 ghasts near at least 2 traps.  Return false if not.
	 */
	public boolean checkGhastsAtTraps() {
		int trapsWithEnoughGhasts = 0;

		for (BlockPos trap : this.getTrapLocations()) {
			AABB aabb = new AABB(trap.getCenter(), trap.offset(1, 1, 1).getCenter()).inflate(8D, 16D, 8D);

			List<CarminiteGhastling> nearbyGhasts = this.level().getEntitiesOfClass(CarminiteGhastling.class, aabb);

			if (nearbyGhasts.size() >= 4) {
				trapsWithEnoughGhasts++;
			}
		}

		return trapsWithEnoughGhasts >= 1;
	}

	@Override
	public void spitFireball() {
		double offsetX = this.getTarget().getX() - this.getX();
		double offsetY = this.getTarget().getBoundingBox().minY + this.getTarget().getBbHeight() / 2.0F - (this.getY() + this.getBbHeight() / 2.0F);
		double offsetZ = this.getTarget().getZ() - this.getZ();

		UrGhastFireball entityFireball = new UrGhastFireball(this.level(), this, offsetX, offsetY, offsetZ, 1);
		double shotSpawnDistance = 8.5D;
		Vec3 lookVec = this.getViewVector(1.0F);
		entityFireball.setPos(
				this.getX() + lookVec.x() * shotSpawnDistance,
				this.getY() + this.getBbHeight() / 2.0F + lookVec.y() * shotSpawnDistance,
				this.getZ() + lookVec.z() * shotSpawnDistance
		);
		this.level().addFreshEntity(entityFireball);

		for (int i = 0; i < 2; i++) {
			entityFireball = new UrGhastFireball(this.level(), this, offsetX + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 8, offsetY, offsetZ + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 8, 1);
			entityFireball.setPos(
					this.getX() + lookVec.x() * shotSpawnDistance,
					this.getY() + this.getBbHeight() / 2.0F + lookVec.y() * shotSpawnDistance,
					this.getZ() + lookVec.z() * shotSpawnDistance
			);
			this.level().addFreshEntity(entityFireball);
		}
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
		return this.getEntityData().get(DATA_TANTRUM);
	}

	public void setInTantrum(boolean inTantrum) {
		this.getEntityData().set(DATA_TANTRUM, inTantrum);
		this.resetDamageUntilNextPhase();
	}

	@Override
	protected float getSoundVolume() {
		return 16.0F;
	}

	@Override
	public float getVoicePitch() {
		return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.5F;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean("inTantrum", this.isInTantrum());
		this.addDeathItemsSaveData(compound);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setInTantrum(compound.getBoolean("inTantrum"));
		this.readDeathItemsSaveData(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		// mark the tower as defeated
		if (this.level() instanceof ServerLevel serverLevel) {
			this.bossInfo.setProgress(0.0F);
			IBossLootBuffer.saveDropsIntoBoss(this, TFLootTables.createLootParams(this, true, cause).create(LootContextParamSets.ENTITY), serverLevel);
			LandmarkUtil.markStructureConquered(this.level(), this, TFStructures.DARK_TOWER, true);
			for (ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.get().trigger(player, this);
			}

			LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(serverLevel);
			if (lightningbolt != null) {
				lightningbolt.moveTo(this.position().add(0.0D, this.getBbHeight() * 0.5F, 0.0D));
				lightningbolt.setVisualOnly(true);
				serverLevel.addFreshEntity(lightningbolt);
			}
		}
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		int maxDeath = 80;
		// extra death explosions
		if (this.deathTime <= maxDeath / 2) {
			float bbWidth = this.getBbWidth();
			float bbHeight = this.getBbHeight();
			for (int k = 0; k < 12; k++) {
				double d = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;

				this.level().addParticle(this.random.nextBoolean() ? (this.random.nextBoolean() ? ParticleTypes.POOF : ParticleTypes.EXPLOSION) : DustParticleOptions.REDSTONE,
						(this.getX() + this.random.nextFloat() * bbWidth * 1.8F) - bbWidth,
						this.getY() + this.random.nextFloat() * bbHeight,
						(this.getZ() + this.random.nextFloat() * bbWidth * 1.8F) - bbWidth,
						d, d1, d2
				);
			}
		} else if (this.level() instanceof ServerLevel) {
			if (this.deathTime >= maxDeath && !this.isRemoved()) {
				this.level().broadcastEntityEvent(this, (byte) 60);
				this.remove(Entity.RemovalReason.KILLED);
				return;
			}
			Vec3 start = this.position().add(0.0D, this.getBbHeight() * 0.5F, 0.0D);
			Vec3 end = Vec3.atCenterOf(EntityUtil.bossChestLocation(this));
			Vec3 diff = end.subtract(start);

			int deathTime2 = this.deathTime - (maxDeath / 2) + 1;
			double factor = (double) deathTime2 / (double) (maxDeath / 2);
			Vec3 particlePos = start.add(diff.scale(factor)).add(Math.sin(deathTime2 * Math.PI * 0.1D), Math.sin(deathTime2 * Math.PI * 0.05D), Math.cos(deathTime2 * Math.PI * 0.1125D));//Some sine waves to make it pretty

			ParticlePacket particlePacket = new ParticlePacket();
			if (this.deathTime >= maxDeath - 2) {
				for (int i = 0; i < 40; i++) {
					double x = (this.random.nextDouble() - 0.5D) * 0.075D * i;
					double y = (this.random.nextDouble() - 0.5D) * 0.075D * i;
					double z = (this.random.nextDouble() - 0.5D) * 0.075D * i;
					particlePacket.queueParticle(ParticleTypes.POOF, false, end.add(x, y, z), Vec3.ZERO);
				}
			}
			for (int i = 0; i < 40; i++) {
				double x = (this.random.nextDouble() - 0.5D) * 0.05D * i;
				double y = (this.random.nextDouble() - 0.5D) * 0.05D * i;
				double z = (this.random.nextDouble() - 0.5D) * 0.05D * i;
				particlePacket.queueParticle(DustParticleOptions.REDSTONE, false, particlePos.add(x, y, z), Vec3.ZERO);
			}
			PacketDistributor.TRACKING_ENTITY.with(this).send(particlePacket);
		}
	}

	@Override
	public Vec3 getDeltaMovement() {
		return this.isDeadOrDying() ? DYING_DECENT : super.getDeltaMovement();
	}

	@Override
	public void remove(RemovalReason reason) {
		if (reason.equals(RemovalReason.KILLED) && this.level() instanceof ServerLevel serverLevel) {
			IBossLootBuffer.depositDropsIntoChest(this, TFBlocks.DARK_CHEST.get().defaultBlockState(), EntityUtil.bossChestLocation(this), serverLevel);
		}
		super.remove(reason);
	}

	@Override
	protected boolean shouldDropLoot() {
		return !TFConfig.COMMON_CONFIG.bossDropChests.get();
	}

	// Don't attack (or even think about attacking) things while we're throwing a tantrum
	@Override
	public boolean shouldAttack(LivingEntity living) {
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

	@Override
	public NonNullList<ItemStack> getItemStacks() {
		return this.dyingInventory;
	}
}
