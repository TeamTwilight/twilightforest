package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFChainBlock;

public class RenderTFChainBlock extends Render<EntityTFChainBlock> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "blockgoblin.png");
	private final ModelBase model;

	public RenderTFChainBlock(RenderManager manager, ModelBase modelTFSpikeBlock) {
		super(manager);
		this.model = modelTFSpikeBlock;
	}

	@Override
	public void doRender(EntityTFChainBlock chainBlock, double x, double y, double z, float yaw, float partialTicks) {
		super.doRender(chainBlock, x, y, z, yaw, partialTicks);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		this.bindEntityTexture(chainBlock);

		GlStateManager.scale(-1.0F, -1.0F, 1.0F);


		GlStateManager.rotate(MathHelper.wrapDegrees((float) y), 1, 0, 1);
		GlStateManager.rotate(MathHelper.wrapDegrees(((float) x + (float) z) * 11F), 0, 1, 0);
//        GlStateManager.rotate(MathHelper.wrapDegrees((float)yaw), 0, 0, 1);


		this.model.render(chainBlock, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		renderManager.renderEntityStatic(chainBlock.chain1, partialTicks, false);
		renderManager.renderEntityStatic(chainBlock.chain2, partialTicks, false);
		renderManager.renderEntityStatic(chainBlock.chain3, partialTicks, false);
		renderManager.renderEntityStatic(chainBlock.chain4, partialTicks, false);
		renderManager.renderEntityStatic(chainBlock.chain5, partialTicks, false);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFChainBlock entity) {
		return textureLoc;
	}
}
