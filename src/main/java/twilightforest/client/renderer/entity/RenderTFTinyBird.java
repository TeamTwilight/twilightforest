package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.EntityTFBird;
import twilightforest.entity.passive.EntityTFTinyBird;

public class RenderTFTinyBird extends RenderTFBird {

	private static final ResourceLocation textureLocSparrow  = TwilightForestMod.getModelTexture("tinybirdbrown.png");
	private static final ResourceLocation textureLocFinch    = TwilightForestMod.getModelTexture("tinybirdgold.png");
	private static final ResourceLocation textureLocCardinal = TwilightForestMod.getModelTexture("tinybirdred.png");
	private static final ResourceLocation textureLocBluebird = TwilightForestMod.getModelTexture("tinybirdblue.png");

	public RenderTFTinyBird(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize, "");
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFBird entity) {
		if (entity instanceof EntityTFTinyBird) {
			switch (((EntityTFTinyBird) entity).getBirdType()) {
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

		// fallback
		return textureLocSparrow;
	}
}
