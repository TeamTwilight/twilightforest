package twilightforest.entity.boss;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.IHostileMount;
import twilightforest.entity.TFEntities;
import twilightforest.entity.ai.EntityAIStayNearHome;
import twilightforest.entity.ai.EntityAITFThrowRider;
import twilightforest.entity.ai.EntityAITFYetiRampage;
import twilightforest.entity.ai.EntityAITFYetiTired;
import twilightforest.enums.BossVariant;
import twilightforest.util.EntityUtil;
import twilightforest.util.WorldUtil;
import twilightforest.world.TFWorld;

import javax.annotation.Nullable;

public class EntityTFYetiAlpha extends MonsterEntity implements IRangedAttackMob, IHostileMount {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/yeti_alpha");
	private static final DataParameter<Byte> RAMPAGE_FLAG = EntityDataManager.createKey(EntityTFYetiAlpha.class, DataSerializers.BYTE);
	private static final DataParameter<Byte> TIRED_FLAG = EntityDataManager.createKey(EntityTFYetiAlpha.class, DataSerializers.BYTE);
	private final ServerBossInfo bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS);
	private int collisionCounter;
	private boolean canRampage;

	public EntityTFYetiAlpha(EntityType<? extends EntityTFYetiAlpha> type, World world) {
		super(type, world);
		this.experienceValue = 317;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new EntityAITFYetiTired(this, 100));
		this.goalSelector.addGoal(2, new EntityAIStayNearHome(this, 2.0F));
		this.goalSelector.addGoal(3, new EntityAITFYetiRampage(this, 10, 180));
		this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0D, 40, 40, 40.0F){
			@Override
			public boolean shouldExecute() {
				return getRNG().nextInt(50) > 0 && getAttackTarget() != null && getDistanceSq(getAttackTarget()) >= 16D && super.shouldExecute(); // Give us a chance to move to the next AI
			}
		});
		this.goalSelector.addGoal(4, new EntityAITFThrowRider(this, 1.0D, false) {
			@Override
			protected void checkAndPerformAttack(LivingEntity p_190102_1_, double p_190102_2_) {
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
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 2.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(RAMPAGE_FLAG, (byte) 0);
		dataManager.register(TIRED_FLAG, (byte) 0);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.38D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
	}

	@Override
	public void livingTick() {
		if (!this.getPassengers().isEmpty() && this.getPassengers().get(0).isSneaking()) {
			this.getPassengers().get(0).setSneaking(false);
		}

		super.livingTick();

		if (this.isBeingRidden()) {
			this.getLookController().setLookPositionWithEntity(getPassengers().get(0), 100F, 100F);
		}

		if (!world.isRemote) {
			bossInfo.setPercent(getHealth() / getMaxHealth());

			if (this.collided) {
				this.collisionCounter++;
			}

			if (this.collisionCounter >= 15) {
				this.destroyBlocksInAABB(this.getBoundingBox());
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
					this.world.addParticle(ParticleTypes.SPLASH, this.getX() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 0.5, this.getY() + this.getEyeHeight(), this.getZ() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 0.5, (rand.nextFloat() - 0.5F) * 0.75F, 0, (rand.nextFloat() - 0.5F) * 0.75F);
				}
			}
		}
	}

	private void addSnowEffect(float rotation, float hgt) {
		double px = 3F * Math.cos(rotation);
		double py = hgt % 5F;
		double pz = 3F * Math.sin(rotation);

		world.addParticle(TFParticleType.SNOW.get(), this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
	}

	@Override
	public void setAttackTarget(@Nullable LivingEntity entity) {
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

			return new Vec3d(this.getX() + dx, this.getY() + this.getMountedYOffset() + this.getPassengers().get(0).getYOffset(), this.getZ() + dz);
		} else {
			return new Vec3d(this.getX(), this.getY(), this.getZ());
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
		int bx = MathHelper.floor(this.getX()) + this.getRNG().nextInt(range) - this.getRNG().nextInt(range);
		int bz = MathHelper.floor(this.getZ()) + this.getRNG().nextInt(range) - this.getRNG().nextInt(range);
		int by = MathHelper.floor(this.getY() + this.getEyeHeight());

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

			int bx = MathHelper.floor(this.getAttackTarget().getX());
			int bz = MathHelper.floor(this.getAttackTarget().getZ());
			int by = MathHelper.floor(this.getAttackTarget().getY() + this.getAttackTarget().getEyeHeight());

			makeBlockFallAbove(new BlockPos(bx, bz, by));
		}

	}

	private void makeBlockFall(BlockPos pos) {
		world.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState());
		world.playEvent(2001, pos, Block.getStateId(Blocks.PACKED_ICE.getDefaultState()));

		EntityTFFallingIce ice = new EntityTFFallingIce(TFEntities.falling_ice.get(), world, pos.getX(), pos.getY() - 3, pos.getZ());
		world.addEntity(ice);
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
		if (!this.canRampage) {
			EntityTFIceBomb ice = new EntityTFIceBomb(TFEntities.thrown_ice.get(), this.world, this);

			// [VanillaCopy] Part of EntitySkeleton.attackEntityWithRangedAttack
			double d0 = target.getX() - this.getX();
			double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - ice.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
			ice.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));

			this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.world.addEntity(ice);
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {} // todo 1.12

	@Override
	public boolean canDespawn(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			if (hasHome()) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner.get().getDefaultState().with(BlockTFBossSpawner.VARIANT, BossVariant.ALPHA_YETI));
			}
			remove();
		} else {
			super.checkDespawn();
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
	public boolean handleFallDamage(float distance, float multiplier) {
		super.handleFallDamage(distance, multiplier);

		if (!this.world.isRemote && isRampaging()) {
			this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			hitNearbyEntities();
		}

		//TODO: Return value?
	}

	private void hitNearbyEntities() {
		for (LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(5, 0, 5))) {
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
	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
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
	public void writeAdditional(CompoundNBT compound) {
		BlockPos home = this.getHomePosition();
		compound.put("Home", newDoubleNBTList(home.getX(), home.getY(), home.getZ()));
		compound.putBoolean("HasHome", this.hasHome());
		super.writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains("Home", 9)) {
			ListNBT nbttaglist = compound.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
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
