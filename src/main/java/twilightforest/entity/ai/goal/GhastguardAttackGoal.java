package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.monster.CarminiteGhastguard;

// [VanillaCopy] Ghast.GhastShootFireballGoal, edits noted
public class GhastguardAttackGoal extends Goal {
	private final CarminiteGhastguard ghast;
	public int attackTimer;
	public int prevAttackTimer; // TF - add for renderer

	public GhastguardAttackGoal(CarminiteGhastguard ghast) {
		this.ghast = ghast;
	}

	@Override
	public boolean canUse() {
		return ghast.getTarget() != null && ghast.shouldAttack(ghast.getTarget());
	}

	@Override
	public void start() {
		attackTimer = prevAttackTimer = 0;
	}

	@Override
	public void stop() {
		ghast.setCharging(false);
	}

	@Override
	public void tick() {
		LivingEntity target = ghast.getTarget();

		if (target.distanceToSqr(ghast) < 4096.0D && ghast.getSensing().hasLineOfSight(target)) {
			prevAttackTimer = attackTimer;
			++attackTimer;

			// TF face our target at all times
			ghast.getLookControl().setLookAt(target, 10.0F, ghast.getMaxHeadXRot());

			if (attackTimer == 10) {
				ghast.playSound(ghast.getWarnSound(), 10.0F, ghast.getVoicePitch());
			}

			if (attackTimer == 20) {
				if (ghast.shouldAttack(target)) {
					// TF - call custom method
					ghast.playSound(ghast.getFireSound(), 10.0F, ghast.getVoicePitch());
					spitFireball();
					prevAttackTimer = attackTimer;
				}
				attackTimer = -40;
			}
		} else if (attackTimer > 0) {
			prevAttackTimer = attackTimer;
			--attackTimer;
		}

		ghast.setCharging(attackTimer > 10);
	}

	public void spitFireball() {
		Vec3 vec3d = ghast.getViewVector(1.0F);
		double sizeScale = ghast.getBbWidth() / 4.0D;
		double forward = 4.0D * sizeScale;
		double up = 0.5D * sizeScale;
		double d2 = ghast.getTarget().getX() - (ghast.getX() + vec3d.x() * forward);
		double d3 = ghast.getTarget().getBoundingBox().minY + ghast.getTarget().getBbHeight() / 2.0F - (up + ghast.getY() + ghast.getBbHeight() / 2.0F);
		double d4 = ghast.getTarget().getZ() - (ghast.getZ() + vec3d.z() * forward);
		LargeFireball fireball = new LargeFireball(ghast.level(), ghast, new Vec3(d2, d3, d4).normalize(), ghast.getExplosionPower());
		fireball.setPos(ghast.getX() + vec3d.x() * forward, ghast.getY() + ghast.getBbHeight() / 2.0F + up, ghast.getZ() + vec3d.z() * forward);
		ghast.level().addFreshEntity(fireball);

		// when we attack, there is a 1-in-6 chance we decide to stop attacking
		if (ghast.getRandom().nextInt(6) == 0) {
			ghast.setTarget(null);
		}
	}
}
