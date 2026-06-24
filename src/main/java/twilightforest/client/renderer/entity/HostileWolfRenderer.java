package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HostileWolfModel;
import twilightforest.entity.monster.HostileWolf;

public class HostileWolfRenderer extends MobRenderer<HostileWolf, WolfRenderState, HostileWolfModel<WolfRenderState>> {

	public HostileWolfRenderer(EntityRendererProvider.Context context) {
		super(context, new HostileWolfModel<>(context.bakeLayer(TFModelLayers.HOSTILE_WOLF)), 0.5F);
	}

	@Override
	public WolfRenderState createRenderState() {
		return new WolfRenderState();
	}

	@Override
	public void extractRenderState(HostileWolf entity, WolfRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.tailAngle = entity.getTailAngle();
		state.texture = entity.getTexture();
	}

	@Override
	public Identifier getTextureLocation(WolfRenderState state) {
		return state.texture;
	}

}
