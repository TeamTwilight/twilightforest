package twilightforest.world.components.feature.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.RootPlacer;

import java.util.List;
import java.util.function.BiConsumer;

public record LargeWinterTreeFeature(
	Holder<BlockStateProvider> trunkProvider,
	Holder<BlockStateProvider> leavesProvider,
	Holder<BlockStateProvider> branchProvider,
	Holder<BlockStateProvider> rootsProvider,
	int minHeight,
	int chanceAddFiveFirst,
	int chanceAddFiveSecond,
	boolean hasLeaves,
	boolean checkWater,
	List<TreeDecorator> decorators
) implements TFTreeFeature {

	public static final MapCodec<LargeWinterTreeFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(LargeWinterTreeFeature::trunkProvider),
		BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(LargeWinterTreeFeature::leavesProvider),
		BlockStateProvider.CODEC.fieldOf("branch_provider").forGetter(LargeWinterTreeFeature::branchProvider),
		BlockStateProvider.CODEC.fieldOf("roots_provider").forGetter(LargeWinterTreeFeature::rootsProvider),
		Codec.INT.fieldOf("minimum_size").forGetter(LargeWinterTreeFeature::minHeight),
		Codec.INT.fieldOf("add_first_five_chance").forGetter(LargeWinterTreeFeature::chanceAddFiveFirst),
		Codec.INT.fieldOf("add_second_five_chance").forGetter(LargeWinterTreeFeature::chanceAddFiveSecond),
		Codec.BOOL.fieldOf("has_leaves").forGetter(LargeWinterTreeFeature::hasLeaves),
		Codec.BOOL.fieldOf("check_water").forGetter(LargeWinterTreeFeature::checkWater),
		TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(LargeWinterTreeFeature::decorators)
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
		if (!TreeFeature.validTreePos(world, pos)) {
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
			FeaturePlacers.buildRoot(world, decorationPlacer, random, pos, offset, b, rootsProvider().value());
		}

		return true;
	}

	private void makeLeaves(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RandomSource random, BlockPos pos, int treeHeight) {
		int offGround = 3;
		int leafType = 1;

		for (int dy = 0; dy < treeHeight; dy++) {

			int radius = leafRadius(treeHeight, dy, leafType);

			FeaturePlacers.placeCircleEven(world, leavesPlacer, FeaturePlacers.VALID_TREE_POS, random, pos.above(offGround + treeHeight - dy), radius, leavesProvider().value());
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
		trunkPlacer.accept(pos, trunkProvider().value().getState(level, rand, pos).setValue(RotatedPillarBlock.AXIS, axis));
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
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 0), trunkProvider().value()) && dy == 0)
				world.setBlock(pos.offset(0, -1, 0), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 1), trunkProvider().value()) && dy == 0)
				world.setBlock(pos.offset(0, -1, 1), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 0), trunkProvider().value()) && dy == 0)
				world.setBlock(pos.offset(1, -1, 0), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
			if (FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 1), trunkProvider().value()) && dy == 0)
				world.setBlock(pos.offset(1, -1, 1), Blocks.DIRT.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_ALL);
		}
	}
}
