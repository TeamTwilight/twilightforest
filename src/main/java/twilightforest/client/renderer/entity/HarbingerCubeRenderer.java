package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HarbingerCubeModel;
import twilightforest.entity.monster.HarbingerCube;

public class HarbingerCubeRenderer extends MobRenderer<HarbingerCube, LivingEntityRenderState, HarbingerCubeModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("apocalypse2.png");

	public HarbingerCubeRenderer(EntityRendererProvider.Context context) {
		super(context, new HarbingerCubeModel(context.bakeLayer(TFModelLayers.HARBINGER_CUBE)), 0.8F);
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
