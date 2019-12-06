package twilightforest.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import twilightforest.entity.EntityTFBlockGoblin;
import twilightforest.entity.EntityTFSpikeBlock;

public class EntityAIThrowSpikeBlock extends Goal {

	protected EntityTFBlockGoblin attacker;
	protected EntityTFSpikeBlock spikeBlock;

	public EntityAIThrowSpikeBlock(EntityTFBlockGoblin entityTFBlockGoblin, EntityTFSpikeBlock entitySpikeBlock) {
		this.attacker = entityTFBlockGoblin;
		this.spikeBlock = entitySpikeBlock;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		LivingEntity target = this.attacker.getAttackTarget();
		if (target == null || this.attacker.getDistanceSq(target) > 42) {
			return false;
		} else {
			return this.attacker.isAlive() && this.attacker.canEntityBeSeen(target) && this.attacker.world.rand.nextInt(56) == 0;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.attacker.getChainMoveLength() > 0;
	}

	@Override
	public void startExecuting() {
		this.attacker.setThrowing(true);
	}

}
