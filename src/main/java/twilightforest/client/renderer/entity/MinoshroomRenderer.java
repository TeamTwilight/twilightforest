package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MinoshroomModel;
import twilightforest.client.state.MinoshroomRenderState;
import twilightforest.entity.boss.Minoshroom;

public class MinoshroomRenderer extends HumanoidMobRenderer<Minoshroom, MinoshroomRenderState, MinoshroomModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("minoshroomtaur.png");

	public MinoshroomRenderer(EntityRendererProvider.Context context) {
		super(context, new MinoshroomModel(context.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F);
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
	}

	@Override
	public ResourceLocation getTextureLocation(MinoshroomRenderState state) {
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
		public void render(PoseStack stack, MultiBufferSource source, int light, MinoshroomRenderState state, float netHeadYaw, float headPitch) {
			if (!state.isBaby) {
				boolean flag = state.appearsGlowing && state.isInvisible;
				if (!state.isInvisible || flag) {
					BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
					BlockState blockstate = Blocks.RED_MUSHROOM.defaultBlockState(); // TF: hardcode mushroom state
					int i = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
					float yOffs = -0.65F;
					float zOffs = 0.25F;
					stack.pushPose();
					this.getParentModel().cowTorso.translateAndRotate(stack);
					stack.mulPose(Axis.XP.rotationDegrees(-90.0F));
					stack.translate(0.2F, yOffs, zOffs);
					stack.mulPose(Axis.YP.rotationDegrees(-48.0F));
					stack.scale(-1.0F, -1.0F, 1.0F);
					stack.translate(-0.5D, -0.5D, -0.5D);
					blockrendererdispatcher.renderSingleBlock(blockstate, stack, source, light, i);
					stack.popPose();
					stack.pushPose();
					this.getParentModel().cowTorso.translateAndRotate(stack);
					stack.mulPose(Axis.XP.rotationDegrees(-90.0F));
					stack.translate(0.2F, yOffs, zOffs + 0.5D);
					stack.mulPose(Axis.YP.rotationDegrees(42.0F));
					stack.translate(0.35F, 0.0D, -0.9F);
					stack.mulPose(Axis.YP.rotationDegrees(-48.0F));
					stack.scale(-1.0F, -1.0F, 1.0F);
					stack.translate(-0.5D, -0.5D, -0.5D);
					blockrendererdispatcher.renderSingleBlock(blockstate, stack, source, light, i);
					stack.popPose();
					stack.pushPose();
					this.getParentModel().head.translateAndRotate(stack);
					// TF - adjust head shroom
					stack.translate(0.0D, -0.9D, 0.05D);
					stack.mulPose(Axis.YP.rotationDegrees(-78.0F));
					stack.scale(-1.0F, -1.0F, 1.0F);
					stack.translate(-0.5D, -0.5D, -0.5D);
					blockrendererdispatcher.renderSingleBlock(blockstate, stack, source, light, i);
					stack.popPose();
				}
			}
		}
	}
}
