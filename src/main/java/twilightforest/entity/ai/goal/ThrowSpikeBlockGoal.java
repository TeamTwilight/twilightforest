package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.entity.SpikeBlock;
import twilightforest.entity.monster.BlockChainGoblin;

import java.util.EnumSet;

public class ThrowSpikeBlockGoal extends Goal {

	protected final BlockChainGoblin attacker;
	protected final SpikeBlock spikeBlock;
	private int cooldown;

	public ThrowSpikeBlockGoal(BlockChainGoblin goblin, SpikeBlock block) {
		this.attacker = goblin;
		this.spikeBlock = block;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity target = this.attacker.getTarget();
		if (target == null || this.attacker.distanceToSqr(target) > 42 || this.cooldown > 0) {
			this.cooldown--;
			return false;
		} else {
			return this.attacker.isAlive() && this.attacker.hasLineOfSight(target) && this.attacker.level().getRandom().nextInt(56) == 0;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return this.attacker.getChainMoveLength() > 0;
	}

	@Override
	public void start() {
		this.attacker.setThrowing(true);
		this.cooldown = 100 + this.attacker.level().getRandom().nextInt(100);
		this.attacker.gameEvent(GameEvent.PROJECTILE_SHOOT);
	}
}
