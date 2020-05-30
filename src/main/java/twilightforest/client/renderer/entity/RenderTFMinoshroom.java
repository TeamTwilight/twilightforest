package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFMinoshroom;
import twilightforest.entity.boss.EntityTFMinoshroom;

public class RenderTFMinoshroom<T extends EntityTFMinoshroom, M extends ModelTFMinoshroom<T>> extends BipedRenderer<T, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("minoshroomtaur.png");

	public RenderTFMinoshroom(EntityRendererManager manager, M model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new LayerMinoshroomMushroom(this));
	}

	// TODO fix offsets (currently copypastedfrom LayerMooshroomMushroom)
	class LayerMinoshroomMushroom extends LayerRenderer<T, M> {

		public LayerMinoshroomMushroom(IEntityRenderer<T, M> renderer) {
			super(renderer);
		}

		@Override
		public void render(MatrixStack stack, IRenderTypeBuffer buffer, int i, T minoshroom, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!minoshroom.isChild() && !minoshroom.isInvisible()) {
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
//				RenderTFMinoshroom.this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
				RenderSystem.enableCull();
//				RenderSystem.cullFace(GlStateManager.CullFace.FRONT);
				stack.push();
				stack.scale(1.0F, -1.0F, 1.0F);
				stack.translate(0.2F, 0.35F, 0.5F);
				RenderSystem.rotatef(42.0F, 0.0F, 1.0F, 0.0F);
				stack.push();
				stack.translate(-0.5F, -0.5F, 0.5F);
//				blockrendererdispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
				stack.pop();
				stack.push();
				stack.translate(0.1F, 0.0F, -0.6F);
				RenderSystem.rotatef(42.0F, 0.0F, 1.0F, 0.0F);
				stack.translate(-0.5F, -0.5F, 0.5F);
//				blockrendererdispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
				stack.pop();
				stack.pop();
				stack.push();
//				RenderTFMinoshroom.this.getEntityModel().bipedHead.postRender(0.0625F);
				stack.scale(1.0F, -1.0F, 1.0F);
				stack.translate(0.0F, 1.0F, 0.0F);
				RenderSystem.rotatef(12.0F, 0.0F, 1.0F, 0.0F);
				stack.translate(-0.5F, -0.5F, 0.5F);
//				blockrendererdispatcher.renderBlockBrightness(Blocks.RED_MUSHROOM.getDefaultState(), 1.0F);
				stack.pop();
//				RenderSystem.cullFace(GlStateManager.CullFace.BACK);
				RenderSystem.disableCull();
			}
		}

//		@Override
//		public boolean shouldCombineTextures() {
//			return false;
//		}
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
