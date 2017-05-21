package twilightforest.client.renderer.entity;


import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFWraith;


public class RenderTFWraith extends RenderBiped<EntityTFWraith> {
    private static final ResourceLocation textureWraith = new ResourceLocation(TwilightForestMod.MODEL_DIR + "ghost.png");
    private static final ResourceLocation textureCrown = new ResourceLocation(TwilightForestMod.MODEL_DIR + "ghost-crown.png");

	public RenderTFWraith(RenderManager manager, ModelBiped modelbiped, float shadowSize) {
		super(manager, modelbiped, shadowSize);
		addLayer(new LayerWraith());
	}

    @Override
	protected ResourceLocation getEntityTexture(EntityTFWraith par1Entity)
    {
        return textureCrown;
    }

    class LayerWraith implements LayerRenderer<EntityTFWraith> {
		@Override
		public void doRenderLayer(EntityTFWraith entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			RenderTFWraith.this.bindTexture(textureWraith);
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
			//GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			//GlStateManager.blendFunc(GL11.GL_ONE_MINUS_DST_ALPHA, GL11.GL_DST_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5f);
			RenderTFWraith.this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			// TODO fix gl state?
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}
}
