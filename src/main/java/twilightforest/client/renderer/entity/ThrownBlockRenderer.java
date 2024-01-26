package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import twilightforest.entity.projectile.ThrownBlock;

/**
 * [VanillaCopy] of {@link net.minecraft.client.renderer.entity.FallingBlockRenderer} because of generic type restrictions
 */
public class ThrownBlockRenderer extends EntityRenderer<ThrownBlock> {

	public ThrownBlockRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		this.shadowRadius = 0.5F;
	}

	@Override
	public void render(ThrownBlock entity, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
		BlockState blockstate = entity.getBlockState();
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			Level world = entity.level();
			if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
				ms.pushPose();
				BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
				ms.translate(-0.5D, 0.0D, -0.5D);
				BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
				var model = dispatcher.getBlockModel(blockstate);
				for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(entity.blockPosition())), ModelData.EMPTY))
					dispatcher.getModelRenderer().tesselateBlock(world, model, blockstate, blockpos, ms, buffers.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(entity.blockPosition()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
				ms.popPose();
				super.render(entity, yaw, partialTicks, ms, buffers, light);
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(ThrownBlock entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
