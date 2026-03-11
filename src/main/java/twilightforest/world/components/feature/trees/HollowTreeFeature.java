package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.loot.TFLootTables;
import twilightforest.util.*;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.features.FeatureUtil;
import twilightforest.util.iterators.VoxelBresenhamIterator;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

import java.util.function.BiConsumer;

public abstract class HollowTreeFeature extends TFTreeFeature<TFTreeFeatureConfig> {

	private static final int LEAF_DUNGEON_CHANCE = 8;

	public HollowTreeFeature(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	public static void makeHollowTree(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer, TFTreeFeatureConfig config) {
		int radius = random.nextInt(4) + 1;
		int height = random.nextInt(64) + 32;

//		// check the top too
//		int crownRadius = radius * 4 + 8;
//		for (int dx = -crownRadius; dx <= crownRadius; dx++) {
//			for (int dz = -crownRadius; dz <= crownRadius; dz++) {
//				for (int dy = height - crownRadius; dy <= height + crownRadius; dy++) {
//					Block whatsThere = world.getBlockState(pos.offset(dx, dy, dz)).getBlock();
//					if (whatsThere != Blocks.AIR && !(whatsThere instanceof LeavesBlock)) {
//						return;
//					}
//				}
//			}
//		}

		// Start with roots first, so they don't fail placement because they intersect the trunk shell first
		// 3-5 roots at the bottom
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 3, 2, 6, 0.75D, 3, 5, 3, false, config);

		// several more taproots
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, 1, 2, 8, 0.9D, 3, 5, 3, false, config);

		// make a tree!

		// build the trunk
		buildTrunk(world, trunkPlacer, decorationPlacer, random, pos, radius, height, config);

		// fireflies
		int numFireflies = random.nextInt(6 * radius) + 5;
		for (int i = 0; i <= numFireflies; i++) {
			int fHeight = (int) (height * random.nextDouble() * 0.9) + (height / 10);
			double fAngle = random.nextDouble();
			addBug(world, TFBlocks.FIREFLY.get(), pos, radius, fHeight, fAngle);
		}

		// cicadas
		int numCicadas = random.nextInt(3 * radius) + 5;
		for (int i = 0; i <= numCicadas; i++) {
			int fHeight = (int) (height * random.nextDouble() * 0.9) + (height / 10);
			double fAngle = random.nextDouble();
			addBug(world, TFBlocks.CICADA.get(), pos, radius, fHeight, fAngle);
		}

		// build the crown
		buildFullCrown(world, trunkPlacer, leavesPlacer, random, pos, radius, height, config);

