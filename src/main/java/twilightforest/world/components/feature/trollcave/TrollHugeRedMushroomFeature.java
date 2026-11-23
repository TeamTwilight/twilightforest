package twilightforest.world.components.feature.trollcave;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.util.features.FeatureLogic;


// [VanillaCopy] HugeRedMushroomFeature
public record TrollHugeRedMushroomFeature(Holder<BlockStateProvider> capProvider, Holder<BlockStateProvider> stemProvider, int foliageRadius, BlockPredicate canPlaceOn) implements AbstractHugeMushroomFeature {

	public static final MapCodec<TrollHugeRedMushroomFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC
			.fieldOf("cap_provider")
			.forGetter(TrollHugeRedMushroomFeature::capProvider),
		BlockStateProvider.CODEC
			.fieldOf("stem_provider")
			.forGetter(TrollHugeRedMushroomFeature::stemProvider),
		Codec.INT
			.optionalFieldOf("foliage_radius", 2)
			.forGetter(TrollHugeRedMushroomFeature::foliageRadius),
		BlockPredicate.CODEC
			.fieldOf("can_place_on")
			.forGetter(TrollHugeRedMushroomFeature::canPlaceOn)
	).apply(instance, TrollHugeRedMushroomFeature::new));

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MAP_CODEC;
	}

	@Override
	public int getTreeRadiusForHeight(int i, int i1, int i2, int i3) {
		int radius = 0;
		if (i3 < i1 && i3 >= i1 - 3) {
			radius = i2;
		} else if (i3 == i1) {
			radius = i2;
		}

		return radius;
	}

	@Override
	public void makeCap(WorldGenLevel worldGenLevel, RandomSource randomSource, BlockPos blockPos, int i, BlockPos.MutableBlockPos mutableBlockPos) {
		for (int y = i - 3; y <= i; y++) {
			int foliageRadius = y < i ? foliageRadius() : foliageRadius() - 1;
			int innerRadius = foliageRadius() - 2;

			for (int x = -foliageRadius; x <= foliageRadius; x++) {
				for (int z = -foliageRadius; z <= foliageRadius; z++) {
					if (y >= i || FeatureLogic.isEdge(x, z, foliageRadius)) {
						mutableBlockPos.setWithOffset(blockPos, x, y, z);
						if (!worldGenLevel.getBlockState(mutableBlockPos).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
							BlockState blockState = capProvider().value().getState(worldGenLevel, randomSource, blockPos);
							if (FeatureLogic.hasAllMushroomsProperties(blockState)) {
								blockState = blockState.setValue(HugeMushroomBlock.UP, y >= i - 1)
									.setValue(HugeMushroomBlock.WEST, x < -innerRadius)
									.setValue(HugeMushroomBlock.EAST, x > innerRadius)
									.setValue(HugeMushroomBlock.NORTH, z < -innerRadius)
									.setValue(HugeMushroomBlock.SOUTH, z > innerRadius);
							}
							setBlock(worldGenLevel, mutableBlockPos, blockState);
						}
					}
				}
			}
		}
	}
}
