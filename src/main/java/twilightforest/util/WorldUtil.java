package twilightforest.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public final class WorldUtil {
	private WorldUtil() {
	}

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
}
