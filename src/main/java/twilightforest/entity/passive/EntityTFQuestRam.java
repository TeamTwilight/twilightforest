package twilightforest.entity.passive;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.ItemTags;
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
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.entity.ai.EntityAITFEatLoose;
import twilightforest.entity.ai.EntityAITFFindLoose;

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
	public AnimalEntity func_241840_a(ServerWorld world, AgeableEntity mate) {
		return null;
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 70.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23);
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
			int chunkX = MathHelper.floor(this.getPosX()) / 16;
			int chunkZ = MathHelper.floor(this.getPosZ()) / 16;

			TFFeature nearFeature = TFFeature.getNearestFeature(chunkX, chunkZ, (ServerWorld) this.world);

			if (nearFeature != TFFeature.QUEST_GROVE) {
				this.detachHome();
			} else {
				// set our home position to the center of the quest grove
				BlockPos cc = TFFeature.getNearestCenterXYZ(MathHelper.floor(this.getPosX()), MathHelper.floor(this.getPosZ()));
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
		LootContext ctx = new LootContext.Builder((ServerWorld) world).withParameter(LootParameters.THIS_ENTITY, this).build(LootParameterSets.field_237453_h_);
		world.getServer().getLootTableManager().getLootTableFromLocation(REWARD_LOOT_TABLE).generate(ctx, s -> entityDropItem(s, 1.0F));

		for (ServerPlayerEntity player : this.world.getEntitiesWithinAABB(ServerPlayerEntity.class, getBoundingBox().grow(16.0D, 16.0D, 16.0D))) {
			TFAdvancements.QUEST_RAM_COMPLETED.trigger(player);
		}
	}

	@Override
	public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
		ItemStack currentItem = player.getHeldItem(hand);

		if (tryAccept(currentItem)) {
			if (!player.abilities.isCreativeMode) {
				currentItem.shrink(1);
			}

			return ActionResultType.SUCCESS;
		} else {
			return super.func_230254_b_(player, hand);
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
		float red = colorVal[0];
		float green = colorVal[1];
		float blue = colorVal[2];

		for (int i = 0; i < iterations; i++) {
			this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosX() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 1.5, this.getPosY() + this.rand.nextDouble() * this.getHeight() * 1.5, this.getPosZ() + (this.rand.nextDouble() - 0.5D) * this.getWidth() * 1.5, red, green, blue);
		}

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
		return TFSounds.QUEST_RAM_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.QUEST_RAM_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.QUEST_RAM_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		this.playSound(TFSounds.QUEST_RAM_STEP, 0.15F, 1.0F);
	}
}
