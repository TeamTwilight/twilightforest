package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.BunnyModel;
import twilightforest.client.state.DwarfRabbitRenderState;
import twilightforest.entity.passive.DwarfRabbit;

public class BunnyRenderer extends MobRenderer<DwarfRabbit, DwarfRabbitRenderState, BunnyModel> {

	public BunnyRenderer(EntityRendererProvider.Context context) {
		super(context, new BunnyModel(context.bakeLayer(TFModelLayers.BUNNY)), 0.3F);
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
	public ResourceLocation getTextureLocation(DwarfRabbitRenderState state) {
		return state.texture;
	}
}
