package twilightforest.util.features;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.GiantBlock;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Feature Utility methods that don't invoke placement. For placement see FeaturePlacers
 */
public final class FeatureLogic {
	public static final Predicate<BlockState> IS_REPLACEABLE_AIR = state -> state.canBeReplaced() || state.isAir();
	public static final Predicate<BlockState> ROOT_SHOULD_SKIP = state -> state.is(BlockTagGenerator.ROOT_TRACE_SKIP);

	public static boolean hasEmptyNeighborExceptBelow(LevelSimulatedReader worldReader, BlockPos pos) {
		return worldReader.isStateAtPosition(pos.above(), IS_REPLACEABLE_AIR)
			|| worldReader.isStateAtPosition(pos.north(), IS_REPLACEABLE_AIR)
			|| worldReader.isStateAtPosition(pos.south(), IS_REPLACEABLE_AIR)
			|| worldReader.isStateAtPosition(pos.west(), IS_REPLACEABLE_AIR)
			|| worldReader.isStateAtPosition(pos.east(), IS_REPLACEABLE_AIR);
	}

	// Slight stretch of logic: We check if the block is completely surrounded by air.
	// If it's not completely surrounded by air, then there's a solid
	public static boolean hasSolidNeighbor(LevelSimulatedReader worldReader, BlockPos pos) {
		return !(worldReader.isStateAtPosition(pos.below(), IS_REPLACEABLE_AIR)
			&& worldReader.isStateAtPosition(pos.north(), IS_REPLACEABLE_AIR)
			&& worldReader.isStateAtPosition(pos.south(), IS_REPLACEABLE_AIR)
			&& worldReader.isStateAtPosition(pos.west(), IS_REPLACEABLE_AIR)
			&& worldReader.isStateAtPosition(pos.east(), IS_REPLACEABLE_AIR)
			&& worldReader.isStateAtPosition(pos.above(), IS_REPLACEABLE_AIR));
	}

	public static boolean canRootGrowIn(LevelSimulatedReader worldReader, BlockPos pos) {
		if (worldReader.isStateAtPosition(pos, IS_REPLACEABLE_AIR)) {
			// roots can grow through air if they are near a solid block
			return hasSolidNeighbor(worldReader, pos);
		} else {
			return worldReader.isStateAtPosition(pos, FeatureLogic::worldGenReplaceable);
		}
	}

	public static boolean isReplaceable(BlockState state, boolean includeFlowers) {
		return (state.canBeReplaced() || state.is(BlockTagGenerator.WORLDGEN_REPLACEABLES) || (includeFlowers && state.is(BlockTags.FLOWERS)))
			&& !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
	}

	public static boolean worldGenReplaceable(BlockState state) {
		return isReplaceable(state, false);
	}

	public static boolean treesReplaceable(BlockState state) {
		return isReplaceable(state, true);
	}

	/**
	 * Moves distance along the vector.
	 * <p>
	 * This goofy function takes a float between 0 and 1 for the angle, where 0 is 0 degrees, .5 is 180 degrees and 1 and 360 degrees.
	 * For the tilt, it takes a float between 0 and 1 where 0 is straight up, 0.5 is straight out and 1 is straight down.
	 */
	public static BlockPos translate(BlockPos pos, double distance, double angle, double tilt) {
		double rangle = angle * 2.0D * Math.PI;
		double rtilt = tilt * Math.PI;

		return pos.offset(
			(int) Math.round(Math.sin(rangle) * Math.sin(rtilt) * distance),
			(int) Math.round(Math.cos(rtilt) * distance),
			(int) Math.round(Math.cos(rangle) * Math.sin(rtilt) * distance)
		);
	}

	//private static final ImmutableSet<Material> MATERIAL_WHITELIST = ImmutableSet.of(Material.DIRT, Material.GRASS, Material.LEAVES, Material.WOOD, Material.PLANT, Material.STONE);

