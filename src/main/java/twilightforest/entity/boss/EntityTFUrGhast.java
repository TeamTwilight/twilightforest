package twilightforest.entity.boss;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import twilightforest.TFFeature;
import twilightforest.TFTreasure;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFTowerDevice;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.EntityTFMiniGhast;
import twilightforest.entity.EntityTFTowerGhast;
import twilightforest.entity.NoClipMoveHelper;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityTFUrGhast extends EntityTFTowerGhast {

	private static final DataParameter<Boolean> DATA_TANTRUM = EntityDataManager.createKey(EntityTFUrGhast.class, DataSerializers.BOOLEAN);

	//private static final int CRUISING_ALTITUDE = 235; // absolute cruising altitude
	private static final int HOVER_ALTITUDE = 20; // how far, relatively, do we hover over ghast traps?

	private List<BlockPos> trapLocations;
	private int nextTantrumCry;

	private float damageUntilNextPhase = 45; // how much damage can we take before we toggle tantrum mode
	private boolean noTrapMode; // are there no traps nearby?  just float around
	private final BossInfoServer bossInfo = new BossInfoServer(getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);

	public EntityTFUrGhast(World par1World) {
		super(par1World);
		this.setSize(14.0F, 18.0F);
		this.wanderFactor = 32.0F;
		this.noClip = true;
		this.setInTantrum(false);
		this.experienceValue = 317;
		this.moveHelper = new NoClipMoveHelper(this);
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(DATA_TANTRUM, false);
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		trapLocations = new ArrayList<BlockPos>();
		this.tasks.taskEntries.removeIf(e -> e.action instanceof EntityTFTowerGhast.AIHomedFly);
		this.tasks.addTask(5, new AIWaypointFly(this));
	}

	static class AIWaypointFly extends EntityAIBase {
		private final EntityTFUrGhast taskOwner;

		private final List<BlockPos> pointsToVisit;
		private int currentPoint = 0;

		AIWaypointFly(EntityTFUrGhast ghast) {
			this.taskOwner = ghast;
			pointsToVisit = createPath();
			setMutexBits(1);
		}

		// [VanillaCopy] EntityGhast.AIRandomFly
		@Override
		public boolean shouldExecute() {
			EntityMoveHelper entitymovehelper = this.taskOwner.getMoveHelper();

			if (!entitymovehelper.isUpdating()) {
				return true;
			} else {
				double d0 = entitymovehelper.getX() - this.taskOwner.posX;
				double d1 = entitymovehelper.getY() - this.taskOwner.posY;
				double d2 = entitymovehelper.getZ() - this.taskOwner.posZ;
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
			BlockPos pos = new BlockPos(this.taskOwner);

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
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (!world.isRemote) {
			bossInfo.setPercent(getHealth() / getMaxHealth());
		}

		// extra death explosions
		if (deathTime > 0) {
			for (int k = 0; k < 5; k++) {
				double d = rand.nextGaussian() * 0.02D;
				double d1 = rand.nextGaussian() * 0.02D;
				double d2 = rand.nextGaussian() * 0.02D;
				EnumParticleTypes explosionType = rand.nextBoolean() ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_NORMAL;
				world.spawnParticle(explosionType, (posX + rand.nextFloat() * width * 2.0F) - width, posY + rand.nextFloat() * height, (posZ + rand.nextFloat() * width * 2.0F) - width, d, d1, d2);
			}
		}
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource src) {
		return src == DamageSource.IN_WALL || super.isEntityInvulnerable(src);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		// in tantrum mode take only 1/4 damage
		if (this.isInTantrum()) {
			damage /= 4;
		}

		float oldHealth = getHealth();
		boolean attackSuccessful;

		if ("fireball".equals(source.getDamageType()) && source.getTrueSource() instanceof EntityPlayer) {
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

		this.damageUntilNextPhase = 48;
	}

	private void startTantrum() {
		this.setInTantrum(true);

		// start raining
		int rainTime = 300 * 20;

		WorldInfo worldInfo = world.getMinecraftServer().worlds[0].getWorldInfo(); // grab the overworld to set weather properly

		worldInfo.setCleanWeatherTime(0);
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
		this.world.addWeatherEffect(new EntityLightningBolt(world, x, y + 4, z, true));


		for (int i = 0; i < tries; i++) {
			EntityTFMiniGhast minion = new EntityTFMiniGhast(world);

			double sx = x + ((rand.nextDouble() - rand.nextDouble()) * rangeXZ);
			double sy = y + (rand.nextDouble() * rangeY);
			double sz = z + ((rand.nextDouble() - rand.nextDouble()) * rangeXZ);

			minion.setLocationAndAngles(sx, sy, sz, this.world.rand.nextFloat() * 360.0F, 0.0F);
			minion.makeBossMinion();

			if (minion.getCanSpawnHere()) {
				this.world.spawnEntity(minion);
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
		for (EntityTFMiniGhast ghast : world.getEntitiesWithinAABB(EntityTFMiniGhast.class, this.getEntityBoundingBox().grow(1, 1, 1))) {
			ghast.spawnExplosionParticle();
			ghast.setDead();
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
			TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.BOSS_TEAR, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0, 0, 0);
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
		AxisAlignedBB below = this.getEntityBoundingBox().offset(0, -16, 0).grow(0, 16, 0);

		for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, below)) {
			if (world.canSeeSky(new BlockPos(player))) {
				player.attackEntityFrom(DamageSource.ANVIL, 3);
			}
		}

		// also suck up mini ghasts
		for (EntityTFMiniGhast ghast : world.getEntitiesWithinAABB(EntityTFMiniGhast.class, below)) {
			ghast.motionY += 1;
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
		double offsetX = this.getAttackTarget().posX - this.posX;
		double offsetY = this.getAttackTarget().getEntityBoundingBox().minY + (double) (this.getAttackTarget().height / 2.0F) - (this.posY + (double) (this.height / 2.0F));
		double offsetZ = this.getAttackTarget().posZ - this.posZ;

		EntityTFUrGhastFireball entityFireball = new EntityTFUrGhastFireball(this.world, this, offsetX, offsetY, offsetZ);
		entityFireball.explosionPower = 1;
		double shotSpawnDistance = 8.5D;
		Vec3d lookVec = this.getLook(1.0F);
		entityFireball.posX = this.posX + lookVec.x * shotSpawnDistance;
		entityFireball.posY = this.posY + (double) (this.height / 2.0F) + lookVec.y * shotSpawnDistance;
		entityFireball.posZ = this.posZ + lookVec.z * shotSpawnDistance;
		this.world.spawnEntity(entityFireball);

		for (int i = 0; i < 2; i++) {
			entityFireball = new EntityTFUrGhastFireball(this.world, this, offsetX + (rand.nextFloat() - rand.nextFloat()) * 8, offsetY, offsetZ + (rand.nextFloat() - rand.nextFloat()) * 8);
			entityFireball.explosionPower = 1;
			entityFireball.posX = this.posX + lookVec.x * shotSpawnDistance;
			entityFireball.posY = this.posY + (double) (this.height / 2.0F) + lookVec.y * shotSpawnDistance;
			entityFireball.posZ = this.posZ + lookVec.z * shotSpawnDistance;
			this.world.spawnEntity(entityFireball);
		}

	}

	/**
	 * Scan a few chunks around us for ghast trap blocks and if we find any, add them to our list
	 */
	private void scanForTrapsTwice() {
		int scanRangeXZ = 48;
		int scanRangeY = 32;

		scanForTraps(scanRangeXZ, scanRangeY, new BlockPos(this));

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
		IBlockState inactive = TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.GHASTTRAP_INACTIVE);
		IBlockState active = TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.GHASTTRAP_ACTIVE);
		return world.isBlockLoaded(pos)
				&& (world.getBlockState(pos) == inactive || world.getBlockState(pos) == active);
	}

	@Override
	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(EntityPlayerMP player) {
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

	public void setInTantrum(boolean par1) {
		dataManager.set(DATA_TANTRUM, par1);
		this.damageUntilNextPhase = 48;
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
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("inTantrum", this.isInTantrum());
		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setInTantrum(nbttagcompound.getBoolean("inTantrum"));
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();

		if (this.deathTime == 20 && !world.isRemote) {
			BlockPos chestCoords = this.findChestCoords();
			TFTreasure.darktower_boss.generateChest(world, chestCoords, false);
		}
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);

		// mark the tower as defeated
		if (!world.isRemote && TFWorld.getChunkGenerator(world) instanceof ChunkGeneratorTwilightForest) {
			BlockPos chestCoords = this.findChestCoords();
			int dx = chestCoords.getX();
			int dy = chestCoords.getY();
			int dz = chestCoords.getZ();

			ChunkGeneratorTwilightForest generator = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(world);
			TFFeature nearbyFeature = TFFeature.getFeatureAt(dx, dz, world);

			if (nearbyFeature == TFFeature.darkTower) {
				generator.setStructureConquered(dx, dy, dz, true);
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
			return new BlockPos(this);
		}
	}

	// Don't attack (or even think about attacking) things while we're throwing a tantrum
	@Override
	protected boolean shouldAttack(EntityLivingBase living) {
		return !this.isInTantrum();
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
