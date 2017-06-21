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
import static twilightforest.block.enums.HugeLilypadPiece.NE;
import static twilightforest.block.enums.HugeLilypadPiece.NW;
import static twilightforest.block.enums.HugeLilypadPiece.SE;
import static twilightforest.block.enums.HugeLilypadPiece.SW;


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

			//TODO: AtomicBlom: Ensure lilypads are generating correctly
			//make x/z even?
			dPos = new BlockPos(
					dPos.getX() >> 1 << 1,
					dPos.getY(),
					dPos.getZ() >> 1 << 1
			);

			if (shouldPlacePadAt(world, dPos)) {
				// this seems like a difficult way to generate 2 pseudorandom bits
				rand.setSeed(8890919293L);
				rand.setSeed((dPos.getX() * rand.nextLong()) ^ (dPos.getZ() * rand.nextLong()) ^ 8890919293L);
				int orient = rand.nextInt(4);

				final EnumFacing horizontal = EnumFacing.getHorizontal(orient);
				final IBlockState lilypad = TFBlocks.hugeLilyPad.getDefaultState().withProperty(FACING, horizontal);

				world.setBlockState(pos, lilypad.withProperty(PIECE, NW), 2);
				world.setBlockState(pos.east(), lilypad.withProperty(PIECE, NE), 2);
				world.setBlockState(pos.east().south(), lilypad.withProperty(PIECE, SE), 2);
				world.setBlockState(pos.south(), lilypad.withProperty(PIECE, SW), 2);
			}
		}

		return true;
	}


	private boolean shouldPlacePadAt(World world, BlockPos pos) {
		return world.isAirBlock(pos) && world.getBlockState(pos.down()).getMaterial() == Material.WATER
				&& world.isAirBlock(pos.east()) && world.getBlockState(pos.add(1, -1, 0)).getMaterial() == Material.WATER
				&& world.isAirBlock(pos.south()) && world.getBlockState(pos.add(0, -1, 1)).getMaterial() == Material.WATER
				&& world.isAirBlock(pos.east().south()) && world.getBlockState(pos.add(1, -1, 1)).getMaterial() == Material.WATER;
	}
}
