package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.BunnyModel;
import twilightforest.client.state.entity.DwarfRabbitRenderState;
import twilightforest.entity.passive.DwarfRabbit;

public class BunnyRenderer extends AgeableMobRenderer<DwarfRabbit, DwarfRabbitRenderState, BunnyModel> {

	public BunnyRenderer(EntityRendererProvider.Context context) {
		super(context, new BunnyModel(context.bakeLayer(TFModelLayers.BUNNY)), new BunnyModel(context.bakeLayer(TFModelLayers.BUNNY_BABY)), 0.3F);
	}

	@Override
	public DwarfRabbitRenderState createRenderState() {
		return new DwarfRabbitRenderState();
	}

	@Override
	public void extractRenderState(DwarfRabbit entity, DwarfRabbitRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.texture = entity.getVariant().value().texture();
	}

	@Override
	public Identifier getTextureLocation(DwarfRabbitRenderState state) {
		return state.texture;
	}
}
