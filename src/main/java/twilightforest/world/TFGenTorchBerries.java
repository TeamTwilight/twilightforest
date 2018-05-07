package twilightforest.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;

import java.util.Random;


public class TFGenTorchBerries extends TFGenerator {
	@Override
	public boolean generate(World par1World, Random par2Random, BlockPos pos) {
		int copyX = pos.getX();
		int copyZ = pos.getZ();

		for (; pos.getY() > 5; pos = pos.down()) {
			if (par1World.isAirBlock(pos) && BlockTFPlant.canPlaceRootAt(par1World, pos) && par2Random.nextInt(6) > 0) {
				par1World.setBlockState(pos, TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.TORCHBERRY), 2);
			} else {
				pos = new BlockPos(
						copyX + par2Random.nextInt(4) - par2Random.nextInt(4),
						pos.getY(),
						copyZ + par2Random.nextInt(4) - par2Random.nextInt(4)
				);
			}
		}

		return true;
	}
}
