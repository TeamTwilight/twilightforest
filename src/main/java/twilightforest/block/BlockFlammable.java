package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockFlammable extends Block {
	private final int flammability;
	private final int spreadSpeed;

	public BlockFlammable(int flammability, int spreadSpeed, Block.Properties props) {
		super(props);
		this.flammability = flammability;
		this.spreadSpeed = spreadSpeed;
	}

	@Override public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return flammability;
	}

	@Override public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return spreadSpeed;
	}
}
