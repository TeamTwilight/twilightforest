package twilightforest.world.components.layer;

import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.DensityFunction;
import twilightforest.world.components.chunkgenerators.TerrainDensityRouter;
import twilightforest.world.components.layer.vanillalegacy.BiomeDensitySource;

public class ChunkCachedDensityRouter extends TerrainDensityRouter {
	private final BiomeDensitySource biomeDensitySource;

	private final BiomeDensitySource.DensityData[] horizontalCache = new BiomeDensitySource.DensityData[16 * 16];

	public ChunkCachedDensityRouter(Holder<BiomeDensitySource> biomeDensitySource, DensityFunction.NoiseHolder noise, double lowerDensityBound, double upperDensityBound, double depthScalar, DensityFunction baseFactor, DensityFunction baseOffset) {
		super(biomeDensitySource, noise, lowerDensityBound, upperDensityBound, depthScalar, baseFactor, baseOffset);
		this.biomeDensitySource = biomeDensitySource.value();
	}

	@Override
	public BiomeDensitySource.DensityData computeTerrain(FunctionContext context) {
		int xInChunk = SectionPos.sectionRelative(context.blockX());
		int zInChunk = SectionPos.sectionRelative(context.blockZ());

		int arrayCoord = zInChunk + (xInChunk << 4);

		BiomeDensitySource.DensityData dataColumn = this.horizontalCache[arrayCoord];

		if (dataColumn == null) {
			dataColumn = this.biomeDensitySource.sampleTerrain(context.blockX(), context.blockZ(), context);
			this.horizontalCache[arrayCoord] = dataColumn;
		}

		return dataColumn;
	}
}
