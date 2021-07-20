package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import twilightforest.enums.NagastoneVariant;

public class BlockTFNagastone extends Block {

	public static final EnumProperty<NagastoneVariant> VARIANT = EnumProperty.create("variant", NagastoneVariant.class);

	public BlockTFNagastone(Properties props) {
		super(props);
		this.setDefaultState(stateContainer.getBaseState().with(VARIANT, NagastoneVariant.SOLID));
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction directionToNeighbor, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		return getVariant(world, pos);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return getVariant(ctx.getWorld(), ctx.getPos());
	}

	@SuppressWarnings("fallthrough")
	private BlockState getVariant(IWorld world, BlockPos pos) {
		int connectionCount = 0;
		BlockState stateOut;
		Direction[] facings = new Direction[2];

		for (Direction side : Direction.values()) {
			BlockState neighborState = world.getBlockState(pos.offset(side));
			if (neighborState.getBlock() == this || (neighborState.getBlock() == TFBlocks.naga_stone_head.get() && side == neighborState.get(BlockTFHorizontal.HORIZONTAL_FACING))) {
				facings[connectionCount++] = side;
				if (connectionCount >= 2) {
					break;
				}
			}
		}

		// if there are 2 sides that don't line on same axis, use an elbow part, else use axis part
		// if there is 1 side, then use an axis part
		// if there are 0 or greater than 2 sides, use solid
		// use default if there are more than 3 connections or 0
		switch (connectionCount) {
			case 1:
				facings[1] = facings[0]; // No null, for next statement
			case 2:
				stateOut = getDefaultState().with(VARIANT, NagastoneVariant.getVariantFromDoubleFacing(facings[0], facings[1]));
				break;
			default:
				stateOut = this.getDefaultState();
				break;
		}
		return stateOut;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(VARIANT);
	}
}
