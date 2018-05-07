package twilightforest.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import twilightforest.block.TFBlocks;

import java.util.Random;

import static twilightforest.block.BlockTFHugeLilyPad.FACING;
import static twilightforest.block.BlockTFHugeLilyPad.PIECE;
import static twilightforest.enums.HugeLilypadPiece.NE;
import static twilightforest.enums.HugeLilypadPiece.NW;
import static twilightforest.enums.HugeLilypadPiece.SE;
import static twilightforest.enums.HugeLilypadPiece.SW;


/**
 * Generate huge lily pads
 *
 * @author Ben
 */
public class TFGenHugeLilyPad extends WorldGenerator {

	private Random rand = new Random();

	@Override
	public boolean generate(World world, Random random, BlockPos pos) {
		for (int i = 0; i < 10; i++) {
			BlockPos dPos = pos.add(
					random.nextInt(8) - random.nextInt(8),
					random.nextInt(4) - random.nextInt(4),
					random.nextInt(8) - random.nextInt(8)
			);

			if (shouldPlacePadAt(world, dPos) && world.isAreaLoaded(dPos, 1)) {
				final EnumFacing horizontal = EnumFacing.getHorizontal(rand.nextInt(4));
				final IBlockState lilypad = TFBlocks.huge_lilypad.getDefaultState().withProperty(FACING, horizontal);

				world.setBlockState(dPos, lilypad.withProperty(PIECE, NW), 2);
				world.setBlockState(dPos.east(), lilypad.withProperty(PIECE, NE), 2);
				world.setBlockState(dPos.east().south(), lilypad.withProperty(PIECE, SE), 2);
				world.setBlockState(dPos.south(), lilypad.withProperty(PIECE, SW), 2);
			}
		}

		return true;
	}


	private boolean shouldPlacePadAt(World world, BlockPos pos) {
		return world.isAirBlock(pos) && world.getBlockState(pos.down()).getMaterial() == Material.WATER
				&& world.isAirBlock(pos.east()) && world.getBlockState(pos.east().down()).getMaterial() == Material.WATER
				&& world.isAirBlock(pos.south()) && world.getBlockState(pos.south().down()).getMaterial() == Material.WATER
				&& world.isAirBlock(pos.east().south()) && world.getBlockState(pos.east().south().down()).getMaterial() == Material.WATER;
	}
}
