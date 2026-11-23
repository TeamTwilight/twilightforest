package twilightforest.client.model.entity;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import twilightforest.client.state.entity.RisingZombieRenderState;

public class RisingZombieModel extends ZombieModel<RisingZombieRenderState> {

	public RisingZombieModel(ModelPart part) {
		super(part);
	}

	@Override
	public void setupAnim(RisingZombieRenderState entity) {
		super.setupAnim(entity);
		this.leftLeg.visible = this.rightLeg.visible = entity.risingTicks > 40;
	}
}
