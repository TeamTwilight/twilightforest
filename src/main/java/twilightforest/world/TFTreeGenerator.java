package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import twilightforest.block.BlockTFHedge;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.HedgeVariant;
import twilightforest.block.enums.WoodVariant;

public abstract class TFTreeGenerator extends WorldGenAbstractTree implements IBlockSettable {

	protected IBlockState treeState = TFBlocks.log.getDefaultState();
	protected IBlockState branchState = TFBlocks.log.getDefaultState().withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE).withProperty(BlockTFLog.VARIANT, WoodVariant.DARK);
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

	/**
	 * Build a root, but don't let it stick out too far into thin air because that's weird
	 */
	protected void buildRoot(World world, BlockPos pos, double offset, int b) {
		BlockPos dest = TFGenerator.translate(pos.down(b + 2), 5, 0.3 * b + offset, 0.8);

		// go through block by block and stop drawing when we head too far into open air
		BlockPos[] lineArray = TFGenerator.getBresehnamArrayCoords(pos.down(), dest);
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
		Block blockID = world.getBlockState(pos).getBlock();

		if (blockID == Blocks.AIR) {
			// roots can grow through air if they are near a solid block
			return TFGenerator.isNearSolid(world, pos);
		} else {
			return blockID != Blocks.BEDROCK && blockID != Blocks.OBSIDIAN && blockID != TFBlocks.shield;
		}
	}
}