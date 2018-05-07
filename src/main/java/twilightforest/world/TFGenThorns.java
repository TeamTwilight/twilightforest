package twilightforest.world;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenThorns extends TFGenerator {

	private static final int MAX_SPREAD = 7;
	private static final int CHANCE_OF_BRANCH = 3;
	private static final int CHANCE_OF_LEAF = 3;
	private static final int CHANCE_LEAF_IS_ROSE = 50;

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {

		// make a 3-5 long stack going up
		int nextLength = 2 + rand.nextInt(4);
		int maxLength = 2 + rand.nextInt(4) + rand.nextInt(4) + rand.nextInt(4);

		placeThorns(world, rand, pos, nextLength, EnumFacing.UP, maxLength, pos);


		return true;
	}

	private void placeThorns(World world, Random rand, BlockPos pos, int length, EnumFacing dir, int maxLength, BlockPos oPos) {
		boolean complete = false;
		for (int i = 0; i < length; i++) {
			BlockPos dPos = pos.offset(dir, i);

			if (Math.abs(dPos.getX() - oPos.getX()) < MAX_SPREAD && Math.abs(dPos.getZ() - oPos.getZ()) < MAX_SPREAD && canPlaceThorns(world, dPos)) {
				this.setBlockAndNotifyAdequately(world, dPos, TFBlocks.thorns.getDefaultState().withProperty(BlockRotatedPillar.AXIS, dir.getAxis()));

				// did we make it to the end?
				if (i == length - 1) {
					complete = true;
					// maybe a leaf?  or a rose?
					if (rand.nextInt(CHANCE_OF_LEAF) == 0 && world.isAirBlock(dPos.offset(dir))) {
						if (rand.nextInt(CHANCE_LEAF_IS_ROSE) > 0) {
							// leaf
							this.setBlockAndNotifyAdequately(world, dPos.offset(dir), TFBlocks.twilight_leaves_3.getDefaultState());
						} else {
							// rose
							this.setBlockAndNotifyAdequately(world, dPos.offset(dir), TFBlocks.thorn_rose.getDefaultState());
						}
					}
				}
			} else {
				break;
			}
		}

		// add another off the end
		if (complete && maxLength > 1) {

			EnumFacing nextDir = EnumFacing.random(rand);

			BlockPos nextPos = pos.offset(dir, length - 1).offset(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos);
		}

		// maybe another branch off the middle
		if (complete && length > 3 && rand.nextInt(CHANCE_OF_BRANCH) == 0) {

			int middle = rand.nextInt(length);

			EnumFacing nextDir = EnumFacing.random(rand);

			BlockPos nextPos = pos.offset(dir, middle).offset(nextDir);
			int nextLength = 1 + rand.nextInt(maxLength);

			this.placeThorns(world, rand, nextPos, nextLength, nextDir, maxLength - 1, oPos);
		}

		// maybe a leaf
		if (complete && length > 3 && rand.nextInt(CHANCE_OF_LEAF) == 0) {

			int middle = rand.nextInt(length);

			EnumFacing nextDir = EnumFacing.random(rand);

			BlockPos nextPos = pos.offset(dir, middle).offset(nextDir);

			if (world.isAirBlock(nextPos)) {
				this.setBlockAndNotifyAdequately(world, nextPos, TFBlocks.twilight_leaves_3.getDefaultState());
			}
		}
	}

	private boolean canPlaceThorns(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getBlock().isAir(state, world, pos)
				|| state.getBlock().isLeaves(state, world, pos);
	}

}
