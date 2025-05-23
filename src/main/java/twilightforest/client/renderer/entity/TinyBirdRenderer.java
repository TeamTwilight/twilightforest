package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.TinyBirdModel;
import twilightforest.client.state.TinyBirdRenderState;
import twilightforest.entity.passive.TinyBird;

public class TinyBirdRenderer extends MobRenderer<TinyBird, TinyBirdRenderState, TinyBirdModel> {

	public TinyBirdRenderer(EntityRendererProvider.Context context) {
		super(context, new TinyBirdModel(context.bakeLayer(TFModelLayers.TINY_BIRD)), 0.3F);
	}

	@Override
	public TinyBirdRenderState createRenderState() {
		return new TinyBirdRenderState();
	}

	@Override
	public void extractRenderState(TinyBird entity, TinyBirdRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.flap = Mth.lerp(partialTick, entity.lastFlapLength, entity.flapLength);
		state.flapSpeed = Mth.lerp(partialTick, entity.lastFlapIntensity, entity.flapIntensity);
		state.texture = entity.getVariant().value().texture();
	}

	@Override
	public ResourceLocation getTextureLocation(TinyBirdRenderState state) {
		return state.texture;
	}
}
