package twilightforest.world.components.feature.trollcave;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.util.features.FeatureLogic;

// [VanillaCopy] HugeBrownMushroomFeature
public record TrollHugeBrownMushroomFeature(Holder<BlockStateProvider> capProvider, Holder<BlockStateProvider> stemProvider, int foliageRadius, BlockPredicate canPlaceOn) implements AbstractHugeMushroomFeature {

	public static final MapCodec<TrollHugeBrownMushroomFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC
			.fieldOf("cap_provider")
			.forGetter(TrollHugeBrownMushroomFeature::capProvider),
		BlockStateProvider.CODEC
			.fieldOf("stem_provider")
			.forGetter(TrollHugeBrownMushroomFeature::stemProvider),
		Codec.INT
			.optionalFieldOf("foliage_radius", 2)
			.forGetter(TrollHugeBrownMushroomFeature::foliageRadius),
		BlockPredicate.CODEC
			.fieldOf("can_place_on")
			.forGetter(TrollHugeBrownMushroomFeature::canPlaceOn)
	).apply(instance, TrollHugeBrownMushroomFeature::new));

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MAP_CODEC;
	}

	@Override
	public int getTreeRadiusForHeight(int i, int i1, int i2, int i3) {
		return i3 <= 3 ? 0 : i2;
	}

	@Override
	public void makeCap(WorldGenLevel worldGenLevel, RandomSource randomSource, BlockPos blockPos, int i, BlockPos.MutableBlockPos mutableBlockPos) {
		int foliageRadius = foliageRadius();

		for (int x = -foliageRadius; x <= foliageRadius; x++) {
			for (int z = -foliageRadius; z <= foliageRadius; z++) {
				if (!FeatureLogic.isCornerInSquare(x, z, foliageRadius)) {
					mutableBlockPos.setWithOffset(blockPos, x, i, z);

					if (!worldGenLevel.getBlockState(mutableBlockPos).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
						BlockState blockState = capProvider().value().getState(worldGenLevel, randomSource, blockPos);
						if (FeatureLogic.hasHorizontalMushroomProperties(blockState)) {
							blockState = FeatureLogic.getHorizontalMushroomBlockState(blockState, x, z, foliageRadius);
						}

						this.setBlock(worldGenLevel, mutableBlockPos, blockState);
					}
				}
			}
		}
	}
}
