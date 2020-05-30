package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import twilightforest.entity.EntityTFSlideBlock;

public class RenderTFSlideBlock<T extends EntityTFSlideBlock> extends EntityRenderer<T> {

	public RenderTFSlideBlock(EntityRendererManager manager) {
		super(manager);
		shadowSize = 0.0f;
	}

	// [VanillaCopy] RenderFallingBlock, with spin
	@Override
	public void render(T entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		if (entity.getBlockState() != null) {
			BlockState iblockstate = entity.getBlockState();

			if (iblockstate.getRenderType() == BlockRenderType.MODEL) {
				World world = entity.world;

				if (iblockstate != world.getBlockState(new BlockPos(entity)) && iblockstate.getRenderType() != BlockRenderType.INVISIBLE) {
//					this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
					stack.push();
					RenderSystem.disableLighting();
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();

//					if (this.renderOutlines) {
//						RenderSystem.enableColorMaterial();
//						RenderSystem.enableOutlineMode(this.getTeamColor(entity));
//					}

					bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
					BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());

					// spin
					if (iblockstate.getProperties().contains(RotatedPillarBlock.AXIS)) {
						Direction.Axis axis = iblockstate.get(RotatedPillarBlock.AXIS);
						float angle = (entity.ticksExisted + partialTicks) * 60F;
//						double dy = y + 0.5;
//						stack.translate((float) x, (float) dy, (float) z);
						if (axis == Direction.Axis.Y) {
							RenderSystem.rotatef(angle, 0F, 1F, 0F);
						} else if (axis == Direction.Axis.X) {
							RenderSystem.rotatef(angle, 1F, 0F, 0F);
						} else if (axis == Direction.Axis.Z) {
							RenderSystem.rotatef(angle, 0F, 0F, 1F);
						}
//						stack.translate((float) -x, (float) -dy, (float) -z);
					}

//					stack.translate((float) (x - (double) blockpos.getX() - 0.5D), (float) (y - (double) blockpos.getY()), (float) (z - (double) blockpos.getZ() - 0.5D));

					BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
//					blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, bufferbuilder, false, 0L);
					tessellator.draw();

//					if (this.renderOutlines) {
//						GlStateManager.disableOutlineMode();
//						RenderSystem.disableColorMaterial();
//					}

					RenderSystem.enableLighting();
					stack.pop();
					super.render(entity, yaw, partialTicks, stack, buffer, light);
				}
			}
		}


        /*World world = sliderEntity.world;
		BlockState block = sliderEntity.getBlockState();

        BlockPos pos = new BlockPos(sliderEntity);

        if (block != null && block.getBlock() != world.getBlockState(pos).getBlock())
        {
            GlStateManager.pushMatrix();
            RenderSystem.translatef((float)x, (float)y + 0.5F, (float)z);

            // spin
            if (block.getValue(BlockRotatedPillar.AXIS_FACING) == Direction.Axis.Y) {
            	GlStateManager.rotatef((sliderEntity.ticksExisted + time) * 60F, 0, 1, 0);
            } else if (block.getValue(BlockRotatedPillar.AXIS_FACING) == Direction.Axis.X) {
            	GlStateManager.rotatef((sliderEntity.ticksExisted + time) * 60F, 1, 0, 0);
            } else if (block.getValue(BlockRotatedPillar.AXIS_FACING) == Direction.Axis.Z) {
            	GlStateManager.rotatef((sliderEntity.ticksExisted + time) * 60F, 0, 0, 1);
            }

            this.bindEntityTexture(sliderEntity);
            RenderSystem.disableLighting();

            Tessellator.getInstance().getBuffer().begin(7, DefaultVertexFormats.BLOCK);
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
            blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(block), block, pos, Tessellator.getInstance().getBuffer(), false, MathHelper.getPositionRandom(pos));
            Tessellator.getInstance().draw();

            RenderSystem.enableLighting();
            GlStateManager.popMatrix();
        }*/
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
