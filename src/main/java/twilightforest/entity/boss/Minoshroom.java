package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.init.TFAdvancements;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.ai.goal.GroundAttackGoal;
import twilightforest.entity.monster.Minotaur;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStructures;
import twilightforest.loot.TFLootTables;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Minoshroom extends Minotaur implements EnforcedHomePoint {

	private static final EntityDataAccessor<Boolean> GROUND_ATTACK = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> GROUND_CHARGE = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(Minoshroom.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

	private float prevClientSideChargeAnimation;
	private float clientSideChargeAnimation;
	private boolean groundSmashState = false;
	private final List<ServerPlayer> hurtBy = new ArrayList<>();
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

	public Minoshroom(EntityType<? extends Minoshroom> type, Level level) {
		super(type, level);
		this.xpReward = 100;
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new GroundAttackGoal(this));
		this.addRestrictionGoals(this, this.goalSelector);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(GROUND_ATTACK, false);
		this.getEntityData().define(GROUND_CHARGE, 0);
		this.getEntityData().define(HOME_POINT, Optional.empty());
	}

	public boolean isGroundAttackCharge() {
		return this.getEntityData().get(GROUND_ATTACK);
	}

	public void setGroundAttackCharge(boolean flag) {
		this.getEntityData().set(GROUND_ATTACK, flag);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Minotaur.registerAttributes()
				.add(Attributes.MAX_HEALTH, 120.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
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
	public void addAdditionalSaveData(CompoundTag compound) {
		this.saveHomePointToNbt(compound);
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.loadHomePointFromNbt(compound);
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			this.prevClientSideChargeAnimation = this.clientSideChargeAnimation;
			if (this.isGroundAttackCharge()) {
				this.clientSideChargeAnimation = Mth.clamp(this.clientSideChargeAnimation + (1.0F / ((float) this.getEntityData().get(GROUND_CHARGE)) * 6.0F), 0.0F, 6.0F);
				this.groundSmashState = true;
			} else {
				this.clientSideChargeAnimation = Mth.clamp(this.clientSideChargeAnimation - 1.0F, 0.0F, 6.0F);
				if (this.groundSmashState) {
					BlockState block = this.level().getBlockState(this.blockPosition().below());

					for (int i = 0; i < 80; i++) {
						double cx = this.blockPosition().getX() + this.level().getRandom().nextFloat() * 10.0F - 5.0F;
						double cy = this.getBoundingBox().minY + 0.1F + level().getRandom().nextFloat() * 0.3F;
						double cz = this.blockPosition().getZ() + this.level().getRandom().nextFloat() * 10.0F - 5.0F;

						this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), cx, cy, cz, 0.0D, 0.0D, 0.0D);
					}
					this.groundSmashState = false;
				}
			}
		}
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MINOSHROOM_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.MINOSHROOM_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.MINOSHROOM_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.MINOSHROOM_STEP.get(), 0.15F, 0.8F);
	}

	@Override
	protected SoundEvent getChargeSound() {
		return TFSounds.MINOSHROOM_ATTACK.get();
	}

	@OnlyIn(Dist.CLIENT)
	public float getChargeAnimationScale(float scale) {
		return (this.prevClientSideChargeAnimation + (this.clientSideChargeAnimation - this.prevClientSideChargeAnimation) * scale) / 6.0F;
	}

	public void setMaxCharge(int charge) {
		this.getEntityData().set(GROUND_CHARGE, charge);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficulty) {
		super.populateDefaultEquipmentSlots(source, difficulty);
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.DIAMOND_MINOTAUR_AXE.get()));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if(source.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
			this.hurtBy.add(player);
		}
		return super.hurt(source, amount);
	}

	@Override
	public void lavaHurt() {
		if (!this.fireImmune()) {
			this.setSecondsOnFire(5);
			if (this.hurt(this.damageSources().lava(), 4F)) {
				this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
				EntityUtil.killLavaAround(this);
			}
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if (!this.level().isClientSide()) {
			this.bossInfo.setProgress(0.0F);
			LandmarkUtil.markStructureConquered(this.level(), this, TFStructures.LABYRINTH, true);
			for(ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.get().trigger(player, this);
			}

			TFLootTables.entityDropsIntoContainer(this, cause, TFBlocks.MANGROVE_CHEST.get().defaultBlockState(), EntityUtil.bossChestLocation(this));
		}
	}

	@Override
	protected boolean shouldDropLoot() {
		return !TFConfig.COMMON_CONFIG.bossDropChests.get();
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.isRestrictionPointValid(this.level().dimension()) && this.level().isLoaded(this.getRestrictionPoint().pos())) {
				this.level().setBlockAndUpdate(this.getRestrictionPoint().pos(), TFBlocks.MINOSHROOM_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean isPushedByFluid(FluidType type) {
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
		return 20;
	}
}
