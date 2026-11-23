package twilightforest.world.components.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * While seemingly similar to RandomFeatureConfiguration, this uses a weighted list and a single random value sample.
 * The goal is to produced pseudorandom result distribution that better match expectations defined by a weighted list.
 */
public record WeightedListFeature(WeightedList<Holder<PlacedFeature>> randomFeatures) implements Feature {
	public static final MapCodec<WeightedListFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		WeightedList.codec(PlacedFeature.CODEC)
			.fieldOf("random_features")
			.forGetter(WeightedListFeature::randomFeatures)
	).apply(instance, WeightedListFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
		Optional<Holder<PlacedFeature>> randomFeature = randomFeatures().getRandom(random);
		//noinspection OptionalIsPresent
		if (randomFeature.isEmpty())
			return false;
		return randomFeature.get().value().place(level, chunkGenerator, random, origin);
	}

	@Override
	public Stream<Holder<Feature>> getSubFeatures() {
		return this.randomFeatures.unwrap()
			.stream()
			.map(Weighted::value)
			.map(Holder::value)
			.map(PlacedFeature::feature)
			.map(Holder::value)
			.flatMap(Feature::getSubFeatures);
	}
}
