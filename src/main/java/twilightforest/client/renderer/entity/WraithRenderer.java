package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.WraithModel;
import twilightforest.entity.monster.Wraith;

public class WraithRenderer extends HumanoidMobRenderer<Wraith, HumanoidRenderState, WraithModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("ghost.png");

	public WraithRenderer(EntityRendererProvider.Context context) {
		super(context, new WraithModel(context.bakeLayer(TFModelLayers.WRAITH)), 0.5F);
	}

	@Override
	protected int getModelTint(HumanoidRenderState state) {
		return ARGB.colorFromFloat(0.6F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	@Override
	public Identifier getTextureLocation(HumanoidRenderState state) {
		return TEXTURE;
	}
}
