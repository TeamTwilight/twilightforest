package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
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

		double blockInX = (goblin.block.getPosX() - goblin.getPosX());
		double blockInY = (goblin.block.getPosY() - goblin.getPosY());
		double blockInZ = (goblin.block.getPosZ() - goblin.getPosZ());

		IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.getRenderType(textureLoc));
		stack.translate(blockInX, blockInY, blockInZ);

		float pitch = goblin.prevRotationPitch + (goblin.rotationPitch - goblin.prevRotationPitch) * partialTicks;
		stack.rotate(Vector3f.YP.rotationDegrees(180 - MathHelper.wrapDegrees(yaw)));
		stack.rotate(Vector3f.XP.rotationDegrees(pitch));

		stack.scale(-1.0F, -1.0F, 1.0F);
		this.model.render(stack, ivertexbuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.pop();

		RenderTFChainBlock.renderChain(goblin, goblin.chain1, yaw, partialTicks, stack, buffer, light, chainModel);
		RenderTFChainBlock.renderChain(goblin, goblin.chain2, yaw, partialTicks, stack, buffer, light, chainModel);
		RenderTFChainBlock.renderChain(goblin, goblin.chain3, yaw, partialTicks, stack, buffer, light, chainModel);
		
		//when you allowed debugBoundingBox, you can see Hitbox
		if (this.renderManager.isDebugBoundingBox() && !goblin.block.isInvisible() && !Minecraft.getInstance().isReducedDebug()) {
			stack.push();
			stack.translate(blockInX, blockInY, blockInZ);
			this.renderMultiBoundingBox(stack, buffer.getBuffer(RenderType.getLines()), goblin.block, 0.25F, 1.0F, 0.0F);
			stack.pop();
		}

	}

	private void renderMultiBoundingBox(MatrixStack stack, IVertexBuilder builder, Entity entity, float red, float grean, float blue) {
		AxisAlignedBB axisalignedbb = entity.getBoundingBox().offset(-entity.getPosX(), -entity.getPosY(), -entity.getPosZ());
		WorldRenderer.drawBoundingBox(stack, builder, axisalignedbb, red, grean, blue, 1.0F);
	}

	@Override
	public boolean shouldRender(T entity, ClippingHelper clippingHelper, double camX, double camY, double camZ) {
		if (super.shouldRender(entity, clippingHelper, camX, camY, camZ)) {
			return true;
		} else {

			Vector3d vec3d = this.getPosition(entity.block, entity.block.getHeight() * 0.5D, 1.0F);
			Vector3d vec3d1 = this.getPosition(entity.block, entity.block.getEyeHeight(), 1.0F);
			return clippingHelper.isBoundingBoxInFrustum(new AxisAlignedBB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z));
		}
	}

	private Vector3d getPosition(Entity entity, double p_177110_2_, float p_177110_4_) {
		// [VanillaCopy] From GuardianRenderer
		double d0 = MathHelper.lerp(p_177110_4_, entity.lastTickPosX, entity.getPosX());
		double d1 = MathHelper.lerp(p_177110_4_, entity.lastTickPosY, entity.getPosY()) + p_177110_2_;
		double d2 = MathHelper.lerp(p_177110_4_, entity.lastTickPosZ, entity.getPosZ());
		return new Vector3d(d0, d1, d2);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
