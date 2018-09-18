package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFKingSpider;

public class RenderTFKingSpider extends RenderSpider<EntityTFKingSpider> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "kingspider.png");

	public RenderTFKingSpider(RenderManager manager) {
		super(manager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFKingSpider entity) {
		return textureLoc;
	}

	@Override
	protected void preRenderCallback(EntityTFKingSpider entity, float partialTicks) {
		float scale = 1.9F;
		GlStateManager.scale(scale, scale, scale);
	}
}
