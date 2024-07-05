package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class DiagonalBlock extends Block {

	public static final BooleanProperty IS_ROTATED = BooleanProperty.create("is_rotated");

	public DiagonalBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(IS_ROTATED);
	}

	@Override
	public BlockState rotate(BlockState state, LevelAccessor accessor, BlockPos pos, Rotation rot) {
		return rot == Rotation.NONE || rot == Rotation.CLOCKWISE_180 ? state : state.setValue(IS_ROTATED, !state.getValue(IS_ROTATED));
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirror) {
		return mirror == Mirror.NONE ? state : state.setValue(IS_ROTATED, !state.getValue(IS_ROTATED));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean rot = context.getHorizontalDirection().getOpposite() == Direction.WEST;
		return this.defaultBlockState().setValue(IS_ROTATED, rot);
	}
}
