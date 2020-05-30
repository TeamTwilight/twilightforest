package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.TFConfig;
import twilightforest.client.particle.TFParticleType;

import java.util.Random;

public class BlockTFMagicLeaves extends LeavesBlock {

	protected BlockTFMagicLeaves(Block.Properties props) {
		super(props);
	}

	@Override
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return TFConfig.COMMON_CONFIG.PERFORMANCE.leavesLightOpacity.get();
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.getBlock() == TFBlocks.transformation_leaves.get()) {
			for (int i = 0; i < 1; ++i) {
				this.sparkleRunes(world, pos, random);
			}
		}
	}

	private void sparkleRunes(World world, BlockPos pos, Random rand) {
		double offset = 0.0625D;

		Direction side = Direction.random(rand);
		double rx = pos.getX() + rand.nextFloat();
		double ry = pos.getY() + rand.nextFloat();
		double rz = pos.getZ() + rand.nextFloat();

		if (side == Direction.DOWN && world.isAirBlock(pos.up())) {
			ry = pos.getY() + 1 + offset;
		}

		if (side == Direction.UP && world.isAirBlock(pos.down())) {
			ry = pos.getY() + 0 - offset;
		}

		if (side == Direction.NORTH && world.isAirBlock(pos.south())) {
			rz = pos.getZ() + 1 + offset;
		}

		if (side == Direction.SOUTH && world.isAirBlock(pos.north())) {
			rz = pos.getZ() + 0 - offset;
		}

		if (side == Direction.WEST && world.isAirBlock(pos.east())) {
			rx = pos.getX() + 1 + offset;
		}

		if (side == Direction.EAST && world.isAirBlock(pos.west())) {
			rx = pos.getX() + 0 - offset;
		}

		if (rx < pos.getX() || rx > pos.getX() + 1 || ry < pos.getY() || ry > pos.getY() + 1 || rz < pos.getZ() || rz > pos.getZ() + 1) {
			world.addParticle(TFParticleType.LEAF_RUNE.get(), rx, ry, rz, 0.0D, 0.0D, 0.0D);
		}
	}
}
