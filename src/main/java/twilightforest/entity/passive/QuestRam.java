package twilightforest.entity.passive;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import twilightforest.world.registration.TFFeature;
import twilightforest.TFSounds;
import twilightforest.advancements.TFAdvancements;
import twilightforest.entity.ai.EatLooseGoal;
import twilightforest.entity.ai.FindLooseGoal;
import twilightforest.loot.TFTreasure;

import javax.annotation.Nullable;
import java.util.List;

public class QuestRam extends Animal {

	private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(QuestRam.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_REWARDED = SynchedEntityData.defineId(QuestRam.class, EntityDataSerializers.BOOLEAN);

	private int randomTickDivider;

	public QuestRam(EntityType<? extends QuestRam> type, Level world) {
		super(type, world);
		this.randomTickDivider = 0;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.38F));
		this.goalSelector.addGoal(2, new TemptGoal(this, 1.0F, Ingredient.of(ItemTags.WOOL), false));
		this.goalSelector.addGoal(3, new EatLooseGoal(this));
		this.goalSelector.addGoal(4, new FindLooseGoal(this, 1.0F, Ingredient.of(ItemTags.WOOL)));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	@Nullable
	@Override
	public Animal getBreedOffspring(ServerLevel world, AgeableMob mate) {
		return null;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 70.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_COLOR, 0);
		entityData.define(DATA_REWARDED, false);
	}

	@Override
	protected void customServerAiStep() {
		if (--this.randomTickDivider <= 0) {
			this.randomTickDivider = 70 + this.random.nextInt(50);

			// check if we're near a quest grove and if so, set that as home
			int chunkX = Mth.floor(this.getX()) / 16;
			int chunkZ = Mth.floor(this.getZ()) / 16;

			TFFeature nearFeature = TFFeature.getNearestFeature(chunkX, chunkZ, (ServerLevel) this.level);

			if (nearFeature != TFFeature.QUEST_GROVE) {
				this.hasRestriction();
			} else {
				// set our home position to the center of the quest grove
				BlockPos cc = TFFeature.getNearestCenterXYZ(Mth.floor(this.getX()), Mth.floor(this.getZ()));
				this.restrictTo(cc, 13);
			}

			if (countColorsSet() > 15 && !getRewarded()) {
				rewardQuest();
				setRewarded(true);
			}

		}

		super.customServerAiStep();
	}

	private void rewardQuest() {
		// todo flesh the context out more
		LootContext ctx = new LootContext.Builder((ServerLevel) level).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.PIGLIN_BARTER);
		level.getServer().getLootTables().get(TFTreasure.QUESTING_RAM_REWARDS).getRandomItems(ctx, s -> spawnAtLocation(s, 1.0F));

		for (ServerPlayer player : this.level.getEntitiesOfClass(ServerPlayer.class, getBoundingBox().inflate(16.0D, 16.0D, 16.0D))) {
			TFAdvancements.QUEST_RAM_COMPLETED.trigger(player);
		}
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
		ItemStack currentItem = player.getItemInHand(hand);

		if (tryAccept(currentItem)) {
			if (!player.getAbilities().instabuild) {
				currentItem.shrink(1);
			}

			return InteractionResult.SUCCESS;
		} else {
			return super.interactAt(player, vec, hand);
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (level.isClientSide && countColorsSet() > 15 && !getRewarded()) {
			animateAddColor(DyeColor.byId(this.random.nextInt(16)), 5);
		}
	}

	public boolean tryAccept(ItemStack stack) {
		if (stack.is(ItemTags.WOOL)) {
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
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ColorFlags", this.getColorFlags());
		compound.putBoolean("Rewarded", this.getRewarded());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setColorFlags(compound.getInt("ColorFlags"));
		this.setRewarded(compound.getBoolean("Rewarded"));
	}

	private int getColorFlags() {
		return entityData.get(DATA_COLOR);
	}

	private void setColorFlags(int flags) {
		entityData.set(DATA_COLOR, flags);
	}

	public boolean isColorPresent(DyeColor color) {
		return (getColorFlags() & (1 << color.getId())) > 0;
	}

	public void setColorPresent(DyeColor color) {
		setColorFlags(getColorFlags() | (1 << color.getId()));
	}

	public boolean getRewarded() {
		return entityData.get(DATA_REWARDED);
	}

	public void setRewarded(boolean rewarded) {
		entityData.set(DATA_REWARDED, rewarded);
	}

	private void animateAddColor(DyeColor color, int iterations) {
		float[] colorVal = color.getTextureDiffuseColors();
		float red = colorVal[0];
		float green = colorVal[1];
		float blue = colorVal[2];

		for (int i = 0; i < iterations; i++) {
			this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 1.5, this.getY() + this.random.nextDouble() * this.getBbHeight() * 1.5, this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth() * 1.5, red, green, blue);
		}

		playAmbientSound();
	}

	public int countColorsSet() {
		return Integer.bitCount(getColorFlags());
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	protected float getSoundVolume() {
		return 1.0F;
	}

	@Override
	public float getVoicePitch() {
		return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F;
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
