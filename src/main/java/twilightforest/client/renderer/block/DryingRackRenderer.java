package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import twilightforest.block.DryingRackBlock;
import twilightforest.block.entity.DryingRackBlockEntity;
import twilightforest.data.tags.ItemTagGenerator;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {

	private final ItemRenderer itemRenderer;

	public DryingRackRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(DryingRackBlockEntity entity, float partialTick, PoseStack stack, MultiBufferSource source, int light, int overlay) {
		ItemStack item = entity.getTheItem();
		if (item.isEmpty())
			return;

		Level level = entity.getLevel();
		var model = this.itemRenderer.getModel(item, level, null, 0);
		Direction dir = entity.getBlockState().getValue(DryingRackBlock.FACING);
		stack.pushPose();
		stack.translate(0.4F * dir.getStepX() + 0.5F, model.isGui3d() ? 0.5F : item.is(ItemTagGenerator.RENDER_LOWER_ON_DRYING_RACK) ? 0.325F : 0.45F, 0.4F * dir.getStepZ() + 0.5F);
		//pain
		if (item.is(ItemTags.BANNERS)) {
			stack.translate(0.0F, -0.4F, 0.0F);
		}
		if (item.is(Tags.Items.TOOLS_SHIELD)) {
			stack.translate(0.0F, -0.1F, 0.0F);
		}
		stack.scale(0.99F, 0.99F, 0.99F); //fix possible z-fighting
		stack.mulPose(Axis.YP.rotationDegrees(-dir.toYRot()));
		this.itemRenderer.renderStatic(entity.getTheItem(), ItemDisplayContext.FIXED, light, overlay, stack, source, level, (int) entity.getBlockPos().asLong());
		stack.popPose();
	}
}
