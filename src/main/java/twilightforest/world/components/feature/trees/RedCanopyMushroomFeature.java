package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RedCanopyMushroomFeature extends CanopyMushroomFeature {
	/**
	 * Weights should produce the following distribution:
	 * 0: Vanilla, 33% Chance, styled big red mushroom cap,
	 * 1: Smooth, 33% Chance, kinda like the ones on a mushroom castle,
	 * 2: Spheroid, 33% Chance, same style of cap as you'd find in TF version 1.17, for example
	 * 3: Flat, 1% chance, same as the brown mushroom one, these used to be in older versions of TF
	 */
	private final int enumHead;

	public RedCanopyMushroomFeature(Codec<HugeMushroomFeatureConfiguration> featureConfigurationCodec, int enumHead) {
		super(featureConfigurationCodec);
		this.enumHead = enumHead;
	}

	@Override
	public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> context) {
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

	@Override
	protected void makeCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		if (this.enumHead == 0) {
			this.makeVanillaCap(levelAccessor, random, pos, height, mutableBlockPos, featureConfiguration);
		} else if (this.enumHead == 1) {
			this.makeSmoothCap(levelAccessor, random, pos, height, mutableBlockPos, featureConfiguration);
		} else if (this.enumHead == 2) {
			this.makeSpheroidCap(levelAccessor, random, pos, height, mutableBlockPos, featureConfiguration);
		} else super.makeCap(levelAccessor, random, pos, height, mutableBlockPos, featureConfiguration);
	}

	//Pretty much a 1:1 vanilla copy of the big red mushroom cap code
	protected void makeVanillaCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		for (int y = height - 3; y <= height; ++y) {
			int j = y < height ? featureConfiguration.foliageRadius() : featureConfiguration.foliageRadius() - 1;
			int k = featureConfiguration.foliageRadius() - 2;

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
						if (this.isReplaceable(levelAccessor, mutableBlockPos)) {
							BlockState blockstate = featureConfiguration.capProvider().getState(levelAccessor, random, pos);

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

	protected void makeSmoothCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		for (int y = height - 2; y <= height + 1; ++y) {
			int j = featureConfiguration.foliageRadius() - Math.max(0, y - (height - 1)) + 1;

			for (int x = -j; x <= j; ++x) {
				for (int z = -j; z <= j; ++z) {
					if (isInsideSmoothShape(height, j, x, y, z)) {
						mutableBlockPos.setWithOffset(pos, x, y, z);
						if (this.isReplaceable(levelAccessor, mutableBlockPos)) {
							BlockState blockstate = featureConfiguration.capProvider().getState(levelAccessor, random, pos);

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

	protected void makeSpheroidCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration featureConfiguration) {
		for (int y = height - 2; y <= height; ++y) {
			int j = y == height - 1 ? featureConfiguration.foliageRadius() + 2 : featureConfiguration.foliageRadius() + 1;

			for (int x = -j; x <= j; ++x) {
				for (int z = -j; z <= j; ++z) {
					double distance = Math.sqrt(x * x + z * z);
					double maxDistance = (double) j + 0.1D;
					if (distance <= maxDistance) {
						mutableBlockPos.setWithOffset(pos, x, y, z);
						if (this.isReplaceable(levelAccessor, mutableBlockPos)) {
							BlockState blockstate = featureConfiguration.capProvider().getState(levelAccessor, random, pos);

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
