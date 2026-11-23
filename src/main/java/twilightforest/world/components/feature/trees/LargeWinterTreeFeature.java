package twilightforest.world.components.feature.trees;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.RootPlacer;
import twilightforest.world.components.feature.config.TreeConfig;

import java.util.function.BiConsumer;

public record LargeWinterTreeFeature(TreeConfig config) implements TFTreeFeature {

	public static final MapCodec<LargeWinterTreeFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		TreeConfig.MAP_CODEC.forGetter(LargeWinterTreeFeature::config)
	).apply(instance, LargeWinterTreeFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer) {
		// Determine a height
		int treeHeight = 35;
		if (random.nextInt(3) == 0) {
			treeHeight += random.nextInt(10);

			if (random.nextInt(8) == 0) {
				treeHeight += random.nextInt(10);
			}
		}

		if (world.isOutsideBuildHeight(pos.getY() + treeHeight)) {
			return false;
		}

		// Check if we're on a valid block
		if (!SnowTreeFeature.validTreePos(world, pos)) {
			return false;
		}

		// Okay build a tree!  Go up to the height
		buildTrunk(world, trunkPlacer, random, pos, treeHeight);

		// Make leaves
		makeLeaves(world, trunkPlacer, leavesPlacer, random, pos, treeHeight);

		// Roots!
		int numRoots = 4 + random.nextInt(3);
		float offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			FeaturePlacers.buildRoot(world, decorationPlacer, random, pos, offset, b, config().rootsProvider());
		}

		return true;
	}

	private void makeLeaves(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int treeHeight) {
		int offGround = 3;
		int leafType = 1;

		for (int dy = 0; dy < treeHeight; dy++) {

			int radius = leafRadius(treeHeight, dy, leafType);

			FeaturePlacers.placeCircleEven(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, pos.above(offGround + treeHeight - dy), radius, config().leavesProvider());
			this.makePineBranches(world, trunkPlacer, random, pos.above(offGround + treeHeight - dy), radius);
		}
	}

	private void makePineBranches(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource rand, BlockPos pos, int radius) {
		int branchLength = radius > 4 ? radius - 1 : radius - 2;

		switch (pos.getY() % 2) {
			case 0:
				// Branches
				for (int i = 1; i <= branchLength; i++) {
					this.placeLogAt(world, trunkPlacer, rand, pos.offset(-i, 0, 0), Direction.Axis.X);
					this.placeLogAt(world, trunkPlacer, rand, pos.offset(0, 0, i + 1), Direction.Axis.Z);
					this.placeLogAt(world, trunkPlacer, rand, pos.offset(i + 1, 0, 1), Direction.Axis.X);
					this.placeLogAt(world, trunkPlacer, rand, pos.offset(1, 0, -i), Direction.Axis.Z);
				}
				break;
			case 1:
				for (int i = 1; i <= branchLength; i++) {
					this.placeLogAt(world, trunkPlacer, rand, pos.offset(-1, 0, 1), Direction.Axis.X);
					this.placeLogAt(world, trunkPlacer, rand, pos.offset(1, 0, i + 1), Direction.Axis.Z);
					this.placeLogAt(world, trunkPlacer, rand, pos.offset(i + 1, 0, 0), Direction.Axis.X);
					this.placeLogAt(world, trunkPlacer, rand, pos.offset(0, 0, -i), Direction.Axis.Z);
				}
				break;
		}
	}

	private void placeLogAt(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource rand, BlockPos pos, Direction.Axis axis) {
		trunkPlacer.accept(pos, config().trunkProvider().getState(level, rand, pos).setValue(RotatedPillarBlock.AXIS, axis));
	}

	private int leafRadius(int treeHeight, int dy, int functionType) {
		return switch (functionType) {
			case 1 -> (int) (4F * dy / treeHeight + (0.75F * dy % 3));
			case 99 -> (treeHeight - (dy / 2) - 1) % 4; // bad
			default -> (dy - 1) % 4;
		};
	}

	private void buildTrunk(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource rand, BlockPos pos, int treeHeight) {
		for (int dy = 0; dy < treeHeight; dy++) {
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 0), config().trunkProvider()) && dy == 0)
				world.setBlock(pos.offset(0, -1, 0), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 1), config().trunkProvider()) && dy == 0)
				world.setBlock(pos.offset(0, -1, 1), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 0), config().trunkProvider()) && dy == 0)
				world.setBlock(pos.offset(1, -1, 0), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 1), config().trunkProvider()) && dy == 0)
				world.setBlock(pos.offset(1, -1, 1), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		}
	}
}
