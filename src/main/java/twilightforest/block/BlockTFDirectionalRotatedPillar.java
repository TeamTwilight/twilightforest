package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;

public abstract class BlockTFDirectionalRotatedPillar extends RotatedPillarBlock {

	public static final BooleanProperty REVERSED = BooleanProperty.create("reversed");

	public BlockTFDirectionalRotatedPillar(Properties props) {
		super(props);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(REVERSED);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return super.getStateForPlacement(context).with(REVERSED, context.getFace().getAxisDirection() == Direction.AxisDirection.NEGATIVE);
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirror) {
		if (mirror != Mirror.NONE) {
			Direction.Axis axis = state.get(AXIS);
			if (axis == Direction.Axis.Y
					|| mirror == Mirror.LEFT_RIGHT && axis == Direction.Axis.Z
					|| mirror == Mirror.FRONT_BACK && axis == Direction.Axis.X) {

				return state.cycle(REVERSED);
			}
		}
		return super.mirror(state, mirror);
	}
}
