package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MinoshroomModel;
import twilightforest.client.state.entity.MinoshroomRenderState;
import twilightforest.entity.boss.Minoshroom;

public class MinoshroomRenderer extends HumanoidMobRenderer<Minoshroom, MinoshroomRenderState, MinoshroomModel> {
	public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("minoshroomtaur.png");
	private final BlockModelResolver blockModelResolver;

	public MinoshroomRenderer(EntityRendererProvider.Context context) {
		super(context, new MinoshroomModel(context.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F);
		this.blockModelResolver = context.getBlockModelResolver();
		this.addLayer(new MinoshroomMushroomLayer(this));
	}

	@Override
	public MinoshroomRenderState createRenderState() {
		return new MinoshroomRenderState();
	}

	@Override
	public void extractRenderState(Minoshroom entity, MinoshroomRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.chargeAnim = Mth.lerp(state.partialTick, entity.prevClientSideChargeAnimation, entity.clientSideChargeAnimation) / 6.0F;
		this.blockModelResolver.update(state.mushroomModel, Blocks.BROWN_MUSHROOM.defaultBlockState(), BLOCK_DISPLAY_CONTEXT);

	}

	@Override
	public Identifier getTextureLocation(MinoshroomRenderState state) {
		return TEXTURE;
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer}
	 */
	static class MinoshroomMushroomLayer extends RenderLayer<MinoshroomRenderState, MinoshroomModel> {

		public MinoshroomMushroomLayer(RenderLayerParent<MinoshroomRenderState, MinoshroomModel> renderer) {
			super(renderer);
		}

		@Override
		public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, MinoshroomRenderState renderState, float v, float v1) {
			if (!renderState.isBaby && !renderState.mushroomModel.isEmpty()) {
				boolean appearsGlowingWithInvisibility = renderState.appearsGlowing() && renderState.isInvisible;
				if (!renderState.isInvisible || appearsGlowingWithInvisibility) {
					int overlayCoords = LivingEntityRenderer.getOverlayCoords(renderState, 0.0F);
					float yOffs = -0.65F;
					float zOffs = 0.25F;
					poseStack.pushPose();
					this.getParentModel().cowTorso.translateAndRotate(poseStack);
					poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
					poseStack.translate(0.2F, yOffs, zOffs);
					poseStack.mulPose(Axis.YP.rotationDegrees(-48.0F));
					poseStack.scale(-1.0F, -1.0F, 1.0F);
					poseStack.translate(-0.5D, -0.5D, -0.5D);
					this.submitMushroomBlock(
						poseStack, submitNodeCollector, renderState.lightCoords, appearsGlowingWithInvisibility, renderState.outlineColor, renderState.mushroomModel, overlayCoords
					);
					poseStack.popPose();
					poseStack.pushPose();
					this.getParentModel().cowTorso.translateAndRotate(poseStack);
					poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
					poseStack.translate(0.2F, yOffs, zOffs + 0.5D);
					poseStack.mulPose(Axis.YP.rotationDegrees(42.0F));
					poseStack.translate(0.35F, 0.0D, -0.9F);
					poseStack.mulPose(Axis.YP.rotationDegrees(-48.0F));
					poseStack.scale(-1.0F, -1.0F, 1.0F);
					poseStack.translate(-0.5D, -0.5D, -0.5D);
					this.submitMushroomBlock(
						poseStack, submitNodeCollector, renderState.lightCoords, appearsGlowingWithInvisibility, renderState.outlineColor, renderState.mushroomModel, overlayCoords
					);
					poseStack.popPose();
					poseStack.pushPose();
					this.getParentModel().head.translateAndRotate(poseStack);
					// TF - adjust head shroom
					poseStack.translate(0.0D, -0.9D, 0.05D);
					poseStack.mulPose(Axis.YP.rotationDegrees(-78.0F));
					poseStack.scale(-1.0F, -1.0F, 1.0F);
					poseStack.translate(-0.5D, -0.5D, -0.5D);
					this.submitMushroomBlock(
						poseStack, submitNodeCollector, renderState.lightCoords, appearsGlowingWithInvisibility, renderState.outlineColor, renderState.mushroomModel, overlayCoords
					);
					poseStack.popPose();
				}
			}
		}

		private void submitMushroomBlock(
			PoseStack poseStack,
			SubmitNodeCollector submitNodeCollector,
			int lightCoords,
			boolean appearsGlowingWithInvisibility,
			int outlineColor,
			BlockModelRenderState mushroomModel,
			int overlayCoords
		) {
			if (appearsGlowingWithInvisibility) {
				mushroomModel.submitOnlyOutline(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
			} else {
				mushroomModel.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
			}
		}
	}
}
