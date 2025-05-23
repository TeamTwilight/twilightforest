package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import twilightforest.enums.NagastoneVariant;
import twilightforest.init.TFBlocks;

public class NagastoneBlock extends Block {

	public static final EnumProperty<NagastoneVariant> VARIANT = EnumProperty.create("variant", NagastoneVariant.class);

	public NagastoneBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(VARIANT, NagastoneVariant.SOLID));
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource random) {
		return this.getVariant(reader, pos);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.getVariant(context.getLevel(), context.getClickedPos());
	}

	@SuppressWarnings("fallthrough")
	private BlockState getVariant(LevelReader accessor, BlockPos pos) {
		int connectionCount = 0;
		BlockState stateOut;
		Direction[] facings = new Direction[2];

		for (Direction side : Direction.values()) {
			BlockState neighborState = accessor.getBlockState(pos.relative(side));
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
		builder.add(VARIANT);
	}
}
