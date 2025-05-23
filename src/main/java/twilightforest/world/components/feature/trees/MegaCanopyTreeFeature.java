package twilightforest.world.components.feature.trees;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.RootPlacer;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.List;
import java.util.function.BiConsumer;

public class MegaCanopyTreeFeature extends CanopyTreeFeature {

	public MegaCanopyTreeFeature(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	protected boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer, TFTreeFeatureConfig config) {
		List<BlockPos> leaves = Lists.newArrayList();
		// determine a height
		int treeHeight = config.minHeight;
		if (random.nextInt(config.chanceAddFiveFirst) == 0) {
			treeHeight += random.nextInt(treeHeight / 2);

			if (random.nextInt(config.chanceAddFiveSecond) == 0) {
				treeHeight += random.nextInt(treeHeight / 4);
			}
		}

		if (world.isOutsideBuildHeight(pos.getY() + treeHeight)) {
			return false;
		}

		// check if we're on dirt or grass
		if (world.getBlockState(pos.below()).canSustainPlant(world, pos.below(), Direction.UP, TFBlocks.CANOPY_SAPLING.get().defaultBlockState()).isFalse()) {
			return false;
		}

		leaves.clear();

		//okay build a tree!  Go up to the height
		buildTrunk(world, leaves, trunkPlacer, random, pos, treeHeight, config);

		// make 3 - 4 branches
		int numBranches = 6 + random.nextInt(3);
		float bangle = random.nextFloat();
		int offset = 0;
		for (int b = 0; b < numBranches; b++) {
			float btilt = 0.25F;// + (random.nextFloat() * 0.05F);
			buildBranch(world, leaves, randomlyOffset(pos, random), trunkPlacer, treeHeight - 15 - (b + offset), 15 + random.nextInt(4), bangle, btilt, false, random, config);

			offset += random.nextInt(2);
			bangle += (random.nextFloat() * 0.4F);
			if (bangle > 1.0F) {
				bangle -= 1.0F;
			}
		}

		// add the actual leaves
		for (BlockPos leafPos : leaves) {
			makeLeafBlob(world, trunkPlacer, leavesPlacer, random, leafPos, config);
		}

		MegaCanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos, config);
		MegaCanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.east(), config);
		MegaCanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.south(), config);
		MegaCanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.east().south(), config);

		return true;
	}

	private void makeLeafBlob(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leafPlacer, RandomSource rand, BlockPos leafPos, TFTreeFeatureConfig config) {
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos, config.branchProvider);
		for (Direction direction : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 1), config.branchProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 2), config.branchProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 3), config.branchProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 4), config.branchProvider);

			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 5).relative(direction.getClockWise()), config.branchProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 5).relative(direction.getCounterClockWise()), config.branchProvider);

			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 2).relative(direction.getClockWise(), 1), config.branchProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 3).relative(direction.getClockWise(), 2), config.branchProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 4).relative(direction.getClockWise(), 3), config.branchProvider);
		}
		// Use circles with legacy distance instead of a spheroid to match the 1.4.7 version as much as possible
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos.above(2), 3, config.leavesProvider, true);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos.above(), 6, config.leavesProvider, true);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos, 8, config.leavesProvider, true);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos.below(), 7, config.leavesProvider, true);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos.below(2), 4.5f, config.leavesProvider, true);
	}

	private void buildTrunk(LevelAccessor world, List<BlockPos> leaves, BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource rand, BlockPos pos, int treeHeight, TFTreeFeatureConfig config) {
		for (int dy = 0; dy <= treeHeight; dy++) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 0), config.trunkProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 0), config.trunkProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 1), config.trunkProvider);
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 1), config.trunkProvider);
		}

		for (int i = 0; i < 7; i++) {
			if (rand.nextInt(3) == 0) {
				Direction direction = Direction.getRandom(rand);
				Direction.Axis axis = direction.getAxis();
				if (axis != Direction.Axis.Y) {
					BlockPos.MutableBlockPos bugPos = new BlockPos.MutableBlockPos();
					bugPos.set(pos.offset(direction == Direction.EAST ? 1 : 0, rand.nextInt(treeHeight), direction == Direction.SOUTH ? 1 : 0));
					bugPos.move(direction).move(axis == Direction.Axis.Z ? rand.nextInt(2) : 0, 0, axis == Direction.Axis.X ? rand.nextInt(2) : 0);
					if (!world.getBlockState(bugPos).isSolidRender()) {
						BlockState bugState = TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, direction);
						this.setBlock(world, bugPos, bugState);
					}
				}
			}
		}

		leaves.add(randomlyOffset(pos.above(treeHeight), rand));
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	@Override
	void buildBranch(LevelAccessor world, List<BlockPos> leaves, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, int height, double length, double angle, double tilt, boolean trunk, RandomSource treeRNG, TFTreeFeatureConfig config) {
		BlockPos src = pos.above(height);
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		// constrain branch spread
		int limit = 12;
		if ((dest.getX() - pos.getX()) < -limit) {
			dest = new BlockPos(pos.getX() - limit, dest.getY(), dest.getZ());
		}
		if ((dest.getX() - pos.getX()) > limit) {
			dest = new BlockPos(pos.getX() + limit, dest.getY(), dest.getZ());
		}
		if ((dest.getZ() - pos.getZ()) < -limit) {
			dest = new BlockPos(dest.getX(), dest.getY(), pos.getZ() - limit);
		}
		if ((dest.getZ() - pos.getZ()) > limit) {
			dest = new BlockPos(dest.getX(), dest.getY(), pos.getZ() + limit);
		}

		if (trunk) {
			FeaturePlacers.drawBresenhamTree(world, trunkPlacer, FeaturePlacers.VALID_TREE_POS, src, dest, config.trunkProvider, treeRNG);
		} else {
			FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, treeRNG, src, dest, config.branchProvider);
			FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, treeRNG, src.below(), dest.below(), config.branchProvider);
		}

		leaves.add(dest);
	}

	protected static BlockPos randomlyOffset(BlockPos pos, RandomSource random) {
		return randomlyOffset(pos.getX(), pos.getY(), pos.getZ(), random);
	}

	protected static BlockPos randomlyOffset(int x, int y, int z, RandomSource random) {
		return switch (random.nextInt(4)) {
			case 0 -> new BlockPos(x, y, z);
			case 1 -> new BlockPos(x + 1, y, z);
			case 2 -> new BlockPos(x, y, z + 1);
			default -> new BlockPos(x + 1, y, z + 1);
		};
	}
}
