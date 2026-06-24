package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.BrazierBlock;
import twilightforest.block.entity.BrazierBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.state.block.BrazierRenderState;

public class BrazierRenderer implements BlockEntityRenderer<BrazierBlockEntity, BrazierRenderState> {

	private final BlockModelResolver blockResolver;
	private final BrazierModel model;
	public static final Identifier TEXTURE_OFF = TwilightForestMod.getModelTexture("brazier/brazier.png");
	public static final Identifier TEXTURE_ON = TwilightForestMod.getModelTexture("brazier/brazier_lit.png");
	public static final Identifier TEXTURE_OVERLAY = TwilightForestMod.getModelTexture("brazier/brazier_overlay.png");

	public BrazierRenderer(BlockEntityRendererProvider.Context context) {
		this.blockResolver = context.blockModelResolver();
		this.model = new BrazierModel(context.bakeLayer(TFModelLayers.BRAZIER));
	}

	@Override
	public void submit(BrazierRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		stack.pushPose();
		float y = 0.35F * state.light.getFireSize();
		stack.translate(0.26F, 1.6F, 0.5F);
		stack.scale(0.35F, y, 0.35F);
		stack.mulPose(Axis.YP.rotationDegrees(45.0F));
		if (state.light.isLit() && y > 0.0F) {
			state.fire.submit(stack, collector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
		}
		stack.popPose();
		stack.pushPose();
		stack.translate(0.5F, 1.5F, 0.5F);
		stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		Identifier loc = state.light.isLit() ? TEXTURE_ON : TEXTURE_OFF;
		collector.submitModel(this.model, Unit.INSTANCE, stack, this.model.renderType(loc), state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
		if (state.light.isLit()) {
			collector.order(1).submitModel(this.model, Unit.INSTANCE, stack, this.model.renderType(TEXTURE_OVERLAY), LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
		}
		stack.popPose();
	}

	@Override
	public BrazierRenderState createRenderState() {
		return new BrazierRenderState();
	}

	@Override
	public void extractRenderState(BrazierBlockEntity blockEntity, BrazierRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.light = blockEntity.getBlockState().getValue(BrazierBlock.LIGHT);
		this.blockResolver.update(state.fire, Blocks.FIRE.defaultBlockState(), BlockDisplayContext.create());
	}

	@Override
	public AABB getRenderBoundingBox(BrazierBlockEntity blockEntity) {
		BlockPos pos = blockEntity.getBlockPos();
		return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 2.0, pos.getZ() + 1.0);
	}
}
