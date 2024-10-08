package twilightforest.world.components.feature.trollcave;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.HugeBrownMushroomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import twilightforest.util.features.FeatureLogic;

import javax.annotation.ParametersAreNonnullByDefault;


// [VanillaCopy]
@ParametersAreNonnullByDefault
public class TrollHugeBrownMushroomFeature extends HugeBrownMushroomFeature {
	public TrollHugeBrownMushroomFeature(Codec<HugeMushroomFeatureConfiguration> config) {
		super(config);
	}

	@Override
	protected void makeCap(LevelAccessor levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		int foliageRadius = featureConfiguration.foliageRadius;

		for (int x = -foliageRadius; x <= foliageRadius; x++) {
			for (int z = -foliageRadius; z <= foliageRadius; z++) {
				if (!FeatureLogic.isCornerInSquare(x, z, foliageRadius)) {
					mutableBlockPos.setWithOffset(pos, x, height, z);

					if (!levelAccessor.getBlockState(mutableBlockPos).is(BlockTags.FEATURES_CANNOT_REPLACE)) {
						BlockState blockState = featureConfiguration.capProvider.getState(random, pos);
						if (FeatureLogic.hasHorizontalMushroomProperties(blockState)) {
							blockState = FeatureLogic.getHorizontalMushroomBlockState(blockState, x, z, foliageRadius);
						}

						this.setBlock(levelAccessor, mutableBlockPos, blockState);
					}
				}
			}
		}
	}
}
