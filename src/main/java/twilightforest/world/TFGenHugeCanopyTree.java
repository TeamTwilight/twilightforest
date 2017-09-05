package twilightforest.world;

import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFLeaves;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.LeavesVariant;
import twilightforest.block.enums.WoodVariant;

public class TFGenHugeCanopyTree extends TFTreeGenerator {

	protected int minHeight;
	private int treeHeight;

	public TFGenHugeCanopyTree() {
		this(false);
	}

	public TFGenHugeCanopyTree(boolean par1) {
		super(par1);
		this.minHeight = 35;
/*		super.treeBlock = TFBlocks.log;
		super.treeMeta = 1;
		super.branchMeta = 13;
		super.leafBlock = TFBlocks.leaves;
		super.leafMeta = 1;
		super.rootBlock = TFBlocks.root;
		super.rootMeta = 0;*/
		treeState = TFBlocks.log.getDefaultState().withProperty(BlockTFLog.VARIANT, WoodVariant.CANOPY);
		branchState = treeState.withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		leafState = TFBlocks.leaves.getDefaultState().withProperty(BlockTFLeaves.VARIANT, LeavesVariant.CANOPY).withProperty(BlockLeaves.CHECK_DECAY, false);
		rootState = TFBlocks.root.getDefaultState();
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		IBlockState state = world.getBlockState(pos.down());
		Material var6 = state.getMaterial();

		if ((var6 == Material.GRASS || var6 == Material.GROUND) && pos.getY() < TFWorld.MAXHEIGHT - this.minHeight) {
			this.treeHeight = minHeight;

			if (random.nextInt(3) == 0) {
				this.treeHeight += random.nextInt(10);

				if (random.nextInt(8) == 0) {
					this.treeHeight += random.nextInt(10);
				}
			}

			this.buildTrunk(world, pos, 0, (double) this.treeHeight, 0.0D, 0.0D, true);
			int var7 = 5 + random.nextInt(3);
			double var8 = random.nextDouble();
			int var10;

			for (var10 = 0; var10 < var7; ++var10) {
				this.buildBranch(world, pos, this.treeHeight - 23 + (int) ((double) var10 * 1.5D), 17.0D,
						0.3D * (double) var10 + var8, 0.25D);
			}

			var10 = 4 + random.nextInt(3);
			var8 = random.nextDouble();

			for (int var11 = 0; var11 < var10; ++var11) {
				this.buildRoot(world, pos, var8, var11);
			}

			return true;
		} else {
			return false;
		}
	}

	private void buildBranch(World world, BlockPos pos, int var1, double var2, double var4, double var6 ) {
		BlockPos src = pos.up(var1);
		BlockPos dest = TFGenerator.translate( src, var2, var4, var6 );
		TFGenerator.drawBresehnam(this, world, src, dest, this.branchState);
		TFGenerator.drawBresehnam(this, world, src.down(), dest.down(), this.branchState);

		this.makeLeafNode(world, dest);
	}

