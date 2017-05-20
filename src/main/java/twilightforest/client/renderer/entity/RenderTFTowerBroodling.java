package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.util.ResourceLocation;

import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFTowerBroodling;

public class RenderTFTowerBroodling extends RenderSpider<EntityTFTowerBroodling> {

    private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "towerbroodling.png");

    public RenderTFTowerBroodling(RenderManager manager) {
        super(manager);
    }

    @Override
	protected ResourceLocation getEntityTexture(EntityTFTowerBroodling entity) {
		return textureLoc;
	}

    @Override
    protected void preRenderCallback(EntityTFTowerBroodling entity, float partialTicks)
    {
    	float scale = 0.7F;
        GlStateManager.scale(scale, scale, scale);
    }
}
