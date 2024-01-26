package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.UrGhastModel;
import twilightforest.entity.boss.UrGhast;

public class UrGhastRenderer extends CarminiteGhastRenderer<UrGhast, UrGhastModel> {

	private final ResourceLocation textureLocClosed = TwilightForestMod.getModelTexture("towerboss.png");
	private final ResourceLocation textureLocOpen   = TwilightForestMod.getModelTexture("towerboss_openeyes.png");
	private final ResourceLocation textureLocAttack = TwilightForestMod.getModelTexture("towerboss_fire.png");

	public UrGhastRenderer(EntityRendererProvider.Context manager, UrGhastModel model, float shadowSize, float scale) {
		super(manager, model, shadowSize, scale);
	}

	@Override
	protected float getFlipDegrees(UrGhast ghast) { //Prevent the body from keeling over
		return ghast.isDeadOrDying() ? 0.0F : super.getFlipDegrees(ghast);
	}

	@Override
	public ResourceLocation getTextureLocation(UrGhast entity) {
		return switch (entity.isCharging() || entity.isDeadOrDying() ? 2 : entity.getAttackStatus()) {
			case 1 -> textureLocOpen;
			case 2 -> textureLocAttack;
			default -> textureLocClosed;
		};
	}
}
