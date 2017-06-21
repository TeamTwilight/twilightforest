package twilightforest.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import twilightforest.entity.EntityTFGoblinKnightLower;
import twilightforest.entity.EntityTFGoblinKnightUpper;

public class EntityAITFRiderSpearAttack extends EntityAIBase {

	private EntityTFGoblinKnightLower entity;

	public EntityAITFRiderSpearAttack(EntityTFGoblinKnightLower par1EntityCreature) {
		this.entity = par1EntityCreature;
		this.setMutexBits(3); // Prevent moving
	}

	@Override
	public boolean shouldExecute() {
		if (!this.entity.getPassengers().isEmpty() && this.entity.getPassengers().get(0) instanceof EntityTFGoblinKnightUpper) {
			int timer = ((EntityTFGoblinKnightUpper) this.entity.getPassengers().get(0)).heavySpearTimer;
			return timer > 0 && timer < EntityTFGoblinKnightUpper.HEAVY_SPEAR_TIMER_START;
		} else {
			return false;
		}
	}

}
