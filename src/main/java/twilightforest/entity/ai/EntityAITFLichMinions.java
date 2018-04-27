package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.entity.boss.EntityTFLichMinion;
import twilightforest.item.TFItems;

public class EntityAITFLichMinions extends EntityAIBase {

	private final EntityTFLich lich;

	public EntityAITFLichMinions(EntityTFLich boss) {
		lich = boss;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return lich.getPhase() == 2 && !lich.isShadowClone();
	}

	@Override
	public void startExecuting() {
		lich.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.zombie_scepter));
	}

	@Override
	public void updateTask() {
		EntityLivingBase targetedEntity = lich.getAttackTarget();
		if (targetedEntity == null)
			return;
		float dist = lich.getDistanceToEntity(targetedEntity);
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
		EntityLivingBase targetedEntity = lich.getAttackTarget();
		Vec3d minionSpot = lich.findVecInLOSOf(targetedEntity);

		if (minionSpot != null) {
			// put a clone there
			EntityTFLichMinion minion = new EntityTFLichMinion(lich.world, lich);
			minion.setPosition(minionSpot.x, minionSpot.y, minionSpot.z);
			minion.onInitialSpawn(lich.world.getDifficultyForLocation(new BlockPos(minionSpot)), null);
			lich.world.spawnEntity(minion);

			minion.setAttackTarget(targetedEntity);

			minion.spawnExplosionParticle();
			minion.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, ((lich.getRNG().nextFloat() - lich.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);

			// make sparkles leading to it
			lich.makeBlackMagicTrail(lich.posX, lich.posY + lich.getEyeHeight(), lich.posZ, minionSpot.x, minionSpot.y + minion.height / 2.0, minionSpot.z);
		}
	}

}
