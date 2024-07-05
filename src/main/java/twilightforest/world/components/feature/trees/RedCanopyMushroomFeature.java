package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RedCanopyMushroomFeature extends CanopyMushroomFeature {
	private int altHeads = 0;

	public RedCanopyMushroomFeature(Codec<HugeMushroomFeatureConfiguration> featureConfigurationCodec) {
		super(featureConfigurationCodec);
	}

	@Override
	public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> context) {
		this.altHeads = context.random().nextInt(100) + 1; //Roll the dice on the style of cap
		return super.place(context);
	}

	@Override
	protected int getTreeHeight(RandomSource random) {
		return super.getTreeHeight(random) + 3;
	}

	@Override
	protected int getBranches(RandomSource random) {
		return 3;
	}

	@Override
	protected double getLength(RandomSource random) {
		return 10 + random.nextInt(2);
	}

	/**
	 * 33% Chance to get a vanilla - styled big red mushroom cap,
	 * 33% chance for it to be a "smooth" cap, kinda like the ones on a mushroom castle,
	 * 33% chance it's the same style of cap as you'd find in TF version 1.17, for example
	 * 1% chance for it to be a flat cap, same as the brown mushroom one, these used to be in older versions of TF
	 */
	@Override
	protected void makeCap(LevelAccessor levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		if (this.altHeads <= 33) {
			this.makeVanillaCap(levelAccessor, random, pos, height, mutableBlockPos, featureConfiguration);
		} else if (this.altHeads <= 66) {
			this.makeSmoothCap(levelAccessor, random, pos, height, mutableBlockPos, featureConfiguration);
		} else if (this.altHeads <= 99) {
			this.makeSpheroidCap(levelAccessor, random, pos, height, mutableBlockPos, featureConfiguration);
		} else super.makeCap(levelAccessor, random, pos, height, mutableBlockPos, featureConfiguration);
	}

	//Pretty much a 1:1 vanilla copy of the big red mushroom cap code
	protected void makeVanillaCap(LevelAccessor levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		for (int y = height - 3; y <= height; ++y) {
			int j = y < height ? featureConfiguration.foliageRadius : featureConfiguration.foliageRadius - 1;
			int k = featureConfiguration.foliageRadius - 2;

			for (int x = -j; x <= j; ++x) {
				for (int z = -j; z <= j; ++z) {
					boolean xIsMin = x == -j;
					boolean xIsMax = x == j;
					boolean zIsMin = z == -j;
					boolean zIsMax = z == j;
					boolean xMinMax = xIsMin || xIsMax;
					boolean zMinMax = zIsMin || zIsMax;
					if (y >= height || xMinMax != zMinMax) {
						mutableBlockPos.setWithOffset(pos, x, y, z);
						if (!levelAccessor.getBlockState(mutableBlockPos).isSolidRender(levelAccessor, mutableBlockPos)) {
							BlockState blockstate = featureConfiguration.capProvider.getState(random, pos);

							if (blockstate.hasProperty(HugeMushroomBlock.WEST) && blockstate.hasProperty(HugeMushroomBlock.EAST) && blockstate.hasProperty(HugeMushroomBlock.NORTH) && blockstate.hasProperty(HugeMushroomBlock.SOUTH) && blockstate.hasProperty(HugeMushroomBlock.UP)) {
								blockstate = blockstate
									.setValue(HugeMushroomBlock.UP, y >= height - 1)
									.setValue(HugeMushroomBlock.WEST, x < -k)
									.setValue(HugeMushroomBlock.EAST, x > k)
									.setValue(HugeMushroomBlock.NORTH, z < -k)
									.setValue(HugeMushroomBlock.SOUTH, z > k);
							}

							this.setBlock(levelAccessor, mutableBlockPos, blockstate);
						}
					}
				}
			}
		}
	}

	protected void makeSmoothCap(LevelAccessor levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		for (int y = height - 2; y <= height + 1; ++y) {
			int j = featureConfiguration.foliageRadius - Math.max(0, y - (height - 1)) + 1;

			for (int x = -j; x <= j; ++x) {
				for (int z = -j; z <= j; ++z) {
					if (isInsideSmoothShape(height, j, x, y, z)) {
						mutableBlockPos.setWithOffset(pos, x, y, z);
						if (!levelAccessor.getBlockState(mutableBlockPos).isSolidRender(levelAccessor, mutableBlockPos)) {
							BlockState blockstate = featureConfiguration.capProvider.getState(random, pos);

							if (blockstate.hasProperty(HugeMushroomBlock.WEST) && blockstate.hasProperty(HugeMushroomBlock.EAST) && blockstate.hasProperty(HugeMushroomBlock.NORTH) && blockstate.hasProperty(HugeMushroomBlock.SOUTH) && blockstate.hasProperty(HugeMushroomBlock.UP)) {
								blockstate = blockstate
									.setValue(HugeMushroomBlock.UP, !isInsideSmoothShape(height, j - (y > height - 2 ? 1 : 0), x, y + 1, z))
									.setValue(HugeMushroomBlock.WEST, !isInsideSmoothShape(height, j, x - 1, y, z) && x < 0)
									.setValue(HugeMushroomBlock.EAST, !isInsideSmoothShape(height, j, x + 1, y, z) && x > 0)
									.setValue(HugeMushroomBlock.NORTH, !isInsideSmoothShape(height, j, x, y, z - 1) && z < 0)
									.setValue(HugeMushroomBlock.SOUTH, !isInsideSmoothShape(height, j, x, y, z + 1) && z > 0);
							}

							this.setBlock(levelAccessor, mutableBlockPos, blockstate);
						}
					}
				}
			}
		}
	}

	/**
	 * Don't know if there's a cleaner way of doing this? Feel free to try, but this works for now.
	 * Most of this code is just for the blockStates to be correct, this would be a lot shorter otherwise and wouldn't be a method.
	 */
	private static boolean isInsideSmoothShape(int height, int j, int x, int y, int z) {
		int i = y - (height - 2);
		if (i == 4 || Math.abs(x) > j || Math.abs(z) > j) return false;
		if (i >= 2) return true;

		boolean xIsMin = x == -j;
		boolean xIsMax = x == j;
		boolean zIsMin = z == -j;
		boolean zIsMax = z == j;
		boolean xMinMax = xIsMin || xIsMax;
		boolean zMinMax = zIsMin || zIsMax;

		if (i == 1 && ((xMinMax && Math.abs(z) == j - 1) || (zMinMax && Math.abs(x) == j - 1))) return false;

		return xMinMax != zMinMax || (Math.abs(x) == Math.abs(z) && Math.abs(x) == j - 1);
	}

	protected void makeSpheroidCap(LevelAccessor levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		for (int y = height - 2; y <= height; ++y) {
			int j = y == height - 1 ? featureConfiguration.foliageRadius + 2 : featureConfiguration.foliageRadius + 1;

			for (int x = -j; x <= j; ++x) {
				for (int z = -j; z <= j; ++z) {
					double distance = Math.sqrt(x * x + z * z);
					double maxDistance = (double) j + 0.1D;
					if (distance <= maxDistance) {
						mutableBlockPos.setWithOffset(pos, x, y, z);
						if (!levelAccessor.getBlockState(mutableBlockPos).isSolidRender(levelAccessor, mutableBlockPos)) {
							BlockState blockstate = featureConfiguration.capProvider.getState(random, pos);

							if (blockstate.hasProperty(HugeMushroomBlock.WEST) && blockstate.hasProperty(HugeMushroomBlock.EAST) && blockstate.hasProperty(HugeMushroomBlock.NORTH) && blockstate.hasProperty(HugeMushroomBlock.SOUTH) && blockstate.hasProperty(HugeMushroomBlock.UP)) {
								blockstate = blockstate
									.setValue(HugeMushroomBlock.UP, y > height - 2 && (y == height || distance > maxDistance - 1D))
									.setValue(HugeMushroomBlock.WEST, Math.sqrt((x - 1) * (x - 1) + z * z) > maxDistance)
									.setValue(HugeMushroomBlock.EAST, Math.sqrt((x + 1) * (x + 1) + z * z) > maxDistance)
									.setValue(HugeMushroomBlock.NORTH, Math.sqrt(x * x + (z - 1) * (z - 1)) > maxDistance)
									.setValue(HugeMushroomBlock.SOUTH, Math.sqrt(x * x + (z + 1) * (z + 1)) > maxDistance);
							}

							this.setBlock(levelAccessor, mutableBlockPos, blockstate);
						}
					}
				}
			}
		}
	}
}
