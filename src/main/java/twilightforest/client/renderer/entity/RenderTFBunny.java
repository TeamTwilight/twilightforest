package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.EntityTFBunny;

public class RenderTFBunny extends RenderLiving<EntityTFBunny> {

	private final ResourceLocation textureLocDutch = TwilightForestMod.getModelTexture("bunnydutch.png");
	private final ResourceLocation textureLocWhite = TwilightForestMod.getModelTexture("bunnywhite.png");
	private final ResourceLocation textureLocBrown = TwilightForestMod.getModelTexture("bunnybrown.png");

	public RenderTFBunny(RenderManager manager, ModelBase model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFBunny entity) {
		switch (entity.getBunnyType()) {
			default:
			case 0:
			case 1:
				return textureLocDutch;
			case 2:
				return textureLocWhite;
			case 3:
				return textureLocBrown;
		}
	}
}
