package twilightforest.client.model.entity;

import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.geom.ModelPart;
import twilightforest.entity.monster.RisingZombie;

public class RisingZombieModel extends AbstractZombieModel<RisingZombie> {

	public RisingZombieModel(ModelPart part) {
		super(part);
	}

	@Override
	public boolean isAggressive(RisingZombie entity) {
		return entity.isAggressive();
	}

	@Override
	public void setupAnim(RisingZombie entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.leftLeg.visible = this.rightLeg.visible = entity.getRisingTicks() > 40;
	}
}
