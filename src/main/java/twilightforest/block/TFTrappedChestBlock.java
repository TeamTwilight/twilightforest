package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.TFTrappedChestBlockEntity;
import twilightforest.init.TFBlockEntities;

import java.util.function.BiPredicate;

public class TFTrappedChestBlock extends TrappedChestBlock {

	public TFTrappedChestBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntityType<? extends ChestBlockEntity> blockEntityType() {
		return TFBlockEntities.TF_TRAPPED_CHEST.get();
	}

	@Override
	public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) {
		BiPredicate<LevelAccessor, BlockPos> blockedChecker = ignoreBeingBlocked ? (_, _) -> false : ChestBlock::isChestBlockedAt;
		return DoubleBlockCombiner.combineWithNeigbour(
			blockEntityType(),
			ChestBlock::getBlockType,
			ChestBlock::getConnectedDirection,
			FACING,
			state,
			level,
			pos,
			blockedChecker
		);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFTrappedChestBlockEntity(pos, state);
	}
}
