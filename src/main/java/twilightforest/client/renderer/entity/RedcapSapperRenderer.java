package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;

public class RedcapSapperRenderer extends RedcapRenderer {

	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("redcapsapper.png");

	public RedcapSapperRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public Identifier getTextureLocation(HumanoidRenderState state) {
		return TEXTURE;
	}
}
