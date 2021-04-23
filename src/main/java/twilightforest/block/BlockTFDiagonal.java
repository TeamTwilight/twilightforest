package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class BlockTFDiagonal extends Block {

    public static final BooleanProperty IS_ROTATED = BooleanProperty.create("is_rotated");

    public BlockTFDiagonal(Properties props) {
        super(props);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(IS_ROTATED);
    }

	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rot) {
		return rot == Rotation.NONE || rot == Rotation.CLOCKWISE_180 ? state : state.with(IS_ROTATED, !state.get(IS_ROTATED));
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return mirrorIn == Mirror.NONE ? state : state.with(IS_ROTATED, !state.get(IS_ROTATED));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		boolean rot = context.getPlacementHorizontalFacing().getOpposite() == Direction.WEST;
		return this.getDefaultState().with(IS_ROTATED, rot);
	}
}
