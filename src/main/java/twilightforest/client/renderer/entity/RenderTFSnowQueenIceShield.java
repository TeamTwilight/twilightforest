package twilightforest.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.entity.boss.EntityTFSnowQueenIceShield;

public class RenderTFSnowQueenIceShield extends Render<EntityTFSnowQueenIceShield> {

	public RenderTFSnowQueenIceShield(RenderManager manager) {
		super(manager);
	}

	// [VanillaCopy] RenderFallingBlock.doRender with hardcoded state
	@Override
	public void doRender(EntityTFSnowQueenIceShield entity, double x, double y, double z, float entityYaw, float partialTicks) {
		IBlockState iblockstate = Blocks.PACKED_ICE.getDefaultState();

		if (iblockstate.getRenderType() == EnumBlockRenderType.MODEL) {
			World world = entity.world;

			if (iblockstate != world.getBlockState(new BlockPos(entity)) && iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
				this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer vertexbuffer = tessellator.getBuffer();

				if (this.renderOutlines) {
					GlStateManager.enableColorMaterial();
					GlStateManager.enableOutlineMode(this.getTeamColor(entity));
				}

				vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
				BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().maxY, entity.posZ);
				GlStateManager.translate((float) (x - (double) blockpos.getX() - 0.5D), (float) (y - (double) blockpos.getY()), (float) (z - (double) blockpos.getZ() - 0.5D));
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
				blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, vertexbuffer, false, MathHelper.getPositionRandom(BlockPos.ORIGIN));
				tessellator.draw();

				if (this.renderOutlines) {
					GlStateManager.disableOutlineMode();
					GlStateManager.disableColorMaterial();
				}

				GlStateManager.enableLighting();
				GlStateManager.popMatrix();
				super.doRender(entity, x, y, z, entityYaw, partialTicks);
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFSnowQueenIceShield var1) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
