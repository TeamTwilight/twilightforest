package twilightforest.client.renderer.entity.newmodels;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.client.model.entity.newmodels.NewTinyBirdModel;
import twilightforest.client.renderer.entity.BirdRenderer;
import twilightforest.entity.passive.TinyBird;

public class NewTinyBirdRenderer extends BirdRenderer<TinyBird, NewTinyBirdModel> {

	public NewTinyBirdRenderer(EntityRendererProvider.Context manager, NewTinyBirdModel model, float shadowSize) {
		super(manager, model, shadowSize, "");
	}

	@Override
	public ResourceLocation getTextureLocation(TinyBird entity) {
		return entity.getVariant().texture();
	}
}
