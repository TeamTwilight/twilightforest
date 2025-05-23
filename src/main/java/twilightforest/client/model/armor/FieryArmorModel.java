package twilightforest.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class FieryArmorModel<S extends HumanoidRenderState> extends HumanoidArmorModel<S> {

	public FieryArmorModel(ModelPart root) {
		super(root);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		super.renderToBuffer(stack, builder, 0xF000F0, overlay, color);
	}
}
