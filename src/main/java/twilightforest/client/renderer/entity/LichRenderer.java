package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.LichModel;
import twilightforest.client.renderer.TFRenderTypes;
import twilightforest.client.renderer.entity.layers.ShieldLayer;
import twilightforest.client.state.LichRenderState;
import twilightforest.entity.boss.Lich;

public class LichRenderer extends HumanoidMobRenderer<Lich, LichRenderState, LichModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("twilightlich64.png");

	public LichRenderer(EntityRendererProvider.Context context) {
		super(context, new LichModel(context.bakeLayer(TFModelLayers.LICH)), 0.6F);
		this.addLayer(new ShieldLayer<>(this));
		this.addLayer(new EyesLayer<>(this) {
			private static final RenderType EYES = RenderType.eyes(TwilightForestMod.getModelTexture("twilightlich64_eyes.png"));

            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T t, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (t.isShadowClone() && !t.isInvisible()) super.render(poseStack, buffer, packedLight, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }

            @Override
            public RenderType renderType() {
                return EYES;
            }
        });
	}

	@Override
	protected int getModelTint(LichRenderState state) {
		if (state.isShadowClone) {
			return ARGB.colorFromFloat(0.5F, 0.333F, 0.333F, 0.333F);
		}
		return super.getModelTint(state);
	}

	@Nullable
	@Override
	protected RenderType getRenderType(LichRenderState state, boolean bodyVisible, boolean translucent, boolean glowing) {
		if (state.isShadowClone) return TFRenderTypes.SHADOW_CLONE;
		else return super.getRenderType(state, bodyVisible, translucent, glowing);
	}

	@Override
	protected boolean isShaking(LichRenderState state) {
		return super.isShaking(state) || (state.deathTime <= Lich.DEATH_ANIMATION_POINT_A);
	}

	@Override
	public void render(LichRenderState state, PoseStack stack, MultiBufferSource buffer, int packedLight) {
		stack.pushPose();
		stack.scale(1.125F, 1.125F, 1.125F);
		if (state.deathTime > 0) {
			if (state.deathTime > Lich.DEATH_ANIMATION_POINT_A) {
				stack.translate(0.0D, -1.8D * Math.pow(Math.min(((state.deathTime - Lich.DEATH_ANIMATION_POINT_A) + state.partialTick) * 0.05D, 1.0D), 3.0D), 0.0D);
			} else {
				float time = state.deathTime + state.partialTick;
				stack.translate(Math.sin(time * time) * 0.01D, 0.0D, Math.cos(time * time) * 0.01D);
			}
			super.render(state, stack, buffer, packedLight);
		} else super.render(state, stack, buffer, packedLight);
		stack.popPose();
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
	}

	@Override
	public ResourceLocation getTextureLocation(LichRenderState state) {
		return TEXTURE;
	}

	@Override
	protected float getShadowRadius(T entity) {
		return entity.isShadowClone() || entity.deathTime > Lich.DEATH_ANIMATION_POINT_A ? 0.0F : super.getShadowRadius(entity);
	}
}
