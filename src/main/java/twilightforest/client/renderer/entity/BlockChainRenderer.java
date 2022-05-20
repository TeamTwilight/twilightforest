package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.ChainModel;
import twilightforest.client.model.entity.SpikeBlockModel;
import twilightforest.entity.ChainBlock;

public class BlockChainRenderer extends EntityRenderer<ChainBlock> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("blockgoblin.png");
	private final Model model;
	private final Model chainModel;

	public BlockChainRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		model = new SpikeBlockModel(manager.bakeLayer(TFModelLayers.CHAIN_BLOCK));
		chainModel = new ChainModel(manager.bakeLayer(TFModelLayers.CHAIN));
	}

	@Override
	public void render(ChainBlock chainBlock, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(chainBlock, yaw, partialTicks, stack, buffer, light);

		stack.pushPose();
		VertexConsumer ivertexbuilder = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(textureLoc), false, chainBlock.isFoil());

		float pitch = chainBlock.xRotO + (chainBlock.getXRot() - chainBlock.xRotO) * partialTicks;
		stack.mulPose(Vector3f.YP.rotationDegrees(180 - Mth.wrapDegrees(yaw)));
		stack.mulPose(Vector3f.XP.rotationDegrees(pitch));

		stack.scale(-1.0F, -1.0F, 1.0F);
		this.model.renderToBuffer(stack, ivertexbuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();

		renderChain(chainBlock, chainBlock.chain1, yaw, partialTicks, stack, buffer, light, chainModel);
		renderChain(chainBlock, chainBlock.chain2, yaw, partialTicks, stack, buffer, light, chainModel);
		renderChain(chainBlock, chainBlock.chain3, yaw, partialTicks, stack, buffer, light, chainModel);
		renderChain(chainBlock, chainBlock.chain4, yaw, partialTicks, stack, buffer, light, chainModel);
		renderChain(chainBlock, chainBlock.chain5, yaw, partialTicks, stack, buffer, light, chainModel);
	}

	public static void renderChain(Entity parent, Entity chain, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, Model chainModel) {
		double chainInX = (Mth.lerp(partialTicks, chain.xOld, chain.getX()) - Mth.lerp(partialTicks, parent.xOld, parent.getX()));
		double chainInY = (Mth.lerp(partialTicks, chain.yOld, chain.getY()) - Mth.lerp(partialTicks, parent.yOld, parent.getY()));
		double chainInZ = (Mth.lerp(partialTicks, chain.zOld, chain.getZ()) - Mth.lerp(partialTicks, parent.zOld, parent.getZ()));

		stack.pushPose();
		VertexConsumer vertexConsumer;
		if(parent instanceof ChainBlock blocc) {
			vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, chainModel.renderType(textureLoc), false, blocc.isFoil());
		} else {
			vertexConsumer = buffer.getBuffer(chainModel.renderType(textureLoc));
		}

		stack.translate(chainInX, chainInY, chainInZ);
		float pitch = chain.xRotO + (chain.getXRot() - chain.xRotO) * partialTicks;
		stack.mulPose(Vector3f.YP.rotationDegrees(180 - Mth.wrapDegrees(yaw)));
		stack.mulPose(Vector3f.XP.rotationDegrees(pitch));

		stack.scale(-1.0F, -1.0F, 1.0F);
		chainModel.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
	}

	private void renderMultiBoundingBox(PoseStack stack, VertexConsumer builder, Entity entity, float red, float grean, float blue) {
		AABB axisalignedbb = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());
		LevelRenderer.renderLineBox(stack, builder, axisalignedbb, red, grean, blue, 1.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(ChainBlock entity) {
		return textureLoc;
	}
}
