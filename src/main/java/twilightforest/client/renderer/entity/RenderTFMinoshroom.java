package twilightforest.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFMinoshroom;

public class RenderTFMinoshroom extends RenderBiped<EntityTFMinoshroom> {

	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "minoshroomtaur.png");

	public RenderTFMinoshroom(RenderManager manager, ModelBiped model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new LayerMinoshroomMushroom());
	}

	// TODO fix offsets (currently copypastedfrom LayerMooshroomMushroom)
	class LayerMinoshroomMushroom implements LayerRenderer<EntityTFMinoshroom> {
		@Override
		public void doRenderLayer(EntityTFMinoshroom entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			if (!entitylivingbaseIn.isChild() && !entitylivingbaseIn.isInvisible()) {
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
				RenderTFMinoshroom.this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				GlStateManager.enableCull();
				GlStateManager.cullFace(GlStateManager.CullFace.FRONT);
				GlStateManager.pushMatrix();
				GlStateManager.scale(1.0F, -1.0F, 1.0F);
				GlStateManager.translate(0.2F, 0.35F, 0.5F);
				GlStateManager.rotate(42.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.5F, -0.5F, 0.5F);
				blockrendererdispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.1F, 0.0F, -0.6F);
				GlStateManager.rotate(42.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(-0.5F, -0.5F, 0.5F);
				blockrendererdispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
				GlStateManager.popMatrix();
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				((ModelBiped) RenderTFMinoshroom.this.getMainModel()).bipedHead.postRender(0.0625F);
				GlStateManager.scale(1.0F, -1.0F, 1.0F);
				GlStateManager.translate(0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(12.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(-0.5F, -0.5F, 0.5F);
				blockrendererdispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
				GlStateManager.popMatrix();
				GlStateManager.cullFace(GlStateManager.CullFace.BACK);
				GlStateManager.disableCull();
			}
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFMinoshroom par1Entity) {
		return textureLoc;
	}

}
