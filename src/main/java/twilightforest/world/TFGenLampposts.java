package twilightforest.world;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenLampposts extends TFGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		// we should start on a grass block
		if (world.getBlockState(pos.down()) == Blocks.GRASS) {
			// generate a height
			int height = 1 + rand.nextInt(4);
			boolean clear = true;

			// is it air or replaceable above our grass block
			for (int dy = 0; dy < height; dy++) {
				if (!world.isAirBlock(pos.up(dy)) && !world.getBlockState(pos.up(dy)).getBlock().isReplaceable(world, pos.up(dy))) {
					clear = false;
				}
			}

			// generate lamp
			if (clear) {
				for (int dy = 0; dy < height; dy++) {
					world.setBlockState(pos.up(dy), Blocks.OAK_FENCE.getDefaultState());
					world.setBlockState(pos.up(height), TFBlocks.firefly_jar.getDefaultState());
				}
			}
			return clear;
		} else {
			return false;
		}
	}

}
