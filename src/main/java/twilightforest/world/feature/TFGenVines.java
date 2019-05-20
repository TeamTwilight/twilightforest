package twilightforest.world.feature;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.world.TFWorld;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

/**
 * This class fixes the vanilla WorldGenVines, which appears to be nonfunctional in 1.11
 */
public class TFGenVines extends WorldGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos position) {

		BlockPos original = position;

		for (; position.getY() > TFWorld.SEALEVEL; position = position.down()) {

			if (world.isAirBlock(position)) {

				Set<EnumFacing> facings = EnumSet.noneOf(EnumFacing.class);

				for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL.facings()) {
					if (Blocks.VINE.canPlaceBlockOnSide(world, position, facing.getOpposite())) {
						facings.add(facing);
					}
				}

				if (!facings.isEmpty()) {

					IBlockState vine = Blocks.VINE.getDefaultState();
					for (EnumFacing facing : facings) {
						vine = vine.withProperty(BlockVine.getPropertyFor(facing), true);
					}

					BlockPos down = position;
					do {
						world.setBlockState(down, vine, 16 | 2);
						down = down.down();
					} while (down.getY() >= 0 && world.isAirBlock(down));

					return true;
				}

			} else {
				position = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
				if (isOutsideBounds(7, original, position)) {
					break;
				}
			}
		}

		return false;
	}

	private static boolean isOutsideBounds(int radius, BlockPos original, BlockPos pos) {
		return Math.abs(original.getX() - pos.getX()) > radius || Math.abs(original.getZ() - pos.getZ()) > radius;
	}
}
