package twilightforest.client.renderer.entity;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.Boar;

public class BoarRenderer extends MobRenderer<Boar, PigRenderState, PigModel> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("wildboar.png");

	public BoarRenderer(EntityRendererProvider.Context context, PigModel model) {
		super(context, model, 0.7F);
	}

	@Override
	public PigRenderState createRenderState() {
		return new PigRenderState();
	}

	@Override
	public ResourceLocation getTextureLocation(PigRenderState state) {
		return TEXTURE;
	}
}
