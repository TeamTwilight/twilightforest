package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.TFChestBlockEntity;
import twilightforest.block.entity.TFTrappedChestBlockEntity;
import twilightforest.init.TFBlockEntities;

public class TFTrappedChestBlock extends TrappedChestBlock {

	public TFTrappedChestBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntityType<? extends ChestBlockEntity> blockEntityType() {
		return TFBlockEntities.TF_TRAPPED_CHEST.get();
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFTrappedChestBlockEntity(pos, state);
	}
}
