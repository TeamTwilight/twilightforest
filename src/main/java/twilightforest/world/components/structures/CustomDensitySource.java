package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import twilightforest.world.components.chunkgenerators.AbsoluteDifferenceFunction;

/**
 * Implement this interface to Structure classes, for influencing density-based chunkgen around a structure.
 * DensityFunction.FunctionContext objects provided to the returned DensityFunction will NOT be position-normalized to
 * the structure center.
 */
public interface CustomDensitySource {
	/**
	 * Expect to make a new density functions for each chunk overlapping this Structure.
	 * It will not process for any chunk outside the StructureStart's X-Z boundaries.
	 *
	 * @param chunkPosAt           The current chunk overlapping this Structure.
	 * @param structurePieceSource The specific Structure instance, represented by the StructureStart. It has plenty of information that distinguishes the structure in-world. Treat it as if it were an Entity meant for immutability and take care to not modify it nor call its property-changing methods.
	 * @return A custom density function, which will be added to the Beardifier's original density value.
	 */
	DensityFunction getStructureTerraformer(ChunkPos chunkPosAt, StructureStart structurePieceSource);

	/**
	 * Provides a DensityFunction that will cut out any terrain that wants to submerge the structure in a upside-down pyramid shape. Anything below the structure should remain untouched.
	 *
	 * @param structurePieceSource The specific Structure instance, represented by the StructureStart. It has plenty of information that distinguishes the structure in-world. Treat it as if it were an Entity meant for immutability and take care to not modify it nor call its property-changing methods.
	 * @param yOffset How high should the inverted pyramid start.
	 * @param horizontalPadding How much extra space that should be arranged around the terraformation
	 * @return A custom density function, which will be added to the Beardifier's original density value.
	 */
	static DensityFunction getInvertedPyramidTerraformer(StructureStart structurePieceSource, int yOffset, int horizontalPadding) {
		int vanillaBoxInflationFactor = 12; // Vanilla automatically inflates a structure's bounds by 12 in Structure.adjustBoundingBox()

		BoundingBox structureBox = structurePieceSource.getBoundingBox();
		int squareHorizontalSpan = Math.max(structureBox.getXSpan(), structureBox.getZSpan()) - 2 * vanillaBoxInflationFactor;

		BlockPos centerPos = structureBox.getCenter();
		yOffset += structureBox.minY() + vanillaBoxInflationFactor;

		DensityFunction invertedPyramid = DensityFunctions.add(
			DensityFunctions.yClampedGradient(yOffset - 1, yOffset + squareHorizontalSpan, 1, -1 - squareHorizontalSpan),
			new AbsoluteDifferenceFunction.Max(squareHorizontalSpan, centerPos.getX() + 0.5F, centerPos.getZ() + 0.5F)
		);

		return DensityFunctions.min(
			DensityFunctions.constant(0),
			DensityFunctions.max(
				DensityFunctions.yClampedGradient(yOffset - 1, yOffset, 1, -1),
				DensityFunctions.add(
					DensityFunctions.constant(-(squareHorizontalSpan >> 1) - horizontalPadding),
					invertedPyramid
				)
			)
		);
	}
}
