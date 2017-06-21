package twilightforest.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import twilightforest.entity.EntityTFGoblinKnightUpper;

public class EntityAITFHeavySpearAttack extends EntityAIBase {

	private EntityTFGoblinKnightUpper entity;

	public EntityAITFHeavySpearAttack(EntityTFGoblinKnightUpper par1EntityCreature) {
		this.entity = par1EntityCreature;
		this.setMutexBits(3); // Prevent moving
	}

	@Override
	public void updateTask() {
		if (entity.heavySpearTimer == 25) {
			entity.landHeavySpearAttack();
		}
	}

	@Override
	public boolean shouldExecute() {
		return entity.heavySpearTimer > 0 && entity.heavySpearTimer < EntityTFGoblinKnightUpper.HEAVY_SPEAR_TIMER_START;
	}

}
