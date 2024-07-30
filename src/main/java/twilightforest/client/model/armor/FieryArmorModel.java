package twilightforest.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;

public class FieryArmorModel extends TFArmorModel {

	public FieryArmorModel(ModelPart part) {
		super(part);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		super.renderToBuffer(stack, builder, 0xF000F0, overlay, 0xFFFFFFFF);
	}

}
