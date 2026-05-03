package twilightforest.client.renderer.entity;

import net.minecraft.client.model.monster.silverfish.SilverfishModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.entity.monster.TowerwoodBorer;

public class TowerwoodBorerRenderer extends MobRenderer<TowerwoodBorer, LivingEntityRenderState, SilverfishModel> {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("towertermite.png");

	public TowerwoodBorerRenderer(EntityRendererProvider.Context context) {
		super(context, new SilverfishModel(context.bakeLayer(TFModelLayers.TOWERWOOD_BORER)), 0.3F);
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
