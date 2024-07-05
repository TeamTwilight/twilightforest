package twilightforest.client.renderer.entity.newmodels;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.newmodels.NewUrGhastModel;
import twilightforest.client.renderer.entity.CarminiteGhastRenderer;
import twilightforest.entity.boss.UrGhast;

public class NewUrGhastRenderer extends MobRenderer<UrGhast, NewUrGhastModel> {

	private final ResourceLocation textureLocClosed = TwilightForestMod.getModelTexture("towerboss.png");
	private final ResourceLocation textureLocOpen = TwilightForestMod.getModelTexture("towerboss_openeyes.png");
	private final ResourceLocation textureLocAttack = TwilightForestMod.getModelTexture("towerboss_fire.png");
	private final float scale;

	public NewUrGhastRenderer(EntityRendererProvider.Context manager, NewUrGhastModel modelTFGhast, float shadowSize, float scale) {
		super(manager, modelTFGhast, shadowSize);
		this.scale = scale;
	}

	@Override
	public ResourceLocation getTextureLocation(UrGhast entity) {
		return switch (entity.isCharging() || entity.isDeadOrDying() ? 2 : entity.getAttackStatus()) {
			case 1 -> textureLocOpen;
			case 2 -> textureLocAttack;
			default -> textureLocClosed;
		};
	}

	@Override
	protected void scale(UrGhast ghast, PoseStack stack, float partialTicks) {
		int attackTimer = ghast.getAttackTimer();
		int prevAttackTimer = ghast.getPrevAttackTimer();
		float scaleVariable = (prevAttackTimer + (attackTimer - prevAttackTimer) * partialTicks) / 20.0F;
		if (scaleVariable < 0.0F) {
			scaleVariable = 0.0F;
		}

		scaleVariable = 1.0F / (scaleVariable * scaleVariable * scaleVariable * scaleVariable * scaleVariable * 2.0F + 1.0F);
		float yScale = (this.scale + scaleVariable) / 2.0F;
		float xzScale = (this.scale + 1.0F / scaleVariable) / 2.0F;
		stack.scale(xzScale, yScale, xzScale);
	}

	@Override
	public boolean shouldRender(UrGhast pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
		if (pLivingEntity.deathTime > 40) return false;
		return super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ);
	}

	@Override
	protected float getFlipDegrees(UrGhast urGhast) { //Prevent the body from keeling over
		return urGhast.isDeadOrDying() ? 0.0F : super.getFlipDegrees(urGhast);
	}
}
