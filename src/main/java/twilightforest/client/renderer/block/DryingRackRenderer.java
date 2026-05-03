package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.Nullable;
import twilightforest.block.DryingRackBlock;
import twilightforest.block.entity.DryingRackBlockEntity;
import twilightforest.client.state.block.DryingRackRenderState;
import twilightforest.tags.TFItemTags;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackBlockEntity, DryingRackRenderState> {

	private final ItemModelResolver resolver;

	public DryingRackRenderer(BlockEntityRendererProvider.Context context) {
		this.resolver = context.itemModelResolver();
	}

	@Override
	public void submit(DryingRackRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		Direction dir = state.facing;
		stack.pushPose();
		stack.translate(0.4F * dir.getStepX() + 0.5F, state.renderOffset, 0.4F * dir.getStepZ() + 0.5F);
		stack.scale(0.99F, 0.99F, 0.99F); //fix possible z-fighting
		stack.mulPose(Axis.YP.rotationDegrees(-dir.toYRot()));
		state.item.submit(stack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		stack.popPose();
	}

	@Override
	public DryingRackRenderState createRenderState() {
		return new DryingRackRenderState();
	}

	@Override
	public void extractRenderState(DryingRackBlockEntity blockEntity, DryingRackRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.facing = blockEntity.getBlockState().getValue(DryingRackBlock.FACING);
		this.resolver.updateForTopItem(state.item, blockEntity.getTheItem(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);

		//TODO I'd like to move away from using these hardcoded offsets. Look into using the shelf display context, or maybe make a custom one
		state.renderOffset = state.item.usesBlockLight() ? 0.5F : blockEntity.getTheItem().is(TFItemTags.RENDER_LOWER_ON_DRYING_RACK) ? 0.325F : 0.45F;

		//pain
		if (blockEntity.getTheItem().is(ItemTags.BANNERS)) {
			state.renderOffset -= 0.4F;
		}
		if (blockEntity.getTheItem().is(Tags.Items.TOOLS_SHIELD)) {
			state.renderOffset -= 0.1F;
		}
	}
}
