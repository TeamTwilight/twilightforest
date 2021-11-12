package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.monster.KingSpider;

public class KingSpiderRenderer extends SpiderRenderer<KingSpider> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("kingspider.png");

	public KingSpiderRenderer(EntityRendererProvider.Context manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getTextureLocation(KingSpider entity) {
		return textureLoc;
	}

	@Override
	protected void scale(KingSpider entity, PoseStack stack, float partialTicks) {
		float scale = 1.9F;
		stack.scale(scale, scale, scale);
	}
}
