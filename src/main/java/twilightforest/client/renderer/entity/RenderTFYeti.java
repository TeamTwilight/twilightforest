package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public class RenderTFYeti extends RenderTFBiped {
	public RenderTFYeti(RenderManager manager, ModelBiped modelBiped, float scale, String textureName) {
		super(manager, modelBiped, scale, textureName);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase living, float par2) {
		float scale = 1.0F;
		GlStateManager.scale(scale, scale, scale);
	}
}
