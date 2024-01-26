package twilightforest.entity.boss;

import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.init.TFAdvancements;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructures;
import twilightforest.loot.TFLootTables;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlateauBoss extends Monster implements EnforcedHomePoint {

	private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(PlateauBoss.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

	private final ServerBossEvent bossInfo = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
	private final List<ServerPlayer> hurtBy = new ArrayList<>();

	public PlateauBoss(EntityType<? extends PlateauBoss> type, Level world) {
		super(type, world);
		this.xpReward = 647;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes();
	}

	@Override
	protected void registerGoals() {
	}

	@Override
	public void aiStep() {
		if (!this.level().isClientSide()) {
			this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
		}
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
			if (this.isRestrictionPointValid(this.level().dimension()) && this.level().isLoaded(this.getRestrictionPoint().pos())) {
				this.level().setBlockAndUpdate(this.getRestrictionPoint().pos(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get().defaultBlockState());
			}
			this.discard();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if(source.getEntity() instanceof ServerPlayer player && !this.hurtBy.contains(player)) {
			this.hurtBy.add(player);
		}
		return super.hurt(source, amount);
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if (!this.level().isClientSide()) {
			this.bossInfo.setProgress(0.0F);
			LandmarkUtil.markStructureConquered(this.level(), this, TFStructures.FINAL_CASTLE, true);
			for(ServerPlayer player : this.hurtBy) {
				TFAdvancements.HURT_BOSS.get().trigger(player, this);
			}
		}
		TFLootTables.entityDropsIntoContainer(this, cause, TFBlocks.SORTING_CHEST.get().defaultBlockState(), EntityUtil.bossChestLocation(this));
	}

	@Override
	protected boolean shouldDropLoot() {
		return !TFConfig.COMMON_CONFIG.bossDropChests.get();
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
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
		return 30;
	}
}
