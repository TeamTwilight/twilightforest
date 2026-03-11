package twilightforest.util.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;
import java.util.stream.IntStream;

// TODO Split into FeatureLogic and FeaturePlacers
@Deprecated
public final class FeatureUtil {

	public static boolean isAreaSuitable(WorldGenLevel world, BlockPos pos, int xWidth, int height, int zWidth) {
		return isAreaSuitable(world, pos, xWidth, height, zWidth, false);
	}

	/**
	 * Checks an area to see if it consists of flat natural ground below and air above
	 */
	public static boolean isAreaSuitable(WorldGenLevel world, BlockPos pos, int xWidth, int height, int zWidth, boolean underwaterAllowed) {
		// check if there's anything within the diameter
		for (int cx = 0; cx < xWidth; cx++) {
			for (int cz = 0; cz < zWidth; cz++) {
				BlockPos pos_ = pos.offset(cx, 0, cz);
				// check if the blocks even exist?
				if (world.hasChunkAt(pos_)) {
					// is there grass, dirt or stone below?
					BlockState state = world.getBlockState(pos_.below());
					if (!state.isSolidRender(world, pos_) || FeatureLogic.isBlockNotOk(state)) {
						if (underwaterAllowed && state.liquid()) {
							continue;
						}
						return false;
					}

					for (int cy = 0; cy < height; cy++) {
						// blank space above?
						if (!world.isEmptyBlock(pos_.above(cy)) && !world.getBlockState(pos_.above(cy)).canBeReplaced()) {
							if (underwaterAllowed && world.getBlockState(pos_.above(cy)).liquid()) {
								continue;
							}
							return false;
						}
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean anyBelowMatch(BlockPos exposedPos, int depth, Predicate<BlockPos> predicate) {
		return isAnyMatchInArea(exposedPos.below(depth), 1, depth + 1, 1, predicate);
	}

	public static boolean isAnyMatchInArea(BlockPos pos, int xWidth, int height, int zWidth, Predicate<BlockPos> predicate) {
		return IntStream.range(0, xWidth)
			.mapToObj(cx -> IntStream.range(0, height)
				.mapToObj(cy -> IntStream.range(0, zWidth)
					.mapToObj(cz -> pos.offset(cx, cy, cz))))
			.flatMap(stream -> stream.flatMap(s -> s))
			.anyMatch(predicate);
	}

	/**
	 * Draw a giant blob of whatevs.
	 */
	public static void drawBlob(LevelAccessor world, BlockPos pos, int rad, BlockState state) {
		// then trace out a quadrant
		for (byte dx = 0; dx <= rad; dx++) {
			for (byte dy = 0; dy <= rad; dy++) {
				for (byte dz = 0; dz <= rad; dz++) {
					// determine how far we are from the center.
					int dist;
					if (dx >= dy && dx >= dz) {
						dist = dx + (Math.max(dy, dz) >> 1) + (Math.min(dy, dz) >> 2);
					} else if (dy >= dx && dy >= dz) {
						dist = dy + (Math.max(dx, dz) >> 1) + (Math.min(dx, dz) >> 2);
					} else {
						dist = dz + (Math.max(dx, dy) >> 1) + (Math.min(dx, dy) >> 2);
					}


					// if we're inside the blob, fill it
					if (dist <= rad) {
						// do eight at a time for easiness!
						world.setBlock(pos.offset(+dx, +dy, +dz), state, Block.UPDATE_ALL);
						world.setBlock(pos.offset(+dx, +dy, -dz), state, Block.UPDATE_ALL);
						world.setBlock(pos.offset(-dx, +dy, +dz), state, Block.UPDATE_ALL);
						world.setBlock(pos.offset(-dx, +dy, -dz), state, Block.UPDATE_ALL);
						world.setBlock(pos.offset(+dx, -dy, +dz), state, Block.UPDATE_ALL);
						world.setBlock(pos.offset(+dx, -dy, -dz), state, Block.UPDATE_ALL);
						world.setBlock(pos.offset(-dx, -dy, +dz), state, Block.UPDATE_ALL);
						world.setBlock(pos.offset(-dx, -dy, -dz), state, Block.UPDATE_ALL);
					}
				}
			}
		}
	}

	/**
	 * Does the block have at least 1 air block adjacent
	 */
	private static final Direction[] directionsExceptDown = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

	public static boolean hasAirAround(LevelAccessor world, BlockPos pos) {
		for (Direction e : directionsExceptDown) {
			if (world.isEmptyBlock(pos.relative(e))) {
				return true;
			}
		}

		return false;
	}

	public static boolean isNearSolid(LevelReader world, BlockPos pos) {
		for (Direction e : Direction.values()) {
			if (world.hasChunkAt(pos.relative(e))
				&& world.getBlockState(pos.relative(e)).isSolid()) {
				return true;
			}
		}

		return false;
	}
}
