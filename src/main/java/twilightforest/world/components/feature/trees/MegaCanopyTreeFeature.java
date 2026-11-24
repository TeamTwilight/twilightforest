package twilightforest.world.components.feature.trees;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.RootPlacer;

import java.util.List;
import java.util.function.BiConsumer;

public record MegaCanopyTreeFeature(
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
) implements CanopyTreeFeature {

	public static final MapCodec<MegaCanopyTreeFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(MegaCanopyTreeFeature::trunkProvider),
		BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(MegaCanopyTreeFeature::leavesProvider),
		BlockStateProvider.CODEC.fieldOf("branch_provider").forGetter(MegaCanopyTreeFeature::branchProvider),
		BlockStateProvider.CODEC.fieldOf("roots_provider").forGetter(MegaCanopyTreeFeature::rootsProvider),
		Codec.INT.fieldOf("minimum_size").forGetter(MegaCanopyTreeFeature::minHeight),
		Codec.INT.fieldOf("add_first_five_chance").forGetter(MegaCanopyTreeFeature::chanceAddFiveFirst),
		Codec.INT.fieldOf("add_second_five_chance").forGetter(MegaCanopyTreeFeature::chanceAddFiveSecond),
		Codec.BOOL.fieldOf("has_leaves").forGetter(MegaCanopyTreeFeature::hasLeaves),
		Codec.BOOL.fieldOf("check_water").forGetter(MegaCanopyTreeFeature::checkWater),
		TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(MegaCanopyTreeFeature::decorators)
	).apply(instance, MegaCanopyTreeFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer) {
		List<BlockPos> leaves = Lists.newArrayList();
		// Determine a height
		int treeHeight = minHeight();
		if (random.nextInt(chanceAddFiveFirst()) == 0) {
			treeHeight += random.nextInt(treeHeight / 2);

			if (random.nextInt(chanceAddFiveSecond()) == 0) {
				treeHeight += random.nextInt(treeHeight / 4);
			}
		}

		if (world.isOutsideBuildHeight(pos.getY() + treeHeight)) {
			return false;
		}

		// Check if we're on dirt or grass
		if (world.getBlockState(pos.below()).canSustainPlant(world, pos.below(), Direction.UP, TFBlocks.CANOPY_SAPLING.get().defaultBlockState()).isFalse()) {
			return false;
		}

		leaves.clear();

		// Okay build a tree!  Go up to the height
		buildTrunk(world, leaves, trunkPlacer, random, pos, treeHeight);

		// Make 3 - 4 branches
		int numBranches = 6 + random.nextInt(3);
		float bangle = random.nextFloat();
		int offset = 0;
		for (int b = 0; b < numBranches; b++) {
			float btilt = 0.25F;
			buildBranch(world, leaves, randomlyOffset(pos, random), trunkPlacer, treeHeight - 15 - (b + offset), 15 + random.nextInt(4), bangle, btilt, false, random);

			offset += random.nextInt(2);
			bangle += (random.nextFloat() * 0.4F);
			if (bangle > 1.0F) {
				bangle -= 1.0F;
			}
		}

		// Add the actual leaves
		for (BlockPos leafPos : leaves) {
			makeLeafBlob(world, trunkPlacer, leavesPlacer, random, leafPos);
		}

		CanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos, trunkProvider(), rootsProvider());
		CanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.east(), trunkProvider(), rootsProvider());
		CanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.south(), trunkProvider(), rootsProvider());
		CanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.east().south(), trunkProvider(), rootsProvider());

		return true;
	}

	private void makeLeafBlob(WorldGenLevel world, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leafPlacer, RandomSource rand, BlockPos leafPos) {
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos, branchProvider().value());
		for (Direction direction : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 1), branchProvider().value());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 2), branchProvider().value());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 3), branchProvider().value());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 4), branchProvider().value());

			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 5).relative(direction.getClockWise()), branchProvider().value());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 5).relative(direction.getCounterClockWise()), branchProvider().value());

			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 2).relative(direction.getClockWise(), 1), branchProvider().value());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 3).relative(direction.getClockWise(), 2), branchProvider().value());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, leafPos.relative(direction, 4).relative(direction.getClockWise(), 3), branchProvider().value());
		}
		// Use circles with legacy distance instead of a spheroid to match the 1.4.7 version as much as possible
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos.above(2), 3, leavesProvider().value(), true);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos.above(), 6, leavesProvider().value(), true);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos, 8, leavesProvider().value(), true);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos.below(), 7, leavesProvider().value(), true);
		FeaturePlacers.placeCircleOdd(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos.below(2), 4.5f, leavesProvider().value(), true);
	}

	private void buildTrunk(WorldGenLevel world, List<BlockPos> leaves, BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource rand, BlockPos pos, int treeHeight) {
		for (int dy = 0; dy <= treeHeight; dy++) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 0), trunkProvider().value());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 0), trunkProvider().value());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 1), trunkProvider().value());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 1), trunkProvider().value());
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
	public void buildBranch(WorldGenLevel world, List<BlockPos> leaves, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, int height, double length, double angle, double tilt, boolean trunk, RandomSource treeRNG) {
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
			FeaturePlacers.drawBresenhamTree(world, trunkPlacer, FeaturePlacers.VALID_TREE_POS, src, dest, trunkProvider().value(), treeRNG);
		} else {
			FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, treeRNG, src, dest, branchProvider().value());
			FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, treeRNG, src.below(), dest.below(), branchProvider().value());
		}

		leaves.add(dest);
	}

	private static BlockPos randomlyOffset(BlockPos pos, RandomSource random) {
		return randomlyOffset(pos.getX(), pos.getY(), pos.getZ(), random);
	}

	private static BlockPos randomlyOffset(int x, int y, int z, RandomSource random) {
		return switch (random.nextInt(4)) {
			case 0 -> new BlockPos(x, y, z);
			case 1 -> new BlockPos(x + 1, y, z);
			case 2 -> new BlockPos(x, y, z + 1);
			default -> new BlockPos(x + 1, y, z + 1);
		};
	}
}
