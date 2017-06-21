package twilightforest.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;


public class TFGenStoneCircle extends TFGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		if (!isAreaSuitable(world, rand, pos.add(-3, 0, -3), 6, 4, 6)) {
			return false;
		}

		IBlockState mossyCobble = Blocks.MOSSY_COBBLESTONE.getDefaultState();

		// okay!  circle!
		for (int cy = 0; cy <= 2; cy++) {
			setBlockAndNotifyAdequately(world, pos.add(-3, cy, 0), mossyCobble);
			setBlockAndNotifyAdequately(world, pos.add(3, cy, 0), mossyCobble);
			setBlockAndNotifyAdequately(world, pos.add(0, cy, -3), mossyCobble);
			setBlockAndNotifyAdequately(world, pos.add(0, cy, 3), mossyCobble);
			setBlockAndNotifyAdequately(world, pos.add(-2, cy, -2), mossyCobble);
			setBlockAndNotifyAdequately(world, pos.add(2, cy, -2), mossyCobble);
			setBlockAndNotifyAdequately(world, pos.add(-2, cy, 2), mossyCobble);
			setBlockAndNotifyAdequately(world, pos.add(2, cy, 2), mossyCobble);
		}


		return true;
	}

}
