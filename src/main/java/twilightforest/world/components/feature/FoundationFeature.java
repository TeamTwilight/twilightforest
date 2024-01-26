package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.loot.TFLootTables;
import twilightforest.util.FeatureLogic;
import twilightforest.util.FeatureUtil;

public class FoundationFeature extends Feature<NoneFeatureConfiguration> {

	public FoundationFeature(Codec<NoneFeatureConfiguration> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource rand = ctx.random();

		int sx = 5 + rand.nextInt(5);
		int sz = 5 + rand.nextInt(5);


		if (!FeatureUtil.isAreaSuitable(world, pos, sx + 1, 4, sz + 1)) {
			return false;
		}

		//okay!
		for (int cx = 0; cx <= sx; cx++) {
			for (int cz = 0; cz <= sz; cz++) {
				if (cx == 0 || cx == sx || cz == 0 || cz == sz) {
					// stone on the edges
					int ht = rand.nextInt(4) + 1;

					for (int cy = 0; cy <= ht; cy++) {
						world.setBlock(pos.offset(cx, cy - 1, cz), FeatureLogic.randStone(rand, cy + 1), 3);
					}
				} else {
					// destroyed wooden plank floor
					if (rand.nextInt(3) != 0) {
						world.setBlock(pos.offset(cx, -1, cz), Blocks.OAK_PLANKS.defaultBlockState(), 3);
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
					world.setBlock(pos.offset(cx, -3, cz), Blocks.AIR.defaultBlockState(), 3);
					world.setBlock(pos.offset(cx, -4, cz), Blocks.AIR.defaultBlockState(), 3);
				}
			}

			// make chest
			int cx = rand.nextInt(sx - 1) + 1;
			int cz = rand.nextInt(sz - 1) + 1;
			TFLootTables.FOUNDATION_BASEMENT.generateChest(world, pos.offset(cx, -4, cz), Direction.NORTH, false);

		}

		return true;
	}
}
