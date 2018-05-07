package twilightforest.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFTrollRoot;
import twilightforest.block.TFBlocks;

import java.util.Random;


public class TFGenTrollRoots extends TFGenerator {
	@Override
	public boolean generate(World par1World, Random par2Random, BlockPos pos) {
		int copyX = pos.getX();
		int copyZ = pos.getZ();

		for (; pos.getY() > 5; pos = pos.down()) {
			if (par1World.isAirBlock(pos) && BlockTFTrollRoot.canPlaceRootBelow(par1World, pos.up()) && par2Random.nextInt(6) > 0) {
				if (par2Random.nextInt(10) == 0) {
					par1World.setBlockState(pos, TFBlocks.unripe_trollber.getDefaultState());
				} else {
					par1World.setBlockState(pos, TFBlocks.trollvidr.getDefaultState());
				}
			} else {
				pos = new BlockPos(
						copyX + par2Random.nextInt(4) - par2Random.nextInt(4),
						pos.getY(),
						copyZ + par2Random.nextInt(4) - par2Random.nextInt(4)
				);
			}
		}

		return true;
	}
}
