package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.AdherentModel;
import twilightforest.entity.monster.Adherent;

public class AdherentRenderer extends HumanoidMobRenderer<Adherent, HumanoidRenderState, AdherentModel> {

	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("adherent.png");

	public AdherentRenderer(EntityRendererProvider.Context context) {
		super(context, new AdherentModel(context.bakeLayer(TFModelLayers.ADHERENT)), 0.625F);
	}

	@Override
	public HumanoidRenderState createRenderState() {
		return new HumanoidRenderState();
	}

	@Override
	protected void scale(HumanoidRenderState state, PoseStack stack) {
		stack.translate(0.0F, -0.125F - Mth.sin(state.ageInTicks * 0.133F) * 0.1F, 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(HumanoidRenderState state) {
		return TEXTURE;
	}
}
