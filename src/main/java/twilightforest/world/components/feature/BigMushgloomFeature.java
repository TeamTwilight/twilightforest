package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BigMushgloomFeature extends AbstractHugeMushroomFeature {

	public BigMushgloomFeature(Codec<HugeMushroomFeatureConfiguration> config) {
		super(config);
	}

	@Override
	protected int getTreeHeight(RandomSource rand) {
		return 2 + rand.nextInt(2);
	}

	@Override
	protected int getTreeRadiusForHeight(int i, int i1, int foliageRadius, int treeHeight) {
		return treeHeight <= 2 ? 0 : foliageRadius;
	}

	@Override
	protected void makeCap(LevelAccessor levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		int i = featureConfiguration.foliageRadius;

		int capHeight = random.nextBoolean() ? 1 : 2;

		for (int y = 0; y < capHeight; y++) {
			for (int x = -i; x <= i; ++x) {
				for (int z = -i; z <= i; ++z) {
					mutableBlockPos.setWithOffset(pos, x, height + y, z);
					if (!levelAccessor.getBlockState(mutableBlockPos).isSolidRender(levelAccessor, mutableBlockPos)) {
						BlockState blockstate = featureConfiguration.capProvider.getState(random, pos);

						if (blockstate.hasProperty(HugeMushroomBlock.WEST) && blockstate.hasProperty(HugeMushroomBlock.EAST) && blockstate.hasProperty(HugeMushroomBlock.NORTH) && blockstate.hasProperty(HugeMushroomBlock.SOUTH) && blockstate.hasProperty(HugeMushroomBlock.UP)) {
							blockstate = blockstate
									.setValue(HugeMushroomBlock.UP, y == 1 || capHeight == 1)
									.setValue(HugeMushroomBlock.WEST, x == -i)
									.setValue(HugeMushroomBlock.EAST, x == i)
									.setValue(HugeMushroomBlock.NORTH, z == -i)
									.setValue(HugeMushroomBlock.SOUTH, z == i);
						}

						this.setBlock(levelAccessor, mutableBlockPos, blockstate);
					}
				}
			}
		}
	}
}
