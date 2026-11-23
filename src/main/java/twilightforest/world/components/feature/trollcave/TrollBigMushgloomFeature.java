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

public record TrollBigMushgloomFeature(Holder<BlockStateProvider> capProvider, Holder<BlockStateProvider> stemProvider, int foliageRadius, BlockPredicate canPlaceOn) implements AbstractHugeMushroomFeature {

	public static final MapCodec<TrollBigMushgloomFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC
			.fieldOf("cap_provider")
			.forGetter(TrollBigMushgloomFeature::capProvider),
		BlockStateProvider.CODEC
			.fieldOf("stem_provider")
			.forGetter(TrollBigMushgloomFeature::stemProvider),
		Codec.INT
			.optionalFieldOf("foliage_radius", 2)
			.forGetter(TrollBigMushgloomFeature::foliageRadius),
		BlockPredicate.CODEC
			.fieldOf("can_place_on")
			.forGetter(TrollBigMushgloomFeature::canPlaceOn)
	).apply(instance, TrollBigMushgloomFeature::new));

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MAP_CODEC;
	}

	@Override
	public int getTreeHeight(RandomSource random) {
		return 2 + random.nextInt(2);
	}

	@Override
	public int getTreeRadiusForHeight(int i, int i1, int i2, int i3) {
		return i3 <= 2 ? 0 : foliageRadius;
	}

	@Override
	public void makeCap(WorldGenLevel worldGenLevel, RandomSource randomSource, BlockPos blockPos, int i, BlockPos.MutableBlockPos mutableBlockPos) {
		int capHeight = randomSource.nextBoolean() ? 1 : 2;

		for (int y = 0; y < capHeight; y++) {
			for (int x = -foliageRadius(); x <= foliageRadius(); ++x) {
				for (int z = -foliageRadius(); z <= foliageRadius(); ++z) {
					mutableBlockPos.setWithOffset(blockPos, x, i + y, z);
					if (!worldGenLevel.getBlockState(mutableBlockPos).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
						BlockState blockstate = capProvider().value().getState(worldGenLevel, randomSource, blockPos);
						blockstate = FeatureLogic.getSphericalMushroomBlockState(blockstate, x, y, z, foliageRadius(), capHeight);
						this.setBlock(worldGenLevel, mutableBlockPos, blockstate);
					}
				}
			}
		}
	}
}
