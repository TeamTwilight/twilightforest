package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.SkeletonDruidModel;
import twilightforest.entity.monster.SkeletonDruid;

public class SkeletonDruidRenderer extends HumanoidMobRenderer<SkeletonDruid, SkeletonRenderState, SkeletonDruidModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("skeletondruid.png");

	public SkeletonDruidRenderer(EntityRendererProvider.Context context) {
		super(context, new SkeletonDruidModel(context.bakeLayer(TFModelLayers.SKELETON_DRUID)), 0.5F);
	}

	@Override
	public SkeletonRenderState createRenderState() {
		return new SkeletonRenderState();
	}

	@Override
	public void extractRenderState(SkeletonDruid entity, SkeletonRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isAggressive = entity.isAggressive();
	}

	@Override
	public ResourceLocation getTextureLocation(SkeletonRenderState state) {
		return TEXTURE;
	}
}
