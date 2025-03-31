package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import twilightforest.world.components.feature.config.WeightedListFeatureConfig;

import java.util.Optional;

/**
 * While seemingly similar to RandomFeatureConfiguration, this uses a weighted list and a single random value sample.
 * The goal is to produced pseudorandom result distribution that better match expectations defined by a weighted list.
 */
public class WeightedListFeature extends Feature<WeightedListFeatureConfig> {
	public WeightedListFeature(Codec<WeightedListFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<WeightedListFeatureConfig> context) {
		WeightedListFeatureConfig weightedListFeatureConfig = context.config();
		RandomSource random = context.random();
		WorldGenLevel worldGenLevel = context.level();
		ChunkGenerator chunkGenerator = context.chunkGenerator();
		BlockPos blockPos = context.origin();

		Optional<Holder<PlacedFeature>> randomFeature = weightedListFeatureConfig.getRandomFeature(random);

		//noinspection OptionalIsPresent
		if (randomFeature.isEmpty())
			return false;

		return randomFeature.get().value().place(worldGenLevel, chunkGenerator, random, blockPos);
	}
}
