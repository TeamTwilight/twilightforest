package twilightforest.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.LightCoordsUtil;

public class FieryArmorModel extends TFArmorModel {

	public FieryArmorModel(ModelPart part) {
		super(part);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		super.renderToBuffer(stack, builder, LightCoordsUtil.FULL_BRIGHT, overlay, color);
	}
}
