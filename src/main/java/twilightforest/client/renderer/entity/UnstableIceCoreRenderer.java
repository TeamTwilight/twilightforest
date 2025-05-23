package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.UnstableIceCoreModel;
import twilightforest.entity.monster.UnstableIceCore;

public class UnstableIceCoreRenderer extends MobRenderer<UnstableIceCore, LivingEntityRenderState, UnstableIceCoreModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("iceexploder.png");

	public UnstableIceCoreRenderer(EntityRendererProvider.Context context) {
		super(context, new UnstableIceCoreModel(context.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE)), 0.4F);
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack stack) {

		stack.translate(0.0F, Mth.sin(state.ageInTicks * 0.2F) * 0.15F, 0.0F);

		// flash
		float f1 = state.deathTime;
		if (f1 > 0) {
			float f2 = 1.0F + Mth.sin(f1 * 100.0F) * f1 * 0.01F;

			if (f1 > 1.0F) {
				f1 = 1.0F;
			}

			f1 *= f1;
			f1 *= f1;
			float f3 = (1.0F + f1 * 0.4F) * f2;
			float f4 = (1.0F + f1 * 0.1F) / f2;
			stack.scale(f3, f4, f3);
		}
	}

	@Override
	protected void setupRotations(LivingEntityRenderState state, PoseStack stack, float yRot, float scale) {
		stack.mulPose(Axis.YP.rotationDegrees(180 - yRot));
	}

	@Override
	protected float getWhiteOverlayProgress(LivingEntityRenderState state) {
		if (state.deathTime > 0) {
			float f2 = state.deathTime + state.partialTick;

			if ((int) (f2 / 2) % 2 == 0) {
				return 0;
			} else {
				int i = (int) (f2 * 0.2F * 255.0F);

				if (i < 0) {
					i = 0;
				}

				if (i > 255) {
					i = 255;
				}

				short short1 = 255;
				short short2 = 255;
				short short3 = 255;
				return i << 24 | short1 << 16 | short2 << 8 | short3;
			}
		} else {
			return 0;
		}
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}
}


