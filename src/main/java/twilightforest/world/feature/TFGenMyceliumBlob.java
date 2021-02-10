package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import java.util.Random;

/**
 * This is a copypasta of the sand/gravel/clay generator that produces mycelium blobs for mushroom biomes
 *
 * @author Ben
 */
public class TFGenMyceliumBlob extends Feature<SphereReplaceConfig> {

//	private final BlockState myceliumState;
//	private final int numberOfBlocks;

	public TFGenMyceliumBlob(Codec<SphereReplaceConfig> configIn) {
		super(configIn);
	}

//	public TFGenMyceliumBlob(int i) {
//		this(Blocks.MYCELIUM, i);
//	}
//
//	public TFGenMyceliumBlob(Block block, int i) {
//		myceliumState = block.getDefaultState();
//		numberOfBlocks = i;
//	}


	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, SphereReplaceConfig config) {
//        if (world.getBlock(i, j, k).getMaterial() != Material.WATER)
//        {
//            return false;
//        }
		int range = config.radius.func_242259_a(random) + 2; // TODO Verify that this works correctly! //random.nextInt(config.radius.func_242259_a(random) - 2) + 2;
		int yRange = 1;
		for (int dx = pos.getX() - range; dx <= pos.getX() + range; dx++) {
			for (int dz = pos.getZ() - range; dz <= pos.getZ() + range; dz++) {
				int l1 = dx - pos.getX();
				int i2 = dz - pos.getZ();
				if (l1 * l1 + i2 * i2 > range * range) {
					continue;
				}
				for (int dy = pos.getY() - yRange; dy <= pos.getY() + yRange; dy++) {
					BlockPos dPos = new BlockPos(dx, dy, dz);
					Block blockThere = world.getBlockState(dPos).getBlock();
					if (blockThere == Blocks.DIRT || blockThere == Blocks.GRASS_BLOCK || blockThere == Blocks.STONE) {
						world.setBlockState(dPos, config.state, 16 | 2);
					}
				}
			}
		}

		return true;
	}
}
