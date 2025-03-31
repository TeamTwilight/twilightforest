package twilightforest.client.model.entity;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import twilightforest.client.state.RisingZombieRenderState;

public class RisingZombieModel extends ZombieModel<RisingZombieRenderState> {

	public RisingZombieModel(ModelPart part) {
		super(part);
	}

	@Override
	public void setupAnim(RisingZombieRenderState state) {
		super.setupAnim(state);
		this.leftLeg.visible = this.rightLeg.visible = state.risingTicks > 40;
	}
}
