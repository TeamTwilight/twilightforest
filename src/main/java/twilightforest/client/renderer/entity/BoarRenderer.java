package twilightforest.client.renderer.entity;

import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.BoarModel;
import twilightforest.entity.passive.Boar;

public class BoarRenderer extends AgeableMobRenderer<Boar, PigRenderState, PigModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("wildboar.png");

	public BoarRenderer(EntityRendererProvider.Context context) {
		super(context, new BoarModel(context.bakeLayer(TFModelLayers.BOAR)), new BoarModel(context.bakeLayer(TFModelLayers.BOAR_BABY)), 0.7F);
	}

	@Override
	public PigRenderState createRenderState() {
		return new PigRenderState();
	}

	@Override
	public Identifier getTextureLocation(PigRenderState state) {
		return TEXTURE;
	}
}
