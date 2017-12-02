package twilightforest.entity.boss;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.IBreathAttacker;
import twilightforest.entity.ai.EntityAITFHoverBeam;
import twilightforest.entity.ai.EntityAITFHoverSummon;
import twilightforest.entity.ai.EntityAITFHoverThenDrop;
import twilightforest.util.WorldUtil;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFWorld;

import java.util.List;

public class EntityTFSnowQueen extends EntityMob implements IEntityMultiPart, IBreathAttacker {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/snow_queen");
	private static final int MAX_SUMMONS = 6;
	private static final DataParameter<Boolean> BEAM_FLAG = EntityDataManager.createKey(EntityTFSnowQueen.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> PHASE_FLAG = EntityDataManager.createKey(EntityTFSnowQueen.class, DataSerializers.BYTE);
	private final BossInfoServer bossInfo = new BossInfoServer(getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS);
	private static final int MAX_DAMAGE_WHILE_BEAMING = 25;
	private static final float BREATH_DAMAGE = 4.0F;


	public enum Phase {SUMMON, DROP, BEAM}

	public final Entity[] iceArray = new Entity[7];

	private int summonsRemaining = 0;
	private int successfulDrops;
	private int maxDrops;
	private int damageWhileBeaming;

	public EntityTFSnowQueen(World par1World) {
		super(par1World);
		this.setSize(0.7F, 2.2F);

		for (int i = 0; i < this.iceArray.length; i++) {
			this.iceArray[i] = new EntityTFSnowQueenIceShield(this);
		}

		this.setCurrentPhase(Phase.SUMMON);

		this.isImmuneToFire = true;
		this.experienceValue = 317;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAITFHoverSummon(this, EntityPlayer.class, 1.0D));
		this.tasks.addTask(2, new EntityAITFHoverThenDrop(this, EntityPlayer.class, 80, 20));
		this.tasks.addTask(3, new EntityAITFHoverBeam(this, EntityPlayer.class, 80, 100));
		this.tasks.addTask(6, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(BEAM_FLAG, false);
		dataManager.register(PHASE_FLAG, (byte) 0);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_DEATH;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!world.isRemote)
			bossInfo.setPercent(getHealth() / getMaxHealth());
		// make snow particles
		for (int i = 0; i < 3; i++) {
			float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
			float py = this.getEyeHeight() + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
			float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;

			TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.SNOW_GUARDIAN, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
		}

		// during drop phase, all the ice blocks should make particles
		if (this.getCurrentPhase() == Phase.DROP) {
			for (int i = 0; i < this.iceArray.length; i++) {
				float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
				float py = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;
				float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.5F;

				TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.SNOW_WARNING, this.iceArray[i].lastTickPosX + px, this.iceArray[i].lastTickPosY + py, this.iceArray[i].lastTickPosZ + pz, 0, 0, 0);
			}
		}

		// when ice beaming, spew particles
		if (isBreathing() && this.isEntityAlive()) {
			Vec3d look = this.getLookVec();

			double dist = 0.5;
			double px = this.posX + look.x * dist;
			double py = this.posY + 1.7F + look.y * dist;
			double pz = this.posZ + look.z * dist;

			for (int i = 0; i < 10; i++) {
				double dx = look.x;
				double dy = 0;//look.y;
				double dz = look.z;

				double spread = 2 + this.getRNG().nextDouble() * 2.5;
				double velocity = 2.0 + this.getRNG().nextDouble() * 0.15;

				// beeeam
				dx += this.getRNG().nextGaussian() * 0.0075D * spread;
				dy += this.getRNG().nextGaussian() * 0.0075D * spread;
				dz += this.getRNG().nextGaussian() * 0.0075D * spread;
				dx *= velocity;
				dy *= velocity;
				dz *= velocity;

				TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.ICE_BEAM, px, py, pz, dx, dy, dz);
			}

