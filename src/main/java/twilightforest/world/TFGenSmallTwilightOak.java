package twilightforest.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFLog;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenSmallTwilightOak extends TFTreeGenerator {
	/**
	 * The minimum height of a generated tree.
	 */
	protected final int minTreeHeight;

	public TFGenSmallTwilightOak(boolean par1) {
		this(par1, 4);
	}

	public TFGenSmallTwilightOak(boolean par1, int par2) {
		super(par1);
		this.minTreeHeight = par2;

		treeState = TFBlocks.twilight_log.getDefaultState();
		branchState = treeState.withProperty(BlockTFLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
		leafState = TFBlocks.twilight_leaves.getDefaultState().withProperty(BlockLeaves.CHECK_DECAY, false);
		rootState = TFBlocks.root.getDefaultState();
	}

	@Override
	public boolean generate(World par1World, Random par2Random, BlockPos pos) {
		int height = par2Random.nextInt(3) + this.minTreeHeight;
		boolean allClear = true;

		if (pos.getY() >= 1 && pos.getY() + height + 1 <= 256) {
			int cy;
			byte width;
			int cz;
			Block blockID;

			for (cy = pos.getY(); cy <= pos.getY() + 1 + height; ++cy) {
				width = 1;

				if (cy == pos.getY()) {
					width = 0;
				}

				if (cy >= pos.getY() + 1 + height - 2) {
					width = 2;
				}

				for (int cx = pos.getX() - width; cx <= pos.getX() + width && allClear; ++cx) {
					for (cz = pos.getZ() - width; cz <= pos.getZ() + width && allClear; ++cz) {
						if (cy >= 0 && cy < 256) {
							BlockPos cPos = new BlockPos(cx, cy, cz);

							IBlockState block = par1World.getBlockState(cPos);
							blockID = block.getBlock();

							if (blockID != Blocks.AIR &&
									!blockID.isLeaves(block, par1World, cPos) &&
									blockID != Blocks.GRASS &&
									blockID != Blocks.DIRT &&
									!blockID.isWood(par1World, cPos)) {
								allClear = false;
							}
						} else {
							allClear = false;
						}
					}
				}
			}

			if (!allClear) {
				return false;
			} else {
				Block blockUsing = par1World.getBlockState(pos.down()).getBlock();

				if ((blockUsing == Blocks.GRASS || blockUsing == Blocks.DIRT) && pos.getY() < 256 - height - 1) {
					setBlockAndNotifyAdequately(par1World, pos.down(), Blocks.DIRT.getDefaultState());
					width = 3;
					byte var18 = 0;
					int treeWidth;
					int tx;
					int var15;

					for (cz = pos.getY() - width + height; cz <= pos.getY() + height; ++cz) {
						int number = cz - (pos.getY() + height);
						treeWidth = var18 + 1 - number / 2;

						for (tx = pos.getX() - treeWidth; tx <= pos.getX() + treeWidth; ++tx) {
							var15 = tx - pos.getX();

							for (int tz = pos.getZ() - treeWidth; tz <= pos.getZ() + treeWidth; ++tz) {
								int var17 = tz - pos.getZ();

								BlockPos tPos = new BlockPos(tx, cz, tz);

								IBlockState state = par1World.getBlockState(tPos);

								if ((Math.abs(var15) != treeWidth || Math.abs(var17) != treeWidth || par2Random.nextInt(2) != 0 && number != 0) &&
										state.getBlock().canBeReplacedByLeaves(state, par1World, tPos)) {
									this.setBlockAndNotifyAdequately(par1World, tPos, leafState);
								}
							}
						}
					}

					for (cz = 0; cz < height; ++cz) {
						BlockPos cPos = pos.up(cz);
						IBlockState block = par1World.getBlockState(cPos);
						blockID = block.getBlock();

						if (blockID == Blocks.AIR || blockID.isLeaves(block, par1World, cPos)) {
							this.setBlockAndNotifyAdequately(par1World, cPos, treeState);
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}
