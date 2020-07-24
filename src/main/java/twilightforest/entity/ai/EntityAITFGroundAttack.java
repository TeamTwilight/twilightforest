package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import twilightforest.entity.boss.EntityTFMinoshroom;

import java.util.EnumSet;
import java.util.List;

public class EntityAITFGroundAttack extends Goal {
	private static final double MIN_RANGE_SQ = 2.0D;
	private static final double MAX_RANGE_SQ = 48.0D;
	private static final int FREQ = 24;

	private EntityTFMinoshroom attacker;
	private LivingEntity attackTarget;

	private int attackTick;

	public EntityAITFGroundAttack(EntityTFMinoshroom entityTFMinoshroom) {
		this.attacker = entityTFMinoshroom;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		this.attackTarget = this.attacker.getAttackTarget();

		if (this.attackTarget == null) {
			return false;
		} else {
			double distance = this.attacker.getDistanceSq(this.attackTarget);
			if (distance < MIN_RANGE_SQ || distance > MAX_RANGE_SQ) {
				return false;
			} else if (!this.attacker.isOnGround()) {
				return false;
			} else {

				if (this.attacker.canEntityBeSeen(attackTarget)) {
					return this.attacker.getRNG().nextInt(FREQ) == 0;
				} else {
					return this.attacker.getRNG().nextInt(FREQ - 4) == 0;
				}

			}

		}
	}

	@Override
	public void startExecuting() {
		attackTick = 30 + attacker.getRNG().nextInt(30);
		attacker.setMaxCharge(attackTick);
		attacker.setGroundAttackCharge(true);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.attackTick >= 0;
	}

	@Override
	public void resetTask() {
		this.attackTick = 0;
		this.attackTarget = null;
	}

	@Override
	public void tick() {
		// look where we're going
		this.attacker.getLookController().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
		this.attacker.getMoveHelper().action = MovementController.Action.WAIT;

		if (this.attackTick-- <= 0) {
			this.attacker.setGroundAttackCharge(false);
			this.attacker.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2, 1F + this.attacker.getRNG().nextFloat() * 0.1F);

			AxisAlignedBB selection = new AxisAlignedBB(this.attacker.func_233580_cy_().getX() - 7.5F, this.attacker.func_233580_cy_().getY(), this.attacker.func_233580_cy_().getZ() - 7.5F, this.attacker.func_233580_cy_().getX() + 7.5F, this.attacker.func_233580_cy_().getY() + 3.0F, this.attacker.func_233580_cy_().getZ() + 7.5F);

			List<Entity> hit = attacker.world.getEntitiesWithinAABB(Entity.class, selection);
			for (Entity entity : hit) {

				if (entity == this.attacker) {

					continue;

				}

				if (entity instanceof LivingEntity) {
					if (entity.isOnGround()) {
						entity.addVelocity(0, 0.23, 0);

						entity.attackEntityFrom(DamageSource.causeMobDamage(this.attacker).setDamageBypassesArmor(), (float) (this.attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 0.5F));
					}
				}
			}

		}
	}
}
