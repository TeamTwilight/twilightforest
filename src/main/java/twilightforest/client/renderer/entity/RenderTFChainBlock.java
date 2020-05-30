package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFChainBlock;

public class RenderTFChainBlock<T extends EntityTFChainBlock> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("blockgoblin.png");
	private final Model model;

	public RenderTFChainBlock(EntityRendererManager manager, Model modelTFSpikeBlock) {
		super(manager);
		this.model = modelTFSpikeBlock;
	}

	@Override
	public void render(T chainBlock, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		super.render(chainBlock, yaw, partialTicks, stack, buffer, light);

		stack.push();
		//stack.translate(x, y, z);

//		this.bindEntityTexture(chainBlock);

		stack.scale(-1.0F, -1.0F, 1.0F);

//		RenderSystem.rotatef(MathHelper.wrapDegrees((float) y), 1, 0, 1);
//		RenderSystem.rotatef(MathHelper.wrapDegrees(((float) x + (float) z) * 11F), 0, 1, 0);
//        GlStateManager.rotatef(MathHelper.wrapDegrees((float)yaw), 0, 0, 1);

//		this.model.render(chainBlock, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		stack.pop();

		//TODO: idk what's supposed to be done here
//		renderManager.renderEntityStatic(chainBlock.chain1, partialTicks, false);
//		renderManager.renderEntityStatic(chainBlock.chain2, partialTicks, false);
//		renderManager.renderEntityStatic(chainBlock.chain3, partialTicks, false);
//		renderManager.renderEntityStatic(chainBlock.chain4, partialTicks, false);
//		renderManager.renderEntityStatic(chainBlock.chain5, partialTicks, false);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFChainBlock entity) {
		return textureLoc;
	}
}
