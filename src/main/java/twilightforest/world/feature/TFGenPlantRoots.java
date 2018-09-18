package twilightforest.world.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;

import java.util.Random;


public class TFGenPlantRoots extends TFGenerator {
	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		int copyX = pos.getX();
		int copyZ = pos.getZ();

		for (; pos.getY() > 5; pos = pos.down()) {
			if (world.isAirBlock(pos) && BlockTFPlant.canPlaceRootAt(world, pos) && random.nextInt(6) > 0) {
				world.setBlockState(pos, TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.ROOT_STRAND), 2);
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
