package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.HydraModel;
import twilightforest.entity.boss.Hydra;

public class HydraRenderer extends MobRenderer<Hydra, HydraModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");

	public HydraRenderer(EntityRendererProvider.Context manager, HydraModel modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	protected float getFlipDegrees(Hydra entity) {
		return 0F;
	}

	@Override
	public ResourceLocation getTextureLocation(Hydra entity) {
		return textureLoc;
	}
}
