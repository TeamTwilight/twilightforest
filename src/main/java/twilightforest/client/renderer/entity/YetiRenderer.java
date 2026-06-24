package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.YetiModel;
import twilightforest.client.state.entity.YetiRenderState;
import twilightforest.entity.monster.Yeti;

public class YetiRenderer extends HumanoidMobRenderer<Yeti, YetiRenderState, YetiModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("yeti2.png");

	public YetiRenderer(EntityRendererProvider.Context context) {
		super(context, new YetiModel(context.bakeLayer(TFModelLayers.YETI)), 0.625F);
	}

	@Override
	public YetiRenderState createRenderState() {
		return new YetiRenderState();
	}

	@Override
	public void extractRenderState(Yeti entity, YetiRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isHoldingEntity = entity.isVehicle();
	}

	@Override
	public Identifier getTextureLocation(YetiRenderState state) {
		return TEXTURE;
	}
}
