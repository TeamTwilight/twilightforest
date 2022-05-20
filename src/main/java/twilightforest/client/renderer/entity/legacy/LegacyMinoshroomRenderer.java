package twilightforest.client.renderer.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.legacy.MinoshroomLegacyModel;
import twilightforest.entity.boss.Minoshroom;

//old renderer had the head mushroom in a different spot - line is commented out
public class LegacyMinoshroomRenderer extends HumanoidMobRenderer<Minoshroom, MinoshroomLegacyModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("minoshroomtaur.png");

	public LegacyMinoshroomRenderer(EntityRendererProvider.Context manager, MinoshroomLegacyModel model, float shadowSize) {
		super(manager, model, shadowSize);
		this.addLayer(new LayerMinoshroomMushroom(this));
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer}
	 */
	static class LayerMinoshroomMushroom extends RenderLayer<Minoshroom, MinoshroomLegacyModel> {

		public LayerMinoshroomMushroom(RenderLayerParent<Minoshroom, MinoshroomLegacyModel> renderer) {
			super(renderer);
		}

		@Override
		public void render(PoseStack ms, MultiBufferSource buffers, int light, Minoshroom entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!entity.isBaby() && !entity.isInvisible()) {
				BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
				BlockState blockstate = Blocks.RED_MUSHROOM.defaultBlockState(); // TF: hardcode mushroom state
				int i = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
				ms.pushPose();
				ms.translate(0.2F, -0.35F, 0.5D);
				ms.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
				ms.scale(-1.0F, -1.0F, 1.0F);
				ms.translate(-0.5D, -0.5D, -0.5D);
				blockrendererdispatcher.renderSingleBlock(blockstate, ms, buffers, light, i);
				ms.popPose();
				ms.pushPose();
				ms.translate(0.2F, -0.35F, 0.5D);
				ms.mulPose(Vector3f.YP.rotationDegrees(42.0F));
				ms.translate(0.1F, 0.0D, -0.6F);
				ms.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
				ms.scale(-1.0F, -1.0F, 1.0F);
				ms.translate(-0.5D, -0.5D, -0.5D);
				blockrendererdispatcher.renderSingleBlock(blockstate, ms, buffers, light, i);
				ms.popPose();
				ms.pushPose();
				this.getParentModel().head.translateAndRotate(ms);
				// TF - adjust head shroom
				ms.translate(0.0D, -0.9, 0.05);
				ms.mulPose(Vector3f.YP.rotationDegrees(-78.0F));
				ms.scale(-1.0F, -1.0F, 1.0F);
				ms.translate(-0.5D, -0.5D, -0.5D);
				blockrendererdispatcher.renderSingleBlock(blockstate, ms, buffers, light, i);
				ms.popPose();
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(Minoshroom entity) {
		return textureLoc;
	}
}
