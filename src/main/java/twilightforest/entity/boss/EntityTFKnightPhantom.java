package twilightforest.entity.boss;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TFTreasure;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;
import twilightforest.entity.NoClipMoveHelper;
import twilightforest.entity.ai.EntityAIPhantomAttackStart;
import twilightforest.entity.ai.EntityAIPhantomThrowWeapon;
import twilightforest.entity.ai.EntityAITFFindEntityNearestPlayer;
import twilightforest.entity.ai.EntityAITFPhantomUpdateFormationAndMove;
import twilightforest.entity.ai.EntityAITFPhantomWatchAndAttack;
import twilightforest.item.TFItems;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFWorld;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class EntityTFKnightPhantom extends EntityFlying implements IMob {
	private static final DataParameter<Boolean> FLAG_CHARGING = EntityDataManager.createKey(EntityTFKnightPhantom.class, DataSerializers.BOOLEAN);
	private static final AttributeModifier CHARGING_MODIFIER = new AttributeModifier("Charging attack boost", 7, 0).setSaved(false);
	private int number;
	private int ticksProgress;
	private Formation currentFormation;
	private BlockPos chargePos = BlockPos.ORIGIN;

	public EntityTFKnightPhantom(World par1World) {
		super(par1World);
		setSize(1.5F, 3.0F);
		noClip = true;
		isImmuneToFire = true;
		currentFormation = Formation.HOVER;
		experienceValue = 93;
		moveHelper = new NoClipMoveHelper(this);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
		setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.knightmetal_sword));
		setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(TFItems.phantom_chestplate));
		setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TFItems.phantom_helmet));
		return data;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(FLAG_CHARGING, false);
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAITFPhantomWatchAndAttack(this));
		tasks.addTask(1, new EntityAITFPhantomUpdateFormationAndMove(this));
		tasks.addTask(2, new EntityAIPhantomAttackStart(this));
		tasks.addTask(3, new EntityAIPhantomThrowWeapon(this));

		targetTasks.addTask(0, new EntityAITFFindEntityNearestPlayer(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35.0D);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
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
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource src) {
		return src == DamageSource.IN_WALL || super.isEntityInvulnerable(src);
	}

	private void despawnIfPeaceful() {
		if (!world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			if (hasHome() && getNumber() == 0) {
				BlockPos home = this.getHomePosition();
				world.setBlockState(home, TFBlocks.bossSpawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.KNIGHT_PHANTOM));
			}

			setDead();
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		despawnIfPeaceful();

		if (isChargingAtPlayer()) {
			// make particles
			for (int i = 0; i < 4; ++i) {
				Item particleID = rand.nextBoolean() ? TFItems.phantom_helmet : TFItems.knightmetal_sword;

				world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX + (rand.nextFloat() - 0.5D) * width, posY + rand.nextFloat() * (height - 0.75D) + 0.5D, posZ + (rand.nextFloat() - 0.5D) * width, 0, -0.1, 0, Item.getIdFromItem(particleID));
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + (rand.nextFloat() - 0.5D) * width, posY + rand.nextFloat() * (height - 0.75D) + 0.5D, posZ + (rand.nextFloat() - 0.5D) * width, 0, 0.1, 0);
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
			world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (double) (rand.nextFloat() * width * 2.0F) - (double) width, posY + (double) (rand.nextFloat() * height), posZ + (double) (rand.nextFloat() * width * 2.0F) - (double) width, d0, d1, d2);
		}
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);

		// mark the stronghold as defeated
		if (!world.isRemote && TFWorld.getChunkGenerator(world) instanceof ChunkGeneratorTwilightForest) {
			int dx = getHomePosition().getX();
			int dy = getHomePosition().getY();
			int dz = getHomePosition().getZ();

			ChunkGeneratorTwilightForest generator = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(world);
			TFFeature nearbyFeature = TFFeature.getFeatureAt(dx, dz, world);

			if (nearbyFeature == TFFeature.tfStronghold) {
				generator.setStructureConquered(dx, dy, dz, true);
			}
		}

		// make treasure for killing the last knight
		if (!world.isRemote) {
			// am I the last one?!?!
			List<EntityTFKnightPhantom> nearbyKnights = getNearbyKnights();
			if (nearbyKnights.size() <= 1) {
				// 	make a treasure!'
				makeATreasure();
			}
		}

	}

	private void makeATreasure() {
		if (hasHome()) {
			TFTreasure.stronghold_boss.generateChest(world, getHomePosition().down(), false);
		} else {
			TFTreasure.stronghold_boss.generateChest(world, new BlockPos(this), false);
		}
	}

	// [VanillaCopy] Exact copy of EntityMob.attackEntityAsMob
	public boolean attackEntityAsMob(Entity entityIn) {
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int i = 0;

		if (entityIn instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0 && entityIn instanceof EntityLivingBase) {
				((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				entityIn.setFire(j * 4);
			}

			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

				if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
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
	public void knockBack(Entity par1Entity, float damage, double par3, double par5) {
		isAirBorne = true;
		float f = MathHelper.sqrt(par3 * par3 + par5 * par5);
		float distance = 0.2F;
		motionX /= 2.0D;
		motionY /= 2.0D;
		motionZ /= 2.0D;
		motionX -= par3 / (double) f * (double) distance;
		motionY += (double) distance;
		motionZ -= par5 / (double) f * (double) distance;

		if (motionY > 0.4000000059604645D) {
			motionY = 0.4000000059604645D;
		}
	}

	public List<EntityTFKnightPhantom> getNearbyKnights() {
		return world.getEntitiesWithinAABB(EntityTFKnightPhantom.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(32.0D, 8.0D, 32.0D));
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
				if (!getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).hasModifier(CHARGING_MODIFIER)) {
					getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(CHARGING_MODIFIER);
				}
			} else {
				getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(CHARGING_MODIFIER);
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
		switch (currentFormation) {
			default:
			case HOVER:
				return 90;
			case LARGE_CLOCKWISE:
				return 180;
			case SMALL_CLOCKWISE:
				return 90;
			case LARGE_ANTICLOCKWISE:
				return 180;
			case SMALL_ANTICLOCKWISE:
				return 90;
			case CHARGE_PLUSX:
				return 180;
			case CHARGE_MINUSX:
				return 180;
			case CHARGE_PLUSZ:
				return 180;
			case CHARGE_MINUSZ:
				return 180;
			case ATTACK_PLAYER_START:
				return 50;
			case ATTACK_PLAYER_ATTACK:
				return 50;
			case WAITING_FOR_LEADER:
				return 10;
		}
	}

	public boolean isSwordKnight() {
		return getHeldItemMainhand().getItem() == TFItems.knightmetal_sword;
	}

	public boolean isAxeKnight() {
		return getHeldItemMainhand().getItem() == TFItems.knightmetal_axe;
	}

	public boolean isPickKnight() {
		return getHeldItemMainhand().getItem() == TFItems.knightmetal_pickaxe;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;

		// set weapon per number
		switch (number % 3) {
			case 0:
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.knightmetal_sword));
				break;
			case 1:
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.knightmetal_axe));
				break;
			case 2:
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.knightmetal_pickaxe));
				break;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		if (hasHome()) {
			BlockPos home = getHomePosition();
			nbttagcompound.setTag("Home", newDoubleNBTList(home.getX(), home.getY(), home.getZ()));
		}
		nbttagcompound.setInteger("MyNumber", getNumber());
		nbttagcompound.setInteger("Formation", getFormationAsNumber());
		nbttagcompound.setInteger("TicksProgress", getTicksProgress());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);

		if (nbttagcompound.hasKey("Home", 9)) {
			NBTTagList nbttaglist = nbttagcompound.getTagList("Home", 6);
			int hx = (int) nbttaglist.getDoubleAt(0);
			int hy = (int) nbttaglist.getDoubleAt(1);
			int hz = (int) nbttaglist.getDoubleAt(2);
			setHomePosAndDistance(new BlockPos(hx, hy, hz), 20);
		} else {
			detachHome();
		}
		setNumber(nbttagcompound.getInteger("MyNumber"));
		switchToFormationByNumber(nbttagcompound.getInteger("Formation"));
		setTicksProgress(nbttagcompound.getInteger("TicksProgress"));
	}

	public enum Formation {

		HOVER,

		LARGE_CLOCKWISE,

		SMALL_CLOCKWISE,

		LARGE_ANTICLOCKWISE,

		SMALL_ANTICLOCKWISE,

		CHARGE_PLUSX,

		CHARGE_MINUSX,

		CHARGE_PLUSZ,

		CHARGE_MINUSZ,

		WAITING_FOR_LEADER,

		ATTACK_PLAYER_START,

		ATTACK_PLAYER_ATTACK

	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	// [VanillaCopy] Home fields and methods from EntityCreature, changes noted
	private BlockPos homePosition = BlockPos.ORIGIN;
	private float maximumHomeDistance = -1.0F;

	public boolean isWithinHomeDistanceCurrentPosition() {
		return this.isWithinHomeDistanceFromPosition(new BlockPos(this));
	}

	public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
		return this.maximumHomeDistance == -1.0F ? true : this.homePosition.distanceSq(pos) < (double) (this.maximumHomeDistance * this.maximumHomeDistance);
	}

	public void setHomePosAndDistance(BlockPos pos, int distance) {
		// set the chargePos here as well so we dont go off flying in some direction when we first spawn
		homePosition = chargePos = pos;
		this.maximumHomeDistance = (float) distance;
	}

	public BlockPos getHomePosition() {
		return this.homePosition;
	}

	public float getMaximumHomeDistance() {
		return this.maximumHomeDistance;
	}

	public void detachHome() {
		this.maximumHomeDistance = -1.0F;
	}

	public boolean hasHome() {
		return this.maximumHomeDistance != -1.0F;
	}
	// End copy
}
