package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MinotaurModel;
import twilightforest.entity.monster.Minotaur;

public class MinotaurRenderer extends MobRenderer<Minotaur, HumanoidRenderState, MinotaurModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("minotaur.png");

	public MinotaurRenderer(EntityRendererProvider.Context context) {
		super(context, new MinotaurModel(context.bakeLayer(TFModelLayers.MINOTAUR)), 0.625F);
		this.addLayer(new ItemInHandLayer<>(this));
	}

	@Override
	public HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	@Override
	public void extractRenderState(Minotaur entity, HumanoidRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		HumanoidRenderState.extractArmedEntityRenderState(entity, state, this.itemModelResolver);
	}

	@Override
	public ResourceLocation getTextureLocation(HumanoidRenderState state) {
		return TEXTURE;
	}
}
