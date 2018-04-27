package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.entity.Entity;


public class ModelTFSwarmSpider extends ModelSpider {

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5 * 0.5f);
	}
}
