package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFSwarmSpider;

public class RenderTFSwarmSpider extends SpiderRenderer<EntityTFSwarmSpider> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("swarmspider.png");

	public RenderTFSwarmSpider(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFSwarmSpider entity) {
		return textureLoc;
	}

	@Override
	public void render(EntityTFSwarmSpider entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if(this.entityModel.isSitting) matrixStackIn.translate(0, 0.15F, 0);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected void preRenderCallback(EntityTFSwarmSpider entity, MatrixStack stack, float partialTicks) {
		float scale = 0.5F;
		stack.scale(scale, scale, scale);
	}
}
