package twilightforest.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import twilightforest.TFSounds;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.entity.boss.EntityTFLichMinion;
import twilightforest.item.TFItems;

import java.util.EnumSet;

public class EntityAITFLichMinions extends Goal {

	private final EntityTFLich lich;

	public EntityAITFLichMinions(EntityTFLich boss) {
		lich = boss;
		setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		return lich.getPhase() == 2 && !lich.isShadowClone();
	}

	@Override
	public void startExecuting() {
		lich.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TFItems.zombie_scepter.get()));
	}

	@Override
	public void tick() {
		LivingEntity targetedEntity = lich.getAttackTarget();
		if (targetedEntity == null)
			return;
		float dist = lich.getDistance(targetedEntity);
		// spawn minions every so often
		if (lich.getAttackCooldown() % 15 == 0) {
			checkAndSpawnMinions();
		}

		if (lich.getAttackCooldown() == 0) {
			if (dist < 2.0F) {
				// melee attack
				lich.attackEntityAsMob(targetedEntity);
				lich.setAttackCooldown(20);
			} else if (dist < 20F && lich.getEntitySenses().canSee(targetedEntity)) {
				if (lich.getNextAttackType() == 0) {
					lich.launchBoltAt();
				} else {
					lich.launchBombAt();
				}

				if (lich.getRNG().nextInt(2) > 0) {
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
		if (!lich.world.isRemote && lich.getMinionsToSummon() > 0) {
			int minions = lich.countMyMinions();

			// if not, spawn one!
			if (minions < EntityTFLich.MAX_ACTIVE_MINIONS) {
				spawnMinionAt();
				lich.setMinionsToSummon(lich.getMinionsToSummon() - 1);
			}
		}
		// if there's no minions left to summon, we should move into phase 3 naturally
	}

	private void spawnMinionAt() {
		// find a good spot
		LivingEntity targetedEntity = lich.getAttackTarget();
		Vector3d minionSpot = lich.findVecInLOSOf(targetedEntity);

		if (minionSpot != null && lich.world instanceof IServerWorld) {
			// put a clone there
			EntityTFLichMinion minion = new EntityTFLichMinion(lich.world, lich);
			minion.setPosition(minionSpot.x, minionSpot.y, minionSpot.z);
			minion.onInitialSpawn((IServerWorld) lich.world, lich.world.getDifficultyForLocation(new BlockPos(minionSpot)), SpawnReason.MOB_SUMMONED, null, null);
			lich.world.addEntity(minion);

			minion.setAttackTarget(targetedEntity);

			minion.spawnExplosionParticle();
			minion.playSound(TFSounds.MINION_SUMMON, 1.0F, ((lich.getRNG().nextFloat() - lich.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);

			// make sparkles leading to it
			lich.makeBlackMagicTrail(lich.getPosX(), lich.getPosY() + lich.getEyeHeight(), lich.getPosZ(), minionSpot.x, minionSpot.y + minion.getHeight() / 2.0, minionSpot.z);
		}
	}

}
