package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record BrownCanopyMushroomFeature(Holder<BlockStateProvider> capProvider, Holder<BlockStateProvider> stemProvider, int foliageRadius, BlockPredicate canPlaceOn) implements CanopyMushroomFeature {
	public static final MapCodec<BrownCanopyMushroomFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC
			.fieldOf("cap_provider")
			.forGetter(BrownCanopyMushroomFeature::capProvider),
		BlockStateProvider.CODEC
			.fieldOf("stem_provider")
			.forGetter(BrownCanopyMushroomFeature::stemProvider),
		Codec.INT
			.optionalFieldOf("foliage_radius", 2)
			.forGetter(BrownCanopyMushroomFeature::foliageRadius),
		BlockPredicate.CODEC
			.fieldOf("can_place_on")
			.forGetter(BrownCanopyMushroomFeature::canPlaceOn)
	).apply(instance, BrownCanopyMushroomFeature::new));

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MAP_CODEC;
	}

	@Override
	public int getBranches(RandomSource random) {
		return Math.max(random.nextInt(5), 3);
	}

	@Override
	public double getLength(RandomSource random) {
		return 9 - random.nextInt(2);
	}
}
