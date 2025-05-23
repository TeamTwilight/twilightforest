package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MosquitoSwarmModel;
import twilightforest.entity.monster.MosquitoSwarm;

public class MosquitoSwarmRenderer extends MobRenderer<MosquitoSwarm, LivingEntityRenderState, MosquitoSwarmModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("mosquitoswarm.png");

	public MosquitoSwarmRenderer(EntityRendererProvider.Context context) {
		super(context, new MosquitoSwarmModel(context.bakeLayer(TFModelLayers.MOSQUITO_SWARM)), 0.0F);
	}

	@Override
	protected float getFlipDegrees() {
		return 0.0F;
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}
}
