package twilightforest.client.renderer.entity.legacy;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.legacy.UrGhastLegacyModel;
import twilightforest.client.renderer.entity.CarminiteGhastRenderer;
import twilightforest.entity.boss.UrGhast;

public class LegacyUrGhastRenderer extends CarminiteGhastRenderer<UrGhast, UrGhastLegacyModel> {

	private final ResourceLocation textureLocClosed = TwilightForestMod.getModelTexture("towerboss.png");
	private final ResourceLocation textureLocOpen   = TwilightForestMod.getModelTexture("towerboss_openeyes.png");
	private final ResourceLocation textureLocAttack = TwilightForestMod.getModelTexture("towerboss_fire.png");

	public LegacyUrGhastRenderer(EntityRendererProvider.Context manager, UrGhastLegacyModel modelTFGhast, float shadowSize, float scale) {
		super(manager, modelTFGhast, shadowSize, scale);
	}

	@Override
	public ResourceLocation getTextureLocation(UrGhast entity) {
		return switch (entity.isCharging() ? 2 : entity.getAttackStatus()) {
			case 1 -> textureLocOpen;
			case 2 -> textureLocAttack;
			default -> textureLocClosed;
		};
	}
}
