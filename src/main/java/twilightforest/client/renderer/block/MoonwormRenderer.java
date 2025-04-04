package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DirectionalBlock;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.CicadaBlockEntity;
import twilightforest.block.entity.MoonwormBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.client.model.entity.MoonwormModel;

public class MoonwormRenderer implements BlockEntityRenderer<MoonwormBlockEntity> {

	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("moonworm.png");
	private final MoonwormModel moonwormModel;

	public MoonwormRenderer(BlockEntityRendererProvider.Context context) {
		this.moonwormModel = new MoonwormModel(context.bakeLayer(TFModelLayers.MOONWORM));
	}

	@Override
	public void render(MoonwormBlockEntity entity, float partialTick, PoseStack stack, MultiBufferSource source, int light, int overlay) {
		renderMoonworm(this.moonwormModel, entity.currentYaw, entity.randRot, (entity.desiredYaw - entity.currentYaw) - partialTick, entity.yawDelay, entity.getBlockState().getValue(DirectionalBlock.FACING), stack, source, light, overlay);
	}

	public static void renderMoonworm(MoonwormModel model, float yaw, float rotation, float wiggleRotation, int delay, Direction facing, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		stack.pushPose();
		stack.translate(0.5F, 0.5F, 0.5F);
		stack.mulPose(facing.getRotation());
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		stack.mulPose(Axis.YP.rotationDegrees(180.0F + rotation));
		stack.mulPose(Axis.YN.rotationDegrees(yaw));

		VertexConsumer consumer = buffer.getBuffer(model.renderType(TEXTURE));
		model.setupAnim(wiggleRotation, delay);
		model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY);

		stack.popPose();
	}
}
