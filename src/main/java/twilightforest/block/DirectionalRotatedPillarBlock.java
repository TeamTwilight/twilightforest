package twilightforest.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class DirectionalRotatedPillarBlock extends RotatedPillarBlock {

	public static final BooleanProperty REVERSED = BooleanProperty.create("reversed");

	public DirectionalRotatedPillarBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, Direction.Axis.Y).setValue(REVERSED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(REVERSED));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(REVERSED, context.getClickedFace().getAxisDirection() == Direction.AxisDirection.NEGATIVE);
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirror) {
		if (mirror != Mirror.NONE) {
			Direction.Axis axis = state.getValue(AXIS);
			if (axis == Direction.Axis.Y
				|| mirror == Mirror.LEFT_RIGHT && axis == Direction.Axis.Z
				|| mirror == Mirror.FRONT_BACK && axis == Direction.Axis.X) {

				return state.cycle(REVERSED);
			}
		}
		return super.mirror(state, mirror);
	}
}
