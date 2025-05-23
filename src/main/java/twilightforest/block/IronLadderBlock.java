package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class IronLadderBlock extends LadderBlock {
	public static final BooleanProperty LEFT = BooleanProperty.create("left");
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");

	public IronLadderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(LEFT, false).setValue(RIGHT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(LEFT, RIGHT));
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource random) {
		Direction facing = state.getValue(LadderBlock.FACING);
		BlockState superUpdated = super.updateShape(state, reader, access, pos, direction, facingPos, facingState, random);
		if (!superUpdated.is(this)) {
			return superUpdated;
		}

		BlockState leftState = reader.getBlockState(pos.relative(facing.getCounterClockWise()));
		BlockState rightState = reader.getBlockState(pos.relative(facing.getClockWise()));

		return superUpdated.setValue(LEFT, leftState.getBlock() instanceof IronLadderBlock && leftState.getValue(LadderBlock.FACING) == facing)
			.setValue(RIGHT, rightState.getBlock() instanceof IronLadderBlock && rightState.getValue(LadderBlock.FACING) == facing);
	}
}
