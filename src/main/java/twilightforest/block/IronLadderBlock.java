package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class IronLadderBlock extends LadderBlock {
	public static final BooleanProperty LEFT = BooleanProperty.create("left");
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");

	@SuppressWarnings("this-escape")
	public IronLadderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(LEFT, false).setValue(RIGHT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LEFT, RIGHT);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor accessor, BlockPos currentPos, BlockPos facingPos) {
		Direction facing = state.getValue(LadderBlock.FACING);
		BlockState superUpdated = super.updateShape(state, direction, facingState, accessor, currentPos, facingPos);
		if (superUpdated.getBlock() != this) {
			return superUpdated;
		}

		BlockState leftState = accessor.getBlockState(currentPos.relative(facing.getCounterClockWise()));
		BlockState rightState = accessor.getBlockState(currentPos.relative(facing.getClockWise()));

		return superUpdated.setValue(LEFT, leftState.getBlock() instanceof IronLadderBlock && leftState.getValue(LadderBlock.FACING) == facing)
			.setValue(RIGHT, rightState.getBlock() instanceof IronLadderBlock && rightState.getValue(LadderBlock.FACING) == facing);
	}
}
