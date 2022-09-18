package twilightforest.entity.passive;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import twilightforest.advancements.TFAdvancements;
import twilightforest.entity.ai.goal.QuestRamEatWoolGoal;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;
import twilightforest.network.ParticlePacket;
import twilightforest.network.TFPacketHandler;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class QuestRam extends Animal {

	private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(QuestRam.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_REWARDED = SynchedEntityData.defineId(QuestRam.class, EntityDataSerializers.BOOLEAN);

	private int randomTickDivider;

	public QuestRam(EntityType<? extends QuestRam> type, Level level) {
		super(type, level);
		this.randomTickDivider = 0;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.38F));
		this.goalSelector.addGoal(2, new QuestRamEatWoolGoal(this));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0F, Ingredient.of(ItemTags.WOOL), false));
		this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.75D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	@Nullable
	@Override
	public Animal getBreedOffspring(ServerLevel level, AgeableMob mate) {
		return null;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 70.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.23D);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_COLOR, 0);
		this.entityData.define(DATA_REWARDED, false);
	}

	@Override
	protected void customServerAiStep() {
		if (--this.randomTickDivider <= 0) {
			this.randomTickDivider = 70 + this.getRandom().nextInt(50);

			if (this.countColorsSet() > 15 && !this.getRewarded()) {
				this.rewardQuest();
				this.setRewarded(true);
			}

		}

		if (this.countColorsSet() > 15 && !this.getRewarded()) {
			this.animateAddColor(DyeColor.byId(this.getRandom().nextInt(16)), 5);
			this.playAmbientSound();
		}

		super.customServerAiStep();
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		if (type == MobSpawnType.STRUCTURE) this.restrictTo(this.blockPosition(), 13);
		return super.finalizeSpawn(accessor, difficulty, type, data, tag);
	}

	private void rewardQuest() {
		// todo flesh the context out more
		LootContext ctx = new LootContext.Builder((ServerLevel) this.getLevel()).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.PIGLIN_BARTER);
		ObjectArrayList<ItemStack> rewards = this.getLevel().getServer().getLootTables().get(TFLootTables.QUESTING_RAM_REWARDS).getRandomItems(ctx);
		rewards.forEach(stack -> this.spawnAtLocation(stack, 1.0F));

		for (ServerPlayer player : this.getLevel().getEntitiesOfClass(ServerPlayer.class, getBoundingBox().inflate(16.0D, 16.0D, 16.0D))) {
			TFAdvancements.QUEST_RAM_COMPLETED.trigger(player);
		}
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
		ItemStack currentItem = player.getItemInHand(hand);

		if (this.tryAccept(currentItem)) {
			if (!player.getAbilities().instabuild) {
				currentItem.shrink(1);
			}

			return InteractionResult.SUCCESS;
		} else {
			return super.interactAt(player, vec, hand);
		}
	}

	public boolean tryAccept(ItemStack stack) {
		if (stack.is(ItemTags.WOOL)) {
			DyeColor color = guessColor(stack);
			if (color != null && !this.isColorPresent(color)) {
				this.setColorPresent(color);
				this.animateAddColor(color, 50);
				this.playAmbientSound();
				return true;
			}
		}
		return false;
	}

	@Nullable
	public DyeColor guessColor(ItemStack stack) {
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
		if (this.getRestrictCenter() != BlockPos.ZERO) {
			BlockPos home = this.getRestrictCenter();
			compound.put("HomePos", this.newDoubleList(home.getX(), home.getY(), home.getZ()));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setColorFlags(compound.getInt("ColorFlags"));
		this.setRewarded(compound.getBoolean("Rewarded"));
		if (compound.contains("HomePos", 9)) {
			ListTag nbttaglist = compound.getList("HomePos", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			this.restrictTo(new BlockPos(hx, hy, hz), 13);
		}
	}

	public int getColorFlags() {
		return this.entityData.get(DATA_COLOR);
	}

	private void setColorFlags(int flags) {
		this.entityData.set(DATA_COLOR, flags);
	}

	public boolean isColorPresent(DyeColor color) {
		return (this.getColorFlags() & (1 << color.getId())) > 0;
	}

	public void setColorPresent(DyeColor color) {
		this.setColorFlags(this.getColorFlags() | (1 << color.getId()));
	}

	public boolean getRewarded() {
		return this.entityData.get(DATA_REWARDED);
	}

	public void setRewarded(boolean rewarded) {
		this.entityData.set(DATA_REWARDED, rewarded);
	}

	private void animateAddColor(DyeColor color, int iterations) {
		float[] colorVal = color.getTextureDiffuseColors();
		float red = colorVal[0];
		float green = colorVal[1];
		float blue = colorVal[2];

		if (!this.getLevel().isClientSide()) {
			for (ServerPlayer serverplayer : ((ServerLevel) this.getLevel()).players()) {
				if (serverplayer.distanceToSqr(Vec3.atCenterOf(this.blockPosition())) < 4096.0D) {
					ParticlePacket packet = new ParticlePacket();

					for (int i = 0; i < iterations; i++) {
						packet.queueParticle(ParticleTypes.ENTITY_EFFECT, false,
								this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 1.5D,
								this.getY() + this.getRandom().nextDouble() * this.getBbHeight() * 1.5D,
								this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 1.5D,
								red, green, blue);
					}

					TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverplayer), packet);
				}
			}
		}
	}

	public int countColorsSet() {
		return Integer.bitCount(this.getColorFlags());
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public float getVoicePitch() {
		return (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.2F + 0.7F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.QUEST_RAM_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.QUEST_RAM_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.QUEST_RAM_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.QUEST_RAM_STEP.get(), 0.15F, 1.0F);
	}
}
