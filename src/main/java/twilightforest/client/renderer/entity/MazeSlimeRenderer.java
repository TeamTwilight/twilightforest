package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.entity.monster.MazeSlime;

public class MazeSlimeRenderer extends MobRenderer<MazeSlime, SlimeRenderState, SlimeModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("mazeslime.png");

	public MazeSlimeRenderer(EntityRendererProvider.Context context) {
		super(context, new SlimeModel(context.bakeLayer(TFModelLayers.MAZE_SLIME)), 0.625F);
		this.addLayer(new MazeSlimeOuterLayer(this, context.getModelSet()));
	}


	@Override
	public void submit(SlimeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		this.shadowRadius = 0.25F * state.size;
		super.submit(state, poseStack, submitNodeCollector, camera);
	}

	@Override
	protected void scale(SlimeRenderState state, PoseStack stack) {
		stack.scale(0.999F, 0.999F, 0.999F);
		stack.translate(0.0D, 0.001D, 0.0D);
		float size = state.size;
		float squishFactor = state.squish / (size * 0.5F + 1.0F);
		float scaledSquish = 1.0F / (squishFactor + 1.0F);
		stack.scale(scaledSquish * size, 1.0F / scaledSquish * size, scaledSquish * size);
	}

	@Override
	public SlimeRenderState createRenderState() {
		return new SlimeRenderState();
	}

	@Override
	public void extractRenderState(MazeSlime entity, SlimeRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.squish = Mth.lerp(partialTick, entity.oSquish, entity.squish);
		state.size = entity.getSize();
	}

	@Override
	public Identifier getTextureLocation(SlimeRenderState state) {
		return TEXTURE;
	}

	public static class MazeSlimeOuterLayer extends RenderLayer<SlimeRenderState, SlimeModel> {
		private final SlimeModel model;

		public MazeSlimeOuterLayer(RenderLayerParent<SlimeRenderState, SlimeModel> renderer, EntityModelSet modelSet) {
			super(renderer);
			this.model = new SlimeModel(modelSet.bakeLayer(TFModelLayers.MAZE_SLIME_OUTER));
		}

		@Override
		public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, SlimeRenderState renderState, float v, float v1) {
			boolean flag = renderState.appearsGlowing() && renderState.isInvisible;
			if (!renderState.isInvisible || flag) {
				RenderType renderType;
				if (flag) renderType = RenderTypes.outline(TEXTURE);
				else renderType = RenderTypes.entityTranslucent(TEXTURE);

				this.model.setupAnim(renderState);
				submitNodeCollector.submitModel(this.model, renderState, poseStack, renderType, renderState.lightCoords, LivingEntityRenderer.getOverlayCoords(renderState, 0.0F), renderState.outlineColor, null);
			}
		}
	}
}
