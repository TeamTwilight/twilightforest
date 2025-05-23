package twilightforest.client.model.entity;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;

public class MistWolfModel extends HostileWolfModel {

	public MistWolfModel(ModelPart root) {
		super(RenderType::entityTranslucent, root);
	}
}
