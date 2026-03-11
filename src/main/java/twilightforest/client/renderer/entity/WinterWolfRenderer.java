package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.monster.HostileWolf;

public class WinterWolfRenderer extends HostileWolfRenderer {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("winterwolf.png");

	public WinterWolfRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 1.0F;
	}

	@Override
	protected void scale(HostileWolf entity, PoseStack stack, float partialTicks) {
		float wolfScale = 1.9F;
		stack.scale(wolfScale, wolfScale, wolfScale);
	}

	@Override
	public ResourceLocation getTextureLocation(HostileWolf entity) {
		return TEXTURE;
	}
}
