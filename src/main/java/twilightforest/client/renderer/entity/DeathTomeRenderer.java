package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.DeathTomeModel;
import twilightforest.client.state.DeathTomeRenderState;
import twilightforest.entity.monster.DeathTome;

public class DeathTomeRenderer extends MobRenderer<DeathTome, DeathTomeRenderState, DeathTomeModel> {

	private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/enchanting_table_book.png");

	public DeathTomeRenderer(EntityRendererProvider.Context context) {
		super(context, new DeathTomeModel(context.bakeLayer(TFModelLayers.DEATH_TOME)), 0.3F);
	}

	@Override
	public DeathTomeRenderState createRenderState() {
		return new DeathTomeRenderState();
	}

	@Override
	public void extractRenderState(DeathTome entity, DeathTomeRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.onLectern = entity.isOnLectern();
		state.flip = Mth.lerp(partialTick, entity.oFlip, entity.flip);
	}

	@Override
	public ResourceLocation getTextureLocation(DeathTomeRenderState state) {
		return TEXTURE;
	}
}
