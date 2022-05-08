package twilightforest.entity.boss;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerBossEvent;
import twilightforest.advancements.TFAdvancements;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PlateauBoss extends Monster {

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
		if (!level.isClientSide) {
			bossInfo.setProgress(getHealth() / getMaxHealth());
		}
	}

	@Override
	public boolean removeWhenFarAway(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (level.getDifficulty() == Difficulty.PEACEFUL) {
			if (!hasRestriction()) {
				level.setBlockAndUpdate(getRestrictCenter(), TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get().defaultBlockState());
			}
			discard();
		} else {
			super.checkDespawn();
		}
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
			TFGenerationSettings.markStructureConquered(level, new BlockPos(this.blockPosition()), TFFeature.FINAL_CASTLE);
			for(ServerPlayer player : hurtBy) {
				TFAdvancements.HURT_BOSS.trigger(player, this);
			}
		}
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
		BlockPos home = this.getRestrictCenter();
		compound.put("Home", newDoubleList(home.getX(), home.getY(), home.getZ()));
		compound.putBoolean("HasHome", this.hasRestriction());
		super.addAdditionalSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Home", 9)) {
			ListTag nbttaglist = compound.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			this.restrictTo(new BlockPos(hx, hy, hz), 30);
		}
		if (!compound.getBoolean("HasHome")) {
			this.hasRestriction();
		}
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
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
