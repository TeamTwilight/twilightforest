package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HelmetCrabModel;
import twilightforest.entity.monster.HelmetCrab;

public class HelmetCrabRenderer extends MobRenderer<HelmetCrab, HelmetCrabModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("helmetcrab.png");
	public static final ResourceLocation BLUE_TEXTURE = TwilightForestMod.getModelTexture("helmetcrabblue.png");

	public HelmetCrabRenderer(EntityRendererProvider.Context context) {
		super(context, new HelmetCrabModel(context.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F);
	}

	@Override
	protected float getFlipDegrees(HelmetCrab entity) {
		return 0.0F;
	}

	@Override
	public ResourceLocation getTextureLocation(HelmetCrab entity) {
		return entity.isBlue() ? BLUE_TEXTURE : TEXTURE;
	}
}
