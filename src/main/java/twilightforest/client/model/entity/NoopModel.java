package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class NoopModel<T extends HumanoidRenderState> extends HumanoidModel<T> {

	public NoopModel(ModelPart part) {
		super(part);
	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffers, int light, int overlay, int color) {
	}
}
