package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.IceCrystalModel;
import twilightforest.entity.monster.IceCrystal;

public class IceCrystalRenderer extends MobRenderer<IceCrystal, LivingEntityRenderState, IceCrystalModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("icecrystal.png");

	public IceCrystalRenderer(EntityRendererProvider.Context context) {
		super(context, new IceCrystalModel(context.bakeLayer(TFModelLayers.ICE_CRYSTAL)), 0.25F);
	}

	@Override
	protected void scale(LivingEntityRenderState state, PoseStack stack) {
		stack.translate(0.0F, Mth.sin(state.ageInTicks * 0.2F) * 0.15F, 0.0F);
	}

	@Override
	protected int getModelTint(LivingEntityRenderState state) {
		return ARGB.colorFromFloat(state.deathTime > 0 ? 1.0F : 0.6F, 1.0F, 1.0F, 1.0F);
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
