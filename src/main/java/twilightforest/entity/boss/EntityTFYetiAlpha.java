package twilightforest.entity.boss;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
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
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.ai.EntityAIStayNearHome;
import twilightforest.entity.ai.EntityAITFThrowRider;
import twilightforest.entity.ai.EntityAITFYetiRampage;
import twilightforest.entity.ai.EntityAITFYetiTired;
import twilightforest.util.WorldUtil;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFWorld;

public class EntityTFYetiAlpha extends EntityMob implements IRangedAttackMob {
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation(TwilightForestMod.ID, "entities/yeti_alpha");
	private static final DataParameter<Byte> RAMPAGE_FLAG = EntityDataManager.createKey(EntityTFYetiAlpha.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> TIRED_FLAG = EntityDataManager.createKey(EntityTFYetiAlpha.class, DataSerializers.BYTE);
	private final BossInfoServer bossInfo = new BossInfoServer(getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS);
	private int collisionCounter;
	private boolean canRampage;

	public EntityTFYetiAlpha(World par1World) {
		super(par1World);
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
				return getRNG().nextInt(50) > 0 && super.shouldExecute(); // Give us a chance to move to the next AI
			}
		});
		this.tasks.addTask(4, new EntityAITFThrowRider(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWander(this, 2.0F));
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

			if (this.isCollided) {
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

		TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.SNOW, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		// no arrow damage when in ranged mode
		if (!this.canRampage && !this.isTired() && par1DamageSource.isProjectile()) {
			return false;
		}

		this.canRampage = true;
		return super.attackEntityFrom(par1DamageSource, par2);
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

			double var1 = Math.cos((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;
			double var3 = Math.sin((this.rotationYaw + 90) * Math.PI / 180.0D) * distance;

			return new Vec3d(this.posX + var1, this.posY + this.getMountedYOffset() + this.getPassengers().get(0).getYOffset(), this.posZ + var3);
		} else {
			return new Vec3d(this.posX, this.posY, this.posZ);
		}
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	public void destroyBlocksInAABB(AxisAlignedBB box) {
		if (world.getGameRules().getBoolean("mobGriefing")) {
			for (BlockPos pos : WorldUtil.getAllInBB(box)) {
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();

				if (!block.isAir(state, world, pos) && block != Blocks.OBSIDIAN && block != Blocks.END_STONE && block != Blocks.BEDROCK && state.getBlockHardness(world, pos) >= 0 && state.getMaterial() != Material.WATER && state.getMaterial() != Material.LAVA) {
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
	public void attackEntityWithRangedAttack(EntityLivingBase target, float par2) {
		if (!this.canRampage) {
			EntityTFIceBomb ice = new EntityTFIceBomb(this.world, this);

			// [VanillaCopy] Part of EntitySkeleton.attackEntityWithRangedAttack
			double d0 = target.posX - this.posX;
			double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - ice.posY;
			double d2 = target.posZ - this.posZ;
			double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
			ice.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));

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

	public boolean canRampage() {
		return this.canRampage;
	}

	public void setRampaging(boolean par1) {
		dataManager.set(RAMPAGE_FLAG, (byte) (par1 ? 1 : 0));
	}

	public boolean isRampaging() {
		return dataManager.get(RAMPAGE_FLAG) == 1;
	}

	public void setTired(boolean par1) {
		dataManager.set(TIRED_FLAG, (byte) (par1 ? 1 : 0));
		this.canRampage = false;
	}

	public boolean isTired() {
		return dataManager.get(TIRED_FLAG) == 1;
	}

	@Override
	public void fall(float par1, float mult) {
		super.fall(par1, mult);

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
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);

		// mark the lair as defeated
		if (!world.isRemote) {
			int dx = MathHelper.floor(this.posX);
			int dy = MathHelper.floor(this.posY);
			int dz = MathHelper.floor(this.posZ);

			if (TFWorld.getChunkGenerator(world) instanceof ChunkGeneratorTwilightForest) {
				ChunkGeneratorTwilightForest generator = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(world);
				TFFeature nearbyFeature = TFFeature.getFeatureAt(dx, dz, world);

				if (nearbyFeature == TFFeature.yetiCave) {
					generator.setStructureConquered(dx, dy, dz, true);
				}
			}
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
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		BlockPos home = this.getHomePosition();
		nbttagcompound.setTag("Home", newDoubleNBTList(home.getX(), home.getY(), home.getZ()));
		nbttagcompound.setBoolean("HasHome", this.hasHome());
		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("Home", 9)) {
			NBTTagList nbttaglist = nbttagcompound.getTagList("Home", 6);
			int hx = (int) nbttaglist.getDoubleAt(0);
			int hy = (int) nbttaglist.getDoubleAt(1);
			int hz = (int) nbttaglist.getDoubleAt(2);
			this.setHomePosAndDistance(new BlockPos(hx, hy, hz), 30);
		}
		if (!nbttagcompound.getBoolean("HasHome")) {
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
