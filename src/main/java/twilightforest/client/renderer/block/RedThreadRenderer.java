package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.RedThreadBlockEntity;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.RedThreadModel;
import twilightforest.client.renderer.TFRenderTypes;
import twilightforest.client.state.block.RedThreadRenderState;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.TravellersModifiersManager;

import java.util.HashMap;
import java.util.Map;

public class RedThreadRenderer implements BlockEntityRenderer<RedThreadBlockEntity, RedThreadRenderState> {
	public static final Identifier TEXTURE = TwilightForestMod.getModelTexture("red_thread.png");
	private final RedThreadModel redThreadModel;

	public RedThreadRenderer(BlockEntityRendererProvider.Context context) {
		this.redThreadModel = new RedThreadModel(context.bakeLayer(TFModelLayers.RED_THREAD));
	}

	@Override
	public void submit(RedThreadRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		for (Map.Entry<Direction, Map<Direction, Boolean>> sides : state.connections.entrySet()) {
			stack.pushPose();
			Vec3 xyz = getXYZ(sides.getKey());
			stack.translate(xyz.x, xyz.y, xyz.z);
			stack.mulPose(Axis.ZP.rotationDegrees(getZPDegrees(sides.getKey())));
			stack.mulPose(Axis.XP.rotationDegrees(getXPDegrees(sides.getKey())));
			stack.pushPose();
			stack.translate(0.5D, 0D, 0.5D);
			this.renderSide(sides.getValue(), sides.getKey(), stack, collector, state.glowing ? TFRenderTypes.RED_THREAD : RenderTypes.entityCutout(TEXTURE), state.glowing ? LightCoordsUtil.FULL_BRIGHT : state.lightCoords);
			stack.popPose();
			stack.popPose();
		}
	}

	private void renderSide(Map<Direction, Boolean> sideFlags, Direction face, PoseStack stack, SubmitNodeCollector collector, RenderType type, int light) {
		collector.submitModelPart(this.redThreadModel.center, stack, type, light, OverlayTexture.NO_OVERLAY, null);
		for (Map.Entry<Direction, Boolean> flags : sideFlags.entrySet()) {
			if (!flags.getKey().getAxis().equals(face.getAxis()) && flags.getValue()) {
				collector.submitModelPart(this.redThreadModel.getPart(face, flags.getKey()), stack, type, light, OverlayTexture.NO_OVERLAY, null);
			}
		}
	}

	@Override
	public RedThreadRenderState createRenderState() {
		return new RedThreadRenderState();
	}

	@Override
	public void extractRenderState(RedThreadBlockEntity blockEntity, RedThreadRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

		Player player = Minecraft.getInstance().player;
		if (player != null) {
			boolean wearsActivatedTravellersGoggles = player.getData(TFDataAttachments.TRAVELLERS_GOGGLES_RED_THREAD_VISION) && TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.RED_THREAD_VISION_MODIFIER);
			state.glowing = player.isHolding(TFBlocks.RED_THREAD.get().asItem()) || wearsActivatedTravellersGoggles;
		}

		Map<Direction, Map<Direction, Boolean>> flags = new HashMap<>();
		for (Direction face : Direction.values()) {
			if (blockEntity.getBlockState().getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(face))) {
				Map<Direction, Boolean> sideFlags = new HashMap<>();
				for (Direction direction : Direction.values()) {
					if (!direction.getAxis().equals(face.getAxis())) {
						//We check the blockState to see if the thread on this face is connecting to a different face of the same block.
						boolean flag = blockEntity.getBlockState().getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction));

						Level level = blockEntity.getLevel();
						if (!flag && level != null) { //If there is no other face to connect to, we check neighboring positions for other red thread blocks.
							BlockPos pos = blockEntity.getBlockPos();
							BlockState checkState = level.getBlockState(pos.relative(direction));
							flag = checkState.getBlock().equals(TFBlocks.RED_THREAD.get()) && checkState.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(face));

							if (!flag) { //And if the above check also fails, we check if there is a red thread behind the corner for us to connect to.
								checkState = level.getBlockState(pos.relative(direction).relative(face));
								//check if there's a block in the way of the connection. If there is, dont connect
								boolean threadBlocked = level.getBlockState(pos.relative(direction)).isFaceSturdy(level, pos, direction.getOpposite());
								flag = checkState.is(TFBlocks.RED_THREAD.get()) && !threadBlocked && checkState.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite()));
							}
						}

						sideFlags.put(direction, flag);
					}
				}
				flags.put(face, sideFlags);
			}
		}
		state.connections = flags;
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
