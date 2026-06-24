package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.PinchBeetleModel;
import twilightforest.client.state.entity.PinchBeetleRenderState;
import twilightforest.entity.monster.PinchBeetle;

public class PinchBeetleRenderer extends MobRenderer<PinchBeetle, PinchBeetleRenderState, PinchBeetleModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("pinchbeetle.png");

	public PinchBeetleRenderer(EntityRendererProvider.Context context) {
		super(context, new PinchBeetleModel(context.bakeLayer(TFModelLayers.PINCH_BEETLE)), 0.6F);
	}

	@Override
	public PinchBeetleRenderState createRenderState() {
		return new PinchBeetleRenderState();
	}

	@Override
	public void extractRenderState(PinchBeetle entity, PinchBeetleRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isHoldingVictim = entity.isVehicle();
	}

	@Override
	public Identifier getTextureLocation(PinchBeetleRenderState state) {
		return TEXTURE;
	}
}
