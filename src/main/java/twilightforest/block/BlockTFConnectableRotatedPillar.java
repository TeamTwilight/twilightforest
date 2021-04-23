package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public abstract class BlockTFConnectableRotatedPillar extends RotatedPillarBlock {
	private static final BooleanProperty NORTH = SixWayBlock.NORTH;
	private static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
	private static final BooleanProperty WEST = SixWayBlock.WEST;
	private static final BooleanProperty EAST = SixWayBlock.EAST;
	private static final BooleanProperty UP = SixWayBlock.UP;
	private static final BooleanProperty DOWN = SixWayBlock.DOWN;

    final double boundingBoxWidthLower;
    final double boundingBoxWidthUpper;

    private final double boundingBoxHeightLower;
    private final double boundingBoxHeightUpper;

    BlockTFConnectableRotatedPillar(Properties props, double size) {
        this(props, size, size);
    }

    BlockTFConnectableRotatedPillar(Properties props, double width, double height) {
        super(props.notSolid());

        if (width >= 16d) {
            this.boundingBoxWidthLower = 0d;
            this.boundingBoxWidthUpper = 16d;
        } else {
            this.boundingBoxWidthLower = 8d-(width/2d);
            this.boundingBoxWidthUpper = 16d-this.boundingBoxWidthLower;
        }

        if (height >= 16d) {
            this.boundingBoxHeightLower = 0d;
            this.boundingBoxHeightUpper = 16d;
        } else {
            this.boundingBoxHeightLower = 8d-(height/2d);
            this.boundingBoxHeightUpper = 16d-this.boundingBoxHeightLower;
        }

        this.setDefaultState(stateContainer.getBaseState().with(AXIS, Direction.Axis.Y)
                .with(NORTH, false).with(WEST, false)
                .with(SOUTH, false).with(EAST, false)
                .with(DOWN, false).with(UP, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, DOWN, UP);
    }

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
		return facing.getAxis().isHorizontal() ? state.with(SixWayBlock.FACING_TO_PROPERTY_MAP.get(facing), Boolean.valueOf(this.canConnectTo(facingState, facingState.isSolidSide(world, facingPos, facing.getOpposite())))) : super.updatePostPlacement(state, facing, facingState, world, pos, facingPos);
	}

	public boolean canConnectTo(BlockState state, boolean solidSide) {
		Block block = state.getBlock();
		return !cannotAttach(block) && solidSide || block instanceof BlockTFForceField || block instanceof PaneBlock || block.isIn(BlockTags.WALLS);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockReader iblockreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.south();
		BlockPos blockpos3 = blockpos.west();
		BlockPos blockpos4 = blockpos.east();
		BlockState blockstate = iblockreader.getBlockState(blockpos1);
		BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
		BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
		BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
		return this.getDefaultState().with(AXIS, context.getFace().getAxis()).with(NORTH, Boolean.valueOf(this.canConnectTo(blockstate, blockstate.isSolidSide(iblockreader, blockpos1, Direction.SOUTH)))).with(SOUTH, Boolean.valueOf(this.canConnectTo(blockstate1, blockstate1.isSolidSide(iblockreader, blockpos2, Direction.NORTH)))).with(WEST, Boolean.valueOf(this.canConnectTo(blockstate2, blockstate2.isSolidSide(iblockreader, blockpos3, Direction.EAST)))).with(EAST, Boolean.valueOf(this.canConnectTo(blockstate3, blockstate3.isSolidSide(iblockreader, blockpos4, Direction.WEST))));
	}

	// Utility to make a bounding-box piece
    protected AxisAlignedBB getSidedAABBStraight(Direction facing, Direction.Axis axis) {
        return makeQuickAABB(
                facing == Direction.EAST  ? 16d : axis == Direction.Axis.X ? this.boundingBoxHeightLower : this.boundingBoxWidthLower,
                facing == Direction.UP    ? 16d : axis == Direction.Axis.Y ? this.boundingBoxHeightLower : this.boundingBoxWidthLower,
                facing == Direction.SOUTH ? 16d : axis == Direction.Axis.Z ? this.boundingBoxHeightLower : this.boundingBoxWidthLower,
                facing == Direction.WEST  ?  0d : axis == Direction.Axis.X ? this.boundingBoxHeightUpper : this.boundingBoxWidthUpper,
                facing == Direction.DOWN  ?  0d : axis == Direction.Axis.Y ? this.boundingBoxHeightUpper : this.boundingBoxWidthUpper,
                facing == Direction.NORTH ?  0d : axis == Direction.Axis.Z ? this.boundingBoxHeightUpper : this.boundingBoxWidthUpper);
    }

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		switch (state.get(AXIS)) {
			case X:
				return makeCuboidShape(
						0d,
						state.get(NORTH) ?  0d : this.boundingBoxWidthLower,
						state.get(WEST ) ?  0d : this.boundingBoxWidthLower,
						16d,
						state.get(SOUTH) ? 16d : this.boundingBoxWidthUpper,
						state.get(EAST ) ? 16d : this.boundingBoxWidthUpper
				);
			case Z:
				return makeCuboidShape(
						state.get(EAST ) ?  0d : this.boundingBoxWidthLower,
						state.get(SOUTH) ?  0d : this.boundingBoxWidthLower,
						0d,
						state.get(WEST ) ? 16d : this.boundingBoxWidthUpper,
						state.get(NORTH) ? 16d : this.boundingBoxWidthUpper,
						16d
				);
			default:
				return makeCuboidShape(
						state.get(WEST)  ?  0d : this.boundingBoxWidthLower,
						0d,
						state.get(NORTH) ?  0d : this.boundingBoxWidthLower,
						state.get(EAST)  ? 16d : this.boundingBoxWidthUpper,
						16d,
						state.get(SOUTH) ? 16d : this.boundingBoxWidthUpper
				);
		}
	}

    protected AxisAlignedBB makeQuickAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AxisAlignedBB(
                x1/16.0d, y1/16.0d,
                z1/16.0d, x2/16.0d,
                y2/16.0d, z2/16.0d);
    }
}
