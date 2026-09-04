package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.UnstableIceCoreModel;
import twilightforest.entity.monster.UnstableIceCore;

public class UnstableIceCoreRenderer extends MobRenderer<UnstableIceCore, LivingEntityRenderState, UnstableIceCoreModel> {

	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("iceexploder.png");

	public UnstableIceCoreRenderer(EntityRendererProvider.Context context) {
		super(context, new UnstableIceCoreModel(context.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE)), 0.4F);
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack stack) {
		stack.translate(0.0F, Mth.sin(state.ageInTicks * 0.2F) * 0.15F, 0.0F);

		// flash
		if (state.deathTime > 0.0F) {
			int deathTick = (int) state.deathTime;
			float wobble = 1.0F + Mth.lerp(state.deathTime - deathTick, this.wobbleAt(deathTick), this.wobbleAt(deathTick + 1));

			float growth = Math.min(state.deathTime, 1.0F);
			growth *= growth;
			growth *= growth;

			float horizontal = (1.0F + growth * 0.4F) * wobble;
			float vertical = (1.0F + growth * 0.1F) / wobble;
			stack.scale(horizontal, vertical, horizontal);
		}
	}

	private float wobbleAt(int deathTick) {
		return Mth.sin(deathTick * 100.0F) * deathTick * 0.01F;
	}

	@Override
	protected void setupRotations(LivingEntityRenderState state, PoseStack stack, float yRot, float scale) {
		stack.mulPose(Axis.YP.rotationDegrees(180 - yRot));
	}

	@Override
	public void extractRenderState(UnstableIceCore entity, LivingEntityRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.hasRedOverlay = entity.hurtTime > 0;
	}

	@Override
	protected float getWhiteOverlayProgress(LivingEntityRenderState state) {
		if (state.deathTime > 0.0F && (int) (state.deathTime / 2.0F) % 2 != 0) {
			return Mth.clamp(state.deathTime * 0.2F, 0.0F, 1.0F);
		} else {
			return 0.0F;
		}
	}

	@Override
	protected int getModelTint(LivingEntityRenderState state) {
		if (state.deathTime > 0.0F) {
			return super.getModelTint(state);
		} else {
			return ARGB.colorFromFloat(0.6F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}

	@Override
	public Identifier getTextureLocation(LivingEntityRenderState state) {
		return TEXTURE;
	}
}


