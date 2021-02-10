package twilightforest.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;

public class ModelNoop<T extends LivingEntity> extends BipedModel<T> {

	public ModelNoop() {
		super(0.0F);
	}

	@Override
	public void render(MatrixStack ms, IVertexBuilder buffers, int light, int overlay, float r, float g, float b, float a) {
	}
}
