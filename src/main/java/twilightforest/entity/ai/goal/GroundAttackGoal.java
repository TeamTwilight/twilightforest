package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import twilightforest.entity.boss.Minoshroom;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFSounds;

import java.util.EnumSet;
import java.util.List;

public class GroundAttackGoal extends Goal {
	private static final double MIN_RANGE_SQ = 2.0D;
	private static final double MAX_RANGE_SQ = 9.0D;
	private static final int FREQ = 24;

	private final Minoshroom attacker;
	private LivingEntity attackTarget;
	private int cooldown;
	private int attackTick;

	public GroundAttackGoal(Minoshroom minoshroom) {
		this.attacker = minoshroom;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		this.attackTarget = this.attacker.getTarget();

		if (this.cooldown-- > 0 || this.attackTarget == null) {
			return false;
		} else {
			double distance = this.attacker.distanceToSqr(this.attackTarget);
			if (distance < MIN_RANGE_SQ || distance > MAX_RANGE_SQ) {
				return false;
			} else if (!this.attacker.onGround()) {
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
		this.cooldown = (20 * 10) + this.attacker.getRandom().nextInt(20 * 10); // 10 - 20 second cooldown
		this.attackTick = 30 + this.attacker.getRandom().nextInt(30);
		this.attacker.setMaxCharge(this.attackTick);
		this.attacker.setGroundAttackCharge(true);
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
		this.attacker.getLookControl().setLookAt(this.attackTarget, 30.0F, 30.0F);
		this.attacker.getMoveControl().operation = MoveControl.Operation.WAIT;

		if (this.attackTick-- <= 0) {
			this.attacker.setGroundAttackCharge(false);
			this.attacker.playSound(TFSounds.MINOSHROOM_SLAM.get(), 2.0F, 1.0F + this.attacker.getRandom().nextFloat() * 0.1F);
			this.attacker.gameEvent(GameEvent.HIT_GROUND);

			AABB selection = new AABB(this.attacker.blockPosition().getX() - 7.5F, this.attacker.blockPosition().getY(), this.attacker.blockPosition().getZ() - 7.5F, this.attacker.blockPosition().getX() + 7.5F, this.attacker.blockPosition().getY() + 3.0F, this.attacker.blockPosition().getZ() + 7.5F);

			List<Entity> hit = attacker.level().getEntitiesOfClass(Entity.class, selection, entity -> entity instanceof Player);
			for (Entity entity : hit) {
				if (entity.onGround()) {
					entity.push(0.0D, 0.23D, 0.0D);
					entity.hurtServer(getServerLevel(this.attacker), this.attacker.level().damageSources().source(TFDamageTypes.SLAM, this.attacker), (float) (this.attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5F));
				}
			}
		}
	}
}
