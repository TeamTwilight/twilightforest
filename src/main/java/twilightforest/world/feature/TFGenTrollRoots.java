package twilightforest.world.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFTrollRoot;
import twilightforest.block.TFBlocks;

import java.util.Random;


public class TFGenTrollRoots extends TFGenerator {
	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		int copyX = pos.getX();
		int copyZ = pos.getZ();

		for (; pos.getY() > 5; pos = pos.down()) {
			if (world.isAirBlock(pos) && BlockTFTrollRoot.canPlaceRootBelow(world, pos.up()) && random.nextInt(6) > 0) {
				if (random.nextInt(10) == 0) {
					world.setBlockState(pos, TFBlocks.unripe_trollber.getDefaultState());
				} else {
					world.setBlockState(pos, TFBlocks.trollvidr.getDefaultState());
				}
			} else {
				pos = new BlockPos(
						copyX + random.nextInt(4) - random.nextInt(4),
						pos.getY(),
						copyZ + random.nextInt(4) - random.nextInt(4)
				);
			}
		}

		return true;
	}
}
