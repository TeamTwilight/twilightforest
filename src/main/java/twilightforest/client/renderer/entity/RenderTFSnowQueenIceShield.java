package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.entity.boss.EntityTFSnowQueenIceShield;

import java.util.Random;

public class RenderTFSnowQueenIceShield<T extends EntityTFSnowQueenIceShield> extends EntityRenderer<T> {
	public RenderTFSnowQueenIceShield(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		BlockState blockstate = Blocks.PACKED_ICE.getDefaultState();
		if (blockstate.getRenderType() == BlockRenderType.MODEL) {
			World world = entityIn.world;
			if (blockstate.getRenderType() != BlockRenderType.INVISIBLE) {
				matrixStackIn.push();
				BlockPos blockpos = new BlockPos(entityIn.getPosX(), entityIn.getBoundingBox().maxY, entityIn.getPosZ());
				matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
				BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
				for (net.minecraft.client.renderer.RenderType type : net.minecraft.client.renderer.RenderType.getBlockRenderTypes()) {
					if (RenderTypeLookup.canRenderInLayer(blockstate, type)) {
						net.minecraftforge.client.ForgeHooksClient.setRenderLayer(type);
						blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(blockstate), blockstate, blockpos, matrixStackIn, bufferIn.getBuffer(type), false, new Random(), blockstate.getPositionRandom(new BlockPos(entityIn.getPositionVec())), OverlayTexture.NO_OVERLAY);
					}
				}
				net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
				matrixStackIn.pop();
				super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
			}
		}
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
