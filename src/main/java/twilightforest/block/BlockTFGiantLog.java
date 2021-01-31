package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class BlockTFGiantLog extends Block {

	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

	public BlockTFGiantLog(Properties props) {
		super(props);
		this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(AXIS);
	}
}
