package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record BerryBushConfig(BlockState bushState, TagKey<Block> placesOn, boolean canBeSnowy) implements FeatureConfiguration {

	public static final Codec<BerryBushConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BlockState.CODEC.fieldOf("bush").forGetter(BerryBushConfig::bushState),
		TagKey.codec(Registries.BLOCK).fieldOf("generates_on").forGetter(BerryBushConfig::placesOn),
		Codec.BOOL.fieldOf("can_generate_snowy").forGetter(BerryBushConfig::canBeSnowy)
	).apply(instance, BerryBushConfig::new));
}
