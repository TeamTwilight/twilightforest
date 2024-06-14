package twilightforest.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

public class FieryArmorModel extends TFArmorModel {

	public FieryArmorModel(ModelPart part) {
		super(part);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float scale) {
		super.renderToBuffer(stack, consumer, 0xF000F0, overlay, red, green, blue, scale);
	}
}
