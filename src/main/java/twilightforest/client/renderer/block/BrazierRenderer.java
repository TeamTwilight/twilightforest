package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.BrazierBlock;
import twilightforest.block.entity.BrazierBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.enums.BrazierLight;

public class BrazierRenderer implements BlockEntityRenderer<BrazierBlockEntity> {

	private final BrazierModel model;
	public static final ResourceLocation TEXTURE_OFF = TwilightForestMod.getModelTexture("brazier/brazier.png");
	public static final ResourceLocation TEXTURE_ON = TwilightForestMod.getModelTexture("brazier/brazier_lit.png");
	public static final ResourceLocation TEXTURE_OVERLAY = TwilightForestMod.getModelTexture("brazier/brazier_overlay.png");

	public BrazierRenderer(BlockEntityRendererProvider.Context context) {
		this.model = new BrazierModel(context.bakeLayer(TFModelLayers.BRAZIER));
	}

	@Override
	public void render(BrazierBlockEntity entity, float v, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		BrazierLight lit = entity.getBlockState().getValue(BrazierBlock.LIGHT);
		stack.pushPose();
		BlockRenderDispatcher dispatch = Minecraft.getInstance().getBlockRenderer();
		BlockState state = Blocks.FIRE.defaultBlockState();
		float y = 0.35F * lit.getFireSize();
		stack.translate(0.26F, 1.7F, 0.5F);
		stack.scale(0.35F, y, 0.35F);
		stack.mulPose(Axis.YP.rotationDegrees(45.0F));
		if (lit.isLit() && y > 0.0F) {
			dispatch.renderSingleBlock(state, stack, buffer, 0x0F00F0, overlay);
		}
		stack.popPose();
		stack.pushPose();
		stack.translate(0.5F, 1.5F, 0.5F);
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		ResourceLocation loc = entity.getBlockState().getValue(BrazierBlock.LIGHT).isLit() ? TEXTURE_ON : TEXTURE_OFF;
		VertexConsumer consumer = buffer.getBuffer(this.model.renderType(loc));
		this.model.renderToBuffer(stack, consumer, light, overlay);
		if (lit.isLit()) {
			VertexConsumer litoverlay = buffer.getBuffer(this.model.renderType(TEXTURE_OVERLAY));
			this.model.renderToBuffer(stack, litoverlay, 0x0F00F0, overlay);
		}
		stack.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(BrazierBlockEntity entity) {
		return true;
	}
}
