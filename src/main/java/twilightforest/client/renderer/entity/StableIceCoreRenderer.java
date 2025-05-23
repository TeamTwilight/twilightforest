package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.StableIceCoreModel;
import twilightforest.entity.monster.StableIceCore;

public class StableIceCoreRenderer extends MobRenderer<StableIceCore, LivingEntityRenderState, StableIceCoreModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("iceshooter.png");

	public StableIceCoreRenderer(EntityRendererProvider.Context context) {
		super(context, new StableIceCoreModel(context.bakeLayer(TFModelLayers.STABLE_ICE_CORE)), 0.4F);
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack stack) {
		stack.translate(0.0F, Mth.sin(state.ageInTicks * 0.2F) * 0.15F, 0.0F);
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
