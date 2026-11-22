package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RootStrandBlock extends TFPlantBlock implements BonemealableBlock {

	private static final VoxelShape ROOT_SHAPE = box(2, 0, 2, 14, 16, 14);

	public RootStrandBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return TFPlantBlock.canPlaceRootAt(reader, pos) || reader.getBlockState(pos.above()).is(this);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return ROOT_SHAPE;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, BonemealSource bonemealSource) {
		return this.isBottomOpen(levelReader, blockPos);
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState, BonemealSource bonemealSource) {
		return this.isBottomOpen(level, blockPos);
	}

	private boolean isBottomOpen(BlockGetter getter, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		do {
			mutable.move(Direction.DOWN);
		} while (getter.getBlockState(mutable).is(this));

		return getter.getBlockState(mutable).isAir() || getter.getBlockState(mutable).canBeReplaced();
	}

	@Override
	public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState, BonemealSource bonemealSource) {
		BlockPos.MutableBlockPos mutable = blockPos.mutable();

		do {
			mutable.move(Direction.DOWN);
		} while (serverLevel.getBlockState(mutable).is(this));

		if (serverLevel.getBlockState(mutable).isAir() || serverLevel.getBlockState(mutable).canBeReplaced()) {
			serverLevel.setBlockAndUpdate(mutable, this.defaultBlockState());
		}
	}
}
