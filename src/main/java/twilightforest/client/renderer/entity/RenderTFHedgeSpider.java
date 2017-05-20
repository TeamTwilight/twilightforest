package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFHedgeSpider;

public class RenderTFHedgeSpider extends RenderSpider<EntityTFHedgeSpider> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "hedgespider.png");

	public RenderTFHedgeSpider(RenderManager manager) {
		super(manager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFHedgeSpider entity) {
		return textureLoc;
	}

}
