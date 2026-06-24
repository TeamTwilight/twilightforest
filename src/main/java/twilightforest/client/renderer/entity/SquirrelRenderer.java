package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.SquirrelModel;
import twilightforest.entity.passive.Squirrel;

public class SquirrelRenderer extends MobRenderer<Squirrel, LivingEntityRenderState, SquirrelModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("squirrel2.png");

	public SquirrelRenderer(EntityRendererProvider.Context context) {
		super(context, new SquirrelModel(context.bakeLayer(TFModelLayers.SQUIRREL)), 0.3F);
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
