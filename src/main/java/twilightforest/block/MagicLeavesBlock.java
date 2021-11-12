package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.client.particle.TFParticleType;

import java.util.Random;

public class MagicLeavesBlock extends LeavesBlock {

	protected MagicLeavesBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		if (state.getBlock() == TFBlocks.TRANSFORMATION_LEAVES.get()) {
			for (int i = 0; i < 1; ++i) {
				this.sparkleRunes(world, pos, random);
			}
		}
	}

	private void sparkleRunes(Level world, BlockPos pos, Random rand) {
		double offset = 0.0625D;

		Direction side = Direction.getRandom(rand); //random?
		double rx = pos.getX() + rand.nextFloat();
		double ry = pos.getY() + rand.nextFloat();
		double rz = pos.getZ() + rand.nextFloat();

		if (side == Direction.DOWN && world.isEmptyBlock(pos.above())) {
			ry = pos.getY() + 1 + offset;
		}

		if (side == Direction.UP && world.isEmptyBlock(pos.below())) {
			ry = pos.getY() - offset;
		}

		if (side == Direction.NORTH && world.isEmptyBlock(pos.south())) {
			rz = pos.getZ() + 1 + offset;
		}

		if (side == Direction.SOUTH && world.isEmptyBlock(pos.north())) {
			rz = pos.getZ() - offset;
		}

		if (side == Direction.WEST && world.isEmptyBlock(pos.east())) {
			rx = pos.getX() + 1 + offset;
		}

		if (side == Direction.EAST && world.isEmptyBlock(pos.west())) {
			rx = pos.getX() - offset;
		}

		if (rx < pos.getX() || rx > pos.getX() + 1 || ry < pos.getY() || ry > pos.getY() + 1 || rz < pos.getZ() || rz > pos.getZ() + 1) {
			world.addParticle(TFParticleType.LEAF_RUNE.get(), rx, ry, rz, 0.0D, 0.0D, 0.0D);
		}
	}
}
