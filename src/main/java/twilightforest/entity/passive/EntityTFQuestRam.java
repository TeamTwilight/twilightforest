package twilightforest.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.TFFeature;
import twilightforest.entity.ai.EntityAITFEatLoose;
import twilightforest.entity.ai.EntityAITFFindLoose;

public class EntityTFQuestRam extends EntityAnimal {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/quest_ram");
	public static final ResourceLocation REWARD_LOOT_TABLE = TwilightForestMod.prefix("entities/questing_ram_rewards");
	private static final DataParameter<Integer> DATA_COLOR = EntityDataManager.createKey(EntityTFQuestRam.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> DATA_REWARDED = EntityDataManager.createKey(EntityTFQuestRam.class, DataSerializers.BOOLEAN);

	private int randomTickDivider;

	public EntityTFQuestRam(World world) {
		super(world);
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
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	protected void updateAITasks() {
		if (--this.randomTickDivider <= 0) {
			this.randomTickDivider = 70 + this.rand.nextInt(50);

			// check if we're near a quest grove and if so, set that as home
			int chunkX = MathHelper.floor(this.posX) / 16;
			int chunkZ = MathHelper.floor(this.posZ) / 16;

			TFFeature nearFeature = TFFeature.getNearestFeature(chunkX, chunkZ, this.world);

			if (nearFeature != TFFeature.QUEST_GROVE) {
				this.detachHome();
			} else {
				// set our home position to the center of the quest grove
				BlockPos cc = TFFeature.getNearestCenterXYZ(MathHelper.floor(this.posX), MathHelper.floor(this.posZ), world);
				this.setHomePosAndDistance(cc, 13);
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
		LootContext ctx = new LootContext.Builder((WorldServer) world).withLootedEntity(this).build();
		for (ItemStack s : world.getLootTableManager().getLootTableFromLocation(REWARD_LOOT_TABLE).generateLootForPools(world.rand, ctx)) {
			entityDropItem(s, 1.0F);
		}

		for (EntityPlayerMP player : this.world.getEntitiesWithinAABB(EntityPlayerMP.class, getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D))) {
			TFAdvancements.QUEST_RAM_COMPLETED.trigger(player);
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

		if (world.isRemote && countColorsSet() > 15 && !getRewarded()) {
			animateAddColor(EnumDyeColor.byMetadata(this.rand.nextInt(16)), 5);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("ColorFlags", this.getColorFlags());
		compound.setBoolean("Rewarded", this.getRewarded());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setColorFlags(compound.getInteger("ColorFlags"));
		this.setRewarded(compound.getBoolean("Rewarded"));
	}

	public int getColorFlags() {
		return dataManager.get(DATA_COLOR);
	}

	public void setColorFlags(int flags) {
		dataManager.set(DATA_COLOR, flags);
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

	public void setRewarded(boolean rewarded) {
		dataManager.set(DATA_REWARDED, rewarded);
	}

	public void animateAddColor(EnumDyeColor color, int iterations) {
		float[] colorVal = color.getColorComponentValues();
		int red = (int) (colorVal[0] * 255F);
		int green = (int) (colorVal[1] * 255F);
		int blue = (int) (colorVal[2] * 255F);

		for (int i = 0; i < iterations; i++) {
			this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width * 1.5, this.posY + this.rand.nextDouble() * this.height * 1.5, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width * 1.5, red, green, blue);
		}

		//TODO: it would be nice to play a custom sound
		playLivingSound();
	}

	public int countColorsSet() {
		return Integer.bitCount(getColorFlags());
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
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SHEEP_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SHEEP_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block block) {
		this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
	}
}
