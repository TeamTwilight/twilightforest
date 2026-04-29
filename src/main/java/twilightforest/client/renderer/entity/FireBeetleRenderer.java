package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.FireBeetleModel;
import twilightforest.entity.monster.FireBeetle;

public class FireBeetleRenderer extends MobRenderer<FireBeetle, LivingEntityRenderState, FireBeetleModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("firebeetle.png");

	public FireBeetleRenderer(EntityRendererProvider.Context context) {
		super(context, new FireBeetleModel(context.bakeLayer(TFModelLayers.FIRE_BEETLE)), 0.8F);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}
}
