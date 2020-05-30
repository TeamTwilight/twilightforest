package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import twilightforest.entity.boss.EntityTFFallingIce;

// [VanillaCopy] complete copy of RenderFallingBlock but scaling by 3 before rendering
public class RenderTFFallingIce<T extends EntityTFFallingIce> extends EntityRenderer<T> {
	public RenderTFFallingIce(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		if (entity.getBlockState() != null) {
			BlockState iblockstate = entity.getBlockState();

			if (iblockstate.getRenderType() == BlockRenderType.MODEL) {
				World world = entity.getWorldObj();

				if (iblockstate != world.getBlockState(new BlockPos(entity)) && iblockstate.getRenderType() != BlockRenderType.INVISIBLE) {
//					this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
					IVertexBuilder builder = buffer.getBuffer(RenderType.getEntityCutout(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
					stack.push();
					RenderSystem.disableLighting();
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();

//					if (this.renderOutlines) {
//						RenderSystem.enableColorMaterial();
//						GlStateManager.enableOutlineMode(this.getTeamColor(entity));
//					}

					bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
					BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
//					stack.translate((float) (x - (double) blockpos.getX() - 0.5D), (float) (y - (double) blockpos.getY()), (float) (z - (double) blockpos.getZ() - 0.5D));
					// TF - 3 times as big
					stack.scale(3, 3, 3);
					BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
//					blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, stack, builder, false, MathHelper.getPositionRandom(entity.getOrigin()));
					tessellator.draw();

//					if (this.renderOutlines) {
//						GlStateManager.disableOutlineMode();
//						RenderSystem.disableColorMaterial();
//					}

					RenderSystem.enableLighting();
					stack.pop();
					super.render(entity, entityYaw, partialTicks, stack, buffer, light);
				}
			}
		}
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFFallingIce entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