	protected void makeLeafNode(World world, BlockPos pos) {
		BlockPos base;
		
		TFGenerator.drawBresehnam(this, world, pos, pos.east(4), this.branchState);
		TFGenerator.drawBresehnam(this, world, pos, pos.west(4), this.branchState);
		TFGenerator.drawBresehnam(this, world, pos, pos.south(4), this.branchState);
		TFGenerator.drawBresehnam(this, world, pos, pos.north(4), this.branchState);
		
		base = pos.east(5);
		setBlockAndNotifyAdequately(world, base.south(1), this.branchState);
		setBlockAndNotifyAdequately(world, base.north(1), this.branchState);
		base = pos.west(5);
		setBlockAndNotifyAdequately(world, base.south(1), this.branchState);
		setBlockAndNotifyAdequately(world, base.north(1), this.branchState);
		base = pos.south(5);
		setBlockAndNotifyAdequately(world, base.east(1), this.branchState);
		setBlockAndNotifyAdequately(world, base.west(1), this.branchState);
		base = pos.north(5);
		setBlockAndNotifyAdequately(world, base.east(1), this.branchState);
		setBlockAndNotifyAdequately(world, base.west(1), this.branchState);

		
/*		this.setBlockAndMetadata(world, var1[0] + 5, var1[1], var1[2] + 1, this.treeBlock, this.branchMeta);
		this.setBlockAndMetadata(world, var1[0] + 5, var1[1], var1[2] - 1, this.treeBlock, this.branchMeta);
		this.setBlockAndMetadata(world, var1[0] - 5, var1[1], var1[2] + 1, this.treeBlock, this.branchMeta);
		this.setBlockAndMetadata(world, var1[0] - 5, var1[1], var1[2] - 1, this.treeBlock, this.branchMeta);
		this.setBlockAndMetadata(world, var1[0] + 1, var1[1], var1[2] + 5, this.treeBlock, this.branchMeta);
		this.setBlockAndMetadata(world, var1[0] - 1, var1[1], var1[2] + 5, this.treeBlock, this.branchMeta);
		this.setBlockAndMetadata(world, var1[0] + 1, var1[1], var1[2] - 5, this.treeBlock, this.branchMeta);
		this.setBlockAndMetadata(world, var1[0] - 1, var1[1], var1[2] - 5, this.treeBlock, this.branchMeta);*/
		
		TFGenerator.drawBresehnam(this, world, pos.east(1), pos.east(4).south(3), this.branchState);
		TFGenerator.drawBresehnam(this, world, pos.west(1), pos.west(4).north(3), this.branchState);
		TFGenerator.drawBresehnam(this, world, pos.south(1), pos.west(3).south(4), this.branchState);
		TFGenerator.drawBresehnam(this, world, pos.north(1), pos.east(3).north(4), this.branchState);
		
/*		this.drawBresehnam(world, var1[0] + 1, var1[1], var1[2] + 0, var1[0] + 4, var1[1], var1[2] + 3, this.treeBlock,
				this.branchMeta);
		this.drawBresehnam(world, var1[0] - 1, var1[1], var1[2] + 0, var1[0] - 4, var1[1], var1[2] - 3, this.treeBlock,
				this.branchMeta);
		this.drawBresehnam(world, var1[0] + 0, var1[1], var1[2] + 1, var1[0] - 3, var1[1], var1[2] + 4, this.treeBlock,
				this.branchMeta);
		this.drawBresehnam(world, var1[0] + 0, var1[1], var1[2] - 1, var1[0] + 3, var1[1], var1[2] - 4, this.treeBlock,
				this.branchMeta); */
		
		TFGenerator.makeLeafCircle(this, world, pos.down(2), 4, this.leafState, true );
		TFGenerator.makeLeafCircle(this, world, pos.down(1), 7, this.leafState, true );
		TFGenerator.makeLeafCircle(this, world, pos, 8, this.leafState, true);
		TFGenerator.makeLeafCircle(this, world, pos.up(1), 6, this.leafState, true);
		TFGenerator.makeLeafCircle(this, world, pos.up(2), 3, this.leafState, true);
		
/*		this.makeLeafCircle(world, var1[0], var1[1] - 2, var1[2], 4, this.leafBlock, this.leafMeta, true);
		this.makeLeafCircle(world, var1[0], var1[1] - 1, var1[2], 7, this.leafBlock, this.leafMeta, true);
		this.makeLeafCircle(world, var1[0], var1[1], var1[2], 8, this.leafBlock, this.leafMeta, true);
		this.makeLeafCircle(world, var1[0], var1[1] + 1, var1[2], 6, this.leafBlock, this.leafMeta, true);
		this.makeLeafCircle(world, var1[0], var1[1] + 2, var1[2], 3, this.leafBlock, this.leafMeta, true); */
	}

	private void buildTrunk(World world, BlockPos pos, int var1, double var2, double var4, double var6,
			boolean var8) {
//		int[] var9 = new int[] { x, y + var1, z };
//		int[] var10 = translate(var9[0], var9[1], var9[2], var2, var4, var6);
		BlockPos src = pos.up(var1);
		BlockPos dest = TFGenerator.translate(src, var2, var4, var6);

		
		for (int var11 = -6; var11 < 0; ++var11) {
			BlockPos base = pos.up(var11);
			this.placeRootBlock(world, base, this.rootState );
			this.placeRootBlock(world, base.east(1), this.rootState );
			this.placeRootBlock(world, base.south(1), this.rootState );
			this.placeRootBlock(world, base.east(1).south(1), this.rootState );
/*			this.placeRootBlock(world, x, y + var11, z, this.rootBlock, this.rootMeta);
			this.placeRootBlock(world, x + 1, y + var11, z, this.rootBlock, this.rootMeta);
			this.placeRootBlock(world, x, y + var11, z + 1, this.rootBlock, this.rootMeta);
			this.placeRootBlock(world, x + 1, y + var11, z + 1, this.rootBlock, this.rootMeta);*/
		}
		
		TFGenerator.drawBresehnam(this, world, src, dest, this.treeState );
		TFGenerator.drawBresehnam(this, world, src.east(1), dest.east(1), this.treeState );
		TFGenerator.drawBresehnam(this, world, src.south(1), dest.south(1), this.treeState );
		TFGenerator.drawBresehnam(this, world, src.east(1).south(1), dest.east(1).south(1), this.treeState );

/*		this.drawBresehnam(world, var9[0], var9[1], var9[2], var10[0], var10[1], var10[2], this.treeBlock,
				this.treeMeta);
		this.drawBresehnam(world, var9[0] + 1, var9[1], var9[2], var10[0] + 1, var10[1], var10[2], this.treeBlock,
				this.treeMeta);
		this.drawBresehnam(world, var9[0] + 1, var9[1], var9[2] + 1, var10[0] + 1, var10[1], var10[2] + 1,
				this.treeBlock, this.treeMeta);
		this.drawBresehnam(world, var9[0], var9[1], var9[2] + 1, var10[0], var10[1], var10[2] + 1, this.treeBlock,
				this.treeMeta);*/
		
		this.makeLeafNode(world, dest);
	}

}
