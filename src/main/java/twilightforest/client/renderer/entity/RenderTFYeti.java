package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import twilightforest.entity.EntityTFYeti;

public class RenderTFYeti<T extends EntityTFYeti, M extends BipedModel<T>> extends RenderTFBiped<T, M> {
	public RenderTFYeti(EntityRendererManager manager, M modelBiped, float shadowSize, String textureName) {
		super(manager, modelBiped, shadowSize, textureName);
	}

	@Override
	protected void scale(T living, MatrixStack stack, float partialTicks) {
		float scale = 1.0F;
		stack.scale(scale, scale, scale);
	}
}
