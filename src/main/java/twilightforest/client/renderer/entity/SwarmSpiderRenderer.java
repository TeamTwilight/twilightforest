package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.monster.SwarmSpider;

public class SwarmSpiderRenderer extends SpiderRenderer<SwarmSpider> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("swarmspider.png");

	public SwarmSpiderRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 0.25F;
	}

	@Override
	public ResourceLocation getTextureLocation(SwarmSpider entity) {
		return textureLoc;
	}

	@Override
	public void render(SwarmSpider entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		if(this.model.riding) matrixStackIn.translate(0, 0.15F, 0);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected void scale(SwarmSpider entity, PoseStack stack, float partialTicks) {
		float scale = 0.5F;
		stack.scale(scale, scale, scale);
	}
}
