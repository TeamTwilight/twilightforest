package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MoonwormModel;
import twilightforest.block.entity.MoonwormBlockEntity;

import javax.annotation.Nullable;

public class MoonwormTileEntityRenderer implements BlockEntityRenderer<MoonwormBlockEntity> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("moonworm.png");
	private final MoonwormModel moonwormModel;

	public MoonwormTileEntityRenderer(BlockEntityRendererProvider.Context renderer) {
		this.moonwormModel = new MoonwormModel(renderer.bakeLayer(TFModelLayers.MOONWORM));
	}

	@Override
	public void render(@Nullable MoonwormBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		int yaw = te != null ? te.currentYaw : BugModelAnimationHelper.currentRotation;
		if (te == null) partialTicks = Minecraft.getInstance().getFrameTime();

		ms.pushPose();
		Direction facing = te != null ? te.getBlockState().getValue(DirectionalBlock.FACING) : Direction.NORTH;

		ms.translate(0.5F, 0.5F, 0.5F);
		ms.mulPose(facing.getRotation());
		ms.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
		ms.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		ms.mulPose(Vector3f.YN.rotationDegrees(yaw));

		VertexConsumer builder = buffer.getBuffer(this.moonwormModel.renderType(textureLoc));
		this.moonwormModel.setRotationAngles(te, partialTicks);
		this.moonwormModel.renderToBuffer(ms, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		ms.popPose();
	}
}
