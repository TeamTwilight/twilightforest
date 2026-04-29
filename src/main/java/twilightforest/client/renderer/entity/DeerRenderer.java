package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.DeerModel;
import twilightforest.entity.passive.Deer;

public class DeerRenderer extends MobRenderer<Deer, LivingEntityRenderState, DeerModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("wilddeer.png");

	public DeerRenderer(EntityRendererProvider.Context context) {
		super(context, new DeerModel(context.bakeLayer(TFModelLayers.DEER)), 0.7F);
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