			//playBreathSound();
		}
	}

	@Override
	public void onUpdate() {

		super.onUpdate();

		for (int i = 0; i < this.iceArray.length; i++) {

			this.iceArray[i].onUpdate();

			if (i < this.iceArray.length - 1) {
				// set block position
				Vec3d blockPos = this.getIceShieldPosition(i);

				this.iceArray[i].setPosition(blockPos.x, blockPos.y, blockPos.z);
				this.iceArray[i].rotationYaw = this.getIceShieldAngle(i);
			} else {
				// last block beneath
				this.iceArray[i].setPosition(this.posX, this.posY - 1, this.posZ);
				this.iceArray[i].rotationYaw = this.getIceShieldAngle(i);
			}

			// collide things with the block
			if (!world.isRemote) {
				this.applyShieldCollisions(this.iceArray[i]);
			}
		}

		// death animation
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
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		// mark the tower as defeated
		if (!world.isRemote) {
			int dx = MathHelper.floor(this.posX);
			int dy = MathHelper.floor(this.posY);
			int dz = MathHelper.floor(this.posZ);

			if (TFWorld.getChunkGenerator(world) instanceof ChunkGeneratorTwilightForest) {
				ChunkGeneratorTwilightForest generator = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(world);
				TFFeature nearbyFeature = TFFeature.getFeatureAt(dx, dz, world);

				if (nearbyFeature == TFFeature.lichTower) {
					generator.setStructureConquered(dx, dy, dz, true);
				}
			}
		}
	}

	private void applyShieldCollisions(Entity collider) {
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(collider, collider.getEntityBoundingBox().grow(-0.2F, -0.2F, -0.2F));

		for (Entity collided : list) {
			if (collided.canBePushed()) {
				applyShieldCollision(collider, collided);
			}
		}
	}

	/**
	 * Do the effect where the shield hits something
	 */
	private void applyShieldCollision(Entity collider, Entity collided) {
		if (collided != this) {
			collided.applyEntityCollision(collider);
			if (collided instanceof EntityLivingBase && super.attackEntityAsMob(collided)) {
				collided.motionY += 0.4;
				this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
			}
		}
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();

		// switch phases
		if (this.getCurrentPhase() == Phase.SUMMON && this.getSummonsRemaining() == 0 && this.countMyMinions() <= 0) {
			this.setCurrentPhase(Phase.DROP);
		}
		if (this.getCurrentPhase() == Phase.DROP && this.successfulDrops >= this.maxDrops) {
			this.setCurrentPhase(Phase.BEAM);
		}
		if (this.getCurrentPhase() == Phase.BEAM && this.damageWhileBeaming >= MAX_DAMAGE_WHILE_BEAMING) {
			this.setCurrentPhase(Phase.SUMMON);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float damage) {
		boolean result = super.attackEntityFrom(par1DamageSource, damage);

		if (result && this.getCurrentPhase() == Phase.BEAM) {
			this.damageWhileBeaming += damage;
		}

		return result;

	}

	private Vec3d getIceShieldPosition(int idx) {
		return this.getIceShieldPosition(getIceShieldAngle(idx), 1F);
	}


	private float getIceShieldAngle(int idx) {
		return 60F * idx + (this.ticksExisted * 5F);
	}

	private Vec3d getIceShieldPosition(float angle, float distance) {
		double var1 = Math.cos((angle) * Math.PI / 180.0D) * distance;
		double var3 = Math.sin((angle) * Math.PI / 180.0D) * distance;

		return new Vec3d(this.posX + var1, this.posY + this.getShieldYOffset(), this.posZ + var3);
	}

	private double getShieldYOffset() {
		return 0.1F;
	}

	@Override
	public void fall(float par1, float mult) {
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart MultiPartEntityPart, DamageSource damagesource, float i) {
		return false;
	}

	/**
	 * We need to do this for the bounding boxes on the parts to become active
	 */
	@Override
	public Entity[] getParts() {
		return iceArray;
	}

	public void destroyBlocksInAABB(AxisAlignedBB box) {
		if (world.getGameRules().getBoolean("mobGriefing")) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() == Blocks.ICE || state.getBlock() == Blocks.PACKED_ICE) {
					world.destroyBlock(pos, false);
				}
			}
		}
	}

	@Override
	public boolean isBreathing() {
		return dataManager.get(BEAM_FLAG);
	}

	@Override
	public void setBreathing(boolean flag) {
		dataManager.set(BEAM_FLAG, flag);
	}

	public Phase getCurrentPhase() {
		return Phase.values()[dataManager.get(PHASE_FLAG)];
	}

	public void setCurrentPhase(Phase currentPhase) {
		dataManager.set(PHASE_FLAG, (byte) currentPhase.ordinal());

		// set variables for current phase
		if (currentPhase == Phase.SUMMON) {
			this.setSummonsRemaining(MAX_SUMMONS);
		}
		if (currentPhase == Phase.DROP) {
			this.successfulDrops = 0;
			this.maxDrops = 2 + this.rand.nextInt(3);
		}
		if (currentPhase == Phase.BEAM) {
			this.damageWhileBeaming = 0;
		}
	}

	public int getSummonsRemaining() {
		return summonsRemaining;
	}

	public void setSummonsRemaining(int summonsRemaining) {
		this.summonsRemaining = summonsRemaining;
	}

	public void summonMinionAt(EntityLivingBase targetedEntity) {
		EntityTFIceCrystal minion = new EntityTFIceCrystal(world);
		minion.setPositionAndRotation(posX, posY, posZ, 0, 0);

		world.spawnEntity(minion);

		for (int i = 0; i < 100; i++) {
			double attemptX = targetedEntity.posX + rand.nextGaussian() * 16D;
			double attemptY = targetedEntity.posY + rand.nextGaussian() * 8D;
			double attemptZ = targetedEntity.posZ + rand.nextGaussian() * 16D;

			if (minion.attemptTeleport(attemptX, attemptY, attemptZ)) {
				break;
			}
		}

		minion.setAttackTarget(targetedEntity);
		minion.setToDieIn30Seconds(); // don't stick around

		this.summonsRemaining--;
	}

	public int countMyMinions() {
		return world.getEntitiesWithinAABB(EntityTFIceCrystal.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 16.0D, 32.0D)).size();
	}

	public void incrementSuccessfulDrops() {
		this.successfulDrops++;
	}

	@Override
	public void doBreathAttack(Entity target) {
		target.attackEntityFrom(DamageSource.causeMobDamage(this), BREATH_DAMAGE);
		// TODO: slow target?
	}

	@Override
	public void setCustomNameTag(String name) {
		super.setCustomNameTag(name);
		this.bossInfo.setName(this.getDisplayName());
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
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		if (this.hasCustomName())
			this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
