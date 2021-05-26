package twilightforest.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.TFGenerationSettings;

import javax.annotation.Nullable;

public class EntityTFPlateauBoss extends MonsterEntity {

	private final ServerBossInfo bossInfo = new ServerBossInfo(getDisplayName(), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS);

	public EntityTFPlateauBoss(EntityType<? extends EntityTFPlateauBoss> type, World world) {
		super(type, world);
		this.experienceValue = 647;
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_();
	}

	@Override
	protected void registerGoals() {
	}

	@Override
	public void livingTick() {
		if (!world.isRemote) {
			bossInfo.setPercent(getHealth() / getMaxHealth());
		}
	}

	@Override
	public boolean canDespawn(double p_213397_1_) {
		return false;
	}

	@Override
	public void checkDespawn() {
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			if (!detachHome()) {
				world.setBlockState(getHomePosition(), TFBlocks.boss_spawner_final_boss.get().getDefaultState());
			}
			remove();
		} else {
			super.checkDespawn();
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!world.isRemote) {
			TFGenerationSettings.markStructureConquered(world, new BlockPos(this.getPosition()), TFFeature.FINAL_CASTLE);
		}
	}

	@Override
	public void setCustomName(@Nullable ITextComponent name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void addTrackingPlayer(ServerPlayerEntity player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void removeTrackingPlayer(ServerPlayerEntity player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		BlockPos home = this.getHomePosition();
		compound.put("Home", newDoubleNBTList(home.getX(), home.getY(), home.getZ()));
		compound.putBoolean("HasHome", this.detachHome());
		super.writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.contains("Home", 9)) {
			ListNBT nbttaglist = compound.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			this.setHomePosAndDistance(new BlockPos(hx, hy, hz), 30);
		}
		if (!compound.getBoolean("HasHome")) {
			this.detachHome();
		}
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
