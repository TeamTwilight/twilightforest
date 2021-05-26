package twilightforest.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;

import javax.annotation.Nullable;

public class StructureBoundingBoxUtils {
	public static Vector3i getCenter(MutableBoundingBox sbb) {
		return new BlockPos(sbb.minX + (sbb.maxX - sbb.minX + 1) / 2, sbb.minY + (sbb.maxY - sbb.minY + 1) / 2, sbb.minZ + (sbb.maxZ - sbb.minZ + 1) / 2);
	}

	@SuppressWarnings("unused")
	@Nullable
	public static MutableBoundingBox getUnionOfSBBs(MutableBoundingBox sbbIn, MutableBoundingBox sbbMask) {
		if (!sbbIn.intersectsWith(sbbMask))
			return null;

		return new MutableBoundingBox(
						Math.max(sbbIn.minX, sbbMask.minX),
						Math.max(sbbIn.minY, sbbMask.minY),
						Math.max(sbbIn.minZ, sbbMask.minZ),
						Math.min(sbbIn.maxX, sbbMask.maxX),
						Math.min(sbbIn.maxY, sbbMask.maxY),
						Math.min(sbbIn.maxZ, sbbMask.maxZ));
	}
}
