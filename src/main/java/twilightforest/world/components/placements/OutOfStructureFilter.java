package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TwilightFeatures;

import java.util.Random;
import java.util.stream.Stream;

public class OutOfStructureFilter extends PlacementModifier {

	public static final Codec<OutOfStructureFilter> CODEC = RecordCodecBuilder.<OutOfStructureFilter>create(instance -> instance.group(
			Codec.BOOL.fieldOf("occupies_surface").forGetter(o -> o.occupiesSurface),
			Codec.BOOL.fieldOf("occupies_underground").forGetter(o -> o.occupiesUnderground),
			Codec.INT.fieldOf("additional_clearance").forGetter(o -> o.additionalClearance)
	).apply(instance, OutOfStructureFilter::new)).flatXmap(OutOfStructureFilter::validate, OutOfStructureFilter::validate);

	private final boolean occupiesSurface;
	private final boolean occupiesUnderground;
	private final int additionalClearance;

	public OutOfStructureFilter(boolean occupiesSurface, boolean occupiesUnderground, int additionalClearance) {
		this.occupiesSurface = occupiesSurface;
		this.occupiesUnderground = occupiesUnderground;
		this.additionalClearance = additionalClearance;
	}

	public static OutOfStructureFilter checkSurface() {
		return new OutOfStructureFilter(true, false, 0);
	}

	public static OutOfStructureFilter checkUnderground() {
		return new OutOfStructureFilter(false, true, 0);
	}

	public static OutOfStructureFilter checkBoth() {
		return new OutOfStructureFilter(true, true, 0);
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext worldDecoratingHelper, Random random, BlockPos blockPos) {
		if (!(worldDecoratingHelper.getLevel().getLevel().getChunkSource().getGenerator() instanceof ChunkGeneratorTwilight tfChunkGen))
			return Stream.of(blockPos);

		// Feature Center
		BlockPos.MutableBlockPos featurePos = TFFeature.getNearestCenterXYZ(blockPos.getX() >> 4, blockPos.getZ() >> 4).mutable();

		final TFFeature feature = tfChunkGen.getFeatureCached(new ChunkPos(featurePos), worldDecoratingHelper.getLevel());

		if ((!occupiesSurface || feature.surfaceDecorationsAllowed) && (!occupiesUnderground || feature.undergroundDecoAllowed))
			return Stream.of(blockPos);

		// Turn Feature Center into Feature Offset
		featurePos.set(Math.abs(featurePos.getX() - blockPos.getX()), 0, Math.abs(featurePos.getZ() - blockPos.getZ()));
		int size = feature.size * 16 + additionalClearance;

		return featurePos.getX() < size && featurePos.getZ() < size ? Stream.empty() : Stream.of(blockPos);
	}

	@Override
	public PlacementModifierType<?> type() {
		return TwilightFeatures.NO_STRUCTURE.get();
	}

	private static DataResult<OutOfStructureFilter> validate(OutOfStructureFilter config) {
		return config.occupiesSurface || config.occupiesUnderground ? DataResult.success(config) : DataResult.error("Feature Decorator cannot occupy neither surface nor underground");
	}
}