		// 3-5 couple branches on the way up...
		int numBranches = random.nextInt(3) + 3;
		for (int i = 0; i <= numBranches; i++) {
			int branchHeight = (int) (height * random.nextDouble() * 0.9) + (height / 10);
			double branchRotation = random.nextDouble();
			makeSmallBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, branchHeight, 4, branchRotation, 0.35D, true, config);
		}

	}

	/**
	 * Build the crown of the tree
	 */
	protected static void buildFullCrown(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int radius, int height, TFTreeFeatureConfig config) {
		int crownRadius = radius * 4 + 4;
		int bvar = radius + 2;

		// okay, let's do 3-5 main branches starting at the bottom of the crown
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height - crownRadius, 0, crownRadius, 0.35D, bvar, bvar + 2, 2, true, config);

		// then, let's do 3-5 medium branches at the crown middle
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height - (crownRadius / 2), 0, crownRadius, 0.28D, bvar, bvar + 2, 1, true, config);

		// finally, let's do 2-4 main branches at the crown top
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height, 0, crownRadius, 0.15D, 2, 4, 2, true, config);

		// and extra finally, let's do 3-6 medium branches going straight up
		buildBranchRing(world, trunkPlacer, leavesPlacer, random, pos, radius, height, 0, (crownRadius / 2), 0.05D, bvar, bvar + 2, 1, true, config);

		// this glass sphere approximates where we want our crown
		//drawBlob(x, y + height, z, (byte)crownRadius, (byte)Blocks.GLASS, false);
	}

	/**
	 * Build a ring of branches around the tree
	 * size 0 = small, 1 = med, 2 = large, 3 = root
	 */
	protected static void buildBranchRing(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int radius
		, int branchHeight, int heightVar, int length, double tilt, int minBranches, int maxBranches, int size, boolean leafy, TFTreeFeatureConfig config) {
		//let's do this!
		int numBranches = random.nextInt(maxBranches - minBranches) + minBranches;
		double branchRotation = 1.0 / (numBranches + 1);
		double branchOffset = random.nextDouble();

		for (int i = 0; i <= numBranches; i++) {
			int dHeight;
			if (heightVar > 0) {
				dHeight = branchHeight - heightVar + random.nextInt(2 * heightVar);
			} else {
				dHeight = branchHeight;
			}

			if (size == 2) {
				makeLargeBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, dHeight, length, i * branchRotation + branchOffset, tilt, leafy, config);
			} else if (size == 1) {
				makeMedBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, dHeight, length, i * branchRotation + branchOffset, tilt, leafy, config);
			} else if (size == 3) {
				makeRoot(world, random, pos, radius, dHeight, length, i * branchRotation + branchOffset, tilt, config);
			} else {
				makeSmallBranch(world, trunkPlacer, leavesPlacer, random, pos, radius, dHeight, length, i * branchRotation + branchOffset, tilt, leafy, config);
			}
		}
	}

	/**
	 * This function builds the hollow trunk of the tree
	 */
	protected static void buildTrunk(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, RootPlacer decoPlacer, RandomSource random, BlockPos pos, int radius, int height, TFTreeFeatureConfig config) {
		final int hollow = radius >> 1;

		// go down 4 squares and fill in extra trunk as needed, in case we're on uneven terrain
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				// determine how far we are from the center.
				int ax = Math.abs(dx);
				int az = Math.abs(dz);
				int dist = Math.max(ax, az) + (Math.min(ax, az) >> 1);
				if (dist > radius) {
					continue;
				}

				for (int dy = -4; dy < 0; dy++) {
					BlockPos dPos = pos.offset(dx, dy, dz);
					if (FeatureUtil.hasAirAround(world, dPos)) {
						if (dist > hollow) {
							trunkPlacer.accept(dPos, config.trunkProvider.getState(random, dPos));
						} else {
							trunkPlacer.accept(dPos, config.branchProvider.getState(random, dPos));
						}
					} else {
						FeaturePlacers.placeIfValidRootPos(world, decoPlacer, random, dPos, config.rootsProvider);
					}
				}
			}
		}

		// build the trunk upwards
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				for (int dy = 0; dy <= height; dy++) {
					BlockPos dPos = pos.offset(dx, dy, dz);
					// determine how far we are from the center.
					int ax = Math.abs(dx);
					int az = Math.abs(dz);
					int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

					// make a trunk!
					if (dist <= radius && dist > hollow) {
						FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, random, dPos, config.trunkProvider);
					}

					// fill it with lava!
					if (dist <= hollow) {
						// just kidding!
						//world.setBlock(dx + x, dy + y, dz + z, Blocks.LAVA);
					}

					// how about a ladder?  is that okay?
					if (dist == hollow && dx == hollow) {
//						putBlockAndMetadata(dx + x, dy + y, dz + z, Blocks.LADDER,  4, true);
						world.setBlock(dPos, Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true), Block.UPDATE_ALL);
					}
				}
			}
		}
	}

	/**
	 * Make a branch!
	 */
	protected static void makeMedBranch(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt, boolean leafy, TFTreeFeatureConfig config) {
		BlockPos src = FeatureLogic.translate(pos.above(branchHeight), diameter, angle, 0.5);
		makeMedBranch(world, trunkPlacer, leavesPlacer, random, src, length, angle, tilt, leafy, config);
	}

	/**
	 * Make a branch!
	 */
	protected static void makeMedBranch(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos src, double length, double angle, double tilt, boolean leafy, TFTreeFeatureConfig config) {
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, random, src, dest, config.branchProvider);

		// with leaves!

		if (leafy) {
			/*
			int numLeafBalls = random.nextInt(2) + 1;
			for(int i = 0; i <= numLeafBalls; i++) {

				double slength = random.nextDouble() * 0.6 + 0.2;
				int[] bdst = translate(src[0], src[1], src[2], slength, angle, tilt);

				drawBlob(bdst[0], bdst[1], bdst[2], 2, leafBlock, false);
			}
			*/

			// and a blob at the end
			FeaturePlacers.placeSpheroid(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, dest, 2.5f, 2.5f, config.leavesProvider);
		}

		// and several small branches

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
	protected static void makeSmallBranch(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos src, double length, double angle, double tilt, boolean leafy, TFTreeFeatureConfig config) {
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, random, src, dest, config.branchProvider);

		if (leafy) {
			float leafRad = random.nextInt(2) + 1.5f;
			FeaturePlacers.placeSpheroid(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, dest, leafRad, leafRad, config.leavesProvider);
		}
	}

	/**
	 * Make a small branch at a certain height
	 */
	protected static void makeSmallBranch(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt, boolean leafy, TFTreeFeatureConfig config) {
		BlockPos src = FeatureLogic.translate(pos.above(branchHeight), diameter, angle, 0.5);
		makeSmallBranch(world, trunkPlacer, leavesPlacer, random, src, length, angle, tilt, leafy, config);
	}

	/**
	 * Make a root
	 */
	protected static void makeRoot(LevelAccessor world, RandomSource random, BlockPos pos, int diameter, int branchHeight, double length, double angle, double tilt, TFTreeFeatureConfig config) {
		BlockPos src = FeatureLogic.translate(pos.above(branchHeight), diameter, angle, 0.5);
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		FeaturePlacers.traceExposedRoot(world, new RootPlacer((checkedPos, state) -> {
			world.setBlock(checkedPos, state, Block.UPDATE_ALL);
			world.setBlock(checkedPos.below(), state, Block.UPDATE_ALL);
		}, 2), random, config.branchProvider, config.rootsProvider, new VoxelBresenhamIterator(src, dest));
	}

	/**
	 * Make a large, branching "base" branch in a specific location.
	 * <p>
	 * The large branch will have 1-4 medium branches and several small branches too
	 */
	protected static void makeLargeBranch(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos src, double length, double angle, double tilt, boolean leafy, TFTreeFeatureConfig config) {
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		// draw the main branch
		FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, random, src, dest, config.branchProvider);

		// reinforce it
		//drawBresehnam(src[0], src[1] + 1, src[2], dest[0], dest[1], dest[2], treeBlock, true);
		int reinforcements = random.nextInt(3);
		for (int i = 0; i <= reinforcements; i++) {
			int vx = (i & 2) == 0 ? 1 : 0;
			int vy = (i & 1) == 0 ? 1 : -1;
			int vz = (i & 2) == 0 ? 0 : 1;
			FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, random, src.offset(vx, vy, vz), dest, config.branchProvider);
		}

		if (leafy) {
			// add a leaf blob at the end
			FeaturePlacers.placeSpheroid(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, dest.above(), 3.5f, 3.5f, config.leavesProvider);
		}

		// go about halfway out and make a few medium branches.
		// the number of medium branches we can support depends on the length of the big branch
		// every other branch switches sides
		int numMedBranches = random.nextInt(Math.max((int) (length / 6), 1)) + random.nextInt(2) + 1;

		for (int i = 0; i <= numMedBranches; i++) {

			double outVar = (random.nextDouble() * 0.3) + 0.3;
			double angleVar = random.nextDouble() * 0.225 * ((i & 1) == 0 ? 1.0 : -1.0);
			BlockPos bsrc = FeatureLogic.translate(src, length * outVar, angle, tilt);

			makeMedBranch(world, trunkPlacer, leavesPlacer, random, bsrc, length * 0.6, angle + angleVar, tilt, leafy, config);
		}

		// make 1-2 small ones near the base
		int numSmallBranches = random.nextInt(2) + 1;
		for (int i = 0; i <= numSmallBranches; i++) {

			double outVar = (random.nextDouble() * 0.25) + 0.25;
			double angleVar = random.nextDouble() * 0.25 * ((i & 1) == 0 ? 1.0 : -1.0);
			BlockPos bsrc = FeatureLogic.translate(src, length * outVar, angle, tilt);

			makeSmallBranch(world, trunkPlacer, leavesPlacer, random, bsrc, Math.max(length * 0.3, 2), angle + angleVar, tilt, leafy, config);
		}
		if (random.nextInt(LEAF_DUNGEON_CHANCE) == 0) {
			makeLeafDungeon(world, leavesPlacer, random, dest.above(), config);
		}
	}

	private static void makeLeafDungeon(WorldGenLevel world, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, TFTreeFeatureConfig config) {
		// make leaves
		FeaturePlacers.placeSpheroid(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, pos, 4.5f, 4.5f, config.leavesProvider);
		// wood support
		FeatureUtil.drawBlob(world, pos, 3, config.branchProvider.getState(random, pos));
		// air
		FeatureUtil.drawBlob(world, pos, 2, Blocks.AIR.defaultBlockState());

		// spawner
		world.setBlock(pos.above(), Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_CLIENTS);
		SpawnerBlockEntity ms = (SpawnerBlockEntity) world.getBlockEntity(pos.above());
		if (ms != null) {
			ms.setEntityId(TFEntities.SWARM_SPIDER.get(), random);
		}

		// treasure chests?
		makeLeafDungeonChest(world, random, pos);
	}

	private static void makeLeafDungeonChest(WorldGenLevel world, RandomSource random, BlockPos pos) {
		Direction chestDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
		pos = pos.relative(chestDir, 2);
		TFLootTables.generateChest(world, pos.below(), chestDir.getOpposite(), false, TFLootTables.TREE_CACHE);
	}

	/**
	 * Make a large, branching "base" branch off of the tree
	 */
	protected static void makeLargeBranch(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int radius, int branchHeight, double length, double angle, double tilt, boolean leafy, TFTreeFeatureConfig config) {
		BlockPos src = FeatureLogic.translate(pos.above(branchHeight), radius, angle, 0.5);
		makeLargeBranch(world, trunkPlacer, leavesPlacer, random, src, length, angle, tilt, leafy, config);
	}

	/**
	 * Add a critter at the specified height and angle.
	 */
	public static void addBug(LevelAccessor level, Block bug, BlockPos pos, int diameter, int fHeight, double fAngle) {
		BlockPos src = FeatureLogic.translate(pos.above(fHeight), diameter + 1, fAngle, 0.5);

		fAngle = fAngle % 1.0;
		Direction facing = Direction.EAST;

		if (fAngle > 0.875 || fAngle <= 0.125) {
			facing = Direction.SOUTH;
		} else if (fAngle > 0.375 && fAngle <= 0.625) {
			facing = Direction.NORTH;
		} else if (fAngle > 0.625) {
			facing = Direction.WEST;
		}

		if (bug.defaultBlockState().setValue(DirectionalBlock.FACING, facing).canSurvive(level, src)) {
			level.setBlock(src, bug.defaultBlockState().setValue(DirectionalBlock.FACING, facing), Block.UPDATE_ALL);
		}
	}
}
