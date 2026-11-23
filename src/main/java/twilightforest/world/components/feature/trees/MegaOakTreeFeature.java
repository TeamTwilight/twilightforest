package twilightforest.world.components.feature.trees;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import twilightforest.init.TFBlocks;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.RootPlacer;
import twilightforest.world.components.feature.config.TreeConfig;

import java.util.List;
import java.util.function.BiConsumer;

public record MegaOakTreeFeature(TreeConfig config) implements CanopyTreeFeature {

	public static final MapCodec<MegaOakTreeFeature> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		TreeConfig.MAP_CODEC.forGetter(MegaOakTreeFeature::config)
	).apply(instance, MegaOakTreeFeature::new));

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean generate(WorldGenLevel world, RandomSource random, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, BiConsumer<BlockPos, BlockState> leavesPlacer, RootPlacer decorationPlacer) {
		List<BlockPos> leaves = Lists.newArrayList();
		// Determine a height
		int treeHeight = config().minHeight();
		if (random.nextInt(config().chanceAddFiveFirst()) == 0) {
			treeHeight += random.nextInt(treeHeight / 2);

			if (random.nextInt(config().chanceAddFiveSecond()) == 0) {
				treeHeight += random.nextInt(5);
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

		// Make 12 - 20 branches
		int numBranches = 12 + random.nextInt(9);
		float bangle = random.nextFloat();
		for (int b = 0; b < numBranches; b++) {
			float btilt = 0.15F + (random.nextFloat() * 0.35F);
			buildBranch(world, leaves, pos, trunkPlacer, treeHeight - 10 + (b / 2), 5, bangle, btilt, false, random);

			bangle += (random.nextFloat() * 0.4F);
			if (bangle > 1.0F) {
				bangle -= 1.0F;
			}
		}

		// Add the actual leaves
		for (BlockPos leafPos : leaves) {
			makeLeafBlob(world, leavesPlacer, random, leafPos);
		}

		CanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos, config());
		CanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.east(), config());
		CanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.south(), config());
		CanopyTreeFeature.makeRoots(world, trunkPlacer, decorationPlacer, random, pos.east().south(), config());

		return true;
	}

	private void makeLeafBlob(WorldGenLevel world, BiConsumer<BlockPos, BlockState> leafPlacer, RandomSource rand, BlockPos leafPos) {
		FeaturePlacers.placeSpheroid(world, leafPlacer, FeaturePlacers.VALID_TREE_POS, rand, leafPos, 2.5f, 2.5f, config().leavesProvider());
	}

	private void buildTrunk(WorldGenLevel world, List<BlockPos> leaves, BiConsumer<BlockPos, BlockState> trunkPlacer, RandomSource rand, BlockPos pos, int treeHeight) {
		for (int dy = 0; dy < treeHeight; dy++) {
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 0), config().trunkProvider());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 0), config().trunkProvider());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(0, dy, 1), config().trunkProvider());
			FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, rand, pos.offset(1, dy, 1), config().trunkProvider());
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

		leaves.add(pos.offset(0, treeHeight, 0));
	}

	/**
	 * Build a branch with a flat blob of leaves at the end.
	 */
	@Override
	public void buildBranch(WorldGenLevel world, List<BlockPos> leaves, BlockPos pos, BiConsumer<BlockPos, BlockState> trunkPlacer, int height, double length, double angle, double tilt, boolean trunk, RandomSource treeRNG) {
		BlockPos src = pos.above(height);
		BlockPos dest = FeatureLogic.translate(src, length, angle, tilt);

		// Constrain branch spread
		int limit = 5;
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
			FeaturePlacers.drawBresenhamTree(world, trunkPlacer, FeaturePlacers.VALID_TREE_POS, src, dest, config().trunkProvider(), treeRNG);
		} else {
			FeaturePlacers.drawBresenhamBranch(world, trunkPlacer, treeRNG, src, dest, config().branchProvider());
		}

		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.east(), config().branchProvider());
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.west(), config().branchProvider());
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.north(), config().branchProvider());
		FeaturePlacers.placeIfValidTreePos(world, trunkPlacer, treeRNG, dest.south(), config().branchProvider());

		leaves.add(dest);
	}
}
