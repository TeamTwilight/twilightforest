package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.item.TFItems;

public class EntityAITFLichShadows extends EntityAIBase {

	private final EntityTFLich lich;

	public EntityAITFLichShadows(EntityTFLich boss) {
		lich = boss;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return lich.getPhase() == 1;
	}

	@Override
	public void startExecuting() {
		lich.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.twilight_scepter));
	}

	@Override
	public void resetTask() {
		despawnClones();
	}

	@Override
	public void updateTask() {
		if (lich.isShadowClone())
			checkForMaster();
		EntityLivingBase targetedEntity = lich.getAttackTarget();
		if (targetedEntity == null)
			return;
		float dist = lich.getDistanceToEntity(targetedEntity);

		if (lich.getAttackCooldown() == 60) {
			lich.teleportToSightOfEntity(targetedEntity);

			if (!lich.isShadowClone()) {
				checkAndSpawnClones();
			}
		}

		if (lich.getEntitySenses().canSee(targetedEntity) && lich.getAttackCooldown() == 0 && dist < 20F) {
			if (lich.getNextAttackType() == 0) {
				lich.launchBoltAt();
			} else {
				lich.launchBombAt();
			}

			if (lich.getRNG().nextInt(3) > 0) {
				lich.setNextAttackType(0);
			} else {
				lich.setNextAttackType(1);
			}
			lich.setAttackCooldown(100);
		}
	}

	private void checkForMaster() {
		if (lich.getMasterLich() == null) {
			findNewMaster();
		}
		if (!lich.world.isRemote && (lich.getMasterLich() == null || lich.getMasterLich().isDead)) {
			lich.setDead();
		}
	}

	private void checkAndSpawnClones() {
		// if not, spawn one!
		if (lich.countMyClones() < EntityTFLich.MAX_SHADOW_CLONES)
			spawnShadowClone();
	}

	private void spawnShadowClone() {
		EntityLivingBase targetedEntity = lich.getAttackTarget();

		// find a good spot
		Vec3d cloneSpot = lich.findVecInLOSOf(targetedEntity);

		if (cloneSpot != null) {
			// put a clone there
			EntityTFLich newClone = new EntityTFLich(lich.world, lich);
			newClone.setPosition(cloneSpot.x, cloneSpot.y, cloneSpot.z);
			lich.world.spawnEntity(newClone);

			newClone.setAttackTarget(targetedEntity);
			newClone.setAttackCooldown(60 + lich.getRNG().nextInt(3) - lich.getRNG().nextInt(3));

			// make sparkles leading to it
			lich.makeTeleportTrail(lich.posX, lich.posY, lich.posZ, cloneSpot.x, cloneSpot.y, cloneSpot.z);
		}
	}

	private void despawnClones() {
		for (EntityTFLich nearbyLich : lich.getNearbyLiches()) {
			if (nearbyLich.isShadowClone()) {
				nearbyLich.isDead = true;
			}
		}
	}

	private void findNewMaster() {

		for (EntityTFLich nearbyLich : lich.getNearbyLiches()) {
			if (!nearbyLich.isShadowClone() && nearbyLich.wantsNewClone(lich)) {
				lich.setMaster(nearbyLich);

				// animate our new linkage!
				lich.makeTeleportTrail(lich.posX, lich.posY, lich.posZ, nearbyLich.posX, nearbyLich.posY, nearbyLich.posZ);

				lich.setAttackTarget(nearbyLich.getAttackTarget());
				break;
			}
		}
	}
}
