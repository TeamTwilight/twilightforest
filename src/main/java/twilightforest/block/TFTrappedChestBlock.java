package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.entity.TFTrappedChestBlockEntity;
import twilightforest.init.TFBlockEntities;

public class TFTrappedChestBlock extends ChestBlock {
	public TFTrappedChestBlock(Properties properties) {
		super(TFBlockEntities.TF_TRAPPED_CHEST::get, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TFTrappedChestBlockEntity(pos, state);
	}

	@Override
	protected Stat<ResourceLocation> getOpenChestStat() {
		return Stats.CUSTOM.get(Stats.TRIGGER_TRAPPED_CHEST);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isSignalSource(BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
		return Mth.clamp(ChestBlockEntity.getOpenCount(getter, pos), 0, 15);
	}

	@Override
	@SuppressWarnings("deprecation")
	public int getDirectSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
		return direction == Direction.UP ? state.getSignal(getter, pos, direction) : 0;
	}
}
