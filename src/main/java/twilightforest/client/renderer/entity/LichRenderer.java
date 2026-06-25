package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.LichModel;
import twilightforest.client.renderer.TFRenderTypes;
import twilightforest.client.renderer.entity.layers.ShieldLayer;
import twilightforest.client.state.entity.LichRenderState;
import twilightforest.entity.boss.Lich;

public class LichRenderer extends HumanoidMobRenderer<Lich, LichRenderState, LichModel> {

	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("twilightlich64.png");

	public LichRenderer(EntityRendererProvider.Context context) {
		super(context, new LichModel(context.bakeLayer(TFModelLayers.LICH)), 0.6F);
		this.addLayer(new ShieldLayer<>(this));
		this.addLayer(new EyesLayer<>(this) {
			private static final RenderType EYES = RenderTypes.eyes(TwilightForestMod.getModelTexture("twilightlich64_eyes.png"));

			@Override
			public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, LichRenderState state, float yRot, float xRot) {
				if (state.isShadowClone && !state.isInvisible) super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
			}

            @Override
            public RenderType renderType() {
                return EYES;
            }
        });
	}

	@Override
	protected int getModelTint(LichRenderState state) {
		if (state.isShadowClone) return ARGB.colorFromFloat(0.5F, 0.333F, 0.333F, 0.333F);
		return super.getModelTint(state);
	}

	@Nullable
	@Override
	protected RenderType getRenderType(LichRenderState state, boolean bodyVisible, boolean translucent, boolean glowing) {
		if (state.isShadowClone && !state.isInvisible) return TFRenderTypes.SHADOW_CLONE;
		else return super.getRenderType(state, bodyVisible, translucent, glowing);
	}

	@Override
	protected boolean isShaking(LichRenderState state) {
		return super.isShaking(state) || (state.deathTime > 0 && state.deathTime <= Lich.DEATH_ANIMATION_POINT_A);
	}

	@Override
	public void submit(LichRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		poseStack.scale(1.125F, 1.125F, 1.125F);

		if (state.deathTime > 0) {
			if (state.deathTime > Lich.DEATH_ANIMATION_POINT_A) {
				poseStack.translate(0.0D, -1.8D * Math.pow(Math.min(((state.deathTime - Lich.DEATH_ANIMATION_POINT_A) + state.partialTick) / (float) (Lich.DEATH_ANIMATION_POINT_B - Lich.DEATH_ANIMATION_POINT_A), 1.0D), 3.0D), 0.0D);
			} else {
				float time = state.deathTime + state.partialTick;
				poseStack.translate(Math.sin(time * time) * 0.01D, 0.0D, Math.cos(time * time) * 0.01D);
			}

		}
		super.submit(state, poseStack, submitNodeCollector, camera);
		poseStack.popPose();
	}

	@Override
	protected float getFlipDegrees() { //Prevent the body from keeling over
		return 0.0F;
	}

	@Override
	public LichRenderState createRenderState() {
		return new LichRenderState();
	}

	@Override
	public void extractRenderState(Lich entity, LichRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.isShadowClone = entity.isShadowClone();
		state.phase = entity.getPhase();
		state.shieldCount = ShieldLayer.getShieldCount(entity);
	}

	@Override
	public Identifier getTextureLocation(LichRenderState state) {
		return TEXTURE;
	}

	@Override
	protected float getShadowRadius(LichRenderState state) {
		return state.isShadowClone || state.deathTime > Lich.DEATH_ANIMATION_POINT_A ? 0.0F : super.getShadowRadius(state);
	}
}
