package twilightforest.entity.ai;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import twilightforest.TFSounds;
import twilightforest.entity.boss.Minoshroom;

import java.util.EnumSet;
import java.util.List;

public class GroundAttackGoal extends Goal {
	private static final double MIN_RANGE_SQ = 2.0D;
	private static final double MAX_RANGE_SQ = 48.0D;
	private static final int FREQ = 24;

	private final Minoshroom attacker;
	private LivingEntity attackTarget;

	private int attackTick;

	public GroundAttackGoal(Minoshroom entityTFMinoshroom) {
		this.attacker = entityTFMinoshroom;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		this.attackTarget = this.attacker.getTarget();

		if (this.attackTarget == null) {
			return false;
		} else {
			double distance = this.attacker.distanceToSqr(this.attackTarget);
			if (distance < MIN_RANGE_SQ || distance > MAX_RANGE_SQ) {
				return false;
			} else if (!this.attacker.isOnGround()) {
				return false;
			} else {

				if (this.attacker.hasLineOfSight(attackTarget)) {
					return this.attacker.getRandom().nextInt(FREQ) == 0;
				} else {
					return this.attacker.getRandom().nextInt(FREQ - 4) == 0;
				}

			}

		}
	}

	@Override
	public void start() {
		attackTick = 30 + attacker.getRandom().nextInt(30);
		attacker.setMaxCharge(attackTick);
		attacker.setGroundAttackCharge(true);
	}

	@Override
	public boolean canContinueToUse() {
		return this.attackTick >= 0;
	}

	@Override
	public void stop() {
		this.attackTick = 0;
		this.attackTarget = null;
	}

	@Override
	public void tick() {
		// look where we're going
		this.attacker.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
		this.attacker.getMoveControl().operation = MoveControl.Operation.WAIT;

		if (this.attackTick-- <= 0) {
			this.attacker.setGroundAttackCharge(false);
			this.attacker.playSound(TFSounds.MINOSHROOM_SLAM, 2, 1F + this.attacker.getRandom().nextFloat() * 0.1F);

			AABB selection = new AABB(this.attacker.blockPosition().getX() - 7.5F, this.attacker.blockPosition().getY(), this.attacker.blockPosition().getZ() - 7.5F, this.attacker.blockPosition().getX() + 7.5F, this.attacker.blockPosition().getY() + 3.0F, this.attacker.blockPosition().getZ() + 7.5F);

			List<Entity> hit = attacker.level.getEntitiesOfClass(Entity.class, selection);
			for (Entity entity : hit) {

				if (entity == this.attacker) {

					continue;

				}

				if (entity instanceof LivingEntity) {
					if (entity.isOnGround()) {
						entity.push(0, 0.23, 0);

						entity.hurt(DamageSource.mobAttack(this.attacker).bypassArmor(), (float) (this.attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 0.5F));
					}
				}
			}

		}
	}
}
