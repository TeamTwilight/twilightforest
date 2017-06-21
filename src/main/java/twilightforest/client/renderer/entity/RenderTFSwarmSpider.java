package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFSwarmSpider;

public class RenderTFSwarmSpider extends RenderSpider<EntityTFSwarmSpider> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "swarmspider.png");

	public RenderTFSwarmSpider(RenderManager manager) {
		super(manager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFSwarmSpider entity) {
		return textureLoc;
	}

	@Override
	protected void preRenderCallback(EntityTFSwarmSpider par1EntityLivingBase, float par2) {
		float scale = 0.5F;
		GlStateManager.scale(scale, scale, scale);
	}
}
