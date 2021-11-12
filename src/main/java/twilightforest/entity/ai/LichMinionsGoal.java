package twilightforest.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ServerLevelAccessor;
import twilightforest.TFSounds;
import twilightforest.entity.boss.Lich;
import twilightforest.entity.monster.LichMinion;
import twilightforest.item.TFItems;

import java.util.EnumSet;

public class LichMinionsGoal extends Goal {

	private final Lich lich;

	public LichMinionsGoal(Lich boss) {
		lich = boss;
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return lich.getPhase() == 2 && !lich.isShadowClone();
	}

	@Override
	public void start() {
		lich.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.ZOMBIE_SCEPTER.get()));
	}

	@Override
	public void tick() {
		LivingEntity targetedEntity = lich.getTarget();
		if (targetedEntity == null)
			return;
		float dist = lich.distanceTo(targetedEntity);
		// spawn minions every so often
		if (lich.getAttackCooldown() % 15 == 0) {
			checkAndSpawnMinions();
		}

		if (lich.getAttackCooldown() == 0) {
			if (dist < 2.0F) {
				// melee attack
				lich.doHurtTarget(targetedEntity);
				lich.setAttackCooldown(20);
			} else if (dist < 20F && lich.getSensing().hasLineOfSight(targetedEntity)) {
				if (lich.getNextAttackType() == 0) {
					lich.launchBoltAt();
				} else {
					lich.launchBombAt();
				}

				if (lich.getRandom().nextInt(2) > 0) {
					lich.setNextAttackType(0);
				} else {
					lich.setNextAttackType(1);
				}
				lich.setAttackCooldown(60);
			} else {
				// if not, teleport around
				lich.teleportToSightOfEntity(targetedEntity);
				lich.setAttackCooldown(20);

			}
		}
	}

	private void checkAndSpawnMinions() {
		if (!lich.level.isClientSide && lich.getMinionsToSummon() > 0) {
			int minions = lich.countMyMinions();

			// if not, spawn one!
			if (minions < Lich.MAX_ACTIVE_MINIONS) {
				spawnMinionAt();
				lich.setMinionsToSummon(lich.getMinionsToSummon() - 1);
			}
		}
		// if there's no minions left to summon, we should move into phase 3 naturally
	}

	private void spawnMinionAt() {
		// find a good spot
		LivingEntity targetedEntity = lich.getTarget();
		Vec3 minionSpot = lich.findVecInLOSOf(targetedEntity);

		if (minionSpot != null && lich.level instanceof ServerLevelAccessor) {
			// put a clone there
			LichMinion minion = new LichMinion(lich.level, lich);
			minion.setPos(minionSpot.x, minionSpot.y, minionSpot.z);
			minion.finalizeSpawn((ServerLevelAccessor) lich.level, lich.level.getCurrentDifficultyAt(new BlockPos(minionSpot)), MobSpawnType.MOB_SUMMONED, null, null);
			lich.level.addFreshEntity(minion);

			minion.setTarget(targetedEntity);

			minion.spawnAnim();
			minion.playSound(TFSounds.MINION_SUMMON, 1.0F, ((lich.getRandom().nextFloat() - lich.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);

			// make sparkles leading to it
			lich.makeBlackMagicTrail(lich.getX(), lich.getY() + lich.getEyeHeight(), lich.getZ(), minionSpot.x, minionSpot.y + minion.getBbHeight() / 2.0, minionSpot.z);
		}
	}

}
