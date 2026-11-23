package twilightforest.world.components.feature.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.iterators.VoxelBresenhamIterator;

public interface CanopyMushroomFeature extends AbstractHugeMushroomFeature {

	@Override
	default void placeTrunk(WorldGenLevel level, RandomSource random, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos) {
		int bugsLeft = Math.max(0, random.nextInt(10) - 4) / 2; // Weird math, I know, but I like the odds (and weird math, sue me)

		for (int i = 0; i < treeHeight; ++i) {
			blockPos.set(origin).move(Direction.UP, i);
			if (isReplaceable(level, blockPos)) {
				setBlock(level, blockPos, stemProvider().value().getState(level, random, origin));

				if (bugsLeft > 0 && i > treeHeight / 2 && random.nextInt(10) == 9)
					if (addFirefly(level, blockPos, random))
						bugsLeft--;
			} else {
				treeHeight = i;
				break;
			}
		}

		int numBranches = getBranches(random);
		float offset = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			bugsLeft = buildABranch(level, origin, treeHeight - 6 + b, getLength(random), 0.3 * b + offset, random, bugsLeft);
		}
	}

	@Override
	default int getTreeHeight(RandomSource random) {
		return 9 + random.nextInt(5);
	}

	@Override
	default int getTreeRadiusForHeight(int trunkHeight, int treeHeight, int leafRadius, int yo) {
		return yo <= 3 ? 0 : (int) ((float) leafRadius * 1.5F);
	}

	@Override
	default void makeCap(WorldGenLevel level, RandomSource random, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos) {
		makeCapWithRadius(level, random, origin, treeHeight, blockPos, foliageRadius());
	}

	default void makeCapWithRadius(WorldGenLevel level, RandomSource random, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos, int radius) {
		for (int x = -radius; x <= radius; ++x) {
			for (int z = -radius; z <= radius; ++z) {
				if (!FeatureLogic.isCornerInSquare(x, z, radius)) {
					blockPos.setWithOffset(origin, x, treeHeight, z);
					if (isReplaceable(level, blockPos)) {
						BlockState blockState = capProvider().value().getState(level, random, origin);
						blockState = FeatureLogic.getHorizontalMushroomBlockState(blockState, x, z, radius);
						setBlock(level, blockPos, blockState);
					}
				}
			}
		}
	}

	default boolean isReplaceable(LevelAccessor level, BlockPos pos) {
		BlockState blockState = level.getBlockState(pos);
		return FeatureLogic.isReplaceable(blockState, true) && !blockState.isSolidRender();
	}

	int getBranches(RandomSource random);

	double getLength(RandomSource random);

	private int buildABranch(WorldGenLevel level, BlockPos pos, int height, double length, double angle, RandomSource random, int bugsLeft) {
		BlockPos src = pos.above(height);
		BlockPos dest = FeatureLogic.translate(src, length, angle, 0.2);

		for (BlockPos pixel : new VoxelBresenhamIterator(src, new BlockPos(dest.getX(), src.getY(), dest.getZ()))) {
			BlockState blockstate = stemProvider().value().getState(level, random, pos);

			if (blockstate.hasProperty(HugeMushroomBlock.UP) && blockstate.hasProperty(HugeMushroomBlock.DOWN)) {
				blockstate = blockstate.setValue(HugeMushroomBlock.DOWN, true).setValue(HugeMushroomBlock.UP, true); // Seal up the ups and downs
			}

			if (isReplaceable(level, pixel)) {
				setBlock(level, pixel, blockstate);
			}
		}

		int max = Math.max(src.getY(), dest.getY());

		for (int i = Math.min(src.getY(), dest.getY()); i < max + 1; i++) {
			BlockState blockstate = stemProvider().value().getState(level, random, pos);

			if (blockstate.hasProperty(HugeMushroomBlock.DOWN)) {
				if (i == Math.min(src.getY(), dest.getY()))
					blockstate = blockstate.setValue(HugeMushroomBlock.DOWN, true); //Seal up the bottom one, so it looks better
			}

			BlockPos blockPos = new BlockPos(dest.getX(), i, dest.getZ());

			if (isReplaceable(level, blockPos)) {
				setBlock(level, blockPos, blockstate);
			}

			if (bugsLeft > 0 && i > Math.min(src.getY(), dest.getY()) / 2 && random.nextInt(20) == 0)
				if (addFirefly(level, blockPos, random))
					bugsLeft--;
		}

		makeCapWithRadius(level, random, dest, 1, new BlockPos.MutableBlockPos(), foliageRadius() - 1); // Branches need caps as well, height in this case is set to 1

		return bugsLeft;
	}

	private boolean addFirefly(LevelAccessor level, BlockPos pos, RandomSource random) {
		Direction direction = Direction.getRandom(random);

		if (direction.getAxis() == Direction.Axis.Y) {
			return false;
		}

		BlockPos.MutableBlockPos bugPos = new BlockPos.MutableBlockPos();
		bugPos.set(pos).move(direction);

		if (!isReplaceable(level, bugPos)) {
			return false;
		}

		BlockState bugState = TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, direction);
		setBlock(level, bugPos, bugState);
		return true;
	}
}
