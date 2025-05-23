package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Optional;
import java.util.stream.Stream;

public class WeightedListFeatureConfig implements FeatureConfiguration {
	public static final Codec<WeightedListFeatureConfig> CODEC = SimpleWeightedRandomList.wrappedCodec(PlacedFeature.CODEC).xmap(WeightedListFeatureConfig::new, c -> c.randomFeatures);

	private final SimpleWeightedRandomList<Holder<PlacedFeature>> randomFeatures;

	public WeightedListFeatureConfig(SimpleWeightedRandomList<Holder<PlacedFeature>> randomFeatures) {
		this.randomFeatures = randomFeatures;
	}

	public Optional<Holder<PlacedFeature>> getRandomFeature(RandomSource random) {
		return this.randomFeatures.getRandomValue(random);
	}

	@Override
	public Stream<ConfiguredFeature<?, ?>> getFeatures() {
		return this.randomFeatures.unwrap()
			.stream()
			.map(WeightedEntry.Wrapper::data)
			.map(Holder::value)
			.map(PlacedFeature::feature)
			.map(Holder::value)
			.flatMap(ConfiguredFeature::getFeatures);
	}
}
