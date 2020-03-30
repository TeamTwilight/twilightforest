package twilightforest.world.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.IWorldGenerationReader;
import twilightforest.block.TFBlocks;
import twilightforest.world.TFWorld;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class TFGenTreeOfTime<T extends TFTreeFeatureConfig> extends TFGenHollowTree<T> {

	public TFGenTreeOfTime(Function<Dynamic<?>, T> config) {
		super(config);
	}

//	public TFGenTreeOfTime(boolean notify) {
//		super(notify);
//
//		this.treeState = TFBlocks.magic_log.getDefaultState();
//		this.branchState = treeState.with(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
//		this.leafState = TFBlocks.magic_leaves.getDefaultState().with(LeavesBlock.CHECK_DECAY, false);
//	}

	@Override
	public boolean generate(IWorldGenerationReader worldIn, Random random, BlockPos pos, Set<BlockPos> trunk, Set<BlockPos> leaves, Set<BlockPos> branch, Set<BlockPos> root, MutableBoundingBox mbb, T config) {
		World world = (World)worldIn;
		int height = 8;
		int diameter = 1;

		// do we have enough height?
		if (pos.getY() < 1 || pos.getY() + height + diameter > TFWorld.MAXHEIGHT) {
			return false;
		}

		// check if we're on dirt or grass
		BlockState state = world.getBlockState(pos.down());
		if (!state.getBlock().canSustainPlant(state, world, pos.down(), Direction.UP, config.getSapling())) {
			return false;
		}

		// make a tree!

		// build the trunk
		buildTrunk(world, random, pos, trunk, branch, root, diameter, height, mbb, config);

		// build the crown
		buildTinyCrown(world, random, pos, leaves, branch, diameter, height, mbb, config);

		// 3-5 roots at the bottom
		buildBranchRing(world, random, pos, leaves, branch, diameter, 1, 0, 12, 0, 0.75D, 0, 3, 5, 3, false, mbb, config);

		// several more taproots
		buildBranchRing(world, random, pos, leaves, branch, diameter, 1, 2, 18, 0, 0.9D, 0, 3, 5, 3, false, mbb, config);

		// add clock block
		world.setBlockState(pos.add(-1, 2, 0), TFBlocks.time_log_core.get().getDefaultState());

		return true;
	}

	/**
	 * Build the crown of the tree. This builds a smaller crown, since the large
	 * ones were causing some performance issues
	 */
	protected void buildTinyCrown(World world, Random random, BlockPos pos, Set<BlockPos> leaves, Set<BlockPos> branch, int diameter, int height, MutableBoundingBox mbb, T config) {
		int crownRadius = 4;
		int bvar = 1;

		// 3-5 medium branches starting at the bottom of the crown
		buildBranchRing(world, random, pos, leaves, branch, diameter, height - crownRadius, 0, crownRadius, 0, 0.35D, 0, bvar, bvar + 2, 1, true, mbb, config);

		// 3-5 medium branches at the crown middle
		buildBranchRing(world, random, pos, leaves, branch, diameter, height - (crownRadius / 2), 0, crownRadius, 0, 0.28D, 0, bvar, bvar + 2, 1, true, mbb, config);

		// 2-4 medium branches at the crown top
		buildBranchRing(world, random, pos, leaves, branch, diameter, height, 0, crownRadius, 0, 0.15D, 0, 2, 4, 0, true, mbb, config);

		// 3-6 medium branches going straight up
		buildBranchRing(world, random, pos, leaves, branch, diameter, height, 0, (crownRadius / 2), 0, 0.05D, 0, bvar, bvar + 2, 0, true, mbb, config);
	}
}
