package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import twilightforest.block.BlockTFHedge;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.enums.HedgeVariant;
import twilightforest.enums.WoodVariant;

public abstract class TFTreeGenerator extends WorldGenAbstractTree implements IBlockSettable {

	protected IBlockState treeState = TFBlocks.twilight_log.getDefaultState();
	protected IBlockState branchState = TFBlocks.twilight_log.getDefaultState().withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE).withProperty(BlockTFLog.VARIANT, WoodVariant.DARK);
	protected IBlockState leafState = TFBlocks.hedge.getDefaultState().withProperty(BlockTFHedge.VARIANT, HedgeVariant.DARKWOOD_LEAVES);
	protected IBlockState rootState = TFBlocks.root.getDefaultState();


	public TFTreeGenerator() {
		this(false);
	}

	public TFTreeGenerator(boolean par1) {
		super(par1);
	}

	@Override
	public final void setBlockAndNotify(World world, BlockPos pos, IBlockState state) {
		setBlockAndNotifyAdequately(world, pos, state);
	}

	@Override
	protected boolean canGrowInto(Block blockType) {
		return TFGenHollowTree.canGrowInto(blockType);
	}

	/**
	 * Build a root, but don't let it stick out too far into thin air because that's weird
	 */
	protected void buildRoot(World world, BlockPos pos, double offset, int b) {
		BlockPos dest = TFGenerator.translate(pos.down(b + 2), 5, 0.3 * b + offset, 0.8);

		// go through block by block and stop drawing when we head too far into open air
		BlockPos[] lineArray = TFGenerator.getBresehnamArrays(pos.down(), dest);
		for (BlockPos coord : lineArray) {
			this.placeRootBlock(world, coord, rootState);
		}
	}

	/**
	 * Function used to actually place root blocks if they're not going to break anything important
	 */
	protected void placeRootBlock(World world, BlockPos pos, IBlockState state) {
		if (canRootGrowIn(world, pos)) {
			this.setBlockAndNotifyAdequately(world, pos, state);
		}
	}

	public static boolean canRootGrowIn(World world, BlockPos pos) {
		IBlockState blockState = world.getBlockState(pos);
		Block blockID = blockState.getBlock();

		if (blockID.isAir(blockState, world, pos)) {
			// roots can grow through air if they are near a solid block
			return TFGenerator.isNearSolid(world, pos);
		} else {
			return (blockState.getBlockHardness(world, pos) >= 0)
					&& blockID != TFBlocks.stronghold_shield
					&& blockID != TFBlocks.trophy_pedestal
					&& blockID != TFBlocks.bossSpawner
					&& (blockState.getMaterial() == Material.GRASS || blockState.getMaterial() == Material.GROUND || blockState.getMaterial() == Material.ROCK);
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
			setIfEmpty(world, pos.add( 1, height,  0), TFBlocks.firefly.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.EAST));
		} else if (iAngle == 1) {
			setIfEmpty(world, pos.add(-1, height,  0), TFBlocks.firefly.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.WEST));
		} else if (iAngle == 2) {
			setIfEmpty(world, pos.add( 0, height,  1), TFBlocks.firefly.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.SOUTH));
		} else if (iAngle == 3) {
			setIfEmpty(world, pos.add( 0, height, -1), TFBlocks.firefly.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.NORTH));
		}
	}

	private void setIfEmpty(World world, BlockPos pos, IBlockState state) {
		if (world.isAirBlock(pos)) {
			this.setBlockAndNotifyAdequately(world, pos, state);
		}
	}
}