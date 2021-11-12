package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import twilightforest.enums.NagastoneVariant;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class NagastoneBlock extends Block {

	public static final EnumProperty<NagastoneVariant> VARIANT = EnumProperty.create("variant", NagastoneVariant.class);

	public NagastoneBlock(Properties props) {
		super(props);
		this.registerDefaultState(stateDefinition.any().setValue(VARIANT, NagastoneVariant.SOLID));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction directionToNeighbor, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return getVariant(world, pos);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return getVariant(ctx.getLevel(), ctx.getClickedPos());
	}

	@SuppressWarnings("fallthrough")
	private BlockState getVariant(LevelAccessor world, BlockPos pos) {
		int connectionCount = 0;
		BlockState stateOut;
		Direction[] facings = new Direction[2];

		for (Direction side : Direction.values()) {
			BlockState neighborState = world.getBlockState(pos.relative(side));
			if (neighborState.getBlock() == this || (neighborState.getBlock() == TFBlocks.NAGASTONE_HEAD.get() && side == neighborState.getValue(TFHorizontalBlock.FACING))) {
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
				stateOut = defaultBlockState().setValue(VARIANT, NagastoneVariant.getVariantFromDoubleFacing(facings[0], facings[1]));
				break;
			default:
				stateOut = this.defaultBlockState();
				break;
		}
		return stateOut;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(VARIANT);
	}
}
