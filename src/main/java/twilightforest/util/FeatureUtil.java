package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

// TODO Split into FeatureLogic and FeaturePlacers
@Deprecated
public final class FeatureUtil {

	public static boolean isAreaSuitable(WorldGenLevel world, BlockPos pos, int width, int height, int depth) {
		return isAreaSuitable(world, pos, width, height, depth, false);
	}
	/**
	 * Checks an area to see if it consists of flat natural ground below and air above
	 */
	public static boolean isAreaSuitable(WorldGenLevel world, BlockPos pos, int width, int height, int depth, boolean underwaterAllowed) {
		boolean flag = true;

		// check if there's anything within the diameter
		for (int cx = 0; cx < width; cx++) {
			for (int cz = 0; cz < depth; cz++) {
				BlockPos pos_ = pos.offset(cx, 0, cz);
				// check if the blocks even exist?
				if (world.hasChunkAt(pos_)) {
					// is there grass, dirt or stone below?
					Material m = world.getBlockState(pos_.below()).getMaterial();
					if (m != Material.DIRT && m != Material.GRASS && m != Material.STONE && m != Material.SNOW && m != Material.SAND) {
						if(underwaterAllowed && m == Material.WATER) {
							continue;
						}
						flag = false;
					}

					for (int cy = 0; cy < height; cy++) {
						// blank space above?
						if (!world.isEmptyBlock(pos_.above(cy)) && !world.getBlockState(pos_.above(cy)).getMaterial().isReplaceable()) {
							if(underwaterAllowed && world.getBlockState(pos_.above(cy)).getMaterial() == Material.WATER) {
								continue;
							}
							flag = false;
						}
					}
				} else {
					flag = false;
				}
			}
		}

		// Okie dokie
		return flag;
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
						world.setBlock(pos.offset(+dx, +dy, +dz), state, 3);
						world.setBlock(pos.offset(+dx, +dy, -dz), state, 3);
						world.setBlock(pos.offset(-dx, +dy, +dz), state, 3);
						world.setBlock(pos.offset(-dx, +dy, -dz), state, 3);
						world.setBlock(pos.offset(+dx, -dy, +dz), state, 3);
						world.setBlock(pos.offset(+dx, -dy, -dz), state, 3);
						world.setBlock(pos.offset(-dx, -dy, +dz), state, 3);
						world.setBlock(pos.offset(-dx, -dy, -dz), state, 3);
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
					&& world.getBlockState(pos.relative(e)).getMaterial().isSolid()) {
				return true;
			}
		}

		return false;
	}
}
