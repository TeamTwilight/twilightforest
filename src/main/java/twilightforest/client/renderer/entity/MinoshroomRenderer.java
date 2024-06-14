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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.client.model.entity.MinoshroomModel;
import twilightforest.entity.boss.Minoshroom;

public class MinoshroomRenderer<T extends Minoshroom, M extends MinoshroomModel<T>> extends HumanoidMobRenderer<T, M> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("minoshroomtaur.png");

	public MinoshroomRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
		super(context, model, shadowSize);
		this.addLayer(new MinoshroomMushroomLayer<>(this));
	}

	/**
	 * [VanillaCopy] {@link net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer}
	 */
	static class MinoshroomMushroomLayer<T extends Minoshroom, M extends MinoshroomModel<T>> extends RenderLayer<T, M> {

		public MinoshroomMushroomLayer(RenderLayerParent<T, M> renderer) {
			super(renderer);
		}

		@Override
		public void render(PoseStack ms, MultiBufferSource buffers, int light, Minoshroom entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			if (!entity.isBaby() && !entity.isInvisible()) {
				BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
				BlockState blockstate = Blocks.RED_MUSHROOM.defaultBlockState(); // TF: hardcode mushroom state
				int i = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
				float yOffs = JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? -0.95F : -0.65F;
				float zOffs = JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? 0.0F : 0.25F;
				ms.pushPose();
				this.getParentModel().cowTorso.translateAndRotate(ms);
				ms.mulPose(Axis.XP.rotationDegrees(-90.0F));
				ms.translate(0.2F, yOffs, zOffs);
				ms.mulPose(Axis.YP.rotationDegrees(-48.0F));
				ms.scale(-1.0F, -1.0F, 1.0F);
				ms.translate(-0.5D, -0.5D, -0.5D);
				blockrendererdispatcher.renderSingleBlock(blockstate, ms, buffers, light, i);
				ms.popPose();
				ms.pushPose();
				this.getParentModel().cowTorso.translateAndRotate(ms);
				ms.mulPose(Axis.XP.rotationDegrees(-90.0F));
				ms.translate(0.2F, yOffs, zOffs + 0.5D);
				ms.mulPose(Axis.YP.rotationDegrees(42.0F));
				ms.translate(0.35F, 0.0D, -0.9F);
				ms.mulPose(Axis.YP.rotationDegrees(-48.0F));
				ms.scale(-1.0F, -1.0F, 1.0F);
				ms.translate(-0.5D, -0.5D, -0.5D);
				blockrendererdispatcher.renderSingleBlock(blockstate, ms, buffers, light, i);
				ms.popPose();
				ms.pushPose();
				this.getParentModel().head.translateAndRotate(ms);
				// TF - adjust head shroom
				if (!JappaPackReloadListener.INSTANCE.isJappaPackLoaded()) {
					ms.translate(0.0D, -0.9D, 0.05D);
				} else {
					ms.translate(0.0D, -1.1D, 0.0D);
				}
				ms.mulPose(Axis.YP.rotationDegrees(-78.0F));
				ms.scale(-1.0F, -1.0F, 1.0F);
				ms.translate(-0.5D, -0.5D, -0.5D);
				blockrendererdispatcher.renderSingleBlock(blockstate, ms, buffers, light, i);
				ms.popPose();
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(Minoshroom entity) {
		return TEXTURE;
	}
}
