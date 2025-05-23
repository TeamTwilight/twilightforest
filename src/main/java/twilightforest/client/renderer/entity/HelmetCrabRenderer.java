package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HelmetCrabModel;
import twilightforest.entity.monster.HelmetCrab;

public class HelmetCrabRenderer extends MobRenderer<HelmetCrab, LivingEntityRenderState, HelmetCrabModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("helmetcrab.png");

	public HelmetCrabRenderer(EntityRendererProvider.Context context) {
		super(context, new HelmetCrabModel(context.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F);
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
