package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;

public class TFChestBlockEntity extends ChestBlockEntity {
	public TFChestBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.TF_CHEST.get(), pos, state);
	}
}
