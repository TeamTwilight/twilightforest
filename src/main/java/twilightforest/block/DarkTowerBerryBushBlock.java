package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.tags.TFBlockTags;

public class DarkTowerBerryBushBlock extends BerryBushBlock {

	public DarkTowerBerryBushBlock(ResourceKey<LootTable> berryTable, Properties properties) {
		super(berryTable, properties);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isAreaLoaded(pos, 1)) return;
		if (this.shouldDie(level, pos))
			level.destroyBlock(pos, true);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (this.shouldDie(level, pos))
			ticks.scheduleTick(pos, this, 1);

		return super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
	}

	@Override
	public boolean canBePlacedAt(BlockState state) {
		return state.is(TFBlockTags.DARK_TOWER_BERRY_BUSHES_SURVIVE);
	}

	@Override
	protected boolean canGrowAt(BlockState state, LevelReader level, BlockPos pos) {
		return super.canGrowAt(state, level, pos) && state.getValue(SNOW_LAYERS) == 0;
	}

	protected boolean shouldDie(LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(TFBlockTags.DARK_TOWER_BERRY_BUSHES_DIE);
	}
}
