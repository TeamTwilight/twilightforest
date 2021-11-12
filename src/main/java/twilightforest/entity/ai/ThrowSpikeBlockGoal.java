package twilightforest.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.monster.BlockChainGoblin;
import twilightforest.entity.SpikeBlock;

import java.util.EnumSet;

public class ThrowSpikeBlockGoal extends Goal {

	protected BlockChainGoblin attacker;
	protected SpikeBlock spikeBlock;

	public ThrowSpikeBlockGoal(BlockChainGoblin entityTFBlockGoblin, SpikeBlock entitySpikeBlock) {
		this.attacker = entityTFBlockGoblin;
		this.spikeBlock = entitySpikeBlock;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity target = this.attacker.getTarget();
		if (target == null || this.attacker.distanceToSqr(target) > 42) {
			return false;
		} else {
			return this.attacker.isAlive() && this.attacker.hasLineOfSight(target) && this.attacker.level.random.nextInt(56) == 0;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return this.attacker.getChainMoveLength() > 0;
	}

	@Override
	public void start() {
		this.attacker.setThrowing(true);
	}
}
