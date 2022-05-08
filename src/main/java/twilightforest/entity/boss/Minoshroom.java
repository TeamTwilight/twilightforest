package twilightforest.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.advancements.TFAdvancements;
import twilightforest.loot.TFTreasure;
import twilightforest.world.registration.TFFeature;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;
import twilightforest.entity.monster.Minotaur;
import twilightforest.entity.ai.GroundAttackGoal;
import twilightforest.item.TFItems;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Minoshroom extends Minotaur {
	private static final EntityDataAccessor<Boolean> GROUND_ATTACK = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> GROUND_CHARGE = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.INT);
	private float prevClientSideChargeAnimation;
	private float clientSideChargeAnimation;
	private boolean groundSmashState = false;
	private final List<ServerPlayer> hurtBy = new ArrayList<>();
	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

	public Minoshroom(EntityType<? extends Minoshroom> type, Level world) {
		super(type, world);
		this.xpReward = 100;
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new GroundAttackGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(GROUND_ATTACK, false);
		entityData.define(GROUND_CHARGE, 0);
	}

	public boolean isGroundAttackCharge() {
		return entityData.get(GROUND_ATTACK);
	}

	public void setGroundAttackCharge(boolean flag) {
		entityData.set(GROUND_ATTACK, flag);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Minotaur.registerAttributes()
				.add(Attributes.MAX_HEALTH, 120.0D);
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level.isClientSide) {
			this.prevClientSideChargeAnimation = this.clientSideChargeAnimation;
			if (this.isGroundAttackCharge()) {
				this.clientSideChargeAnimation = Mth.clamp(this.clientSideChargeAnimation + (1F / ((float) entityData.get(GROUND_CHARGE)) * 6F), 0.0F, 6.0F);
				groundSmashState = true;
			} else {
				this.clientSideChargeAnimation = Mth.clamp(this.clientSideChargeAnimation - 1.0F, 0.0F, 6.0F);
				if (groundSmashState) {
					BlockState block = level.getBlockState(blockPosition().below());

					for (int i = 0; i < 80; i++) {
						double cx = blockPosition().getX() + level.random.nextFloat() * 10F - 5F;
						double cy = getBoundingBox().minY + 0.1F + level.random.nextFloat() * 0.3F;
						double cz = blockPosition().getZ() + level.random.nextFloat() * 10F - 5F;

						level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), cx, cy, cz, 0D, 0D, 0D);
					}
					groundSmashState = false;
				}
			}
		}
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MINOSHROOM_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.MINOSHROOM_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.MINOSHROOM_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
		playSound(TFSounds.MINOSHROOM_STEP, 0.15F, 0.8F);
	}
	
	@Override
	public boolean doHurtTarget(Entity entity) {
		boolean success = super.doHurtTarget(entity);

		if (success && this.isCharging()) {
			entity.push(0, 0.4, 0);
			playSound(TFSounds.MINOSHROOM_ATTACK, 1.0F, 1.0F);
		}

		return success;
	}

	@OnlyIn(Dist.CLIENT)
	public float getChargeAnimationScale(float p_189795_1_) {
		return (this.prevClientSideChargeAnimation + (this.clientSideChargeAnimation - this.prevClientSideChargeAnimation) * p_189795_1_) / 6.0F;
	}

	public void setMaxCharge(int charge) {
		entityData.set(GROUND_CHARGE, charge);
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(difficulty);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.DIAMOND_MINOTAUR_AXE.get()));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if(source.getEntity() instanceof ServerPlayer player && !hurtBy.contains(player)) {
			hurtBy.add(player);
		}
		return super.hurt(source, amount);
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if (!level.isClientSide) {
			TFGenerationSettings.markStructureConquered(level, new BlockPos(this.blockPosition()), TFFeature.LABYRINTH);
			for(ServerPlayer player : hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}

			TFTreasure.entityDropsIntoContainer(this, this.createLootContext(true, cause).create(LootContextParamSets.ENTITY), TFBlocks.MANGROVE_CHEST.get().defaultBlockState(), new BlockPos(this.position()));
		}
	}

	@Override
	protected boolean shouldDropLoot() {
		// Invoked the mob's loot during die, this will avoid duplicating during the actual drop phase
		return false;
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			if (hasRestriction()) {
				level.setBlockAndUpdate(getRestrictCenter(), TFBlocks.MINOSHROOM_BOSS_SPAWNER.get().defaultBlockState());
			}
			discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected float getWaterSlowDown() {
		return 1.0F;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}
}
