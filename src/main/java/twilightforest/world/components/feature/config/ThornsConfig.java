package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record ThornsConfig(int maxSpread, int chanceOfBranch , int chanceOfLeaf , int chanceLeafIsRose) implements FeatureConfiguration {
    public static final Codec<ThornsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("max_spread").forGetter(ThornsConfig::maxSpread),
            Codec.INT.fieldOf("chance_of_branch").forGetter(ThornsConfig::chanceOfBranch),
            Codec.INT.fieldOf("chance_of_leaf").forGetter(ThornsConfig::chanceOfLeaf),
            Codec.INT.fieldOf("chance_leaf_is_rose").forGetter(ThornsConfig::chanceLeafIsRose)
    ).apply(instance, ThornsConfig::new));
}
