package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.DeferredBlock;
import twilightforest.beans.Autowired;
import twilightforest.beans.Configurable;
import twilightforest.block.DryingRackBlock;
import twilightforest.block.entity.DryingRackBlockEntity;
import twilightforest.block.entity.JarBlockEntity;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.enums.extensions.TFItemDisplayContextEnumExtension;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackBlockEntity> {

	private final ItemRenderer itemRenderer;

	public DryingRackRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(DryingRackBlockEntity entity, float partialTick, PoseStack stack, MultiBufferSource source, int light, int overlay) {
		ItemStack item = entity.getTheItem();
		if (!item.isEmpty()) {
			var model = this.itemRenderer.getModel(item, null, null, 0);
			Direction dir = entity.getBlockState().getValue(DryingRackBlock.FACING);
			stack.pushPose();
			stack.translate(0.4F * dir.getStepX() + 0.5F, model.isGui3d() ? 0.5F : item.is(ItemTagGenerator.RENDER_LOWER_ON_DRYING_RACK) ? 0.325F : 0.45F, 0.4F * dir.getStepZ() + 0.5F);
			stack.scale(0.99F, 0.99F, 0.99F); //fix possible z-fighting
			stack.mulPose(Axis.YP.rotationDegrees(-dir.toYRot()));
			this.itemRenderer.renderStatic(entity.getTheItem(), ItemDisplayContext.FIXED, light, overlay, stack, source, null, (int) entity.getBlockPos().asLong());
			stack.popPose();
		}
	}
}
