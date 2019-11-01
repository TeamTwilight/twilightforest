package twilightforest.world.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class TFGenLampposts extends TFGenerator {

	private static final Rotation[] ROTATIONS = Rotation.values();
	private final IBlockState lamp;

	public TFGenLampposts(IBlockState state) {
		lamp = state;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		// we should start on a grass block
		if (world.getBlockState(pos.down()).getBlock() != Blocks.GRASS) {
			return false;
		}

		// generate a height
		int height = 1 + rand.nextInt(4);

		// is it air or replaceable above our grass block
		for (int dy = 0; dy <= height; dy++) {
			IBlockState state = world.getBlockState(pos.up(dy));
			if (!state.getBlock().isAir(state, world, pos.up(dy)) && !state.getBlock().isReplaceable(world, pos.up(dy))) {
				return false;
			}
		}

		// generate lamp
		for (int dy = 0; dy < height; dy++) {
			world.setBlockState(pos.up(dy), Blocks.OAK_FENCE.getDefaultState(), 16 | 2);
		}
		world.setBlockState(pos.up(height), lamp.withRotation(ROTATIONS[rand.nextInt(ROTATIONS.length)]), 16 | 2);
		return true;
	}

}
