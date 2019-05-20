package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.entity.Entity;


public class ModelTFSwarmSpider extends ModelSpider {

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale * 0.5f);
	}
}
