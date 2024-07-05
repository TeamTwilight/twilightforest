package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.LargeFireball;
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
		return this.ghast.getTarget() != null && this.ghast.shouldAttack(this.ghast.getTarget());
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
			this.prevAttackTimer = attackTimer;
			++this.attackTimer;

			// TF face our target at all times
			this.ghast.getLookControl().setLookAt(target, 10.0F, this.ghast.getMaxHeadXRot());

			if (this.attackTimer == 10) {
				this.ghast.playSound(this.ghast.getWarnSound(), 10.0F, ghast.getVoicePitch());
			}

			if (this.attackTimer == 20) {
				if (this.ghast.shouldAttack(target)) {
					// TF - call custom method
					this.ghast.playSound(this.ghast.getFireSound(), 10.0F, this.ghast.getVoicePitch());
					this.spitFireball();
					this.prevAttackTimer = attackTimer;
				}
				this.attackTimer = -40;
			}
		} else if (this.attackTimer > 0) {
			this.prevAttackTimer = attackTimer;
			--this.attackTimer;
		}

		this.ghast.setCharging(this.attackTimer > 10);
	}

	public void spitFireball() {
		Vec3 vec3d = this.ghast.getViewVector(1.0F);
		double d2 = this.ghast.getTarget().getX() - (this.ghast.getX() + vec3d.x() * 4.0D);
		double d3 = this.ghast.getTarget().getBoundingBox().minY + this.ghast.getTarget().getBbHeight() / 2.0F - (0.5D + this.ghast.getY() + this.ghast.getBbHeight() / 2.0F);
		double d4 = this.ghast.getTarget().getZ() - (this.ghast.getZ() + vec3d.z() * 4.0D);
		LargeFireball fireball = new LargeFireball(this.ghast.level(), this.ghast, new Vec3(d2, d3, d4).normalize(), this.ghast.getExplosionPower());
		fireball.setPos(this.ghast.getX() + vec3d.x() * 4.0D, this.ghast.getY() + this.ghast.getBbHeight() / 2.0F + 0.5D, this.ghast.getZ() + vec3d.z() * 4.0D);
		this.ghast.level().addFreshEntity(fireball);

		// when we attack, there is a 1-in-6 chance we decide to stop attacking
		if (this.ghast.getRandom().nextInt(6) == 0) {
			this.ghast.setTarget(null);
		}
	}
}