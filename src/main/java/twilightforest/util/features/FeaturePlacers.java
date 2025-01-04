package twilightforest.util.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.init.TFBlocks;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.RootPlacer;
import twilightforest.util.iterators.VoxelBresenhamIterator;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Feature Utility methods that invoke placement. For non-placement see FeatureLogic
 */
public final class FeaturePlacers {
	public static final BiFunction<LevelSimulatedReader, BlockPos, Boolean> VALID_TREE_POS = TreeFeature::validTreePos;

	public static <T extends Mob> void placeEntity(EntityType<T> entityType, BlockPos pos, ServerLevelAccessor levelAccessor) {
		Mob mob = entityType.create(levelAccessor.getLevel());

		if (mob == null) return;

		mob.setPersistenceRequired();
		mob.moveTo(pos, 0.0F, 0.0F);
		EventHooks.finalizeMobSpawn(mob, levelAccessor, levelAccessor.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
		if (mob instanceof EnforcedHomePoint home) {
			home.setRestrictionPoint(GlobalPos.of(levelAccessor.getLevel().dimension(), pos));
		}
		levelAccessor.addFreshEntityWithPassengers(mob);
		levelAccessor.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
	}

	/**
	 * Draws a line from {x1, y1, z1} to {x2, y2, z2}
	 * This takes all variables for setting Branch
	 */
	public static void drawBresenhamBranch(LevelAccessor world, BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource random, BlockPos start, BlockPos end, BlockStateProvider config) {
		for (BlockPos pixel : new VoxelBresenhamIterator(start, end)) {
			placeIfValidTreePos(world, trunkPlacer, random, pixel, config);
		}
	}

	/**
	 * Build a root, but don't let it stick out too far into thin air because that's weird
	 */
	public static void buildRoot(LevelAccessor world, RootPlacer placer, RandomSource rand, BlockPos start, double offset, int b, BlockStateProvider config) {
		BlockPos dest = FeatureLogic.translate(start.below(b + 2), 5, 0.3 * b + offset, 0.8);

		// go through block by block and stop drawing when we head too far into open air
		for (BlockPos coord : new VoxelBresenhamIterator(start.below(), dest)) {
			if (!placeIfValidRootPos(world, placer, rand, coord, config)) return;
		}
	}

	/**
	 * Draws a line from {x1, y1, z1} to {x2, y2, z2}
	 * This just takes a BlockState, used to set Trunk
	 */
	public static void drawBresenhamTree(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, BlockPos from, BlockPos to, BlockStateProvider config, RandomSource random) {
		for (BlockPos pixel : new VoxelBresenhamIterator(from, to)) {
			placeProvidedBlock(world, placer, predicate, pixel, config, random);
		}
	}

	public static void placeProvidedBlock(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> worldPlacer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, BlockPos pos, BlockStateProvider config, RandomSource random) {
		if (predicate.apply(world, pos)) worldPlacer.accept(pos, config.getState(random, pos));
	}

	public static void placeLeaf(LevelSimulatedReader world, FoliagePlacer.FoliageSetter setter, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, BlockPos pos, BlockStateProvider config, RandomSource random) {
		if (predicate.apply(world, pos)) setter.set(pos, config.getState(random, pos));
	}

	public static void placeCircleOdd(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, RandomSource random, BlockPos centerPos, float radius, BlockStateProvider config) {
		placeCircleOdd(world, placer, predicate, random, centerPos, radius, config, false);
	}

	// Use for trunks with Odd-count widths
	public static void placeCircleOdd(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, RandomSource random, BlockPos centerPos, float radius, BlockStateProvider config, boolean useLegacyDistance) {
		// Normally, I'd use mutable pos here but there are multiple bits of logic down the line that force
		// the pos to be immutable causing multiple same BlockPos instances to exist.
		FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

		// trace out a quadrant
		for (int x = 0; x <= radius; x++) {
			for (int z = 1; z <= radius; z++) {
				// if we're inside the blob, fill it
				if (isWithinCircle(x, z, radius, useLegacyDistance)) {
					// do four at a time for easiness!
					FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(x, 0, z), config, random);
					FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-x, 0, -z), config, random);
					FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-z, 0, x), config, random);
					FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(z, 0, -x), config, random);
					// Confused how this circle pixel-filling algorithm works exactly? https://www.desmos.com/calculator/psqynhk21k
				}
			}
		}
	}

	public static void placeCircleEven(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, RandomSource random, BlockPos centerPos, float radius, BlockStateProvider config) {
		placeCircleEven(world, placer, predicate, random, centerPos, radius, config, false);
	}

	// Use for trunks with Even-count widths
	public static void placeCircleEven(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, RandomSource random, BlockPos centerPos, float radius, BlockStateProvider config, boolean useLegacyDistance) {
		// Normally, I'd use mutable pos here but there are multiple bits of logic down the line that force
		// the pos to be immutable causing multiple same BlockPos instances to exist.
		FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

		// trace out a quadrant
		for (int x = 0; x <= radius; x++) {
			for (int z = 0; z <= radius; z++) {
				// if we're inside the blob, fill it
				if (isWithinCircle(x, z, radius, useLegacyDistance)) {
					// do four at a time for easiness!
					FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(1 + x, 0, 1 + z), config, random);
					FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-x, 0, -z), config, random);
					FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-x, 0, 1 + z), config, random);
					FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(1 + x, 0, -z), config, random);
					// Confused how this circle pixel-filling algorithm works exactly? https://www.desmos.com/calculator/psqynhk21k
				}
			}
		}
	}

	public static boolean isWithinCircle(int x, int z, float radius, boolean useLegacyDistance) {
		if (useLegacyDistance) {
			int absX = Math.abs(x);
			int absZ = Math.abs(z);
			int legacyDistance = absX == 3 && absZ == 3 ? 6 : (int) (Math.max(absX, absZ) + Math.min(absX, absZ) * 0.5F);
			return legacyDistance <= radius;
		}

		return (x * x + z * z) <= radius * radius;
	}

	public static void placeSpheroid(LevelSimulatedReader world, FoliagePlacer.FoliageSetter setter, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, RandomSource random, BlockPos centerPos, float xzRadius, float yRadius, float verticalBias, BlockStateProvider config) {
		float xzRadiusSquared = xzRadius * xzRadius;
		float yRadiusSquared = yRadius * yRadius;
		float superRadiusSquared = xzRadiusSquared * yRadiusSquared;
		FeaturePlacers.placeLeaf(world, setter, predicate, centerPos, config, random);

		for (int y = 0; y <= yRadius; y++) {
			if (y > yRadius) continue;

			FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(0, y, 0), config, random);
			FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(0, -y, 0), config, random);
		}

		for (int x = 0; x <= xzRadius; x++) {
			for (int z = 1; z <= xzRadius; z++) {
				if (x * x + z * z > xzRadiusSquared) continue;

				FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(x, 0, z), config, random);
				FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(-x, 0, -z), config, random);
				FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(-z, 0, x), config, random);
				FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(z, 0, -x), config, random);

				for (int y = 1; y <= yRadius; y++) {
					float xzSquare = ((x * x + z * z) * yRadiusSquared);

					if (xzSquare + (((y - verticalBias) * (y - verticalBias)) * xzRadiusSquared) <= superRadiusSquared) {
						FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(x, y, z), config, random);
						FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(-x, y, -z), config, random);
						FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(-z, y, x), config, random);
						FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(z, y, -x), config, random);
					}

					if (xzSquare + (((y + verticalBias) * (y + verticalBias)) * xzRadiusSquared) <= superRadiusSquared) {
						FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(x, -y, z), config, random);
						FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(-x, -y, -z), config, random);
						FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(-z, -y, x), config, random);
						FeaturePlacers.placeLeaf(world, setter, predicate, centerPos.offset(z, -y, -x), config, random);
					}
				}
			}
		}
	}

	// Version without the `verticalBias` unlike above
	public static void placeSpheroid(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, BiFunction<LevelSimulatedReader, BlockPos, Boolean> predicate, RandomSource random, BlockPos centerPos, float xzRadius, float yRadius, BlockStateProvider config) {
		float xzRadiusSquared = xzRadius * xzRadius;
		float yRadiusSquared = yRadius * yRadius;
		float superRadiusSquared = xzRadiusSquared * yRadiusSquared;
		FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos, config, random);

		for (int y = 0; y <= yRadius; y++) {
			if (y > yRadius) continue;

			FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(0, y, 0), config, random);
			FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(0, -y, 0), config, random);
		}

		for (int x = 0; x <= xzRadius; x++) {
			for (int z = 1; z <= xzRadius; z++) {
				if (x * x + z * z > xzRadiusSquared) continue;

				FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(x, 0, z), config, random);
				FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-x, 0, -z), config, random);
				FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-z, 0, x), config, random);
				FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(z, 0, -x), config, random);

				for (int y = 1; y <= yRadius; y++) {
					float ySquare = (y * y) * xzRadiusSquared;
					if ((x * x + z * z) * yRadiusSquared + ySquare <= superRadiusSquared) {
						if ((((x + 1) * (x + 1) + z * z) * yRadiusSquared) + ySquare > superRadiusSquared && ((x * x + (z + 1) * (z + 1)) * yRadiusSquared) + ySquare > superRadiusSquared) {
							// randomly don't generate some blocks on the very edges of the circles that comprise the leaf blob
							if (random.nextInt(3) != 0) FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(x, y, z), config, random);
							if (random.nextInt(3) != 0) FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-x, y, -z), config, random);
							if (random.nextInt(3) != 0) FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-z, y, x), config, random);
							if (random.nextInt(3) != 0) FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(z, y, -x), config, random);

							if (random.nextInt(3) != 0) FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(x, -y, z), config, random);
							if (random.nextInt(3) != 0) FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-x, -y, -z), config, random);
							if (random.nextInt(3) != 0) FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-z, -y, x), config, random);
							if (random.nextInt(3) != 0) FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(z, -y, -x), config, random);
							continue;
						}

						FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(x, y, z), config, random);
						FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-x, y, -z), config, random);
						FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-z, y, x), config, random);
						FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(z, y, -x), config, random);

						FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(x, -y, z), config, random);
						FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-x, -y, -z), config, random);
						FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(-z, -y, x), config, random);
						FeaturePlacers.placeProvidedBlock(world, placer, predicate, centerPos.offset(z, -y, -x), config, random);
					}
				}
			}
		}
	}

	// [VanillaCopy] TrunkPlacer.placeLog - Swapped TreeConfiguration for BlockStateProvider
	// If possible, use TrunkPlacer.placeLog instead
	public static boolean placeIfValidTreePos(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> placer, RandomSource random, BlockPos pos, BlockStateProvider config) {
		if (validTreePos(world, pos)) {
			placer.accept(pos, config.getState(random, pos));
			return true;
		} else {
			return false;
		}
	}

	public static boolean placeIfValidRootPos(LevelSimulatedReader world, RootPlacer placer, RandomSource random, BlockPos pos, BlockStateProvider config) {
		if (!FeatureUtil.anyBelowMatch(pos, placer.getRootPenetrability() - 1, (blockPos -> !FeatureLogic.canRootGrowIn(world, blockPos)))) {
			placer.getPlacer().accept(pos, config.getState(random, pos));
			return true;
		} else {
			return false;
		}
	}

	public static boolean validTreePos(LevelSimulatedReader world, BlockPos pos) {
		return TreeFeature.validTreePos(world, pos) || world.isStateAtPosition(pos, (state -> state.is(BlockTags.FLOWERS)));
	}

	/**
	 * Add a firefly at the specified height and angle.
	 *
	 * @param height how far up the tree
	 * @param angle  from 0 - 1 rotation around the tree
	 */
	public static void addFirefly(LevelAccessor world, BlockPos pos, int height, double angle) {
		int iAngle = (int) (angle * 4.0);
		if (iAngle == 0) {
			setIfEmpty(world, pos.offset(1, height, 0), TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.EAST));
		} else if (iAngle == 1) {
			setIfEmpty(world, pos.offset(-1, height, 0), TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.WEST));
		} else if (iAngle == 2) {
			setIfEmpty(world, pos.offset(0, height, 1), TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.SOUTH));
		} else if (iAngle == 3) {
			setIfEmpty(world, pos.offset(0, height, -1), TFBlocks.FIREFLY.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.NORTH));
		}
	}

	private static void setIfEmpty(LevelAccessor world, BlockPos pos, BlockState state) {
		if (world.isEmptyBlock(pos)) {
			world.setBlock(pos, state, Block.UPDATE_ALL);
		}
	}

	public static BlockState transferAllStateKeys(BlockState stateIn, Block blockOut) {
		return transferAllStateKeys(stateIn, blockOut.defaultBlockState());
	}

	public static BlockState transferAllStateKeys(BlockState stateIn, BlockState stateOut) {
		for (Property<?> property : stateOut.getProperties()) {
			stateOut = transferStateKey(stateIn, stateOut, property);
		}
		return stateOut;
	}

	public static <T extends Comparable<T>> BlockState transferStateKey(BlockState stateIn, BlockState stateOut, Property<T> property) {
		if (!stateIn.hasProperty(property) || !stateOut.hasProperty(property)) return stateOut;
		return stateOut.setValue(property, stateIn.getValue(property));
	}

	public static void traceRoot(LevelSimulatedReader worldReader, RootPlacer worldPlacer, RandomSource random, BlockStateProvider dirtRoot, Iterable<BlockPos> posTracer) {
		// Trace block positions and stop tracing too far into open air
		for (BlockPos rootPos : posTracer) {
			if (FeatureUtil.anyBelowMatch(rootPos, worldPlacer.getRootPenetrability() - 1, (blockPos -> worldReader.isStateAtPosition(blockPos, FeatureLogic.ROOT_SHOULD_SKIP))))
				return; // End root if this block cannot be penetrated

			// If the block/position cannot be replaced or is detached from ground-mass, stop
			if (!FeaturePlacers.placeIfValidRootPos(worldReader, worldPlacer, random, rootPos, dirtRoot))
				return;
		}
	}

	@SuppressWarnings("deprecation")
	public static void traceExposedRoot(LevelSimulatedReader worldReader, RootPlacer worldPlacer, RandomSource random, BlockStateProvider exposedRoot, BlockStateProvider dirtRoot, Iterable<BlockPos> posTracer) {
		// Trace block positions and alternate the root tracing once "underground"
		for (BlockPos exposedPos : posTracer) {
			if (worldReader.isStateAtPosition(exposedPos, FeatureLogic.ROOT_SHOULD_SKIP)) {
                continue;
            }

			// Is the position considered not underground?
			if (FeatureLogic.hasEmptyNeighborExceptBelow(worldReader, exposedPos)) {
				// Check if the position is not replaceable
				if (FeatureUtil.anyBelowMatch(exposedPos, worldPlacer.getRootPenetrability() - 1, (blockPos -> worldReader.isStateAtPosition(blockPos, (state) -> !FeatureLogic.worldGenReplaceable(state)
					&& state != exposedRoot.getState(random, exposedPos)))))
					return; // Root must stop

				// Good to go!
				worldPlacer.getPlacer().accept(exposedPos, exposedRoot.getState(random, exposedPos));
			} else { // We are in-fact underground, finish tracing root's path by placing underground roots
				// Retry placement at position as underground root. If successful, continue the tracing as regular root
				if (FeaturePlacers.placeIfValidRootPos(worldReader, worldPlacer, random, exposedPos, dirtRoot))
					traceRoot(worldReader, worldPlacer, random, dirtRoot, posTracer);

				return; // Now the outer loop can end as we terminate here. Goodbye!
			}
		}
	}


	@Deprecated
	@SuppressWarnings("SameParameterValue")
	public static void replaceBlocksDome(WorldGenLevel level, BlockPos centerPos, float radius, float squish, BoundingBox chunkBox, BoundingBox structureBox, Block target, BlockState replacement) {
		float radiusSquared = radius * radius;

		BoundingBox commonRegion = BoundingBoxUtils.getIntersectionOfSBBs(chunkBox, structureBox);
		if (commonRegion == null) return;

		processDomeColumn(level, centerPos, radiusSquared, commonRegion, target, replacement);

		for (int z = 1; z <= radius; z++) {
			for (int x = 0; x <= radius; x++) {
				int distSq = x * x + z * z;

				if (distSq <= radiusSquared) {
					float height = (radiusSquared - distSq) * squish;

					processDomeColumn(level, centerPos.offset(x, 0, z), height, commonRegion, target, replacement);
					processDomeColumn(level, centerPos.offset(-x, 0, -z), height, commonRegion, target, replacement);
					processDomeColumn(level, centerPos.offset(-z, 0, x), height, commonRegion, target, replacement);
					processDomeColumn(level, centerPos.offset(z, 0, -x), height, commonRegion, target, replacement);
				}
			}
		}
	}

	@SuppressWarnings("SameParameterValue")
	public static void replaceBlocksDome(WorldGenLevel level, BlockPos centerPos, float radius, float squish, BoundingBox chunkBox, BoundingBox structureBox, HolderSet<Block> target, BlockState replacement) {
		float radiusSquared = radius * radius;

		BoundingBox commonRegion = BoundingBoxUtils.getIntersectionOfSBBs(chunkBox, structureBox);
		if (commonRegion == null) return;

		processDomeColumn(level, centerPos, radiusSquared, commonRegion, target, replacement);

		for (int z = 1; z <= radius; z++) {
			for (int x = 0; x <= radius; x++) {
				int distSq = x * x + z * z;

				if (distSq <= radiusSquared) {
					float height = (radiusSquared - distSq) * squish;

					processDomeColumn(level, centerPos.offset(x, 0, z), height, commonRegion, target, replacement);
					processDomeColumn(level, centerPos.offset(-x, 0, -z), height, commonRegion, target, replacement);
					processDomeColumn(level, centerPos.offset(-z, 0, x), height, commonRegion, target, replacement);
					processDomeColumn(level, centerPos.offset(z, 0, -x), height, commonRegion, target, replacement);
				}
			}
		}
	}

	public static void processDomeColumn(WorldGenLevel level, BlockPos pos, float height, BoundingBox mask, HolderSet<Block> target, BlockState replacement) {
		if (!mask.isInside(pos)) return;

		for (int dY = 0; dY < height; dY++) {
			BlockPos posElevated = pos.above(dY);

			if (level.getBlockState(posElevated).is(target)) {
				level.setBlock(posElevated, replacement, Block.UPDATE_ALL);
			}
		}
	}

	public static void processDomeColumn(WorldGenLevel level, BlockPos pos, float height, BoundingBox mask, Block target, BlockState replacement) {
		if (!mask.isInside(pos)) return;

		for (int dY = 0; dY < height; dY++) {
			BlockPos posElevated = pos.above(dY);

			if (level.getBlockState(posElevated).is(target)) {
				level.setBlock(posElevated, replacement, Block.UPDATE_ALL);
			}
		}
	}
}
