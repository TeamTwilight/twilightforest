package twilightforest.client.renderer.tileentity;

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
import twilightforest.block.entity.MoonwormBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.MoonwormModel;

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
		float randRot = te != null ? te.randRot : 0.0F;

		ms.pushPose();
		Direction facing = te != null ? te.getBlockState().getValue(DirectionalBlock.FACING) : Direction.NORTH;

		ms.translate(0.5F, 0.5F, 0.5F);
		ms.mulPose(facing.getRotation());
		ms.mulPose(Axis.ZP.rotationDegrees(180.0F));
		ms.mulPose(Axis.YP.rotationDegrees(180.0F + randRot));
		ms.mulPose(Axis.YN.rotationDegrees(yaw));

		VertexConsumer builder = buffer.getBuffer(this.moonwormModel.renderType(textureLoc));
		this.moonwormModel.setRotationAngles(te, partialTicks);
		this.moonwormModel.renderToBuffer(ms, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		ms.popPose();
	}
}
