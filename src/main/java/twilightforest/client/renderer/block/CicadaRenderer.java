package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.CicadaBlock;
import twilightforest.block.entity.CicadaBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.CicadaModel;
import twilightforest.client.state.block.CicadaRenderState;

public class CicadaRenderer implements BlockEntityRenderer<CicadaBlockEntity, CicadaRenderState> {

	private final CicadaModel cicadaModel;
	private static final Identifier TEXTURE = TwilightForestMod.getModelTexture("cicada-model.png");

	public CicadaRenderer(BlockEntityRendererProvider.Context context) {
		this.cicadaModel = new CicadaModel(context.bakeLayer(TFModelLayers.CICADA));
	}

	@Override
	public CicadaRenderState createRenderState() {
		return new CicadaRenderState();
	}

	@Override
	public void extractRenderState(CicadaBlockEntity blockEntity, CicadaRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.facing = blockEntity.getBlockState().getValue(CicadaBlock.FACING);
		state.yaw = blockEntity.currentYaw;
		state.rotation = blockEntity.randRot;
	}

	@Override
	public void submit(CicadaRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		submitCicada(this.cicadaModel, state.yaw, state.rotation, state.facing, stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
	}

	public static void submitCicada(CicadaModel model, float yaw, float rotation, Direction facing, PoseStack stack, SubmitNodeCollector collector, int light, int overlay, int outlineColor, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		stack.pushPose();
		stack.translate(0.5F, 0.5F, 0.5F);
		stack.mulPose(facing.getRotation());
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		stack.mulPose(Axis.YP.rotationDegrees(180.0F + rotation));
		stack.mulPose(Axis.YN.rotationDegrees(yaw));
		collector.submitModel(model, Unit.INSTANCE, stack, model.renderType(TEXTURE), light, overlay, outlineColor, breakProgress);
		stack.popPose();
	}
}
