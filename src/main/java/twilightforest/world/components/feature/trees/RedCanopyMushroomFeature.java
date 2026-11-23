package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.enums.MushroomCapType;

public record RedCanopyMushroomFeature(Holder<BlockStateProvider> capProvider, Holder<BlockStateProvider> stemProvider, int foliageRadius, BlockPredicate canPlaceOn, MushroomCapType capType) implements CanopyMushroomFeature {
	public static final MapCodec<RedCanopyMushroomFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC
			.fieldOf("cap_provider")
			.forGetter(RedCanopyMushroomFeature::capProvider),
		BlockStateProvider.CODEC
			.fieldOf("stem_provider")
			.forGetter(RedCanopyMushroomFeature::stemProvider),
		Codec.INT
			.optionalFieldOf("foliage_radius", 2)
			.forGetter(RedCanopyMushroomFeature::foliageRadius),
		BlockPredicate.CODEC
			.fieldOf("can_place_on")
			.forGetter(RedCanopyMushroomFeature::canPlaceOn),
		MushroomCapType.CODEC
			.fieldOf("cap_type")
			.forGetter(RedCanopyMushroomFeature::capType)
	).apply(instance, RedCanopyMushroomFeature::new));

	@Override
	public MapCodec<? extends AbstractHugeMushroomFeature> codec() {
		return MAP_CODEC;
	}

	@Override
	public int getTreeHeight(RandomSource random) {
		return CanopyMushroomFeature.super.getTreeHeight(random) + 3;
	}

	@Override
	public int getBranches(RandomSource random) {
		return 3;
	}

	@Override
	public double getLength(RandomSource random) {
		return 10 + random.nextInt(2);
	}

	@Override
	public void makeCapWithRadius(WorldGenLevel level, RandomSource random, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos, int radius) {
		switch (capType()) {
			case VANILLA -> makeVanillaCap(level, random, origin, treeHeight, blockPos, radius);
			case SMOOTH -> makeSmoothCap(level, random, origin, treeHeight, blockPos, radius);
			case SPHEROID -> makeSpheroidCap(level, random, origin, treeHeight, blockPos, radius);
			case FLAT -> CanopyMushroomFeature.super.makeCapWithRadius(level, random, origin, treeHeight, blockPos, radius);
		}
	}

	// Pretty much a 1:1 vanilla copy of the big red mushroom cap code
	private void makeVanillaCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, int radius) {
		for (int y = height - 3; y <= height; ++y) {
			int j = y < height ? radius : radius - 1;
			int k = radius - 2;

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
							BlockState blockstate = capProvider().value().getState(levelAccessor, random, pos);

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

	private void makeSmoothCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, int radius) {
		for (int y = height - 2; y <= height + 1; ++y) {
			int j = radius - Math.max(0, y - (height - 1)) + 1;

			for (int x = -j; x <= j; ++x) {
				for (int z = -j; z <= j; ++z) {
					if (isInsideSmoothShape(height, j, x, y, z)) {
						mutableBlockPos.setWithOffset(pos, x, y, z);
						if (this.isReplaceable(levelAccessor, mutableBlockPos)) {
							BlockState blockstate = capProvider().value().getState(levelAccessor, random, pos);

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
		if (i == 4 || Math.abs(x) > j || Math.abs(z) > j)
			return false;
		if (i >= 2)
			return true;

		boolean xIsMin = x == -j;
		boolean xIsMax = x == j;
		boolean zIsMin = z == -j;
		boolean zIsMax = z == j;
		boolean xMinMax = xIsMin || xIsMax;
		boolean zMinMax = zIsMin || zIsMax;

		if (i == 1 && ((xMinMax && Math.abs(z) == j - 1) || (zMinMax && Math.abs(x) == j - 1)))
			return false;

		return xMinMax != zMinMax || (Math.abs(x) == Math.abs(z) && Math.abs(x) == j - 1);
	}

	private void makeSpheroidCap(WorldGenLevel levelAccessor, RandomSource random, BlockPos pos, int height, BlockPos.MutableBlockPos mutableBlockPos, int radius) {
		for (int y = height - 2; y <= height; ++y) {
			int j = y == height - 1 ? radius + 2 : radius + 1;

			for (int x = -j; x <= j; ++x) {
				for (int z = -j; z <= j; ++z) {
					double distance = Math.sqrt(x * x + z * z);
					double maxDistance = (double) j + 0.1D;
					if (distance <= maxDistance) {
						mutableBlockPos.setWithOffset(pos, x, y, z);
						if (this.isReplaceable(levelAccessor, mutableBlockPos)) {
							BlockState blockstate = capProvider().value().getState(levelAccessor, random, pos);

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
