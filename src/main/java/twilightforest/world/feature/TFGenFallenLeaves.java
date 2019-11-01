package twilightforest.world.feature;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.enums.PlantVariant;
import twilightforest.world.TFWorld;

import java.util.Random;

public class TFGenFallenLeaves extends WorldGenerator {

	private final IBlockState state = TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.FALLEN_LEAVES);

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		do {
			IBlockState state = worldIn.getBlockState(position.down());
			if (worldIn.isAirBlock(position) && (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND))
				break;
			position = position.down();
		} while (position.getY() > TFWorld.SEALEVEL);

		for (int x = 0; x < 5; x++)
			for (int z = 0; z < 5; z++) {
				if (rand.nextInt(3) != 0)
					continue;
				boolean flag = false;
				int y = 2;
				do {
					IBlockState state = worldIn.getBlockState(position.add(x, y, z).down());
					if (worldIn.isAirBlock(position.add(x, y, z)) && (state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND)) {
						flag = true;
						break;
					}
					y--;
				} while (y >= -2);
				if (!flag)
					continue;
				BlockPos pos = position.add(x, y, z);
				if (((BlockBush) state.getBlock()).canBlockStay(worldIn, pos, state))
					worldIn.setBlockState(pos, state, 16 | 2);
			}

		return true;
	}

}
