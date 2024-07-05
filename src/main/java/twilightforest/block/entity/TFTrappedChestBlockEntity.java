package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;

public class TFTrappedChestBlockEntity extends ChestBlockEntity {
	public TFTrappedChestBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.TF_TRAPPED_CHEST.get(), pos, state);
	}

	@Override
	protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int newCount, int oldCount) {
		super.signalOpenCount(level, pos, state, newCount, oldCount);
		if (newCount != oldCount) {
			Block block = state.getBlock();
			level.updateNeighborsAt(pos, block);
			level.updateNeighborsAt(pos.below(), block);
		}
	}
}
