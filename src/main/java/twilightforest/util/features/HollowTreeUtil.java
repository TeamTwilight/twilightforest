package twilightforest.util.features;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import tamaized.beanification.Component;
import twilightforest.util.RootPlacer;
import twilightforest.util.iterators.VoxelBresenhamIterator;
import twilightforest.world.components.feature.config.TreeConfig;

import java.util.function.BiConsumer;

@Component
public class HollowTreeUtil {

	public enum BranchType {
		SMALL,
		MEDIUM,
		ROOT
	}

	/**
	 * Build a ring of branches around the tree
	 */
	public void buildBranchRing(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int radius, int branchHeight, int heightVar, int length, double tilt, int minBranches, int maxBranches, BranchType type, boolean leafy, TreeConfig config) {
		// Let's do this!
		int numBranches = random.nextInt(maxBranches - minBranches) + minBranches;
		double branchRotation = 1.0 / (numBranches + 1);
		double branchOffset = random.nextDouble();

		for (int i = 0; i <= numBranches; i++) {
			int dHeight;

			if (heightVar > 0)
				dHeight = branchHeight - heightVar + random.nextInt(2 * heightVar);
			else
				dHeight = branchHeight;

			switch (type) {
				case SMALL -> makeSmallBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, dHeight, length, i * branchRotation + branchOffset, tilt, leafy, config);
				case MEDIUM -> makeMedBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, dHeight, length, i * branchRotation + branchOffset, tilt, leafy, config);
				case ROOT -> makeRoot(world, random, pos, radius, dHeight, length, i * branchRotation + branchOffset, tilt, config);
			}
		}
	}

	/**
	 * This function builds the hollow trunk of the tree
	 */
	public void buildTrunk(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, RootPlacer decoPlacer, RandomSource random, BlockPos pos, int radius, int height, TreeConfig config) {
		final int hollow = radius >> 1;

		// Go down 4 squares and fill in extra trunk as needed, in case we're on uneven terrain
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				// Determine how far we are from the center.
				int ax = Math.abs(dx);
				int az = Math.abs(dz);
				int dist = Math.max(ax, az) + (Math.min(ax, az) >> 1);
				if (dist > radius)
					continue;

				for (int dy = -4; dy < 0; dy++) {
					BlockPos dPos = pos.offset(dx, dy, dz);
					if (FeatureUtil.hasAirAround(world, dPos)) {
						if (dist > hollow)
							trunkPlacer.accept(dPos, config.trunkProvider().getState(world, random, dPos));
						else
							trunkPlacer.accept(dPos, config.branchProvider().getState(world, random, dPos));
					} else {
						FeaturePlacers.placeIfValidRootPos(world, decoPlacer, random, dPos, config.rootsProvider());
					}
				}
			}
		}

		// Build the trunk upwards
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				for (int dy = 0; dy <= height; dy++) {
					BlockPos dPos = pos.offset(dx, dy, dz);
					// Determine how far we are from the center.
					int ax = Math.abs(dx);
					int az = Math.abs(dz);
					int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

					// Make a trunk!
					if (dist <= radius && dist > hollow)
						FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, dPos, config.trunkProvider());

					// How about a ladder?  Is that okay?
					if (dist == hollow && dx == hollow)
						world.setBlock(dPos, Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true), Block.UPDATE_ALL);
				}
			}
		}
	}

	/**
	 * Make a branch!
	 */
	public void makeMedBranch(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt, boolean leafy, TreeConfig config) {
		BlockPos src = FeatureLogic.translate(pos.above(branchHeight), diameter, angle, 0.5);
		makeMedBranch(world, trunkPlacer, leavesPlacer, random, src, length, angle, tilt, leafy, config);
	}

	/**
	 * Make a branch!
	 */
	public void makeMedBranch(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos src, double length, double angle, double tilt, boolean leafy, TreeConfig config) {
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, random, src, dest, config.branchProvider());

		// With leaves!

		if (leafy) {
			// And a blob at the end
			FeaturePlacers.placeSpheroid(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, dest, 2.5f, 2.5f, config.leavesProvider());
		}

		// And several small branches
		int numShoots = random.nextInt(2) + 1;
		double angleInc, angleVar, outVar, tiltVar;

		angleInc = 0.8 / numShoots;

		for (int i = 0; i <= numShoots; i++) {
			angleVar = (angleInc * i) - 0.4;
			outVar = (random.nextDouble() * 0.8) + 0.2;
			tiltVar = (random.nextDouble() * 0.75) + 0.15;
			BlockPos bsrc = FeatureLogic.translate(src, length * outVar, angle, tilt);
			double slength = length * 0.4;
			makeSmallBranch(world, trunkPlacer, leavesPlacer, random, bsrc, slength, angle + angleVar, tilt * tiltVar, leafy, config);
		}
	}

	/**
	 * Make a small branch with a leaf blob at the end
	 */
	public void makeSmallBranch(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos src, double length, double angle, double tilt, boolean leafy, TreeConfig config) {
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, random, src, dest, config.branchProvider());

		if (leafy) {
			float leafRad = random.nextInt(2) + 1.5f;
			FeaturePlacers.placeSpheroid(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, dest, leafRad, leafRad, config.leavesProvider());
		}
	}

	/**
	 * Make a small branch at a certain height
	 */
	public void makeSmallBranch(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt, boolean leafy, TreeConfig config) {
		BlockPos src = FeatureLogic.translate(pos.above(branchHeight), diameter, angle, 0.5);
		makeSmallBranch(world, trunkPlacer, leavesPlacer, random, src, length, angle, tilt, leafy, config);
	}

	/**
	 * Make a root
	 */
	public void makeRoot(WorldGenLevel world, RandomSource random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt, TreeConfig config) {
		BlockPos src = FeatureLogic.translate(pos.above(branchHeight), diameter, angle, 0.5);
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		FeaturePlacers.traceExposedRoot(world, new RootPlacer((checkedPos, state) -> {
			world.setBlock(checkedPos, state, Block.UPDATE_ALL);
			world.setBlock(checkedPos.below(), state, Block.UPDATE_ALL);
		}, 2), random, config.branchProvider(), config.rootsProvider(), new VoxelBresenhamIterator(src, dest));
	}
}
