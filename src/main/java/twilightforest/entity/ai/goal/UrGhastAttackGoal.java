package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.UrGhast;
import twilightforest.entity.projectile.UrGhastFireball;
import twilightforest.init.TFSounds;

// [VanillaCopy] Ghast.GhastShootFireballGoal, edits noted
public class UrGhastAttackGoal extends Goal {
	private final UrGhast ghast;
	public int attackTimer;
	public int prevAttackTimer; // TF - add for renderer

	public UrGhastAttackGoal(UrGhast ghast) {
		this.ghast = ghast;
	}

	//TF - only attack if not in tantrum mode
	@Override
	public boolean canUse() {
		return this.ghast.getTarget() != null && !this.ghast.isInTantrum();
	}

	@Override
	public void start() {
		this.attackTimer = this.prevAttackTimer = 0;
	}

	@Override
	public void stop() {
		this.ghast.setCharging(false);
	}

	@Override
	public void tick() {
		LivingEntity target = this.ghast.getTarget();

		if (target.distanceToSqr(this.ghast) < 4096.0D && this.ghast.getSensing().hasLineOfSight(target)) {
			this.prevAttackTimer = this.attackTimer;
			++this.attackTimer;

			// TF face our target at all times
			this.ghast.getLookControl().setLookAt(target, 10.0F, this.ghast.getMaxHeadXRot());

			if (this.attackTimer == 10) {
				this.ghast.playSound(TFSounds.UR_GHAST_WARN.get(), 10.0F, this.ghast.getVoicePitch());
			}

			if (this.attackTimer == 20) {
				if (!this.ghast.isInTantrum()) {
					// TF - call custom method
					this.ghast.playSound(TFSounds.UR_GHAST_SHOOT.get(), 10.0F, this.ghast.getVoicePitch());
					this.spitFireball();
					this.prevAttackTimer = this.attackTimer;
				}
				this.attackTimer = -40;
			}
		} else if (this.attackTimer > 0) {
			this.prevAttackTimer = this.attackTimer;
			--this.attackTimer;
		}

		this.ghast.setCharging(this.attackTimer > 10);
	}

	public void spitFireball() {
		double offsetX = this.ghast.getTarget().getX() - this.ghast.getX();
		double offsetY = this.ghast.getTarget().getBoundingBox().minY + this.ghast.getTarget().getBbHeight() / 2.0F - (this.ghast.getY() + this.ghast.getBbHeight() / 2.0F);
		double offsetZ = this.ghast.getTarget().getZ() - this.ghast.getZ();

		UrGhastFireball fireball = new UrGhastFireball(this.ghast.level(), this.ghast, offsetX, offsetY, offsetZ, 1);
		double shotSpawnDistance = 8.5D;
		Vec3 lookVec = this.ghast.getViewVector(1.0F);
		fireball.setPos(
			this.ghast.getX() + lookVec.x() * shotSpawnDistance,
			this.ghast.getY() + this.ghast.getBbHeight() / 2.0F + lookVec.y() * shotSpawnDistance + 2.0D,
			this.ghast.getZ() + lookVec.z() * shotSpawnDistance
		);
		this.ghast.level().addFreshEntity(fireball);

		for (int i = 0; i < 2; i++) {
			fireball = new UrGhastFireball(this.ghast.level(), this.ghast, offsetX + (this.ghast.getRandom().nextFloat() - this.ghast.getRandom().nextFloat()) * 8, offsetY, offsetZ + (this.ghast.getRandom().nextFloat() - this.ghast.getRandom().nextFloat()) * 8, 1);
			fireball.setPos(
				this.ghast.getX() + lookVec.x() * shotSpawnDistance,
				this.ghast.getY() + this.ghast.getBbHeight() / 2.0F + lookVec.y() * shotSpawnDistance,
				this.ghast.getZ() + lookVec.z() * shotSpawnDistance
			);
			this.ghast.level().addFreshEntity(fireball);
		}
	}
}