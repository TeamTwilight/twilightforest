package twilightforest.entity.boss;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particles.ItemParticleData;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
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
import twilightforest.world.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class EntityTFKnightPhantom extends FlyingEntity implements IMob {

	private static final DataParameter<Boolean> FLAG_CHARGING = EntityDataManager.createKey(EntityTFKnightPhantom.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier CHARGING_MODIFIER = new AttributeModifier("Charging attack boost", 7, AttributeModifier.Operation.ADDITION);

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
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
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

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 35.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0D);
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

				world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(particleID)), getPosX() + (rand.nextFloat() - 0.5D) * getWidth(), getPosY() + rand.nextFloat() * (getHeight() - 0.75D) + 0.5D, getPosZ() + (rand.nextFloat() - 0.5D) * getWidth(), 0, -0.1, 0);
				world.addParticle(ParticleTypes.SMOKE, getPosX() + (rand.nextFloat() - 0.5D) * getWidth(), getPosY() + rand.nextFloat() * (getHeight() - 0.75D) + 0.5D, getPosZ() + (rand.nextFloat() - 0.5D) * getWidth(), 0, 0.1, 0);
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
			world.addParticle(ParticleTypes.EXPLOSION, getPosX() + rand.nextFloat() * getWidth() * 2.0F - getWidth(), getPosY() + rand.nextFloat() * getHeight(), getPosZ() + rand.nextFloat() * getWidth() * 2.0F - getWidth(), d0, d1, d2);
		}
	}

	@Override
	public void onDeath(DamageSource cause) {

		super.onDeath(cause);

		if (!world.isRemote && getNearbyKnights().isEmpty()) {

			BlockPos treasurePos = hasHome() ? getHomePosition().down() : new BlockPos(this.getPosition());

			// make treasure for killing the last knight
			TFTreasure.stronghold_boss.generateChest(world, treasurePos, false);

			// mark the stronghold as defeated
			TFGenerationSettings.markStructureConquered(world, treasurePos, TFFeature.KNIGHT_STRONGHOLD);
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
		float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
		int i = 0;

		if (entityIn instanceof LivingEntity) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof LivingEntity) {
				((LivingEntity) entityIn).applyKnockback(i * 0.5F, MathHelper.sin(this.rotationYaw * 0.017453292F), (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				setMotion(new Vector3d(
						getMotion().getX() * 0.6D,
						getMotion().getY(),
						getMotion().getZ() * 0.6D));
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
					float f1 = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

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
	public void applyKnockback(float damage, double xRatio, double zRatio) {
		isAirBorne = true;
		float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
		float distance = 0.2F;
		setMotion(new Vector3d(getMotion().getX() / 2.0D, getMotion().getY() / 2.0D, getMotion().getZ() / 2.0D));
		setMotion(new Vector3d(
				getMotion().getX() - xRatio / f * distance,
				getMotion().getY() + distance,
				getMotion().getZ() - zRatio / f * distance));

		if (this.getMotion().getY() > 0.4000000059604645D) {
			setMotion(getMotion().getX(), 0.4000000059604645D, getMotion().getZ());
		}
	}

	public List<EntityTFKnightPhantom> getNearbyKnights() {
		return world.getEntitiesWithinAABB(EntityTFKnightPhantom.class, new AxisAlignedBB(getPosX(), getPosY(), getPosZ(), getPosX() + 1, getPosY() + 1, getPosZ() + 1).grow(32.0D, 8.0D, 32.0D), LivingEntity::isAlive);
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
				if (!getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(CHARGING_MODIFIER)) {
					getAttribute(Attributes.ATTACK_DAMAGE).applyNonPersistentModifier(CHARGING_MODIFIER);
				}
			} else {
				getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(CHARGING_MODIFIER);
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
		return this.isWithinHomeDistanceFromPosition(new BlockPos(this.getPosition()));
	}

	@Override
	public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
		return this.maximumHomeDistance == -1.0F ? true : this.homePosition.distanceSq(pos) < this.maximumHomeDistance * this.maximumHomeDistance;
	}

	@Override
	public void setHomePosAndDistance(BlockPos pos, int distance) {
		// set the chargePos here as well so we dont go off flying in some direction when we first spawn
		homePosition = chargePos = pos;
		this.maximumHomeDistance = distance;
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
