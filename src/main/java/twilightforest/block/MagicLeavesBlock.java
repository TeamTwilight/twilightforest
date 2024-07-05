package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFParticleType;

public class MagicLeavesBlock extends TFLeavesBlock {

	public MagicLeavesBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getBlock() == TFBlocks.TRANSFORMATION_LEAVES.get()) {
			for (int i = 0; i < 1; ++i) {
				this.sparkleRunes(level, pos, random);
			}
		}
	}

	private void sparkleRunes(Level level, BlockPos pos, RandomSource rand) {
		double offset = 0.0625D;

		Direction side = Direction.getRandom(rand); //random?
		double rx = pos.getX() + rand.nextFloat();
		double ry = pos.getY() + rand.nextFloat();
		double rz = pos.getZ() + rand.nextFloat();

		if (side == Direction.DOWN && level.isEmptyBlock(pos.above())) {
			ry = pos.getY() + 1 + offset;
		}

		if (side == Direction.UP && level.isEmptyBlock(pos.below())) {
			ry = pos.getY() - offset;
		}

		if (side == Direction.NORTH && level.isEmptyBlock(pos.south())) {
			rz = pos.getZ() + 1 + offset;
		}

		if (side == Direction.SOUTH && level.isEmptyBlock(pos.north())) {
			rz = pos.getZ() - offset;
		}

		if (side == Direction.WEST && level.isEmptyBlock(pos.east())) {
			rx = pos.getX() + 1 + offset;
		}

		if (side == Direction.EAST && level.isEmptyBlock(pos.west())) {
			rx = pos.getX() - offset;
		}

		if (rx < pos.getX() || rx > pos.getX() + 1 || ry < pos.getY() || ry > pos.getY() + 1 || rz < pos.getZ() || rz > pos.getZ() + 1) {
			level.addParticle(TFParticleType.LEAF_RUNE.get(), rx, ry, rz, 0.0D, 0.0D, 0.0D);
		}
	}
}
