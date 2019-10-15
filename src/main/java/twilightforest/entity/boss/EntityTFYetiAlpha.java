package twilightforest.entity.boss;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.ai.EntityAIStayNearHome;
import twilightforest.entity.ai.EntityAITFThrowRider;
import twilightforest.entity.ai.EntityAITFYetiRampage;
import twilightforest.entity.ai.EntityAITFYetiTired;
import twilightforest.enums.BossVariant;
import twilightforest.util.EntityUtil;
import twilightforest.util.WorldUtil;
import twilightforest.world.TFWorld;

import javax.annotation.Nullable;

public class EntityTFYetiAlpha extends EntityMob implements IRangedAttackMob, IHostileMount {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/yeti_alpha");
	private static final DataParameter<Byte> RAMPAGE_FLAG = EntityDataManager.createKey(EntityTFYetiAlpha.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> TIRED_FLAG = EntityDataManager.createKey(EntityTFYetiAlpha.class, DataSerializers.BYTE);
	private final BossInfoServer bossInfo = new BossInfoServer(getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS);
	private int collisionCounter;
	private boolean canRampage;

	public EntityTFYetiAlpha(World world) {
		super(world);
		this.setSize(3.8F, 5.0F);
		this.experienceValue = 317;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAITFYetiTired(this, 100));
		this.tasks.addTask(2, new EntityAIStayNearHome(this, 2.0F));
		this.tasks.addTask(3, new EntityAITFYetiRampage(this, 10, 180));
		this.tasks.addTask(4, new EntityAIAttackRanged(this, 1.0D, 40, 40, 40.0F){
			@Override
			public boolean shouldExecute() {
				return getRNG().nextInt(50) > 0 && getAttackTarget() != null && getDistanceSq(getAttackTarget()) >= 16D && super.shouldExecute(); // Give us a chance to move to the next AI
			}
		});
		this.tasks.addTask(4, new EntityAITFThrowRider(this, 1.0D, false) {
			@Override
			protected void checkAndPerformAttack(EntityLivingBase p_190102_1_, double p_190102_2_) {
				super.checkAndPerformAttack(p_190102_1_, p_190102_2_);
				if (!getPassengers().isEmpty())
					playSound(TFSounds.ALPHAYETI_GRAB, 4F, 0.75F + getRNG().nextFloat() * 0.25F);
			}

			@Override
			public void resetTask() {
				if (!getPassengers().isEmpty())
					playSound(TFSounds.ALPHAYETI_THROW, 4F, 0.75F + getRNG().nextFloat() * 0.25F);
				super.resetTask();
			}
		});
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 2.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(RAMPAGE_FLAG, (byte) 0);
		dataManager.register(TIRED_FLAG, (byte) 0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.38D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
	}

	@Override
	public void onLivingUpdate() {
		if (!this.getPassengers().isEmpty() && this.getPassengers().get(0).isSneaking()) {
			this.getPassengers().get(0).setSneaking(false);
		}

		super.onLivingUpdate();

		if (this.isBeingRidden()) {
			this.getLookHelper().setLookPositionWithEntity(getPassengers().get(0), 100F, 100F);
		}

		if (!world.isRemote) {
			bossInfo.setPercent(getHealth() / getMaxHealth());

			if (this.collided) {
				this.collisionCounter++;
			}

			if (this.collisionCounter >= 15) {
				this.destroyBlocksInAABB(this.getEntityBoundingBox());
				this.collisionCounter = 0;
			}
		} else {
			if (this.isRampaging()) {
				float rotation = this.ticksExisted / 10F;

				for (int i = 0; i < 20; i++) {
					addSnowEffect(rotation + (i * 50), i + rotation);
				}

				// also swing limbs
				this.limbSwingAmount += 0.6;
			}

			if (this.isTired()) {
				for (int i = 0; i < 20; i++) {
					this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (this.rand.nextDouble() - 0.5D) * this.width * 0.5, this.posY + this.getEyeHeight(), this.posZ + (this.rand.nextDouble() - 0.5D) * this.width * 0.5, (rand.nextFloat() - 0.5F) * 0.75F, 0, (rand.nextFloat() - 0.5F) * 0.75F);
				}
			}
		}
	}

