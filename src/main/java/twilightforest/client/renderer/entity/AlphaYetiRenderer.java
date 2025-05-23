package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.AlphaYetiModel;
import twilightforest.client.state.AlphaYetiRenderState;
import twilightforest.entity.boss.AlphaYeti;

public class AlphaYetiRenderer extends HumanoidMobRenderer<AlphaYeti, AlphaYetiRenderState, AlphaYetiModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("yetialpha.png");

	public AlphaYetiRenderer(EntityRendererProvider.Context context) {
		super(context, new AlphaYetiModel(context.bakeLayer(TFModelLayers.ALPHA_YETI)), 1.75F);
	}

	@Override
	public AlphaYetiRenderState createRenderState() {
		return new AlphaYetiRenderState();
	}

	@Override
	public void extractRenderState(AlphaYeti entity, AlphaYetiRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isHoldingEntity = entity.isVehicle();
		state.isRampaging = entity.isRampaging();
		state.isTired = entity.isTired();
	}

	@Override
	public ResourceLocation getTextureLocation(AlphaYetiRenderState state) {
		return TEXTURE;
	}
}
