package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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

		stack.push();

		double blockInX = (goblin.block.getX() - goblin.getX());
		double blockInY = (goblin.block.getY() - goblin.getY());
		double blockInZ = (goblin.block.getZ() - goblin.getZ());

		IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.getLayer(textureLoc));
		stack.translate(blockInX, blockInY, blockInZ);

		float pitch = goblin.prevRotationPitch + (goblin.rotationPitch - goblin.prevRotationPitch) * partialTicks;
		stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180 - MathHelper.wrapDegrees(yaw)));
		stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(pitch));

		stack.scale(-1.0F, -1.0F, 1.0F);
		this.model.render(stack, ivertexbuilder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.pop();
		
		//when you allowed debugBoundingBox, you can see Hitbox
		if (this.renderManager.isDebugBoundingBox() && !goblin.block.isInvisible() && !Minecraft.getInstance().isReducedDebug()) {
			stack.push();
			stack.translate(blockInX, blockInY, blockInZ);
			this.renderMultiBoundingBox(stack, buffer.getBuffer(RenderType.getLines()), goblin.block, 0.25F, 1.0F, 0.0F);
			stack.pop();
		}

		renderChain(goblin, goblin.chain1, yaw, partialTicks, stack, buffer, light);
		renderChain(goblin, goblin.chain2, yaw, partialTicks, stack, buffer, light);
		renderChain(goblin, goblin.chain3, yaw, partialTicks, stack, buffer, light);
	}

	private void renderChain(T goblin, Entity chain, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		if (chain != null) {
			double chainInX = (chain.getX() - goblin.getX());
			double chainInY = (chain.getY() - goblin.getY());
			double chainInZ = (chain.getZ() - goblin.getZ());

			stack.push();
			IVertexBuilder ivertexbuilder = buffer.getBuffer(this.chainModel.getLayer(textureLoc));

			stack.translate(chainInX, chainInY, chainInZ);
			float pitch = chain.prevRotationPitch + (chain.rotationPitch - chain.prevRotationPitch) * partialTicks;
			stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180 - MathHelper.wrapDegrees(yaw)));
			stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(pitch));

			stack.scale(-1.0F, -1.0F, 1.0F);
			this.chainModel.render(stack, ivertexbuilder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
			stack.pop();

			//when you allowed debugBoundingBox, you can see Hitbox
			if (this.renderManager.isDebugBoundingBox() && !chain.isInvisible() && !Minecraft.getInstance().isReducedDebug()) {
				stack.push();
				stack.translate(chainInX, chainInY, chainInZ);
				this.renderMultiBoundingBox(stack, buffer.getBuffer(RenderType.getLines()), chain, 0.25F, 1.0F, 0.0F);
				stack.pop();
			}
		}
	}

	private void renderMultiBoundingBox(MatrixStack stack, IVertexBuilder builder, Entity entity, float red, float grean, float blue) {
		AxisAlignedBB axisalignedbb = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ());
		WorldRenderer.drawBox(stack, builder, axisalignedbb, red, grean, blue, 1.0F);
	}

	@Override
	public boolean shouldRender(T entity, ClippingHelperImpl clippingHelper, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
		if (super.shouldRender(entity, clippingHelper, p_225626_3_, p_225626_5_, p_225626_7_)) {
			return true;
		} else {

			Vec3d vec3d = this.getPosition(entity.block, (double) entity.block.getHeight() * 0.5D, 1.0F);
			Vec3d vec3d1 = this.getPosition(entity.block, (double) entity.block.getEyeHeight(), 1.0F);
			return clippingHelper.isVisible(new AxisAlignedBB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z));
		}
	}

	private Vec3d getPosition(Entity p_177110_1_, double p_177110_2_, float p_177110_4_) {
		// [VanillaCopy] From GuardianRenderer
		double d0 = MathHelper.lerp((double) p_177110_4_, p_177110_1_.lastTickPosX, p_177110_1_.getX());
		double d1 = MathHelper.lerp((double) p_177110_4_, p_177110_1_.lastTickPosY, p_177110_1_.getY()) + p_177110_2_;
		double d2 = MathHelper.lerp((double) p_177110_4_, p_177110_1_.lastTickPosZ, p_177110_1_.getZ());
		return new Vec3d(d0, d1, d2);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
