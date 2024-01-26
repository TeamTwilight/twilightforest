package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.ChainModel;
import twilightforest.client.model.entity.SpikeBlockModel;
import twilightforest.entity.projectile.ChainBlock;

public class BlockChainRenderer extends EntityRenderer<ChainBlock> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("block_and_chain.png");
	private final Model model;
	private final Model chainModel;

	public BlockChainRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.model = new SpikeBlockModel(manager.bakeLayer(TFModelLayers.CHAIN_BLOCK));
		this.chainModel = new ChainModel(manager.bakeLayer(TFModelLayers.CHAIN));
	}

	@Override
	public void render(ChainBlock chainBlock, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		super.render(chainBlock, yaw, partialTicks, stack, buffer, light);

		stack.pushPose();
		VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(TEXTURE), false, chainBlock.isFoil());

		stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, chainBlock.yRotO, chainBlock.getYRot()) - 90.0F));
		stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, chainBlock.xRotO, chainBlock.getXRot())));

		stack.scale(-1.0F, -1.0F, 1.0F);
		this.model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();

		Entity owner = chainBlock.getOwner();
		if (owner != null) {
			Vec3 xyz = owner.getEyePosition(partialTicks).subtract(chainBlock.getEyePosition(partialTicks));
			double links = xyz.length();
			xyz = xyz.normalize();
			int ownerLight = Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(owner, partialTicks);
			for (int i = 1; i < links; i++) {
				renderChain(chainBlock, xyz, links - i, yaw, partialTicks, stack, buffer, Math.max(light, ownerLight), this.chainModel);
			}
		}
	}

	public static void renderChain(Entity chainBlock, Vec3 xyz, double scale, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, Model chainModel) {
		Vec3 pos = xyz.scale(scale);

		stack.pushPose();
		VertexConsumer vertexConsumer;
		if(chainBlock instanceof ChainBlock block) {
			vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, chainModel.renderType(TEXTURE), false, block.isFoil());
		} else {
			vertexConsumer = buffer.getBuffer(chainModel.renderType(TEXTURE));
		}

		stack.translate(pos.x(), pos.y(), pos.z());

		stack.scale(-1.0F, -1.0F, 1.0F);
		chainModel.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(ChainBlock entity) {
		return TEXTURE;
	}
}
