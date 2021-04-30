package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.IModelData;
import org.lwjgl.opengl.GL11;
import twilightforest.entity.EntityTFSlideBlock;

import java.util.Random;

public class RenderTFSlideBlock extends EntityRenderer<EntityTFSlideBlock> {

	public RenderTFSlideBlock(EntityRendererManager manager) {
		super(manager);
		shadowSize = 0.0f;
	}

	// [VanillaCopy] RenderFallingBlock, with spin
	@Override
	public void render(EntityTFSlideBlock entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		if (entity.getBlockState() != null) {
			BlockState blockstate = entity.getBlockState();

			if (blockstate.getRenderType() == BlockRenderType.MODEL) {
				World world = entity.world;

				if (blockstate != world.getBlockState(entity.getPosition()) && blockstate.getRenderType() != BlockRenderType.INVISIBLE) {
					stack.push();
					BlockPos blockpos = new BlockPos(entity.getPosX(), entity.getBoundingBox().maxY, entity.getPosZ());
					// spin
					if (blockstate.getProperties().contains(RotatedPillarBlock.AXIS)) {
						Direction.Axis axis = blockstate.get(RotatedPillarBlock.AXIS);
						float angle = (entity.ticksExisted + partialTicks) * 60F;
						stack.translate(0.0, 0.5, 0.0);
						if (axis == Direction.Axis.Y) {
							stack.rotate(Vector3f.YP.rotationDegrees(angle));
						} else if (axis == Direction.Axis.X) {
							stack.rotate(Vector3f.XP.rotationDegrees(angle));
						} else if (axis == Direction.Axis.Z) {
							stack.rotate(Vector3f.ZP.rotationDegrees(angle));
						}
						stack.translate(-0.5, -0.5, -0.5);
					}

					BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
					for (RenderType type : RenderType.getBlockRenderTypes()) {
						if (RenderTypeLookup.canRenderInLayer(blockstate, type)) {
							ForgeHooksClient.setRenderLayer(type);
							blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(blockstate), blockstate, blockpos, stack, buffer.getBuffer(type), false, new Random(), blockstate.getPositionRandom(blockpos), OverlayTexture.NO_OVERLAY);
						}
					}
					ForgeHooksClient.setRenderLayer(null);

					stack.pop();
					super.render(entity, yaw, partialTicks, stack, buffer, light);
				}
			}
		}
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFSlideBlock entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
