package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.Lich;
import twilightforest.entity.projectile.LichBolt;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.init.TFItems;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class LichShadowsGoal extends Goal {

	private final Lich lich;

	public LichShadowsGoal(Lich boss) {
		this.lich = boss;
		this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return this.lich.getPhase() == 1;
	}

	@Override
	public void start() {
		this.lich.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.TWILIGHT_SCEPTER.get()));
	}

	@Override
	public void stop() {
		this.despawnClones();
	}

	@Override
	public void tick() {
		if (this.lich.isShadowClone())
			this.checkForMaster();
		LivingEntity targetedEntity = this.lich.getTarget();
		if (targetedEntity == null)
			return;
		float dist = this.lich.distanceTo(targetedEntity);

		if (this.lich.getAttackCooldown() == 60) {
			this.lich.teleportToSightOfEntity(targetedEntity);

			if (!this.lich.isShadowClone()) {
				this.checkAndSpawnClones();
			}
		}

		if (this.lich.getSensing().hasLineOfSight(targetedEntity) && this.lich.getAttackCooldown() == 0 && dist < 20F) {
			if (this.lich.getNextAttackType() == 0) {
				this.lich.launchProjectileAt(new LichBolt(this.lich.level(), this.lich));
			} else {
				this.lich.launchProjectileAt(new LichBomb(this.lich.level(), this.lich));
			}

			this.lich.swing(InteractionHand.MAIN_HAND);

			if (this.lich.getRandom().nextInt(3) > 0) {
				this.lich.setNextAttackType(0);
			} else {
				this.lich.setNextAttackType(1);
			}
			this.lich.setAttackCooldown(100);
		}
	}

	private void checkForMaster() {
		if (this.lich.getMaster() == null) {
			this.findNewMaster();
		}
		if (this.lich.getMaster() == null || !this.lich.getMaster().isAlive() || this.lich.getMaster().getPhase() != 1) {
			this.lich.discard();
		}
	}

	private void checkAndSpawnClones() {
		// if not, spawn one!
		if (this.lich.countMyClones() < Lich.MAX_SHADOW_CLONES)
			this.spawnShadowClone();
	}

	private void spawnShadowClone() {
		LivingEntity targetedEntity = this.lich.getTarget();

		// find a good spot
		Vec3 cloneSpot = this.lich.findVecInLOSOf(targetedEntity);

		if (cloneSpot != null) {
			// put a clone there
			Lich newClone = new Lich(this.lich.level(), this.lich);
			newClone.setPos(cloneSpot.x(), cloneSpot.y(), cloneSpot.z());
			this.lich.level().addFreshEntity(newClone);

			newClone.setTarget(targetedEntity);
			newClone.setAttackCooldown(60 + this.lich.getRandom().nextInt(3) - this.lich.getRandom().nextInt(3));
			this.lich.addClone(newClone.getUUID());
			// make sparkles leading to it
			this.lich.makeTeleportTrail(this.lich.getX(), this.lich.getY(), this.lich.getZ(), cloneSpot.x(), cloneSpot.y(), cloneSpot.z());
		}
	}

	private void despawnClones() {
		if (this.lich.level() instanceof ServerLevel server) {
			for (UUID uuid : this.lich.getClones()) {
				Entity entity = server.getEntity(uuid);
				if (entity instanceof Lich clone && lich.getMaster() == this.lich) {
					clone.remove(Entity.RemovalReason.DISCARDED);
				}
			}
		}
	}

	private void findNewMaster() {
		for (Lich nearbyLich : this.getNearbyLiches()) {
			if (!nearbyLich.isShadowClone() && nearbyLich.wantsNewClone(this.lich)) {
				this.lich.setMasterUUID(nearbyLich.getUUID());
				nearbyLich.addClone(this.lich.getUUID());

				// animate our new linkage!
				this.lich.makeTeleportTrail(this.lich.getX(), this.lich.getY(), this.lich.getZ(), nearbyLich.getX(), nearbyLich.getY(), nearbyLich.getZ());

				this.lich.setTarget(nearbyLich.getTarget());
				break;
			}
		}
	}

	private List<? extends Lich> getNearbyLiches() {
		return this.lich.level().getEntitiesOfClass(this.lich.getClass(), new AABB(this.lich.getX(), this.lich.getY(), this.lich.getZ(), this.lich.getX() + 1, this.lich.getY() + 1, this.lich.getZ() + 1).inflate(32.0D, 16.0D, 32.0D));
	}
}
