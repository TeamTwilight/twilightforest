package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.util.ResourceLocation;
import twilightforest.entity.EntityTFGiantMiner;

public class RenderTFGiant extends RenderBiped<EntityTFGiantMiner> {
	public RenderTFGiant(RenderManager manager) {
		super(manager, new ModelBiped(), 0.625F);
	}

    @Override
	protected ResourceLocation getEntityTexture(EntityTFGiantMiner entity)
    {
		return Minecraft.getMinecraft().player.getLocationSkin();
    }
    
    @Override
	protected void preRenderCallback(EntityTFGiantMiner entity, float partialTicks)
    {
    	float scale = 4.0F;
        GlStateManager.scale(scale, scale, scale);
    }
}
