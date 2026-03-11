package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class SpecialStemLeavesBlock extends LeavesBlock {
	protected final Predicate<BlockState> stemPredicate;

	public SpecialStemLeavesBlock(Properties properties, Predicate<BlockState> stemPredicate) {
		super(properties);
		this.stemPredicate = stemPredicate;
	}

	@Override
	public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
		serverLevel.setBlock(pos, updateDistance(state, serverLevel, pos), Block.UPDATE_ALL);
	}

	//Vanilla copy from LeavesBlock class, due to getDistanceAt being a private static method, we need to copy and slightly alter both
	protected BlockState updateDistance(BlockState state, LevelAccessor levelAccessor, BlockPos pos) {
		int i = 7;
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

		for (Direction direction : Direction.values()) {
			mutableBlockPos.setWithOffset(pos, direction);
			i = Math.min(i, getDistanceAt(levelAccessor.getBlockState(mutableBlockPos)) + 1);
			if (i == 1) break;
		}

		return state.setValue(DISTANCE, i);
	}

	protected int getDistanceAt(BlockState state) {
		return this.stemPredicate.test(state) ? 0 : state.getBlock() instanceof LeavesBlock ? state.getValue(DISTANCE) : 7;
	}
}
