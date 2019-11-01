package twilightforest.world.feature;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.world.TFWorld;

import java.util.Random;

public class TFGenWebs extends TFGenerator {

	private static boolean isValidMaterial(Material material) {
		return material == Material.LEAVES || material == Material.WOOD;
	}

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		while (pos.getY() > TFWorld.SEALEVEL && world.isAirBlock(pos))
			pos = pos.down();

		if (!isValidMaterial(world.getBlockState(pos).getMaterial()))
			return false;

		do {
			if (world.isAirBlock(pos.down())) {
				world.setBlockState(pos.down(), Blocks.WEB.getDefaultState(), 16 | 2);
				return true;
			}
			pos = pos.down();
		} while (pos.getY() > TFWorld.SEALEVEL && isValidMaterial(world.getBlockState(pos).getMaterial()));

		return false;
	}
}
