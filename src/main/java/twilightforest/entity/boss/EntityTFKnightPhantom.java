package twilightforest.entity.boss;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.loot.TFTreasure;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;
import twilightforest.entity.NoClipMoveHelper;
import twilightforest.entity.ai.EntityAIPhantomAttackStart;
import twilightforest.entity.ai.EntityAIPhantomThrowWeapon;
import twilightforest.entity.ai.TFNearestPlayerGoal;
import twilightforest.entity.ai.EntityAITFPhantomUpdateFormationAndMove;
import twilightforest.entity.ai.EntityAITFPhantomWatchAndAttack;
import twilightforest.item.TFItems;
import twilightforest.world.TFWorld;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class EntityTFKnightPhantom extends FlyingEntity implements IMob {

	private static final DataParameter<Boolean> FLAG_CHARGING = EntityDataManager.createKey(EntityTFKnightPhantom.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier CHARGING_MODIFIER = new AttributeModifier("Charging attack boost", 7, AttributeModifier.Operation.ADDITION).setSaved(false);

	private int number;
	private int ticksProgress;
	private Formation currentFormation;
	private BlockPos chargePos = BlockPos.ZERO;

	public EntityTFKnightPhantom(EntityType<? extends EntityTFKnightPhantom> type, World world) {
		super(type, world);
		noClip = true;
		isImmuneToFire();
		currentFormation = Formation.HOVER;
		experienceValue = 93;
		moveController = new NoClipMoveHelper(this);
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		ILivingEntityData data = super.onInitialSpawn(worldIn, difficulty, reason, spawnDataIn, dataTag);
		setEquipmentBasedOnDifficulty(difficulty);
		setEnchantmentBasedOnDifficulty(difficulty);
		return data;
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TFItems.knightmetal_sword.get()));
		setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(TFItems.phantom_chestplate.get()));
		setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(TFItems.phantom_helmet.get()));
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(FLAG_CHARGING, false);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new EntityAITFPhantomWatchAndAttack(this));
		goalSelector.addGoal(1, new EntityAITFPhantomUpdateFormationAndMove(this));
		goalSelector.addGoal(2, new EntityAIPhantomAttackStart(this));
		goalSelector.addGoal(3, new EntityAIPhantomThrowWeapon(this));

		targetSelector.addGoal(0, new TFNearestPlayerGoal(this));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35.0D);
		getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}

	public Formation getCurrentFormation() {
		return currentFormation;
	}

	public BlockPos getChargePos() {
		return chargePos;
	}

	public void setChargePos(BlockPos pos) {
		chargePos = pos;
	}

	@Override
	public boolean canDespawn(double p_213397_1_) {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource src) {
		return src == DamageSource.IN_WALL || super.isInvulnerableTo(src);
	}

	@Override
	public void checkDespawn() {
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			if (hasHome() && getNumber() == 0) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner.get().getDefaultState().with(BlockTFBossSpawner.VARIANT, BossVariant.KNIGHT_PHANTOM));
			}
			remove();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (isChargingAtPlayer()) {
			// make particles
			for (int i = 0; i < 4; ++i) {
				Item particleID = rand.nextBoolean() ? TFItems.phantom_helmet.get() : TFItems.knightmetal_sword.get();

				world.addParticle(ParticleTypes.ITEM_CRACK, getX() + (rand.nextFloat() - 0.5D) * getWidth(), getY() + rand.nextFloat() * (getHeight() - 0.75D) + 0.5D, getZ() + (rand.nextFloat() - 0.5D) * getWidth(), 0, -0.1, 0, Item.getIdFromItem(particleID));
				world.addParticle(ParticleTypes.SMOKE, getX() + (rand.nextFloat() - 0.5D) * getWidth(), getY() + rand.nextFloat() * (getHeight() - 0.75D) + 0.5D, getZ() + (rand.nextFloat() - 0.5D) * getWidth(), 0, 0.1, 0);
			}
		}
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();

		for (int i = 0; i < 20; ++i) {
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			world.addParticle(ParticleTypes.EXPLOSION_NORMAL, getX() + (double) (rand.nextFloat() * getWidth() * 2.0F) - (double) getWidth(), getY() + (double) (rand.nextFloat() * getHeight()), getZ() + (double) (rand.nextFloat() * getWidth() * 2.0F) - (double) getWidth(), d0, d1, d2);
		}
	}

	@Override
	public void onDeath(DamageSource cause) {

		super.onDeath(cause);

		if (!world.isRemote && getNearbyKnights().isEmpty()) {

			BlockPos treasurePos = hasHome() ? getHomePosition().down() : new BlockPos(this);

			// make treasure for killing the last knight
			TFTreasure.stronghold_boss.generateChest(world, treasurePos, false);

			// mark the stronghold as defeated
			TFWorld.markStructureConquered(world, treasurePos, TFFeature.KNIGHT_STRONGHOLD);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(this.canBlockDamageSource(source)){
			playSound(SoundEvents.ITEM_SHIELD_BLOCK,1.0F,0.8F + this.world.rand.nextFloat() * 0.4F);
		}

		return super.attackEntityFrom(source, amount);
	}

	// [VanillaCopy] Exact copy of EntityMob.attackEntityAsMob
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		float f = (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
		int i = 0;

		if (entityIn instanceof LivingEntity) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof LivingEntity) {
				((LivingEntity) entityIn).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				entityIn.setFire(j * 4);
			}

			if (entityIn instanceof PlayerEntity) {
				PlayerEntity entityplayer = (PlayerEntity) entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof AxeItem && itemstack1.getItem() == Items.SHIELD) {
					float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

					if (this.rand.nextFloat() < f1) {
						entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
						this.world.setEntityState(entityplayer, (byte) 30);
					}
				}
			}

			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	public void knockBack(Entity entity, float damage, double xRatio, double zRatio) {
		isAirBorne = true;
		float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
		float distance = 0.2F;
//		motionX /= 2.0D;
//		motionY /= 2.0D;
//		motionZ /= 2.0D;
		setMotion(new Vec3d(getMotion().getX() / 2.0D, getMotion().getY() / 2.0D, getMotion().getZ() / 2.0D));
//		motionX -= xRatio / (double) f * (double) distance;
//		motionY += (double) distance;
//		motionZ -= zRatio / (double) f * (double) distance;
		setMotion(new Vec3d(
				getMotion().getX() - xRatio / (double) f * (double) distance,
				getMotion().getY() + (double) distance,
				getMotion().getZ() - zRatio / (double) f * (double) distance));

		if (motionY > 0.4000000059604645D) {
			motionY = 0.4000000059604645D;
		}
	}

	public List<EntityTFKnightPhantom> getNearbyKnights() {
		return world.getEntitiesWithinAABB(EntityTFKnightPhantom.class, new AxisAlignedBB(getX(), getY(), getZ(), getX() + 1, getY() + 1, getZ() + 1).grow(32.0D, 8.0D, 32.0D), EntitySelectors.IS_ALIVE);
	}

	private void updateMyNumber() {
		List<Integer> nums = Lists.newArrayList();
		List<EntityTFKnightPhantom> knights = getNearbyKnights();
		for (EntityTFKnightPhantom knight : knights) {
			if (knight == this)
				continue;
			nums.add(knight.getNumber());
			if (knight.getNumber() == 0)
				setHomePosAndDistance(knight.getHomePosition(), 20);
		}
		if (nums.isEmpty())
			return;
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
		if (number > smallestUnused || nums.contains(number))
			setNumber(smallestUnused);
	}

	public boolean isChargingAtPlayer() {
		return dataManager.get(FLAG_CHARGING);
	}

	private void setChargingAtPlayer(boolean flag) {
		dataManager.set(FLAG_CHARGING, flag);
		if (!world.isRemote) {
			if (flag) {
				if (!getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).hasModifier(CHARGING_MODIFIER)) {
					getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(CHARGING_MODIFIER);
				}
			} else {
				getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(CHARGING_MODIFIER);
			}
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.WRAITH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.WRAITH;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.WRAITH;
	}

	private void switchToFormationByNumber(int formationNumber) {
		currentFormation = Formation.values()[formationNumber];
		ticksProgress = 0;
	}

	public void switchToFormation(Formation formation) {
		currentFormation = formation;
		ticksProgress = 0;
		updateMyNumber();
		setChargingAtPlayer(currentFormation == Formation.ATTACK_PLAYER_START || currentFormation == Formation.ATTACK_PLAYER_ATTACK);

	}

	private int getFormationAsNumber() {
		return currentFormation.ordinal();
	}

	public int getTicksProgress() {
		return ticksProgress;
	}

	public void setTicksProgress(int ticksProgress) {
		this.ticksProgress = ticksProgress;
	}

	public int getMaxTicksForFormation() {
		return currentFormation.duration;
	}

	public boolean isSwordKnight() {
		return getHeldItemMainhand().getItem() == TFItems.knightmetal_sword.get();
	}

	public boolean isAxeKnight() {
		return getHeldItemMainhand().getItem() == TFItems.knightmetal_axe.get();
	}

	public boolean isPickKnight() {
		return getHeldItemMainhand().getItem() == TFItems.knightmetal_pickaxe.get();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;

		// set weapon per number
		switch (number % 3) {
			case 0:
				setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TFItems.knightmetal_sword.get()));
				break;
			case 1:
				setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TFItems.knightmetal_axe.get()));
				break;
			case 2:
				setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TFItems.knightmetal_pickaxe.get()));
				break;
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (hasHome()) {
			BlockPos home = getHomePosition();
			compound.put("Home", newDoubleNBTList(home.getX(), home.getY(), home.getZ()));
		}
		compound.putInt("MyNumber", getNumber());
		compound.putInt("Formation", getFormationAsNumber());
		compound.putInt("TicksProgress", getTicksProgress());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);

		if (compound.contains("Home", 9)) {
			ListNBT nbttaglist = compound.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			setHomePosAndDistance(new BlockPos(hx, hy, hz), 20);
		} else {
			detachHome();
		}
		setNumber(compound.getInt("MyNumber"));
		switchToFormationByNumber(compound.getInt("Formation"));
		setTicksProgress(compound.getInt("TicksProgress"));
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
	public boolean isNonBoss() {
		return false;
	}

	// [VanillaCopy] Home fields and methods from CreatureEntity, changes noted
	private BlockPos homePosition = BlockPos.ZERO;
	private float maximumHomeDistance = -1.0F;

	@Override
	public boolean isWithinHomeDistanceCurrentPosition() {
		return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
	}

	@Override
	public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
		return this.maximumHomeDistance == -1.0F ? true : this.homePosition.distanceSq(pos) < (double) (this.maximumHomeDistance * this.maximumHomeDistance);
	}

	@Override
	public void setHomePosAndDistance(BlockPos pos, int distance) {
		// set the chargePos here as well so we dont go off flying in some direction when we first spawn
		homePosition = chargePos = pos;
		this.maximumHomeDistance = (float) distance;
	}

	@Override
	public BlockPos getHomePosition() {
		return this.homePosition;
	}

	@Override
	public float getMaximumHomeDistance() {
		return this.maximumHomeDistance;
	}

	@Override
	public boolean detachHome() {
		this.maximumHomeDistance = -1.0F;
		return false;
	}

	public boolean hasHome() {
		return this.maximumHomeDistance != -1.0F;
	}
	// End copy
}
