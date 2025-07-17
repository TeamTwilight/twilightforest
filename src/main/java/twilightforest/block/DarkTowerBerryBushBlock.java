package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredItem;
import twilightforest.data.tags.BlockTagGenerator;

public class DarkTowerBerryBushBlock extends BerryBushBlock {
	public DarkTowerBerryBushBlock(DeferredItem<Item> harvestItem) {
		super(harvestItem, BlockTagGenerator.DARK_TOWER_BERRY_BUSHES_SURVIVE);
	}

	// [VanillaCopy] CactusBlock with shouldDie instead of canSurvive
	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isAreaLoaded(pos, 1)) return;
		if (shouldDie(state, level, pos))
			level.destroyBlock(pos, true);
	}

	// [VanillaCopy] CactusBlock with shouldDie instead of canSurvive
	@Override
	protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (shouldDie(state, level, currentPos))
			level.scheduleTick(currentPos, this, 1);

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
	}

	protected boolean shouldDie(BlockState state, LevelAccessor level, BlockPos pos) {
		return state.getBlock() instanceof DarkTowerBerryBushBlock darkTowerBerryBushBlock && darkTowerBerryBushBlock.shouldDie(level, pos);
	}

	protected boolean shouldDie(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(BlockTagGenerator.DARK_TOWER_BERRY_BUSHES_DIE);
	}
}
