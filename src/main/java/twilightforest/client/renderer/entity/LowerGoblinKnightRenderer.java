package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.LowerGoblinKnightModel;
import twilightforest.client.state.LowerGoblinKnightRenderState;
import twilightforest.entity.monster.LowerGoblinKnight;
import twilightforest.entity.monster.UpperGoblinKnight;

public class LowerGoblinKnightRenderer extends MobRenderer<LowerGoblinKnight, LowerGoblinKnightRenderState, LowerGoblinKnightModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("doublegoblin.png");

	public LowerGoblinKnightRenderer(EntityRendererProvider.Context context) {
		super(context, new LowerGoblinKnightModel(context.bakeLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT)), 0.625F);
	}

	@Override
	public LowerGoblinKnightRenderState createRenderState() {
		return new LowerGoblinKnightRenderState();
	}

	@Override
	public void extractRenderState(LowerGoblinKnight entity, LowerGoblinKnightRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.hasArmor = entity.hasArmor();
		state.hasUpperGoblin = entity.isVehicle() && entity.getFirstPassenger() instanceof UpperGoblinKnight;
	}

	@Override
	public ResourceLocation getTextureLocation(LowerGoblinKnightRenderState state) {
		return TEXTURE;
	}
}
