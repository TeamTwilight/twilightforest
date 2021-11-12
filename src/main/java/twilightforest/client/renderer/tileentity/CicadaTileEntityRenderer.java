package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.block.entity.CicadaBlockEntity;

import javax.annotation.Nullable;

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

		ms.translate(0.5F, 0.5F, 0.5F);
		ms.mulPose(facing.getRotation());
		ms.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
		ms.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		ms.mulPose(Vector3f.YN.rotationDegrees(yaw));

		//ms.push();
		VertexConsumer vertex = buffers.getBuffer(cicadaModel.renderType(textureLoc));
		cicadaModel.renderToBuffer(ms, vertex, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		ms.popPose();
		//ms.pop();
	}
}
