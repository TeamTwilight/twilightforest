package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;

public class RenderTFSpikeBlock<T extends Entity, M extends EntityModel<T>> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("blockgoblin.png");

	private final Model model;

	public RenderTFSpikeBlock(EntityRendererManager manager, M model) {
		super(manager);
		this.model = model;
	}

	@Override
	public void render(T goblin, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		super.render(goblin, yaw, partialTicks, stack, buffer, light);
		double x = MathHelper.lerp((double) partialTicks, goblin.lastTickPosX, goblin.getX());
		double y = MathHelper.lerp((double) partialTicks, goblin.lastTickPosY, goblin.getY());
		double z = MathHelper.lerp((double) partialTicks, goblin.lastTickPosZ, goblin.getZ());
		stack.push();
		stack.scale(-1.0F, -1.0F, 1.0F);

		IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.getLayer(textureLoc));

		//stack.multiply(MathHelper.wrapDegrees((float) y), 1, 0, 1);
		//stack.multiply(MathHelper.wrapDegrees(((float) x + (float) z) * 11F), 0, 1, 0);
		stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(yaw));

		this.model.render(stack, ivertexbuilder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.pop();

		//uh bruh...?
		/*renderManager.render(goblin.block, x, y, z, yaw, partialTicks, stack, buffer, light);
		renderManager.render(goblin.chain1, x, y, z, yaw, partialTicks, stack, buffer, light);
		renderManager.render(goblin.chain2, x, y, z, yaw, partialTicks, stack, buffer, light);
		renderManager.render(goblin.chain3, x, y, z, yaw, partialTicks, stack, buffer, light);*///renderEntity
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
