package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.RedThreadBlockEntity;
import twilightforest.client.TFShaders;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.RedThreadModel;
import twilightforest.init.TFBlocks;

public class RedThreadRenderer<T extends RedThreadBlockEntity> implements BlockEntityRenderer<T> {
	private static final ResourceLocation TEXTURE = TwilightForestMod.getModelTexture("red_thread.png");
	private final RedThreadModel redThreadModel;

	private static final RenderType GLOW = RenderType
		.create(TwilightForestMod.ID + ":glow", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, RenderType.CompositeState
			.builder()
			.setLightmapState(new RenderStateShard.LightmapStateShard(true))
			.setCullState(new RenderStateShard.CullStateShard(true))
			.setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519))
			.setShaderState(new RenderStateShard.ShaderStateShard(TFShaders.RED_THREAD))
			.setTextureState(new RenderStateShard.TextureStateShard(TEXTURE, TriState.FALSE, true))
			.createCompositeState(true));

	public RedThreadRenderer(BlockEntityRendererProvider.Context context) {
		this.redThreadModel = new RedThreadModel(context.bakeLayer(TFModelLayers.RED_THREAD));
	}

	@Override
	public void render(T entity, float ticks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
		BlockState state = entity.getBlockState();
		boolean glow = Minecraft.getInstance().player != null && Minecraft.getInstance().player.isHolding(TFBlocks.RED_THREAD.get().asItem());

		for (Direction face : Direction.values()) {
			if (state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(face))) {
				stack.pushPose();
				Vec3 xyz = getXYZ(face);
				stack.translate(xyz.x, xyz.y, xyz.z);
				stack.mulPose(Axis.ZP.rotationDegrees(getZPDegrees(face)));
				stack.mulPose(Axis.XP.rotationDegrees(getXPDegrees(face)));
				stack.pushPose();
				stack.translate(0.5D, 0D, 0.5D);
				this.render(entity, face, stack, glow ? buffer.getBuffer(GLOW) : buffer.getBuffer(RenderType.entityCutout(TEXTURE)), glow ? 15728880 : light);
				stack.popPose();
				stack.popPose();
			}
		}
	}

	private void render(T entity, Direction face, PoseStack stack, VertexConsumer consumer, int light) {
		Level level = entity.getLevel();
		BlockPos pos = entity.getBlockPos();

		this.redThreadModel.renderCenterPiece(stack, consumer, light);
		for (Direction direction : Direction.values()) {
			if (!direction.getAxis().equals(face.getAxis())) {
				//We check the blockState to see if the thread on this face is connecting to a different face of the same block.
				boolean flag = entity.getBlockState().getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction));

				if (!flag && level != null) { //If there is no other face to connect to, we check neighbouring positions for other red thread blocks.
					BlockState state = level.getBlockState(pos.relative(direction));
					flag = state.getBlock().equals(TFBlocks.RED_THREAD.get()) && state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(face));

					if (!flag) { //And if the above check also fails, we check if there is a red thread behind the corner for us to connect to.
						state = level.getBlockState(pos.relative(direction).relative(face));
						//check if theres a block in the way of the connection. If there is, dont connect
						boolean threadBlocked = level.getBlockState(pos.relative(direction)).isFaceSturdy(level, pos, direction.getOpposite());
						flag = state.is(TFBlocks.RED_THREAD.get()) && !threadBlocked && state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite()));
					}
				}

				if (flag) this.redThreadModel.getPart(face, direction).render(stack, consumer, light, OverlayTexture.NO_OVERLAY);
			}
		}
	}

	/**
	 * Get the offset for our model, based on the face it is placed on.
	 */
	private static Vec3 getXYZ(Direction face) {
		return new Vec3(
			face == Direction.EAST || face == Direction.UP ? 1D : 0D,
			face == Direction.WEST || face == Direction.UP || face == Direction.NORTH ? 1D : 0D,
			face == Direction.SOUTH ? 1D : 0D);
	}

	/**
	 * Get the amount of degrees our model will be rotated by on the Z axis, based on the face it is placed on.
	 */
	private static float getZPDegrees(Direction face) {
		return switch (face) {
			case EAST -> 90.0F;
			case UP -> 180.0F;
			case WEST -> 270.0F;
			default -> 0.0F;
		};
	}

	/**
	 * Get the amount of degrees our model will be rotated by on the X axis, based on the face it is placed on.
	 */
	private static float getXPDegrees(Direction face) {
		return switch (face) {
			case NORTH -> 90.0F;
			case SOUTH -> 270.0F;
			default -> 0.0F;
		};
	}
}
