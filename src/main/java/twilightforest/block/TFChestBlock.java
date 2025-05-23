package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.TFChestBlockEntity;
import twilightforest.init.TFBlockEntities;

public class TFChestBlock extends ChestBlock {
	public TFChestBlock(Properties properties) {
		super(TFBlockEntities.TF_CHEST::get, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFChestBlockEntity(pos, state);
	}
}
