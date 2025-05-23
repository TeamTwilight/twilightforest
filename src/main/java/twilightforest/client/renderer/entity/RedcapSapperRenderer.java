package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public class RedcapSapperRenderer extends RedcapRenderer {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("redcapsapper.png");

	public RedcapSapperRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(HumanoidRenderState state) {
		return TEXTURE;
	}
}
