package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IWorldGenerationReader;
import twilightforest.util.FeatureUtil;
import twilightforest.world.TFGenerationSettings;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.Random;
import java.util.Set;

public class TFGenLargeWinter extends TFTreeGenerator<TFTreeFeatureConfig> {

	public TFGenLargeWinter(Codec<TFTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	protected boolean generate(IWorld world, Random random, BlockPos pos, Set<BlockPos> trunk, Set<BlockPos> leaves, Set<BlockPos> branch, Set<BlockPos> root, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		// determine a height
		int treeHeight = 35;
		if (random.nextInt(3) == 0) {
			treeHeight += random.nextInt(10);

			if (random.nextInt(8) == 0) {
				treeHeight += random.nextInt(10);
			}
		}

		if (pos.getY() >= TFGenerationSettings.MAXHEIGHT - treeHeight) {
			return false;
		}

		// check if we're on dirt or grass
		BlockState state = world.getBlockState(pos.down());
		if (!state.getBlock().canSustainPlant(state, world, pos.down(), Direction.UP, config.getSapling(random, pos))) {
			return false;
		}

		//okay build a tree!  Go up to the height
		buildTrunk(world, random, pos, trunk, treeHeight, mbb, config);

		// make leaves
		makeLeaves(world, random, pos, treeHeight, trunk, leaves, mbb, config);

		// roots!
		int numRoots = 4 + random.nextInt(3);
		float offset = random.nextFloat();
		for (int b = 0; b < numRoots; b++) {
			buildRoot(world, random, pos, root, offset, b, mbb, config);
		}

		return true;
	}

	private void makeLeaves(IWorld world, Random random, BlockPos pos, int treeHeight, Set<BlockPos> trunk, Set<BlockPos> leaves, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		int offGround = 3;
		int leafType = 1;

		for (int dy = 0; dy < treeHeight; dy++) {

			int radius = leafRadius(treeHeight, dy, leafType);

			FeatureUtil.makeLeafCircle2(world, pos.up(offGround + treeHeight - dy), radius, config.leavesProvider.getBlockState(random, pos.up(offGround + treeHeight - dy)), leaves, false);
			this.makePineBranches(world, random, pos.up(offGround + treeHeight - dy), trunk, radius, mbb, config);
		}
	}

	private void makePineBranches(IWorld world, Random rand, BlockPos pos, Set<BlockPos> trunk, int radius, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		int branchLength = radius > 4 ? radius - 1 : radius - 2;

		switch (pos.getY() % 2) {
			case 0:
				// branches
				for (int i = 1; i <= branchLength; i++) {
					this.placeLogAt(world, rand, pos.add(-i, 0, 0), Direction.Axis.X, trunk, mbb, config);
					this.placeLogAt(world, rand, pos.add(0, 0, i + 1), Direction.Axis.Z, trunk, mbb, config);
					this.placeLogAt(world, rand, pos.add(i + 1, 0, 1), Direction.Axis.X, trunk, mbb, config);
					this.placeLogAt(world, rand, pos.add(1, 0, -i), Direction.Axis.Z, trunk, mbb, config);
				}
				break;
			case 1:
				for (int i = 1; i <= branchLength; i++) {
					this.placeLogAt(world, rand, pos.add(-1, 0, 1), Direction.Axis.X, trunk, mbb, config);
					this.placeLogAt(world, rand, pos.add(1, 0, i + 1), Direction.Axis.Z, trunk, mbb, config);
					this.placeLogAt(world, rand, pos.add(i + 1, 0, 0), Direction.Axis.X, trunk, mbb, config);
					this.placeLogAt(world, rand, pos.add(0, 0, -i), Direction.Axis.Z, trunk, mbb, config);
				}
				break;
		}
	}

	private void placeLogAt(IWorldGenerationReader reader, Random rand, BlockPos pos, Direction.Axis axis, Set<BlockPos> logPos, MutableBoundingBox boundingBox, TFTreeFeatureConfig config) {
		this.setBlockState(reader, pos, config.trunkProvider.getBlockState(rand, pos).with(RotatedPillarBlock.AXIS, axis), boundingBox);
		logPos.add(pos.toImmutable());
	}

	private int leafRadius(int treeHeight, int dy, int functionType) {
		switch (functionType) {
			case 0:
			default:
				return (dy - 1) % 4;
			case 1:
				return (int) (4F * dy / treeHeight + (0.75F * dy % 3));
			case 99:
				return (treeHeight - (dy / 2) - 1) % 4; // bad
		}
	}

	private void buildTrunk(IWorld world, Random rand, BlockPos pos, Set<BlockPos> trunk, int treeHeight, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		for (int dy = 0; dy < treeHeight; dy++) {
			this.setLogBlockState(world, rand, pos.add(0, dy, 0), trunk, mbb, config);
			this.setLogBlockState(world, rand, pos.add(1, dy, 0), trunk, mbb, config);
			this.setLogBlockState(world, rand, pos.add(0, dy, 1), trunk, mbb, config);
			this.setLogBlockState(world, rand, pos.add(1, dy, 1), trunk, mbb, config);
		}
	}
}
