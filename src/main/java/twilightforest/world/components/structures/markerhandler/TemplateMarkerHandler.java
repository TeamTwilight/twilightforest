package twilightforest.world.components.structures.markerhandler;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.apache.commons.lang3.StringUtils;

public interface TemplateMarkerHandler {
	// Enhanced version of TemplateStructurePiece#handleDataMarker() method that up-levels ServerLevelAccessor into WorldGenLevel while also adding ChunkGenerator parameter
	boolean handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen, Rotation rotation);

	TemplateMarkerHandlerType getType();

	static int parseRange(String amountLabel, RandomSource random, int defaultMin, int defaultMax) {
		String[] amountParams = amountLabel.split("-");

		if (amountParams.length == 1 && StringUtils.isNumeric(amountParams[0])) {
			return Integer.parseInt(amountParams[0]);
		} else if (amountParams.length == 2 && StringUtils.isNumeric(amountParams[0]) && StringUtils.isNumeric(amountParams[1])) {
			return random.nextIntBetweenInclusive(Integer.parseInt(amountParams[0]), Integer.parseInt(amountParams[1]));
		}

		return random.nextIntBetweenInclusive(defaultMin, defaultMax);
	}
}
