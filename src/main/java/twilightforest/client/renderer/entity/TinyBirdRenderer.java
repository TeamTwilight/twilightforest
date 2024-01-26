package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.client.model.entity.TinyBirdModel;
import twilightforest.entity.passive.TinyBird;

public class TinyBirdRenderer extends BirdRenderer<TinyBird, TinyBirdModel> {

	public TinyBirdRenderer(EntityRendererProvider.Context manager, TinyBirdModel model, float shadowSize) {
		super(manager, model, shadowSize, "");
	}

	@Override
	public ResourceLocation getTextureLocation(TinyBird entity) {
		return entity.getVariant().texture();
	}
}
