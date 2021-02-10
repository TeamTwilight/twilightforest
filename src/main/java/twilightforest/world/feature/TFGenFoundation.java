package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.loot.TFTreasure;
import twilightforest.util.FeatureUtil;

import java.util.Random;

public class TFGenFoundation extends Feature<NoFeatureConfig> {

	public TFGenFoundation(Codec<NoFeatureConfig> configIn) {
		super(configIn);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		int sx = 5 + rand.nextInt(5);
		int sz = 5 + rand.nextInt(5);


		if (!FeatureUtil.isAreaSuitable(world, rand, pos, sx, 4, sz)) {
			return false;
		}

		//okay!
		for (int cx = 0; cx <= sx; cx++) {
			for (int cz = 0; cz <= sz; cz++) {
				if (cx == 0 || cx == sx || cz == 0 || cz == sz) {
					// stone on the edges
					int ht = rand.nextInt(4) + 1;

					for (int cy = 0; cy <= ht; cy++) {
						world.setBlockState(pos.add(cx, cy - 1, cz), FeatureUtil.randStone(rand, cy + 1), 3);
					}
				} else {
					// destroyed wooden plank floor
					if (rand.nextInt(3) != 0) {
						world.setBlockState(pos.add(cx, -1, cz), Blocks.OAK_PLANKS.getDefaultState(), 3);
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
					world.setBlockState(pos.add(cx, -3, cz), Blocks.AIR.getDefaultState(), 3);
					world.setBlockState(pos.add(cx, -4, cz), Blocks.AIR.getDefaultState(), 3);
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
