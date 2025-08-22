package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredItem;
import twilightforest.data.tags.BlockTagGenerator;

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
	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (this.shouldDie(level, currentPos))
			level.scheduleTick(currentPos, this, 1);

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public boolean canBePlacedAt(BlockState state) {
		return state.is(BlockTagGenerator.DARK_TOWER_BERRY_BUSHES_SURVIVE);
	}

	protected boolean shouldDie(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(BlockTagGenerator.DARK_TOWER_BERRY_BUSHES_DIE);
	}
}
