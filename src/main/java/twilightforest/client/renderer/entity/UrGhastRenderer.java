package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.UrGhastModel;
import twilightforest.client.state.TFGhastRenderState;
import twilightforest.entity.boss.UrGhast;

public class UrGhastRenderer extends MobRenderer<UrGhast, TFGhastRenderState, UrGhastModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("towerboss.png");
	public static final ResourceLocation LOOKING_TEXTURE = TwilightForestMod.getModelTexture("towerboss_openeyes.png");
	public static final ResourceLocation ATTACKING_TEXTURE = TwilightForestMod.getModelTexture("towerboss_fire.png");

	public UrGhastRenderer(EntityRendererProvider.Context context) {
		super(context, new UrGhastModel(context.bakeLayer(TFModelLayers.UR_GHAST)), 8.0F);
	}

	@Override
	public boolean shouldRender(UrGhast entity, Frustum frustum, double x, double y, double z) {
		if (entity.deathTime > UrGhast.DEATH_ANIMATION_DURATION / 3) return false;
		return super.shouldRender(entity, frustum, x, y, z);
	}

	@Override
	protected float getFlipDegrees() { //Prevent the body from keeling over
		return 0.0F;
	}

	@Override
	protected void scale(TFGhastRenderState state, PoseStack stack) {
		float scaleVariable = state.attackTimer / 20.0F;
		if (scaleVariable < 0.0F) {
			scaleVariable = 0.0F;
		}

		scaleVariable = 1.0F / (scaleVariable * scaleVariable * scaleVariable * scaleVariable * scaleVariable * 2.0F + 1.0F);
		float yScale = (24.0F + scaleVariable) / 2.0F;
		float xzScale = (24.0F + 1.0F / scaleVariable) / 2.0F;
		stack.scale(xzScale, yScale, xzScale);
	}

	@Override
	public TFGhastRenderState createRenderState() {
		return new TFGhastRenderState();
	}

	@Override
	public void extractRenderState(UrGhast entity, TFGhastRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isCharging = entity.isCharging();
		state.attackTimer = Mth.lerp(partialTick, entity.getPrevAttackTimer(), entity.getAttackTimer());
		state.attackState = entity.getAttackStatus();
	}

	@Override
	public ResourceLocation getTextureLocation(TFGhastRenderState state) {
		if (state.isCharging || state.deathTime > 0) {
			return ATTACKING_TEXTURE;
		}

		return switch (state.attackState) {
			case 1 -> LOOKING_TEXTURE;
			case 2 -> ATTACKING_TEXTURE;
			default -> TEXTURE;
		};
	}
}
