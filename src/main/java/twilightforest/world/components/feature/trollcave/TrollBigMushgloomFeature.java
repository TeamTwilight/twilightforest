package twilightforest.world.components.feature.trollcave;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import twilightforest.util.features.FeatureLogic;
import twilightforest.world.components.feature.BigMushgloomFeature;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TrollBigMushgloomFeature extends BigMushgloomFeature {
	public TrollBigMushgloomFeature(Codec<HugeMushroomFeatureConfiguration> config) {
		super(config);
	}

	@Override
	protected void makeCap(LevelAccessor levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		int foliageRadius = featureConfiguration.foliageRadius;
		int capHeight = random.nextBoolean() ? 1 : 2;

		for (int y = 0; y < capHeight; y++) {
			for (int x = -foliageRadius; x <= foliageRadius; ++x) {
				for (int z = -foliageRadius; z <= foliageRadius; ++z) {
					mutableBlockPos.setWithOffset(pos, x, height + y, z);
					if (!levelAccessor.getBlockState(mutableBlockPos).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
						BlockState blockstate = featureConfiguration.capProvider.getState(random, pos);
						blockstate = FeatureLogic.getSphericalMushroomBlockState(blockstate, x, y, z, foliageRadius, capHeight);
						this.setBlock(levelAccessor, mutableBlockPos, blockstate);
					}
				}
			}
		}
	}
}
