package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;

public class RenderTFYeti extends RenderTFBiped<EntityLiving> {
	public RenderTFYeti(RenderManager manager, ModelBiped modelBiped, float scale, String textureName) {
		super(manager, modelBiped, scale, textureName);
	}

	@Override
	protected void preRenderCallback(EntityLiving living, float par2) {
		float scale = 1.0F;
		GlStateManager.scale(scale, scale, scale);
	}
}
