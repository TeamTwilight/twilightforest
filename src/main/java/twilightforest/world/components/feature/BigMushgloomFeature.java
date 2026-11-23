package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.util.features.FeatureLogic;

public record BigMushgloomFeature(Holder<BlockStateProvider> capProvider, Holder<BlockStateProvider> stemProvider, int foliageRadius, BlockPredicate canPlaceOn) implements AbstractHugeMushroomFeature {

	public static final MapCodec<BigMushgloomFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC
			.fieldOf("cap_provider")
			.forGetter(BigMushgloomFeature::capProvider),
		BlockStateProvider.CODEC
			.fieldOf("stem_provider")
			.forGetter(BigMushgloomFeature::stemProvider),
		Codec.INT
			.optionalFieldOf("foliage_radius", 2)
			.forGetter(BigMushgloomFeature::foliageRadius),
		BlockPredicate.CODEC
			.fieldOf("can_place_on")
			.forGetter(BigMushgloomFeature::canPlaceOn)
	).apply(instance, BigMushgloomFeature::new));

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MAP_CODEC;
	}

	@Override
	public int getTreeHeight(RandomSource rand) {
		return 2 + rand.nextInt(2);
	}

	@Override
	public int getTreeRadiusForHeight(int i, int i1, int foliageRadius, int treeHeight) {
		return treeHeight <= 2 ? 0 : foliageRadius;
	}

	@Override
	public void makeCap(WorldGenLevel worldGenLevel, RandomSource randomSource, BlockPos blockPos, int i, BlockPos.MutableBlockPos mutableBlockPos) {
		int foliageRadius = foliageRadius();
		int capHeight = randomSource.nextBoolean() ? 1 : 2;

		for (int y = 0; y < capHeight; y++) {
			for (int x = -foliageRadius; x <= foliageRadius; ++x) {
				for (int z = -foliageRadius; z <= foliageRadius; ++z) {
					mutableBlockPos.setWithOffset(blockPos, x, i + y, z);
					if (!worldGenLevel.getBlockState(mutableBlockPos).isSolidRender()) {
						BlockState blockstate = capProvider().value().getState(worldGenLevel, randomSource, blockPos);
						blockstate = FeatureLogic.getSphericalMushroomBlockState(blockstate, x, y, z, foliageRadius, capHeight);
						this.setBlock(worldGenLevel, mutableBlockPos, blockstate);
					}
				}
			}
		}
	}
}
