package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFGoblinChain;
import twilightforest.entity.EntityTFChainBlock;

public class RenderTFChainBlock<T extends EntityTFChainBlock> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("blockgoblin.png");
	private final Model model;
	private final Model chainModel = new ModelTFGoblinChain();

	public RenderTFChainBlock(EntityRendererManager manager, Model modelTFSpikeBlock) {
		super(manager);
		this.model = modelTFSpikeBlock;
	}

	@Override
	public void render(T chainBlock, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		super.render(chainBlock, yaw, partialTicks, stack, buffer, light);

		double x = MathHelper.lerp((double) partialTicks, chainBlock.lastTickPosX, chainBlock.getX());
		double y = MathHelper.lerp((double) partialTicks, chainBlock.lastTickPosY, chainBlock.getY());
		double z = MathHelper.lerp((double) partialTicks, chainBlock.lastTickPosZ, chainBlock.getZ());

		double chain1InX = (chainBlock.chain1.getX() - chainBlock.getX());
		double chain1InY = (chainBlock.chain1.getY() - chainBlock.getY());
		double chain1InZ = (chainBlock.chain1.getZ() - chainBlock.getZ());

		double chain2InX = (chainBlock.chain2.getX() - chainBlock.getX());
		double chain2InY = (chainBlock.chain2.getY() - chainBlock.getY());
		double chain2InZ = (chainBlock.chain2.getZ() - chainBlock.getZ());

		double chain3InX = (chainBlock.chain1.getX() - chainBlock.getX());
		double chain3InY = (chainBlock.chain1.getY() - chainBlock.getY());
		double chain3InZ = (chainBlock.chain1.getZ() - chainBlock.getZ());

		stack.push();
		stack.scale(-1.0F, -1.0F, 1.0F);

		IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.getLayer(textureLoc));

		stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(((float) y)));
		stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(((float) y)));
		stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(((float) x + (float) z) * 11F));
		stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(yaw));

		this.model.render(stack, ivertexbuilder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.pop();

		renderChain(chainBlock, yaw, partialTicks, stack, buffer, light, chain1InX, chain1InY, chain1InZ);
		renderChain(chainBlock, yaw, partialTicks, stack, buffer, light, chain2InX, chain2InY, chain2InZ);
		renderChain(chainBlock, yaw, partialTicks, stack, buffer, light, chain3InX, chain3InY, chain3InZ);
	}

	private void renderChain(T chainBlock, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light, double chainInX, double chainInY, double chainInZ) {
		double x = MathHelper.lerp((double) partialTicks, chainBlock.lastTickPosX, chainBlock.getX());
		double y = MathHelper.lerp((double) partialTicks, chainBlock.lastTickPosY, chainBlock.getY());
		double z = MathHelper.lerp((double) partialTicks, chainBlock.lastTickPosZ, chainBlock.getZ());

		stack.push();
		IVertexBuilder ivertexbuilder = buffer.getBuffer(this.chainModel.getLayer(textureLoc));

		stack.translate(chainInX, chainInY, chainInZ);
		stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(((float) y)));
		stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(((float) y)));
		stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(((float) x + (float) z) * 11F));
		stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(yaw));

		this.chainModel.render(stack, ivertexbuilder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFChainBlock entity) {
		return textureLoc;
	}
}
