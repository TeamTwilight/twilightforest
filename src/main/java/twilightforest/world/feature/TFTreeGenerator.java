package twilightforest.world.feature;

import com.google.common.collect.Sets;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import twilightforest.block.TFBlocks;
import twilightforest.util.FeatureUtil;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public abstract class TFTreeGenerator<T extends TFTreeFeatureConfig> extends AbstractTreeFeature<T> {

//	protected BlockState treeState = TFBlocks.twilight_log.getDefaultState();
//	protected BlockState branchState = TFBlocks.twilight_log.getDefaultState().with(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE).with(BlockTFLog.VARIANT, WoodVariant.DARK);
//	protected BlockState leafState = TFBlocks.hedge.getDefaultState().with(BlockTFHedge.VARIANT, HedgeVariant.DARKWOOD_LEAVES);
//	protected BlockState rootState = TFBlocks.root.getDefaultState();
//
//	protected IPlantable source = TFBlocks.twilight_sapling;

//	public TFTreeGenerator() {
//		this(false);
//	}
//
//	public TFTreeGenerator(boolean notify) {
//		super(notify);
//	}

	public TFTreeGenerator(Function<Dynamic<?>, T> configIn) {
		super(configIn);
	}

//	@Override
//	public final void setBlockAndNotify(World world, BlockPos pos, BlockState state) {
//		setBlockAndNotifyAdequately(world, pos, state);
//	}

	//TODO: Figure out how to get this working
//	@Override
//	protected boolean canGrowInto(Block blockType) {
//		return TFGenHollowTree.canGrowInto(blockType);
//	}

	//TODO: Check the logic here
	@Override
	protected boolean generate(IWorldGenerationReader world, Random random, BlockPos pos, Set<BlockPos> logpos, Set<BlockPos> leavespos, MutableBoundingBox mbb, T config) {
		Set<BlockPos> branchSet = Sets.newHashSet();
		Set<BlockPos> rootSet = Sets.newHashSet();
		return generate(world, random, pos, logpos, leavespos, branchSet, rootSet, mbb, config);
	}

	/**
	 * This works akin to the AbstractTreeFeature.generate, but put our branches and roots here
	 */
	protected abstract boolean generate(IWorldGenerationReader world, Random random, BlockPos pos, Set<BlockPos> logpos, Set<BlockPos> leavespos, Set<BlockPos> branchpos, Set<BlockPos> rootpos, MutableBoundingBox mbb, T config);

	public boolean setBranchBlockState(IWorldGenerationReader world, Random random, BlockPos pos, Set<BlockPos> branchpos, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		if (!isAirOrLeaves(world, pos) && !isTallPlants(world, pos) && !isWater(world, pos)) {
			return false;
		} else {
			this.setBlockState(world, pos, config.branchProvider.getBlockState(random, pos), mbb);
			branchpos.add(pos.toImmutable());
			return true;
		}
	}

	protected boolean setRootsBlockState(IWorldGenerationReader world, Random random, BlockPos pos, Set<BlockPos> branchpos, MutableBoundingBox mbb, TFTreeFeatureConfig config) {
		if (canRootGrowIn((World)world, pos)) { //this feels dirty. Is it even going to work?
			this.setBlockState(world, pos, config.rootsProvider.getBlockState(random, pos), mbb);
			branchpos.add(pos.toImmutable());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Build a root, but don't let it stick out too far into thin air because that's weird
	 */
	protected void buildRoot(World world, Random rand, BlockPos pos, Set<BlockPos> setpos, double offset, int b, MutableBoundingBox mbb, T config) {
		BlockPos dest = FeatureUtil.translate(pos.down(b + 2), 5, 0.3 * b + offset, 0.8);

		// go through block by block and stop drawing when we head too far into open air
		BlockPos[] lineArray = FeatureUtil.getBresehnamArrays(pos.down(), dest);
		for (BlockPos coord : lineArray) {
			this.setRootsBlockState(world, rand, coord, setpos, mbb, config);
		}
	}

	/**
	 * Function used to actually place root blocks if they're not going to break anything important
	 * TODO: Deprecated. Use setRootsBlockState
	 */
	@Deprecated
	protected void placeRootBlock(World world, BlockPos pos, BlockState state) {
		if (canRootGrowIn(world, pos)) {
			world.setBlockState(pos, state);
		}
	}

	public static boolean canRootGrowIn(World world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		Block blockID = blockState.getBlock();

		if (blockID.isAir(blockState, world, pos)) {
			// roots can grow through air if they are near a solid block
			return FeatureUtil.isNearSolid(world, pos);
		} else {
			return (blockState.getBlockHardness(world, pos) >= 0)
					&& blockID != TFBlocks.stronghold_shield.get()
					&& blockID != TFBlocks.trophy_pedestal.get()
					&& blockID != TFBlocks.boss_spawner.get()
					&& (blockState.getMaterial() == Material.ORGANIC || blockState.getMaterial() == Material.EARTH || blockState.getMaterial() == Material.ROCK);
		}
	}

	/**
	 * Add a firefly at the specified height and angle.
	 *
	 * @param height how far up the tree
	 * @param angle  from 0 - 1 rotation around the tree
	 */
	protected void addFirefly(World world, BlockPos pos, int height, double angle) {
		int iAngle = (int) (angle * 4.0);
		if (iAngle == 0) {
			setIfEmpty(world, pos.add( 1, height,  0), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.EAST));
		} else if (iAngle == 1) {
			setIfEmpty(world, pos.add(-1, height,  0), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.WEST));
		} else if (iAngle == 2) {
			setIfEmpty(world, pos.add( 0, height,  1), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.SOUTH));
		} else if (iAngle == 3) {
			setIfEmpty(world, pos.add( 0, height, -1), TFBlocks.firefly.get().getDefaultState().with(DirectionalBlock.FACING, Direction.NORTH));
		}
	}

	private void setIfEmpty(World world, BlockPos pos, BlockState state) {
		if (world.isAirBlock(pos)) {
			world.setBlockState(pos, state);
		}
	}
}