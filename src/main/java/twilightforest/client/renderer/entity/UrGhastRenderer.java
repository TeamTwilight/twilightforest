package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.TFGhastModel;
import twilightforest.entity.boss.UrGhast;

public class UrGhastRenderer<T extends UrGhast, M extends TFGhastModel<T>> extends MobRenderer<T, M> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("towerboss.png");
	public static final ResourceLocation LOOKING_TEXTURE = TwilightForestMod.getModelTexture("towerboss_openeyes.png");
	public static final ResourceLocation ATTACKING_TEXTURE = TwilightForestMod.getModelTexture("towerboss_fire.png");
	private final float scale;

	public UrGhastRenderer(EntityRendererProvider.Context context, M model, float shadowSize, float scale) {
		super(context, model, shadowSize);
		this.scale = scale;
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		if (entity.isCharging() || entity.isDeadOrDying()) {
			return ATTACKING_TEXTURE;
		}

		return switch (entity.getAttackStatus()) {
			case 1 -> LOOKING_TEXTURE;
			case 2 -> ATTACKING_TEXTURE;
			default -> TEXTURE;
		};
	}

	@Override
	protected void scale(T entity, PoseStack stack, float partialTicks) {
		int attackTimer = entity.getAttackTimer();
		int prevAttackTimer = entity.getPrevAttackTimer();
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
	public boolean shouldRender(T entity, Frustum frustum, double x, double y, double z) {
		if (entity.deathTime > UrGhast.DEATH_ANIMATION_DURATION / 3) return false;
		return super.shouldRender(entity, frustum, x, y, z);
	}

	@Override
	protected float getFlipDegrees(T entity) { //Prevent the body from keeling over
		return entity.isDeadOrDying() ? 0.0F : super.getFlipDegrees(entity);
	}
}
