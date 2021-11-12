package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.ForgeHooksClient;
import twilightforest.entity.SlideBlock;

import java.util.Random;

import net.minecraft.client.renderer.block.BlockRenderDispatcher;

public class SlideBlockRenderer extends EntityRenderer<SlideBlock> {

	public SlideBlockRenderer(EntityRendererProvider.Context manager) {
		super(manager);
		shadowRadius = 0.0f;
	}

	// [VanillaCopy] RenderFallingBlock, with spin
	@Override
	public void render(SlideBlock entity, float yaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light) {
		if (entity.getBlockState() != null) {
			BlockState blockstate = entity.getBlockState();

			if (blockstate.getRenderShape() == RenderShape.MODEL) {
				Level world = entity.level;

				if (blockstate != world.getBlockState(entity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
					stack.pushPose();
					BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
					// spin
					if (blockstate.getProperties().contains(RotatedPillarBlock.AXIS)) {
						Direction.Axis axis = blockstate.getValue(RotatedPillarBlock.AXIS);
						float angle = (entity.tickCount + partialTicks) * 60F;
						stack.translate(0.0, 0.5, 0.0);
						if (axis == Direction.Axis.Y) {
							stack.mulPose(Vector3f.YP.rotationDegrees(angle));
						} else if (axis == Direction.Axis.X) {
							stack.mulPose(Vector3f.XP.rotationDegrees(angle));
						} else if (axis == Direction.Axis.Z) {
							stack.mulPose(Vector3f.ZP.rotationDegrees(angle));
						}
						stack.translate(-0.5, -0.5, -0.5);
					}

					BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
					for (RenderType type : RenderType.chunkBufferLayers()) {
						if (ItemBlockRenderTypes.canRenderInLayer(blockstate, type)) {
							ForgeHooksClient.setRenderLayer(type);
							blockrendererdispatcher.getModelRenderer().tesselateBlock(world, blockrendererdispatcher.getBlockModel(blockstate), blockstate, blockpos, stack, buffer.getBuffer(type), false, new Random(), blockstate.getSeed(blockpos), OverlayTexture.NO_OVERLAY);
						}
					}
					ForgeHooksClient.setRenderLayer(null);

					stack.popPose();
					super.render(entity, yaw, partialTicks, stack, buffer, light);
				}
			}
		}
	}

	@Override
	public ResourceLocation getTextureLocation(SlideBlock entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
