package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import twilightforest.block.BlockTFTrollRoot;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TFGenTrollRoots extends Feature<NoFeatureConfig> {

	public TFGenTrollRoots(Codec<NoFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
		int copyX = pos.getX();
		int copyZ = pos.getZ();

		for (; pos.getY() > 5; pos = pos.down()) {
			if (world.isAirBlock(pos) && BlockTFTrollRoot.canPlaceRootBelow(world, pos.up()) && random.nextInt(6) > 0) {
				if (random.nextInt(10) == 0) {
					world.setBlockState(pos, TFBlocks.unripe_trollber.get().getDefaultState(), 16 | 2);
				} else {
					world.setBlockState(pos, TFBlocks.trollvidr.get().getDefaultState(), 16 | 2);
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
