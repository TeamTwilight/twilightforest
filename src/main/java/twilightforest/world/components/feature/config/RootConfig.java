package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record RootConfig(BlockStateProvider blockRoot, BlockStateProvider oreRoot) implements FeatureConfiguration {
	public static final Codec<RootConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("root_block").forGetter(RootConfig::blockRoot),
		BlockStateProvider.CODEC.fieldOf("root_ore").forGetter(RootConfig::oreRoot)
	).apply(instance, RootConfig::new));
}
