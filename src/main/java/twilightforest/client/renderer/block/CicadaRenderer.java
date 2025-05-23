package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DirectionalBlock;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.CicadaBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;

public class CicadaRenderer implements BlockEntityRenderer<CicadaBlockEntity> {

	private final CicadaModel cicadaModel;
	public static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("cicada-model.png");

	public CicadaRenderer(BlockEntityRendererProvider.Context context) {
		this.cicadaModel = new CicadaModel(context.bakeLayer(TFModelLayers.CICADA));
	}

	@Override
	public void render(CicadaBlockEntity entity, float partialTick, PoseStack stack, MultiBufferSource source, int light, int overlay) {
		renderCicada(this.cicadaModel, entity.currentYaw, entity.randRot, entity.getBlockState().getValue(DirectionalBlock.FACING), stack, source, light, overlay);
	}

	public static void renderCicada(CicadaModel model, float yaw, float rotation, Direction facing, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		stack.pushPose();
		stack.translate(0.5F, 0.5F, 0.5F);
		stack.mulPose(facing.getRotation());
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		stack.mulPose(Axis.YP.rotationDegrees(180.0F + rotation));
		stack.mulPose(Axis.YN.rotationDegrees(yaw));

		VertexConsumer consumer = buffer.getBuffer(model.renderType(TEXTURE));
		model.renderToBuffer(stack, consumer, light, overlay);
		stack.popPose();
	}
}
