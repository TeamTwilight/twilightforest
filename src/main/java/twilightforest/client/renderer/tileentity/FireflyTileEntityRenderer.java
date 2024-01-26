package twilightforest.client.renderer.tileentity;

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

public class FireflyTileEntityRenderer implements BlockEntityRenderer<FireflyBlockEntity> {

	private final FireflyModel fireflyModel;
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("firefly-tiny.png");

	public FireflyTileEntityRenderer(BlockEntityRendererProvider.Context renderer) {
		this.fireflyModel = new FireflyModel(renderer.bakeLayer(TFModelLayers.FIREFLY));
	}

	@Override
	public void render(@Nullable FireflyBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		int yaw = te != null ? te.currentYaw : BugModelAnimationHelper.currentYaw;
		float glow = te != null ? te.glowIntensity : BugModelAnimationHelper.glowIntensity;
		float randRot = te != null ? te.randRot : 0.0F;

		ms.pushPose();
		Direction facing = te != null ? te.getBlockState().getValue(DirectionalBlock.FACING) : Direction.NORTH;

		ms.translate(0.5F, 0.5F, 0.5F);
		ms.mulPose(facing.getRotation());
		ms.mulPose(Axis.ZP.rotationDegrees(180.0F));
		ms.mulPose(Axis.YP.rotationDegrees(180.0F + randRot));
		ms.mulPose(Axis.YN.rotationDegrees(yaw));

		ms.pushPose();

		VertexConsumer builder = buffer.getBuffer(RenderType.entityCutout(textureLoc));
		fireflyModel.renderToBuffer(ms, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		builder = buffer.getBuffer(RenderType.entityTranslucent(textureLoc));
		fireflyModel.glow.render(ms, builder, 0xF000F0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, glow);

		ms.popPose();
		ms.popPose();
	}
}
