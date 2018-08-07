package twilightforest.world.feature;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.loot.TFTreasure;

import java.util.Random;


public class TFGenFoundation extends TFGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		int sx = 5 + rand.nextInt(5);
		int sz = 5 + rand.nextInt(5);


		if (!isAreaSuitable(world, rand, pos, sx, 4, sz)) {
			return false;
		}

		//okay!
		for (int cx = 0; cx <= sx; cx++) {
			for (int cz = 0; cz <= sz; cz++) {
				if (cx == 0 || cx == sx || cz == 0 || cz == sz) {
					// stone on the edges
					int ht = rand.nextInt(4) + 1;

					for (int cy = 0; cy <= ht; cy++) {
						setBlockAndNotifyAdequately(world, pos.add(cx, cy - 1, cz), randStone(rand, cy + 1));
					}
				} else {
					// destroyed wooden plank floor
					if (rand.nextInt(3) != 0) {
						setBlockAndNotifyAdequately(world, pos.add(cx, -1, cz), Blocks.PLANKS.getDefaultState());
					}
				}
			}
		}

		//TODO: chimney?

		// 50% basement chance!
		if (rand.nextInt(2) == 0) {
			// clear basement
			for (int cx = 1; cx < sx; cx++) {
				for (int cz = 1; cz < sz; cz++) {
					setBlockAndNotifyAdequately(world, pos.add(cx, -3, cz), Blocks.AIR.getDefaultState());
					setBlockAndNotifyAdequately(world, pos.add(cx, -4, cz), Blocks.AIR.getDefaultState());
				}
			}

			// make chest
			int cx = rand.nextInt(sx - 1) + 1;
			int cz = rand.nextInt(sz - 1) + 1;
			TFTreasure.basement.generateChest(world, pos.add(cx, -4, cz), false);

		}


		return true;
	}
}
