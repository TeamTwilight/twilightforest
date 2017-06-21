package twilightforest.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import twilightforest.entity.passive.EntityTFTinyBird;

public class EntityAITFBirdFly extends EntityAIBase {

	private EntityTFTinyBird entity;

	public EntityAITFBirdFly(EntityTFTinyBird par1EntityCreature) {
		this.entity = par1EntityCreature;
		this.setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		return !entity.isBirdLanded();
	}
}
