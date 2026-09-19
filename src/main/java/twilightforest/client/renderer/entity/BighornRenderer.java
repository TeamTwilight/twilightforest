package twilightforest.client.renderer.entity;

import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.BighornModel;
import twilightforest.client.renderer.entity.layers.BighornWoolLayer;
import twilightforest.entity.passive.Bighorn;

@SuppressWarnings("deprecation")
public class BighornRenderer extends AgeableMobRenderer<Bighorn, SheepRenderState, SheepModel> {
	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("bighorn.png");

	public BighornRenderer(EntityRendererProvider.Context context) {
		super(context, new BighornModel(context.bakeLayer(TFModelLayers.BIGHORN_SHEEP)), new BighornModel(context.bakeLayer(TFModelLayers.BIGHORN_SHEEP_BABY)), 0.7F);
		this.addLayer(new BighornWoolLayer(this, context.getModelSet()));
	}

	@Override
	public SheepRenderState createRenderState() {
		return new SheepRenderState();
	}

	@Override
	public void extractRenderState(Bighorn sheep, SheepRenderState renderState, float partialTick) {
		super.extractRenderState(sheep, renderState, partialTick);
		renderState.headEatAngleScale = sheep.getHeadEatAngleScale(partialTick);
		renderState.headEatPositionScale = sheep.getHeadEatPositionScale(partialTick);
		renderState.isSheared = sheep.isSheared();
		renderState.woolColor = sheep.getColor();
	}

	@Override
	public Identifier getTextureLocation(SheepRenderState entity) {
		return TEXTURE;
	}
}
