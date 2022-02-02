package twilightforest.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.monster.BlockChainGoblin;
import twilightforest.entity.SpikeBlock;

import java.util.EnumSet;

public class ThrowSpikeBlockGoal extends Goal {

	protected BlockChainGoblin attacker;
	protected SpikeBlock spikeBlock;
	private int cooldown;

	public ThrowSpikeBlockGoal(BlockChainGoblin entityTFBlockGoblin, SpikeBlock entitySpikeBlock) {
		this.attacker = entityTFBlockGoblin;
		this.spikeBlock = entitySpikeBlock;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		this.cooldown = Math.max(0, this.cooldown - 1);
		LivingEntity target = this.attacker.getTarget();
		if (target == null || this.attacker.distanceToSqr(target) > 42 || this.cooldown > 0) {
			return false;
		} else {

			return this.attacker.isAlive() && this.attacker.hasLineOfSight(target) && this.cooldown <= 0;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return this.attacker.getChainMoveLength() > 0;
	}

	@Override
	public void start() {
		this.attacker.setThrowing(true);
		this.cooldown = 100 + this.attacker.level.random.nextInt(100);
	}
}
