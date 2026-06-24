package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
import net.minecraft.world.level.redstone.Orientation;
import twilightforest.init.TFBlockEntities;

public class TFTrappedChestBlockEntity extends ChestBlockEntity {

	public TFTrappedChestBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.TF_TRAPPED_CHEST.get(), pos, state);
	}

	@Override
	protected void signalOpenCount(Level level, BlockPos pos, BlockState blockState, int previous, int current) {
		super.signalOpenCount(level, pos, blockState, previous, current);
		if (previous != current) {
			Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(
				level, blockState.getValue(TrappedChestBlock.FACING).getOpposite(), Direction.UP
			);
			Block block = blockState.getBlock();
			level.updateNeighborsAt(pos, block, orientation);
			level.updateNeighborsAt(pos.below(), block, orientation);
		}
	}
}
