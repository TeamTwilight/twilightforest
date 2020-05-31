package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFBlockGoblin;
import twilightforest.client.model.entity.ModelTFGoblinChain;
import twilightforest.client.model.entity.ModelTFSpikeBlock;
import twilightforest.entity.EntityTFBlockGoblin;

public class RenderTFBlockGoblin<T extends EntityTFBlockGoblin, M extends ModelTFBlockGoblin<T>> extends BipedRenderer<T, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("blockgoblin.png");

	private final Model model = new ModelTFSpikeBlock();
	private final Model chainModel = new ModelTFGoblinChain();

	public RenderTFBlockGoblin(EntityRendererManager manager, M model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	public void render(T goblin, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		super.render(goblin, yaw, partialTicks, stack, buffer, light);
		double x = MathHelper.lerp((double) partialTicks, goblin.lastTickPosX, goblin.getX());
		double y = MathHelper.lerp((double) partialTicks, goblin.lastTickPosY, goblin.getY());
		double z = MathHelper.lerp((double) partialTicks, goblin.lastTickPosZ, goblin.getZ());
		stack.push();

		double blockInX = (goblin.block.getX() - goblin.getX());
		double blockInY = (goblin.block.getY() - goblin.getY());
		double blockInZ = (goblin.block.getZ() - goblin.getZ());

		double chain1InX = (goblin.chain1.getX() - goblin.getX());
		double chain1InY = (goblin.chain1.getY() - goblin.getY());
		double chain1InZ = (goblin.chain1.getZ() - goblin.getZ());

		double chain2InX = (goblin.chain2.getX() - goblin.getX());
		double chain2InY = (goblin.chain2.getY() - goblin.getY());
		double chain2InZ = (goblin.chain2.getZ() - goblin.getZ());

		double chain3InX = (goblin.chain1.getX() - goblin.getX());
		double chain3InY = (goblin.chain1.getY() - goblin.getY());
		double chain3InZ = (goblin.chain1.getZ() - goblin.getZ());

		IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.getLayer(textureLoc));

		stack.translate(blockInX, blockInY, blockInZ);
		stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(((float) y)));
		stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(((float) y)));
		stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(((float) x + (float) z) * 11F));
		stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(yaw));

		this.model.render(stack, ivertexbuilder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.pop();

		renderChain(goblin, yaw, partialTicks, stack, buffer, light, chain1InX, chain1InY, chain1InZ);
		renderChain(goblin, yaw, partialTicks, stack, buffer, light, chain2InX, chain2InY, chain2InZ);
		renderChain(goblin, yaw, partialTicks, stack, buffer, light, chain3InX, chain3InY, chain3InZ);
	}

	private void renderChain(T goblin, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light, double chainInX, double chainInY, double chainInZ) {
		double x = MathHelper.lerp((double) partialTicks, goblin.lastTickPosX, goblin.getX());
		double y = MathHelper.lerp((double) partialTicks, goblin.lastTickPosY, goblin.getY());
		double z = MathHelper.lerp((double) partialTicks, goblin.lastTickPosZ, goblin.getZ());

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
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
