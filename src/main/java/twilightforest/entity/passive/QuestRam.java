package twilightforest.entity.passive;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FastColor;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.ai.goal.QuestRamEatWoolGoal;
import twilightforest.init.TFAdvancements;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.network.ParticlePacket;
import twilightforest.util.landmarks.LandmarkUtil;

import java.util.Optional;

public class QuestRam extends Animal implements EnforcedHomePoint {

	private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(QuestRam.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_REWARDED = SynchedEntityData.defineId(QuestRam.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(QuestRam.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

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
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0F, this::isItemTempting, false));
		this.addRestrictionGoals(this, this.goalSelector);
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	public boolean isItemTempting(ItemStack stack) {
		for (var questEntry : TwilightForestMod.getQuests().getQuestingRam().questItems().entrySet()) {
			if (questEntry.getValue().test(stack)) {
				DyeColor color = questEntry.getKey();
				return color != null && !this.isColorPresent(color);
			}
		}
		return false;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return false;
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
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_COLOR, 0);
		builder.define(DATA_REWARDED, false);
		builder.define(HOME_POINT, Optional.empty());
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

	private void rewardQuest() {
		// todo flesh the context out more
		LootParams ctx = new LootParams.Builder((ServerLevel) this.level()).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.PIGLIN_BARTER);
		ObjectArrayList<ItemStack> rewards = this.level().getServer().reloadableRegistries().getLootTable(TwilightForestMod.getQuests().getQuestingRam().lootTable()).getRandomItems(ctx);
		rewards.forEach(stack -> this.spawnAtLocation(stack, 1.0F));

		for (ServerPlayer player : this.level().getEntitiesOfClass(ServerPlayer.class, getBoundingBox().inflate(16.0D, 16.0D, 16.0D))) {
			TFAdvancements.QUEST_RAM_COMPLETED.get().trigger(player);
		}

		LandmarkUtil.markStructureConquered(this.level(), this, TFStructures.QUEST_GROVE, true);
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
		ItemStack currentItem = player.getItemInHand(hand);

		if (this.tryAccept(currentItem)) {
			currentItem.consume(1, player);

			return InteractionResult.SUCCESS;
		} else {
			return super.interactAt(player, vec, hand);
		}
	}

	@Override
	public AABB getBoundingBoxForCulling() {
		return super.getBoundingBoxForCulling().inflate(3.0D);
	}

	public boolean tryAccept(ItemStack stack) {
		for (var questEntry : TwilightForestMod.getQuests().getQuestingRam().questItems().entrySet()) {
			if (questEntry.getValue().test(stack)) {
				DyeColor color = questEntry.getKey();
				if (color != null && !this.isColorPresent(color)) {
					this.setColorPresent(color);
					this.animateAddColor(color, 50);
					this.playAmbientSound();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ColorFlags", this.getColorFlags());
		compound.putBoolean("Rewarded", this.getRewarded());
		this.saveHomePointToNbt(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setColorFlags(compound.getInt("ColorFlags"));
		this.setRewarded(compound.getBoolean("Rewarded"));
		this.loadHomePointFromNbt(compound);
	}

	public int getColorFlags() {
		return this.getEntityData().get(DATA_COLOR);
	}

	private void setColorFlags(int flags) {
		this.getEntityData().set(DATA_COLOR, flags);
	}

	public boolean isColorPresent(DyeColor color) {
		return (this.getColorFlags() & (1 << color.getId())) > 0;
	}

	public void setColorPresent(DyeColor color) {
		this.setColorFlags(this.getColorFlags() | (1 << color.getId()));
	}

	public boolean getRewarded() {
		return this.getEntityData().get(DATA_REWARDED);
	}

	public void setRewarded(boolean rewarded) {
		this.getEntityData().set(DATA_REWARDED, rewarded);
	}

	private void animateAddColor(DyeColor color, int iterations) {
		int colorVal = color.getTextureDiffuseColor();

		if (!this.level().isClientSide()) {
			for (ServerPlayer serverplayer : ((ServerLevel) this.level()).players()) {
				if (serverplayer.distanceToSqr(Vec3.atCenterOf(this.blockPosition())) < 4096.0D) {
					ParticlePacket packet = new ParticlePacket();

					for (int i = 0; i < iterations; i++) {
						packet.queueParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, FastColor.ARGB32.red(colorVal), FastColor.ARGB32.green(colorVal), FastColor.ARGB32.blue(colorVal)), false,
							this.getX() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 1.5D,
							this.getY() + this.getRandom().nextDouble() * this.getBbHeight() * 1.5D,
							this.getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getBbWidth() * 1.5D,
							0.0F, 0.0F, 0.0F);
					}

					PacketDistributor.sendToPlayer(serverplayer, packet);
				}
			}
		}
	}

	public int countColorsSet() {
		return Integer.bitCount(this.getColorFlags());
	}

	@Override
	protected AABB makeBoundingBox() {
		return super.makeBoundingBox();
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

	@Override
	public @Nullable GlobalPos getRestrictionPoint() {
		return this.getEntityData().get(HOME_POINT).orElse(null);
	}

	@Override
	public void setRestrictionPoint(@Nullable GlobalPos pos) {
		this.getEntityData().set(HOME_POINT, Optional.ofNullable(pos));
	}

	@Override
	public int getHomeRadius() {
		return 13;
	}
}
