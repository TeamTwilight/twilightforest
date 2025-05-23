package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.TrollModel;
import twilightforest.client.state.TrollRenderState;
import twilightforest.entity.monster.Troll;

public class TrollRenderer extends HumanoidMobRenderer<Troll, TrollRenderState, TrollModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("troll.png");

	public TrollRenderer(EntityRendererProvider.Context context) {
		super(context, new TrollModel(context.bakeLayer(TFModelLayers.TROLL)), 0.625F);
	}

	@Override
	public TrollRenderState createRenderState() {
		return new TrollRenderState();
	}

	@Override
	public void extractRenderState(Troll entity, TrollRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isHoldingRock = entity.hasRock();
	}

	@Override
	public ResourceLocation getTextureLocation(TrollRenderState state) {
		return TEXTURE;
	}
}
