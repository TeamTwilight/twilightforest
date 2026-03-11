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
	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("firefly-tiny.png");

	public FireflyRenderer(BlockEntityRendererProvider.Context context) {
		this.fireflyModel = new FireflyModel(context.bakeLayer(TFModelLayers.FIREFLY));
	}

	@Override
	public void render(@Nullable FireflyBlockEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		int yaw = entity != null ? entity.currentYaw : BugModelAnimationHelper.currentYaw;
		float glow = entity != null ? entity.glowIntensity : BugModelAnimationHelper.glowIntensity;
		float randRot = entity != null ? entity.randRot : 0.0F;

		stack.pushPose();
		Direction facing = entity != null ? entity.getBlockState().getValue(DirectionalBlock.FACING) : Direction.NORTH;

		stack.translate(0.5F, 0.5F, 0.5F);
		stack.mulPose(facing.getRotation());
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		stack.mulPose(Axis.YP.rotationDegrees(180.0F + randRot));
		stack.mulPose(Axis.YN.rotationDegrees(yaw));

		stack.pushPose();

		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
		this.fireflyModel.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY);

		consumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
		this.fireflyModel.renderGlow(stack, consumer, glow);

		stack.popPose();
		stack.popPose();
	}
}
