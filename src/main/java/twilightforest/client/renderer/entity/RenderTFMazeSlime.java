package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class RenderTFMazeSlime extends SlimeRenderer {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("mazeslime.png");

	public RenderTFMazeSlime(EntityRendererManager manager, float shadowSize) {
		super(manager);
		this.shadowSize = shadowSize;
	}

	@Override
	public void render(SlimeEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		if(this.entityModel.isSitting) matrixStackIn.translate(0, 0.25F, 0);
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(SlimeEntity entity) {
		return textureLoc;
	}
}
