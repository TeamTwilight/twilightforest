package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

public class WinterWolfRenderer extends HostileWolfRenderer {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("winterwolf.png");

	public WinterWolfRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 1.0F;
	}

	@Override
	protected void scale(WolfRenderState state, PoseStack stack) {
		stack.scale(1.9F, 1.9F, 1.9F);
	}

	@Override
	public ResourceLocation getTextureLocation(WolfRenderState state) {
		return TEXTURE;
	}
}
