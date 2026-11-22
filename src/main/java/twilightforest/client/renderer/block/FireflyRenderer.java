package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.CicadaBlock;
import twilightforest.block.entity.FireflyBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.FireflyModel;
import twilightforest.client.state.block.FireflyRenderState;

public class FireflyRenderer implements BlockEntityRenderer<FireflyBlockEntity, FireflyRenderState> {
	private final FireflyModel fireflyModel;
	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("firefly-tiny.png");

	public FireflyRenderer(BlockEntityRendererProvider.Context context) {
		this.fireflyModel = new FireflyModel(context.bakeLayer(TFModelLayers.FIREFLY));
	}

	@Override
	public void submit(FireflyRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		submitFirefly(this.fireflyModel, state.yaw, state.glowIntensity, state.rotation, state.facing, stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.breakProgress);
	}

	public static void submitFirefly(FireflyModel model, int yaw, float glow, float rotation, Direction facing, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		stack.pushPose();
		stack.translate(0.5F, 0.5F, 0.5F);
		stack.rotate(facing.getRotation());
		stack.rotate(Axis.ZP.rotationDegrees(180.0F));
		stack.rotate(Axis.YP.rotationDegrees(180.0F + rotation));
		stack.rotate(Axis.YN.rotationDegrees(yaw));
		RenderType bodyRenderType = RenderTypes.entityCutout(TEXTURE);
		RenderType glowRenderType = RenderTypes.entityTranslucentEmissive(TEXTURE);
		collector.submitModelPart(model.legs, stack, bodyRenderType, light, overlay, null, -1);
		collector.submitModelPart(model.fatBody, stack, bodyRenderType, light, overlay, null, -1);
		collector.submitModelPart(model.skinnyBody, stack, bodyRenderType, light, overlay, null, -1);
		collector.submitModelPart(model.glow, stack, glowRenderType, light, overlay, null, ARGB.white(glow));
		if (breakProgress != null) {
			collector.order(1).submitCrumblingOverlay(model.legs, stack, bodyRenderType, light, overlay, -1, breakProgress);
			collector.order(1).submitCrumblingOverlay(model.fatBody, stack, bodyRenderType, light, overlay, -1, breakProgress);
			collector.order(1).submitCrumblingOverlay(model.skinnyBody, stack, bodyRenderType, light, overlay, -1, breakProgress);
			collector.order(1).submitCrumblingOverlay(model.glow, stack, glowRenderType, light, overlay, ARGB.white(glow), breakProgress);
		}
		stack.popPose();
	}

	@Override
	public FireflyRenderState createRenderState() {
		return new FireflyRenderState();
	}

	@Override
	public void extractRenderState(FireflyBlockEntity blockEntity, FireflyRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.facing = blockEntity.getBlockState().getValue(CicadaBlock.FACING);
		state.yaw = blockEntity.currentYaw;
		state.rotation = blockEntity.randRot;
		state.glowIntensity = blockEntity.glowIntensity;
	}
}