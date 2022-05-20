package twilightforest.client.renderer.entity.legacy;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.legacy.HydraLegacyModel;
import twilightforest.entity.boss.Hydra;

public class LegacyHydraRenderer extends MobRenderer<Hydra, HydraLegacyModel> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");

	public LegacyHydraRenderer(EntityRendererProvider.Context manager, HydraLegacyModel modelbase, float shadowSize) {
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
