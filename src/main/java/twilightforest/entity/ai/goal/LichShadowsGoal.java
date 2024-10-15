package twilightforest.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.Lich;
import twilightforest.entity.projectile.LichBolt;
import twilightforest.entity.projectile.LichBomb;
import twilightforest.init.TFAttributes;
import twilightforest.init.TFItems;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class LichShadowsGoal extends Goal {

	private final Lich lich;

	@SuppressWarnings("this-escape")
	public LichShadowsGoal(Lich boss) {
		this.lich = boss;
		this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return this.lich.getPhase() == 1 && this.lich.tickCount > 20 && this.lich.getTarget() != null;
	}

	@Override
	public void start() {
		this.lich.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.TWILIGHT_SCEPTER.get()));
	}

	@Override
	public void stop() {
		this.lich.despawnClones();
	}

	@Override
	public void tick() {
		if (!this.lich.isShadowClone()) {
			this.getClones().forEach(clone -> {
				clone.setAttackCooldown(this.lich.getAttackCooldown());
				clone.setTeleportInvisibility(this.lich.getTeleportInvisibility());
			});
		}

		if (this.lich.getTeleportInvisibility() > 0) return;
		if (this.lich.isShadowClone()) this.checkForMaster();

		LivingEntity targetedEntity = this.lich.getTarget();
		if (targetedEntity == null) return;
		float dist = this.lich.distanceTo(targetedEntity);

		if (this.lich.getAttackCooldown() == 60 && !this.lich.isShadowClone()) {
			this.lich.teleportToSightOfEntity(targetedEntity);
			this.getClones().forEach(clone -> clone.teleportToSightOfEntity(targetedEntity));
			this.checkAndSpawnClones();
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

	protected List<Lich> getClones() {
		if (!this.lich.isShadowClone()) {
			List<Lich> clones = new ArrayList<>();
			if (this.lich.level() instanceof ServerLevel server) {
				for (UUID uuid : this.lich.getClones()) {
					if (server.getEntity(uuid) instanceof Lich clone && clone.getMaster() == this.lich) clones.add(clone);
				}
			}
			return clones;
		} else return List.of();
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
		if (this.lich.countMyClones() < this.lich.getAttributeValue(TFAttributes.CLONE_COUNT))
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
			newClone.setItemInHand(InteractionHand.MAIN_HAND, TFItems.TWILIGHT_SCEPTER.toStack());
			newClone.setTeleportInvisibility(this.lich.getTeleportInvisibility());
			this.lich.addClone(newClone.getUUID());
		}
	}

	private void findNewMaster() {
		for (Lich nearbyLich : this.getNearbyLiches()) {
			if (!nearbyLich.isShadowClone() && nearbyLich.wantsNewClone(this.lich)) {
				this.lich.setMasterUUID(nearbyLich.getUUID());
				nearbyLich.addClone(this.lich.getUUID());

				// animate our new linkage!
				this.lich.setTarget(nearbyLich.getTarget());
				break;
			}
		}
	}

	private List<? extends Lich> getNearbyLiches() {
		return this.lich.level().getEntitiesOfClass(this.lich.getClass(), new AABB(this.lich.getX(), this.lich.getY(), this.lich.getZ(), this.lich.getX() + 1, this.lich.getY() + 1, this.lich.getZ() + 1).inflate(32.0D, 16.0D, 32.0D));
	}
}