	private void addSnowEffect(float rotation, float hgt) {
		double px = 3F * Math.cos(rotation);
		double py = hgt % 5F;
		double pz = 3F * Math.sin(rotation);

		TwilightForestMod.proxy.spawnParticle(TFParticleType.SNOW, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
	}

	@Override
	public void setAttackTarget(@Nullable EntityLivingBase entity) {
		if (entity != null && entity != getAttackTarget())
			playSound(TFSounds.ALPHAYETI_ALERT, 4F, 0.5F + getRNG().nextFloat() * 0.5F);
		super.setAttackTarget(entity);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		// no arrow damage when in ranged mode
		if (!this.canRampage && !this.isTired() && source.isProjectile()) {
			return false;
		}

		this.canRampage = true;
		return super.attackEntityFrom(source, amount);
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
		return TFSounds.ALPHAYETI_DIE;
	}

	@Override
	protected float getSoundPitch() {
		return 0.5F + getRNG().nextFloat() * 0.5F;
	}

	@Override
	protected float getSoundVolume() {
		return 4F;
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public void updatePassenger(Entity passenger) {
		Vec3d riderPos = this.getRiderPosition();
		passenger.setPosition(riderPos.x, riderPos.y, riderPos.z);
	}

	@Override
	public double getMountedYOffset() {
		return 5.75D;
	}

	/**
	 * Used to both get a rider position and to push out of blocks
	 */
	private Vec3d getRiderPosition() {
		if (isBeingRidden()) {
			float distance = 0.4F;

			double dx = Math.cos((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;
			double dz = Math.sin((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;

			return new Vec3d(this.posX + dx, this.posY + this.getMountedYOffset() + this.getPassengers().get(0).getYOffset(), this.posZ + dz);
		} else {
			return new Vec3d(this.posX, this.posY, this.posZ);
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	public void destroyBlocksInAABB(AxisAlignedBB box) {
		if (ForgeEventFactory.getMobGriefingEvent(world, this)) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				if (EntityUtil.canDestroyBlock(world, pos, this)) {
					world.destroyBlock(pos, false);
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
		int bx = MathHelper.floor(this.posX) + this.getRNG().nextInt(range) - this.getRNG().nextInt(range);
		int bz = MathHelper.floor(this.posZ) + this.getRNG().nextInt(range) - this.getRNG().nextInt(range);
		int by = MathHelper.floor(this.posY + this.getEyeHeight());

		makeBlockFallAbove(new BlockPos(bx, bz, by));
	}

	private void makeBlockFallAbove(BlockPos pos) {
		if (world.isAirBlock(pos)) {
			for (int i = 1; i < 30; i++) {
				BlockPos up = pos.up(i);
				if (!world.isAirBlock(up)) {
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
		if (this.getAttackTarget() != null) {

			int bx = MathHelper.floor(this.getAttackTarget().posX);
			int bz = MathHelper.floor(this.getAttackTarget().posZ);
			int by = MathHelper.floor(this.getAttackTarget().posY + this.getAttackTarget().getEyeHeight());

			makeBlockFallAbove(new BlockPos(bx, bz, by));
		}

	}

	private void makeBlockFall(BlockPos pos) {
		world.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState());
		world.playEvent(2001, pos, Block.getStateId(Blocks.PACKED_ICE.getDefaultState()));

		EntityTFFallingIce ice = new EntityTFFallingIce(world, pos.getX(), pos.getY() - 3, pos.getZ());
		world.spawnEntity(ice);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		if (!this.canRampage) {
			EntityTFIceBomb ice = new EntityTFIceBomb(this.world, this);

			// [VanillaCopy] Part of EntitySkeleton.attackEntityWithRangedAttack
			double d0 = target.posX - this.posX;
			double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - ice.posY;
			double d2 = target.posZ - this.posZ;
			double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
			ice.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));

			this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.world.spawnEntity(ice);
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {} // todo 1.12

	@Override
	public boolean canDespawn() {
		return false;
	}

	@Override
	protected void despawnEntity() {
		if (world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			if (hasHome()) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.ALPHA_YETI));
			}
			setDead();
		} else {
			super.despawnEntity();
		}
	}

	public boolean canRampage() {
		return this.canRampage;
	}

	public void setRampaging(boolean rampaging) {
		dataManager.set(RAMPAGE_FLAG, (byte) (rampaging ? 1 : 0));
	}

	public boolean isRampaging() {
		return dataManager.get(RAMPAGE_FLAG) == 1;
	}

	public void setTired(boolean tired) {
		dataManager.set(TIRED_FLAG, (byte) (tired ? 1 : 0));
		this.canRampage = false;
	}

	public boolean isTired() {
		return dataManager.get(TIRED_FLAG) == 1;
	}

	@Override
	public void fall(float distance, float multiplier) {
		super.fall(distance, multiplier);

		if (!this.world.isRemote && isRampaging()) {
			this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			hitNearbyEntities();
		}
	}

	private void hitNearbyEntities() {
		for (EntityLivingBase entity : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(5, 0, 5))) {
			if (entity != this && entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5F)) {
				entity.motionY += 0.4;
			}
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		// mark the lair as defeated
		if (!world.isRemote) {
			TFWorld.markStructureConquered(world, new BlockPos(this), TFFeature.YETI_CAVE);
		}
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
	public void writeEntityToNBT(NBTTagCompound compound) {
		BlockPos home = this.getHomePosition();
		compound.setTag("Home", newDoubleNBTList(home.getX(), home.getY(), home.getZ()));
		compound.setBoolean("HasHome", this.hasHome());
		super.writeEntityToNBT(compound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasKey("Home", 9)) {
			NBTTagList nbttaglist = compound.getTagList("Home", 6);
			int hx = (int) nbttaglist.getDoubleAt(0);
			int hy = (int) nbttaglist.getDoubleAt(1);
			int hz = (int) nbttaglist.getDoubleAt(2);
			this.setHomePosAndDistance(new BlockPos(hx, hy, hz), 30);
		}
		if (!compound.getBoolean("HasHome")) {
			this.detachHome();
		}
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
