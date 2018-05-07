package twilightforest.biomes;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.block.TFBlocks;

import java.util.Random;


/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class TFGenHugeWaterLily extends WorldGenerator {

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		for (int i = 0; i < 4; i++) {
			BlockPos pos_ = pos.add(
					random.nextInt(8) - random.nextInt(8),
					random.nextInt(4) - random.nextInt(4),
					random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, pos_)) {
				world.setBlockState(pos_, TFBlocks.huge_waterlily.getDefaultState());
			}
		}

		return true;
	}

	private boolean shouldPlacePadAt(World world, BlockPos pos) {
		return world.isAirBlock(pos) && world.getBlockState(pos.down()).getMaterial() == Material.WATER;
	}
}
