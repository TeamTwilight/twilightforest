package twilightforest.client.renderer.tileentity;

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

public class CicadaTileEntityRenderer implements BlockEntityRenderer<CicadaBlockEntity> {

	private final CicadaModel cicadaModel;
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("cicada-model.png");

	public CicadaTileEntityRenderer(BlockEntityRendererProvider.Context renderer) {
		this.cicadaModel = new CicadaModel(renderer.bakeLayer(TFModelLayers.CICADA));
	}

	@Override
	public void render(@Nullable CicadaBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		int yaw = te != null ? te.currentYaw : BugModelAnimationHelper.currentYaw;

		ms.pushPose();
		Direction facing = te != null ? te.getBlockState().getValue(DirectionalBlock.FACING) : Direction.NORTH;
		float randRot = te != null ? te.randRot : 0.0F;

		ms.translate(0.5F, 0.5F, 0.5F);
		ms.mulPose(facing.getRotation());
		ms.mulPose(Axis.ZP.rotationDegrees(180.0F));
		ms.mulPose(Axis.YP.rotationDegrees(180.0F + randRot));
		ms.mulPose(Axis.YN.rotationDegrees(yaw));

		VertexConsumer vertex = buffers.getBuffer(cicadaModel.renderType(textureLoc));
		cicadaModel.renderToBuffer(ms, vertex, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		ms.popPose();
	}
}
