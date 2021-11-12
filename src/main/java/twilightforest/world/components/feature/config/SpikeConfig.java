package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class SpikeConfig implements FeatureConfiguration {
	public static final Codec<SpikeConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			BlockStateProvider.CODEC.fieldOf("state").forGetter(c -> c.blockState),
			// How long this Stalactite should be
			IntProvider.codec(1, 64).fieldOf("length_bounds").forGetter(c -> c.lengthBounds),
			// How much space is required before the surface of an opposing surface is touched
			IntProvider.codec(0, 10).fieldOf("tip_clearance").forGetter(c -> c.tipClearance),
			// false = stalagmite, true = stalactite
			Codec.BOOL.fieldOf("hanging").orElse(true).forGetter(c -> c.hang)
	).apply(instance, SpikeConfig::new));

	public final BlockStateProvider blockState;
	public final IntProvider lengthBounds;
	public final IntProvider tipClearance;
	public final boolean hang;

	public SpikeConfig(BlockStateProvider state, IntProvider lengthBounds, IntProvider groundClearance, boolean hang) {
		this.blockState = state;
		this.lengthBounds = lengthBounds;
		this.tipClearance = groundClearance;
		this.hang = hang;
	}
}
