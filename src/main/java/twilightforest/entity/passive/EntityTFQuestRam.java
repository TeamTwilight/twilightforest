package twilightforest.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;
import twilightforest.TFFeature;
import twilightforest.entity.ai.EntityAITFEatLoose;
import twilightforest.entity.ai.EntityAITFFindLoose;
import twilightforest.item.TFItems;

import java.util.List;


public class EntityTFQuestRam extends EntityAnimal {

	private static final DataParameter<Integer> DATA_COLOR = EntityDataManager.createKey(EntityTFQuestRam.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> DATA_REWARDED = EntityDataManager.createKey(EntityTFQuestRam.class, DataSerializers.BOOLEAN);

	private int randomTickDivider;

	public EntityTFQuestRam(World par1World) {
		super(par1World);
		this.setSize(1.25F, 2.9F);
		this.randomTickDivider = 0;
	}

	@Override
	protected void initEntityAI() {
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.38F));
		this.tasks.addTask(2, new EntityAITempt(this, 1.0F, Item.getItemFromBlock(Blocks.WOOL), false));
		this.tasks.addTask(3, new EntityAITFEatLoose(this, Item.getItemFromBlock(Blocks.WOOL)));
		this.tasks.addTask(4, new EntityAITFFindLoose(this, 1.0F, Item.getItemFromBlock(Blocks.WOOL)));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
	}

	@Override
	public EntityAnimal createChild(EntityAgeable entityanimal) {
		return null;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(70.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(DATA_COLOR, 0);
		dataManager.register(DATA_REWARDED, false);
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected void updateAITasks() {
		if (--this.randomTickDivider <= 0) {
			this.randomTickDivider = 70 + this.rand.nextInt(50);

			// check if we're near a quest grove and if so, set that as home
			int chunkX = MathHelper.floor(this.posX) / 16;
			int chunkZ = MathHelper.floor(this.posZ) / 16;

			TFFeature nearFeature = TFFeature.getNearestFeature(chunkX, chunkZ, this.world);

			if (nearFeature != TFFeature.questGrove) {
				this.detachHome();
			} else {
				// set our home position to the center of the quest grove
				BlockPos cc = TFFeature.getNearestCenterXYZ(MathHelper.floor(this.posX), MathHelper.floor(this.posZ), world);
				this.setHomePosAndDistance(cc, 13);

				//System.out.println("Set home area to " + cc.posX + ", " + cc.posY + ", " + cc.posZ);
			}

			if (countColorsSet() > 15 && !getRewarded()) {
				rewardQuest();
				setRewarded(true);
			}

		}

		super.updateAITasks();
	}

	/**
	 * Pay out!
	 */
	private void rewardQuest() {
		dropItemWithOffset(Item.getItemFromBlock(Blocks.DIAMOND_BLOCK), 1, 1.0F);
		dropItemWithOffset(Item.getItemFromBlock(Blocks.IRON_BLOCK), 1, 1.0F);
		dropItemWithOffset(Item.getItemFromBlock(Blocks.EMERALD_BLOCK), 1, 1.0F);
		dropItemWithOffset(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 1, 1.0F);
		dropItemWithOffset(Item.getItemFromBlock(Blocks.LAPIS_BLOCK), 1, 1.0F);
		dropItemWithOffset(TFItems.crumbleHorn, 1, 1.0F);

		rewardNearbyPlayers(this.world, this.posX, this.posY, this.posZ);
	}

	private void rewardNearbyPlayers(World world, double posX, double posY, double posZ) {
		// scan for players nearby to give the achievement
		List<EntityPlayer> nearbyPlayers = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).expand(16.0D, 16.0D, 16.0D));

		for (EntityPlayer player : nearbyPlayers) {
			player.addStat(TFAchievementPage.twilightQuestRam);
		}
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack currentItem = player.getHeldItem(hand);

		if (!currentItem.isEmpty() && currentItem.getItem() == Item.getItemFromBlock(Blocks.WOOL) && !isColorPresent(EnumDyeColor.byMetadata(currentItem.getItemDamage()))) {
			this.setColorPresent(EnumDyeColor.byMetadata(currentItem.getItemDamage()));
			this.animateAddColor(EnumDyeColor.byMetadata(currentItem.getItemDamage()), 50);

			if (!player.capabilities.isCreativeMode) {
				currentItem.shrink(1);
			}

			return true;
		} else {
			return super.processInteract(player, hand);
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		checkAndAnimateColors();
	}

	private void checkAndAnimateColors() {
		if (countColorsSet() > 15 && !getRewarded()) {
			animateAddColor(EnumDyeColor.byMetadata(this.rand.nextInt(16)), 5);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("ColorFlags", this.getColorFlags());
		par1NBTTagCompound.setBoolean("Rewarded", this.getRewarded());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setColorFlags(par1NBTTagCompound.getInteger("ColorFlags"));
		this.setRewarded(par1NBTTagCompound.getBoolean("Rewarded"));
	}

	public int getColorFlags() {
		return dataManager.get(DATA_COLOR);
	}

	public void setColorFlags(int par1) {
		dataManager.set(DATA_COLOR, par1);
	}

	public boolean isColorPresent(EnumDyeColor color) {
		return (getColorFlags() & (1 << color.getMetadata())) > 0;
	}

	public void setColorPresent(EnumDyeColor color) {
		setColorFlags(getColorFlags() | (1 << color.getMetadata()));
	}

	public boolean getRewarded() {
		return dataManager.get(DATA_REWARDED);
	}

	public void setRewarded(boolean par1) {
		dataManager.set(DATA_REWARDED, par1);
	}

	public void animateAddColor(EnumDyeColor color, int iterations) {
		int colorVal = color.getMapColor().colorValue;
		int red = colorVal >>> 16 & 0xFF;
		int green = colorVal >>> 8 & 0xFF;
		int blue = colorVal & 0xFF;

		for (int i = 0; i < iterations; i++) {
			this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width * 1.5, this.posY + this.rand.nextDouble() * this.height * 1.5, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width * 1.5, red, green, blue);
		}

		//TODO: it would be nice to play a custom sound
		playLivingSound();
	}

	public int countColorsSet() {
		int count = 0;

		for (EnumDyeColor color : EnumDyeColor.values()) {
			if (isColorPresent(color)) {
				count++;
			}
		}

		return count;
	}

	@Override
	protected float getSoundVolume() {
		return 5.0F;
	}

	@Override
	protected float getSoundPitch() {
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SHEEP_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return SoundEvents.ENTITY_SHEEP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SHEEP_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block p_145780_4_) {
		this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
	}
}
