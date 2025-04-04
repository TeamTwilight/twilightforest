package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DirectionalBlock;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.FireflyBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.FireflyModel;

public class FireflyRenderer implements BlockEntityRenderer<FireflyBlockEntity> {

	private final FireflyModel fireflyModel;
	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("firefly-tiny.png");

	public FireflyRenderer(BlockEntityRendererProvider.Context context) {
		this.fireflyModel = new FireflyModel(context.bakeLayer(TFModelLayers.FIREFLY));
	}

	@Override
	public void render(FireflyBlockEntity entity, float partialTick, PoseStack stack, MultiBufferSource source, int light, int overlay) {
		renderFirefly(this.fireflyModel, entity.currentYaw, entity.glowIntensity, entity.randRot, entity.getBlockState().getValue(DirectionalBlock.FACING), stack, source, light, overlay);
	}

	public static void renderFirefly(FireflyModel model, int yaw, float glow, float rotation, Direction facing, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		stack.pushPose();
		stack.translate(0.5F, 0.5F, 0.5F);
		stack.mulPose(facing.getRotation());
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		stack.mulPose(Axis.YP.rotationDegrees(180.0F + rotation));
		stack.mulPose(Axis.YN.rotationDegrees(yaw));

		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
		model.setupGlow();
		model.renderToBuffer(stack, consumer, light, overlay);

		consumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));
		model.renderGlow(stack, consumer, overlay, glow);

		stack.popPose();
	}
}
