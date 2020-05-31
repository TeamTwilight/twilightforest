package twilightforest.world.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.gen.IWorldGenerationReader;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;
import twilightforest.world.TFWorld;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class TFGenMinersTree<T extends TFTreeFeatureConfig> extends TFTreeGenerator<T> {

	public TFGenMinersTree(Function<Dynamic<?>, T> config) {
		super(config);
	}

	@Override
	protected boolean generate(IWorldGenerationReader worldIn, Random rand, BlockPos pos, Set<BlockPos> trunk, Set<BlockPos> leaves, Set<BlockPos> branch, Set<BlockPos> root, MutableBoundingBox mbb, T config) {
		World world = (World)worldIn;

		if (pos.getY() >= TFWorld.MAXHEIGHT - 12) {
			return false;
		}

		// check soil
		BlockState state = world.getBlockState(pos.down());
		if (!state.getBlock().canSustainPlant(state, world, pos.down(), Direction.UP, config.getSapling())) {
			return false;
		}

		// 9 block high trunk
		for (int dy = 0; dy < 10; dy++) {
			setBranchBlockState(world, rand, pos.up(dy), branch, mbb, config);
		}

		// branches with leaf blocks
		putBranchWithLeaves(world, rand, pos.add(0, 9, 1), leaves, branch, true, mbb, config);
		putBranchWithLeaves(world, rand, pos.add(0, 9, 2), leaves, branch, false, mbb, config);
		putBranchWithLeaves(world, rand, pos.add(0, 8, 3), leaves, branch, false, mbb, config);
		putBranchWithLeaves(world, rand, pos.add(0, 7, 4), leaves, branch, false, mbb, config);
		putBranchWithLeaves(world, rand, pos.add(0, 6, 5), leaves, branch, false, mbb, config);

		putBranchWithLeaves(world, rand, pos.add(0, 9, -1), leaves, branch, true, mbb, config);
		putBranchWithLeaves(world, rand, pos.add(0, 9, -2), leaves, branch, false, mbb, config);
		putBranchWithLeaves(world, rand, pos.add(0, 8, -3), leaves, branch, false, mbb, config);
		putBranchWithLeaves(world, rand, pos.add(0, 7, -4), leaves, branch, false, mbb, config);
		putBranchWithLeaves(world, rand, pos.add(0, 6, -5), leaves, branch, false, mbb, config);

		// place minewood core
		world.setBlockState(pos.up(), TFBlocks.mining_log_core.get().getDefaultState());
		world.getPendingBlockTicks().scheduleTick(pos.up(), TFBlocks.mining_log_core.get(), TFBlocks.mining_log_core.get().tickRate(world));

		// root bulb
		if (FeatureUtil.hasAirAround(world, pos.down())) {
			this.setLogBlockState(world, rand, pos.down(), trunk, mbb, config);
		} else {
			this.setRootsBlockState(world, rand, pos.down(), root, mbb, config);
		}

		// roots!
		/*int numRoots = 3 + rand.nextInt(2);
		double offset = rand.nextDouble();
		for (int b = 0; b < numRoots; b++) {
			buildRoot(world, rand, pos, root, offset, b, mbb, config);
		}*/

		return true;
	}

	protected void putBranchWithLeaves(World world, Random rand, BlockPos pos, Set<BlockPos> leaves, Set<BlockPos> branch, boolean bushy, MutableBoundingBox mbb, T config) {
		setBranchBlockState(world, rand, pos, branch, mbb, config);

		for (int lx = -1; lx <= 1; lx++) {
			for (int ly = -1; ly <= 1; ly++) {
				for (int lz = -1; lz <= 1; lz++) {
					if (!bushy && Math.abs(ly) > 0 && Math.abs(lx) > 0) {
						continue;
					}
					FeatureUtil.putLeafBlock(world, pos.add(lx, ly, lz), config.leavesProvider.getBlockState(rand, pos.add(lx, ly, lz)), leaves);
				}
			}
		}
	}
}