	//FIXME turn this into a tag list, see MATERIAL_WHITELIST
	//turn isSolid into said tag
	public static boolean isAreaClear(BlockGetter world, BlockPos min, BlockPos max) {
		for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
			BlockState state = world.getBlockState(pos);
			if (!state.canBeReplaced() && state.isSolid() && !state.liquid()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isBlockOk(BlockState state) {
		return state.isSolid();
	}

	public static boolean isBlockNotOk(BlockState state) {
		return state.liquid() || state.is(Blocks.BEDROCK) || state.getBlock() instanceof GiantBlock || state.is(BlockTagGenerator.CLOUDS) || state.is(TFBlocks.HARDENED_DARK_LEAVES);
	}

	// North is treated for default rotation and null means you're not on the wall
	@Nullable
	public static Rotation wallVolumeRotation(RandomSource rand, int deltaX, int deltaZ, int xBound, int zBound) {
		boolean westWall = deltaX == 0;
		boolean northWall = deltaZ == 0;
		boolean eastWall = deltaX == xBound;
		boolean southWall = deltaZ == zBound;

		if (westWall || northWall || eastWall || southWall)
			return getRotation(rand, northWall, eastWall, southWall, westWall);

		return null;
	}

	// North is treated for default rotation, plan your code accordingly
	@NotNull
	public static Rotation getRotation(RandomSource rand, boolean north, boolean east, boolean south, boolean west) {
		// North -> NONE
		// East -> CLOCKWISE_90
		// South -> CLOCKWISE_180
		// West -> COUNTERCLOCKWISE_90
		// Combined adjacents will be settled by random

		List<Rotation> availableForRandom = new ArrayList<>();

		if (north) availableForRandom.add(Rotation.NONE);
		if (east) availableForRandom.add(Rotation.CLOCKWISE_90);
		if (south) availableForRandom.add(Rotation.CLOCKWISE_180);
		if (west) availableForRandom.add(Rotation.COUNTERCLOCKWISE_90);

		if (availableForRandom.isEmpty()) return Rotation.NONE;

		Util.shuffle(availableForRandom, rand);

		return Util.getRandom(availableForRandom, rand);
	}

	public static boolean hasHorizontalMushroomProperties(BlockState blockState) {
		return blockState.hasProperty(HugeMushroomBlock.WEST)
			&& blockState.hasProperty(HugeMushroomBlock.EAST)
			&& blockState.hasProperty(HugeMushroomBlock.NORTH)
			&& blockState.hasProperty(HugeMushroomBlock.SOUTH);
	}

	public static boolean hasAllMushroomsProperties(BlockState blockState) {
		return blockState.hasProperty(HugeMushroomBlock.UP) && hasHorizontalMushroomProperties(blockState);  // Down is never used in HugeMushrooms
	}

	public static boolean isEdge(int x, int edge) {
		return x == edge || x == -edge;
	}

	public static boolean isEdge(int x, int z, int edge) {
		return isEdge(x, edge) != isEdge(z, edge);
	}

	public static boolean isCorner(int x, int z, int edgeX, int edgeZ) {
		return isEdge(x, edgeX) && isEdge(z, edgeZ);
	}

	public static boolean isCornerInSquare(int x, int z, int edge) {
		return isCorner(x, z, edge, edge);
	}

	public static BlockState getHorizontalMushroomBlockState(BlockState blockState, int x, int z, int foliageRadius) {
		if (hasHorizontalMushroomProperties(blockState)) {
			boolean west = x == -foliageRadius || (isEdge(z, foliageRadius) && x == 1 - foliageRadius);
			boolean east = x == foliageRadius || (isEdge(z, foliageRadius) && x == foliageRadius - 1);
			boolean north = z == -foliageRadius || (isEdge(x, foliageRadius) && z == 1 - foliageRadius);
			boolean south = z == foliageRadius || (isEdge(x, foliageRadius) && z == foliageRadius - 1);

			blockState = blockState
				.setValue(HugeMushroomBlock.WEST, west)
				.setValue(HugeMushroomBlock.EAST, east)
				.setValue(HugeMushroomBlock.NORTH, north)
				.setValue(HugeMushroomBlock.SOUTH, south);
		}

		return blockState;
	}

	public static BlockState getSphericalMushroomBlockState(BlockState blockState, int x, int y, int z, int foliageRadius, int height) {
		if (hasAllMushroomsProperties(blockState)) {
			blockState = blockState
				.setValue(HugeMushroomBlock.UP, y >= height - 1)
				.setValue(HugeMushroomBlock.WEST, x == -foliageRadius)
				.setValue(HugeMushroomBlock.EAST, x == foliageRadius)
				.setValue(HugeMushroomBlock.NORTH, z == -foliageRadius)
				.setValue(HugeMushroomBlock.SOUTH, z == foliageRadius);
		}
		return blockState;
	}
}
