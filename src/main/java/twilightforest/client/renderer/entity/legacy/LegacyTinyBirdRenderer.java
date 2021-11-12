package twilightforest.client.renderer.entity.legacy;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.TinyBirdModel;
import twilightforest.client.model.entity.legacy.TinyBirdLegacyModel;
import twilightforest.client.renderer.entity.BirdRenderer;
import twilightforest.entity.passive.TinyBird;

public class LegacyTinyBirdRenderer extends BirdRenderer<TinyBird, TinyBirdLegacyModel> {

	private static final ResourceLocation textureLocSparrow  = TwilightForestMod.getModelTexture("tinybirdbrown.png");
	private static final ResourceLocation textureLocFinch    = TwilightForestMod.getModelTexture("tinybirdgold.png");
	private static final ResourceLocation textureLocCardinal = TwilightForestMod.getModelTexture("tinybirdred.png");
	private static final ResourceLocation textureLocBluebird = TwilightForestMod.getModelTexture("tinybirdblue.png");

	public LegacyTinyBirdRenderer(EntityRendererProvider.Context manager, TinyBirdLegacyModel model, float shadowSize) {
		super(manager, model, shadowSize, "");
	}

	@Override
	public ResourceLocation getTextureLocation(TinyBird entity) {
		switch (entity.getBirdType()) {
			default:
			case 0:
				return textureLocSparrow;
			case 1:
				return textureLocBluebird;
			case 2:
				return textureLocCardinal;
			case 3:
				return textureLocFinch;
		}
	}
}
