package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.client.state.entity.NagaRenderState;
import twilightforest.entity.boss.Naga;

public class NagaRenderer extends MobRenderer<Naga, NagaRenderState, NagaModel<NagaRenderState>> {

	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("nagahead.png");
	public static final Identifier CHARGING_TEXTURE = TwilightForestMod.getModelTexture("nagahead_charging.png");
	public static final Identifier DAZED_TEXTURE = TwilightForestMod.getModelTexture("nagahead_dazed.png");

	public NagaRenderer(EntityRendererProvider.Context context) {
		super(context, new NagaModel<>(context.bakeLayer(TFModelLayers.NAGA)), 1.45F);
	}

	@Override
	protected void scale(NagaRenderState state, PoseStack stack) {
		super.scale(state, stack);
		//make size adjustment
		stack.scale(2.01F, 2.01F, 2.01F);
		stack.translate(0.0F, state.isDazed ? 1.075F : 0.75F, state.isDazed ? 0.175F : 0.0F);
	}

	@Override
	protected int getModelTint(NagaRenderState state) {
		return ARGB.colorFromFloat(1.0F, 1.0F, 1.0F - state.stunlessChargeProgress, 1.0F - state.stunlessChargeProgress);
	}

	@Override
	protected float getFlipDegrees() { //Prevent the body from keeling over
		return 0.0F;
	}

	@Override
	public NagaRenderState createRenderState() {
		return new NagaRenderState();
	}

	@Override
	public void extractRenderState(Naga entity, NagaRenderState state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.stunlessChargeProgress = entity.stunlessRedOverlayProgress;
		state.isCharging = entity.isCharging() || entity.isStunlessCharging();
		state.isDazed = entity.isDazed();
	}

	@Override
	public Identifier getTextureLocation(NagaRenderState state) {
		if (state.isDazed) {
			return DAZED_TEXTURE;
		} else if (state.isCharging || state.deathTime > 0) {
			return CHARGING_TEXTURE;
		} else {
			return TEXTURE;
		}
	}
}
