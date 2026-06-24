package twilightforest.client.model.entity;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import twilightforest.client.state.entity.MistWolfRenderState;


public class MistWolfModel extends HostileWolfModel<MistWolfRenderState> {

	public MistWolfModel(ModelPart root) {
		super(RenderTypes::entityTranslucent, root);
	}
}

