package twilightforest.entity.passive;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.TFFeature;
import twilightforest.entity.ai.EntityAITFEatLoose;
import twilightforest.entity.ai.EntityAITFFindLoose;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EntityTFQuestRam extends AnimalEntity {

	public static final ResourceLocation REWARD_LOOT_TABLE = TwilightForestMod.prefix("entities/questing_ram_rewards");
	private static final DataParameter<Integer> DATA_COLOR = EntityDataManager.createKey(EntityTFQuestRam.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> DATA_REWARDED = EntityDataManager.createKey(EntityTFQuestRam.class, DataSerializers.BOOLEAN);

	private int randomTickDivider;

	public EntityTFQuestRam(EntityType<? extends EntityTFQuestRam> type, World world) {
		super(type, world);
		this.randomTickDivider = 0;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.38F));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.0F, Ingredient.fromTag(ItemTags.WOOL), false));
		this.goalSelector.addGoal(3, new EntityAITFEatLoose(this, Ingredient.fromTag(ItemTags.WOOL)));
		this.goalSelector.addGoal(4, new EntityAITFFindLoose(this, 1.0F, Ingredient.fromTag(ItemTags.WOOL)));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
	}

	@Nullable
	@Override
	public AnimalEntity createChild(@Nonnull AgeableEntity mate) {
		return null;
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(70.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(DATA_COLOR, 0);
		dataManager.register(DATA_REWARDED, false);
	}

	@Override
	protected void updateAITasks() {
		if (--this.randomTickDivider <= 0) {
			this.randomTickDivider = 70 + this.rand.nextInt(50);

			// check if we're near a quest grove and if so, set that as home
			int chunkX = MathHelper.floor(this.getX()) / 16;
			int chunkZ = MathHelper.floor(this.getZ()) / 16;

			TFFeature nearFeature = TFFeature.getNearestFeature(chunkX, chunkZ, this.world);

			if (nearFeature != TFFeature.QUEST_GROVE) {
				this.detachHome();
			} else {
				// set our home position to the center of the quest grove
				BlockPos cc = TFFeature.getNearestCenterXYZ(MathHelper.floor(this.getX()), MathHelper.floor(this.getZ()));
				this.setHomePosAndDistance(cc, 13);
			}

			if (countColorsSet() > 15 && !getRewarded()) {
				rewardQuest();
				setRewarded(true);
			}

		}

		super.updateAITasks();
	}

	private void rewardQuest() {
		// todo flesh the context out more
		LootContext ctx = new LootContext.Builder((ServerWorld) world).withParameter(LootParameters.THIS_ENTITY, this).build(LootParameterSets.EMPTY);
		world.getServer().getLootTableManager().getLootTableFromLocation(REWARD_LOOT_TABLE).generate(ctx, s -> entityDropItem(s, 1.0F));

		for (ServerPlayerEntity player : this.world.getEntitiesWithinAABB(ServerPlayerEntity.class, getBoundingBox().grow(16.0D, 16.0D, 16.0D))) {
			TFAdvancements.QUEST_RAM_COMPLETED.trigger(player);
		}
	}

	@Override
	public boolean processInteract(PlayerEntity player, @Nonnull Hand hand) {
		ItemStack currentItem = player.getHeldItem(hand);

		if (tryAccept(currentItem)) {
			if (!player.abilities.isCreativeMode) {
				currentItem.shrink(1);
			}

			return true;
		} else {
			return super.processInteract(player, hand);
		}
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (world.isRemote && countColorsSet() > 15 && !getRewarded()) {
			animateAddColor(DyeColor.byId(this.rand.nextInt(16)), 5);
		}
	}

	public boolean tryAccept(ItemStack stack) {
		if (stack.getItem().isIn(ItemTags.WOOL)) {
			DyeColor color = guessColor(stack);
			if (color != null && !isColorPresent(color)) {
				setColorPresent(color);
				animateAddColor(color, 50);
				return true;
			}
		}
		return false;
	}

	@Nullable
	public static DyeColor guessColor(ItemStack stack) {
		List<Item> wools = ImmutableList.of(
						Blocks.WHITE_WOOL.asItem(), Blocks.ORANGE_WOOL.asItem(), Blocks.MAGENTA_WOOL.asItem(), Blocks.LIGHT_BLUE_WOOL.asItem(),
						Blocks.YELLOW_WOOL.asItem(), Blocks.LIME_WOOL.asItem(), Blocks.PINK_WOOL.asItem(), Blocks.GRAY_WOOL.asItem(),
						Blocks.LIGHT_GRAY_WOOL.asItem(), Blocks.CYAN_WOOL.asItem(), Blocks.PURPLE_WOOL.asItem(), Blocks.BLUE_WOOL.asItem(),
						Blocks.BROWN_WOOL.asItem(), Blocks.GREEN_WOOL.asItem(), Blocks.RED_WOOL.asItem(), Blocks.BLACK_WOOL.asItem()
		);
		int i = wools.indexOf(stack.getItem());
		if (i < 0) {
			// todo 1.15 potentially do some guessing based on registry name for modded wools
			return null;
		} else {
			return DyeColor.byId(i);
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("ColorFlags", this.getColorFlags());
		compound.putBoolean("Rewarded", this.getRewarded());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setColorFlags(compound.getInt("ColorFlags"));
		this.setRewarded(compound.getBoolean("Rewarded"));
	}

	private int getColorFlags() {
		return dataManager.get(DATA_COLOR);
	}

	private void setColorFlags(int flags) {
		dataManager.set(DATA_COLOR, flags);
	}

	public boolean isColorPresent(DyeColor color) {
		return (getColorFlags() & (1 << color.getId())) > 0;
	}

	public void setColorPresent(DyeColor color) {
		setColorFlags(getColorFlags() | (1 << color.getId()));
	}

	public boolean getRewarded() {
		return dataManager.get(DATA_REWARDED);
	}

	public void setRewarded(boolean rewarded) {
		dataManager.set(DATA_REWARDED, rewarded);
	}

	private void animateAddColor(DyeColor color, int iterations) {
		float[] colorVal = color.getColorComponentValues();
		int red = (int) (colorVal[0] * 255F);
		int green = (int) (colorVal[1] * 255F);
		int blue = (int) (colorVal[2] * 255F);

		for (int i = 0; i < iterations; i++) {
			this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 1.5, this.getY() + this.rand.nextDouble() * this.getHeight() * 1.5, this.getZ() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 1.5, red, green, blue);
		}

		//TODO: it would be nice to play a custom sound
		playAmbientSound();
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
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
	}
}
