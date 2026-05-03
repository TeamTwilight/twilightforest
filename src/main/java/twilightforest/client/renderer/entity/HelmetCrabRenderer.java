package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HelmetCrabModel;
import twilightforest.client.state.entity.HelmetCrabRenderState;
import twilightforest.entity.monster.HelmetCrab;

public class HelmetCrabRenderer extends MobRenderer<HelmetCrab, HelmetCrabRenderState, HelmetCrabModel> {
	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("helmetcrab.png");
	public static final Identifier BLUE_TEXTURE = TwilightForestMod.getModelTexture("helmetcrabblue.png");

	public HelmetCrabRenderer(EntityRendererProvider.Context context) {
		super(context, new HelmetCrabModel(context.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F);
	}

	@Override
	public HelmetCrabRenderState createRenderState() {
		return new HelmetCrabRenderState();
	}

	@Override
	public void extractRenderState(HelmetCrab entity, HelmetCrabRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.helmetRot = state.getHelmetRotation(entity, partialTicks);
		state.id = entity.getId();
		state.blue = entity.isBlue();
	}

	@Override
	protected float getFlipDegrees() {
		return 0.0F;
	}

	@Override
	public Identifier getTextureLocation(HelmetCrabRenderState state) {
		return state.blue ? BLUE_TEXTURE : TEXTURE;
	}
}
