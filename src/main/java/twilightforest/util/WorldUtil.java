package twilightforest.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public final class WorldUtil {

	private WorldUtil() {}

	/**
	 * Inclusive of edges
	 */
	public static Iterable<BlockPos> getAllAround(BlockPos center, int range) {
		return BlockPos.getAllInBox(center.add(-range, -range, -range), center.add(range, range, range));
	}

	/**
	 * Floors both corners of the bounding box to integers
	 * Inclusive of edges
	 */
	public static Iterable<BlockPos> getAllInBB(AxisAlignedBB bb) {
		return BlockPos.getAllInBox(new BlockPos(bb.minX, bb.minY, bb.minZ), new BlockPos(bb.maxX, bb.maxY, bb.maxZ));
	}

	public static BlockPos randomOffset(Random random, BlockPos pos, int range) {
		return randomOffset(random, pos, range, range, range);
	}

	public static BlockPos randomOffset(Random random, BlockPos pos, int rx, int ry, int rz) {
		int dx = random.nextInt(rx * 2 + 1) - rx;
		int dy = random.nextInt(ry * 2 + 1) - ry;
		int dz = random.nextInt(rz * 2 + 1) - rz;
		return pos.add(dx, dy, dz);
	}
}
