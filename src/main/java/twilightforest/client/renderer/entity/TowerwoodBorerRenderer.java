package twilightforest.client.renderer.entity;

import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.entity.monster.TowerwoodBorer;

public class TowerwoodBorerRenderer extends MobRenderer<TowerwoodBorer, LivingEntityRenderState, SilverfishModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("towertermite.png");

	public TowerwoodBorerRenderer(EntityRendererProvider.Context context) {
		super(context, new SilverfishModel(context.bakeLayer(TFModelLayers.TOWERWOOD_BORER)), 0.3F);
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}
}
