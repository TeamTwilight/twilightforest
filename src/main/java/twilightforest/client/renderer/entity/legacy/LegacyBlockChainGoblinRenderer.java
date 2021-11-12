package twilightforest.client.renderer.entity.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.ChainModel;
import twilightforest.client.model.entity.SpikeBlockModel;
import twilightforest.client.model.entity.legacy.BlockChainGoblinLegacyModel;
import twilightforest.client.renderer.entity.BlockChainRenderer;
import twilightforest.entity.monster.BlockChainGoblin;

public class LegacyBlockChainGoblinRenderer<T extends BlockChainGoblin, M extends BlockChainGoblinLegacyModel<T>> extends HumanoidMobRenderer<T, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("blockgoblin.png");

	private final Model model;
	private final Model chainModel;

	public LegacyBlockChainGoblinRenderer(EntityRendererProvider.Context manager, M goblinModel, float shadowSize) {
		super(manager, goblinModel, shadowSize);
		model = new SpikeBlockModel(manager.bakeLayer(TFModelLayers.CHAIN_BLOCK));
		chainModel = new ChainModel(manager.bakeLayer(TFModelLayers.CHAIN));
	}

	@Override
	public void render(T goblin, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(goblin, yaw, partialTicks, stack, buffer, light);

		stack.pushPose();

		double blockInX = (goblin.block.getX() - goblin.getX());
		double blockInY = (goblin.block.getY() - goblin.getY());
		double blockInZ = (goblin.block.getZ() - goblin.getZ());

		VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(textureLoc));
		stack.translate(blockInX, blockInY, blockInZ);

		float pitch = goblin.xRotO + (goblin.getXRot() - goblin.xRotO) * partialTicks;
		stack.mulPose(Vector3f.YP.rotationDegrees(180 - Mth.wrapDegrees(yaw)));
		stack.mulPose(Vector3f.XP.rotationDegrees(pitch));

		stack.scale(-1.0F, -1.0F, 1.0F);
		this.model.renderToBuffer(stack, ivertexbuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();

		BlockChainRenderer.renderChain(goblin, goblin.chain1, yaw, partialTicks, stack, buffer, light, chainModel);
		BlockChainRenderer.renderChain(goblin, goblin.chain2, yaw, partialTicks, stack, buffer, light, chainModel);
		BlockChainRenderer.renderChain(goblin, goblin.chain3, yaw, partialTicks, stack, buffer, light, chainModel);
		
		//when you allowed debugBoundingBox, you can see Hitbox
		if (this.entityRenderDispatcher.shouldRenderHitBoxes() && !goblin.block.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo()) {
			stack.pushPose();
			stack.translate(blockInX, blockInY, blockInZ);
			this.renderMultiBoundingBox(stack, buffer.getBuffer(RenderType.lines()), goblin.block, 0.25F, 1.0F, 0.0F);
			stack.popPose();
		}

	}

	private void renderMultiBoundingBox(PoseStack stack, VertexConsumer builder, Entity entity, float red, float grean, float blue) {
		AABB axisalignedbb = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());
		LevelRenderer.renderLineBox(stack, builder, axisalignedbb, red, grean, blue, 1.0F);
	}

	@Override
	public boolean shouldRender(T entity, Frustum clippingHelper, double camX, double camY, double camZ) {
		if (super.shouldRender(entity, clippingHelper, camX, camY, camZ)) {
			return true;
		} else {

			Vec3 vec3d = this.getPosition(entity.block, entity.block.getBbHeight() * 0.5D, 1.0F);
			Vec3 vec3d1 = this.getPosition(entity.block, entity.block.getEyeHeight(), 1.0F);
			return clippingHelper.isVisible(new AABB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z));
		}
	}

	private Vec3 getPosition(Entity entity, double p_177110_2_, float p_177110_4_) {
		// [VanillaCopy] From GuardianRenderer
		double d0 = Mth.lerp(p_177110_4_, entity.xOld, entity.getX());
		double d1 = Mth.lerp(p_177110_4_, entity.yOld, entity.getY()) + p_177110_2_;
		double d2 = Mth.lerp(p_177110_4_, entity.zOld, entity.getZ());
		return new Vec3(d0, d1, d2);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return textureLoc;
	}
}
