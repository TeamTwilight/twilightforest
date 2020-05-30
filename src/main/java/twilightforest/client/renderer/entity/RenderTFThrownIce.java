package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import twilightforest.entity.boss.EntityTFIceBomb;

// [VanillaCopy] direct of RenderFallingBlock because of generic type restrictions
public class RenderTFThrownIce<T extends EntityTFIceBomb> extends EntityRenderer<T> {

	public RenderTFThrownIce(EntityRendererManager manager) {
		super(manager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		if (entity.getBlock() != null) {
			BlockState iblockstate = entity.getBlock();

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
//						GlStateManager.enableOutlineMode(this.getTeamColor(entity));
//					}

					bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
					BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
//					stack.translate((float) (x - (double) blockpos.getX() - 0.5D), (float) (y - (double) blockpos.getY()), (float) (z - (double) blockpos.getZ() - 0.5D));
					BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
//					blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, bufferbuilder, false, MathHelper.getPositionRandom(BlockPos.ZERO));
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
	public ResourceLocation getEntityTexture(T entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
