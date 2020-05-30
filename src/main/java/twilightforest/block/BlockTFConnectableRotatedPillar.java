package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import twilightforest.TwilightForestMod;

public abstract class BlockTFConnectableRotatedPillar extends RotatedPillarBlock {

    final double boundingBoxWidthLower;
    final double boundingBoxWidthUpper;

    private final double boundingBoxHeightLower;
    private final double boundingBoxHeightUpper;

    BlockTFConnectableRotatedPillar(Properties props, double size) {
        this(props, size, size);
    }

    BlockTFConnectableRotatedPillar(Properties props, double width, double height) {
        super(props.nonOpaque());

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
                .with(FenceBlock.NORTH, false).with(FenceBlock.WEST, false)
                .with(FenceBlock.SOUTH, false).with(FenceBlock.EAST, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FenceBlock.NORTH, FenceBlock.EAST, FenceBlock.SOUTH, FenceBlock.WEST);
    }

//	@Override
//    public BlockState getActualState(BlockState state, IBlockAccess world, BlockPos pos) {
//        // If our axis is rotated (i.e. not upright), then adjust the actual sides tested
//        // This allows the entire model to be rotated in the assets in a cleaner way
//
//        Direction.Axis axis = state.getValue(AXIS);
//
//        for (PairHelper pair : PairHelper.values()) {
//            Direction connectTo = PairHelper.getFacingFromPropertyWithAxis(pair.property, axis);
//            state = state.with(pair.property, canConnectTo(state, world.getBlockState(pos.offset(connectTo)), world, pos, connectTo));
//        }
//
//        return state;
//    }

//    protected boolean canConnectTo(BlockState state, BlockState otherState, IBlockAccess world, BlockPos pos, Direction connectTo) {
//        return state.getBlock() == otherState.getBlock() && state.get(AXIS) != connectTo.getAxis();
//    }

//    @Override
//    public void addCollisionBoxToList(BlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean useActualState) {
//        if (!useActualState) state = this.getActualState(state, world, pos);
//
//        Direction.Axis axis = state.getValue(AXIS);
//
//        // Add main pillar
//        addCollisionBoxToList(pos, aabb, list, makeQuickAABB(
//                axis == Direction.Axis.X ? 16d : this.boundingBoxWidthLower,
//                axis == Direction.Axis.Y ? 16d : this.boundingBoxWidthLower,
//                axis == Direction.Axis.Z ? 16d : this.boundingBoxWidthLower,
//                axis == Direction.Axis.X ?  0d : this.boundingBoxWidthUpper,
//                axis == Direction.Axis.Y ?  0d : this.boundingBoxWidthUpper,
//                axis == Direction.Axis.Z ?  0d : this.boundingBoxWidthUpper));
//
//        // Add axillary pillar connections
//        // Cycle through all possible faces
//        for (Direction facing : Direction.values()) {
//            // If the facing in loop doesn't cover the axis of the block and if that side's state is true, then add to list
//            if (facing.getAxis() != axis && state.getValue(PairHelper.getPropertyFromFacingWithAxis(facing, axis))) {
//                // Create AABB piece and add to list
//                addCollisionBoxToList(pos, aabb, list, getSidedAABBStraight(facing, axis));
//            }
//        }
//    }

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
		//state = this.getActualState(state, world, pos);

		state = this.getDefaultState(); //Look, this probably isn't going to work

		switch (state.get(AXIS)) {
			case X:
				return VoxelShapes.create(makeQuickAABB(
						0d,
						state.get(FenceBlock.NORTH) ?  0d : this.boundingBoxWidthLower,
						state.get(FenceBlock.WEST ) ?  0d : this.boundingBoxWidthLower,
						16d,
						state.get(FenceBlock.SOUTH) ? 16d : this.boundingBoxWidthUpper,
						state.get(FenceBlock.EAST ) ? 16d : this.boundingBoxWidthUpper
				));
			case Z:
				return VoxelShapes.create(makeQuickAABB(
						state.get(FenceBlock.EAST ) ?  0d : this.boundingBoxWidthLower,
						state.get(FenceBlock.SOUTH) ?  0d : this.boundingBoxWidthLower,
						0d,
						state.get(FenceBlock.WEST ) ? 16d : this.boundingBoxWidthUpper,
						state.get(FenceBlock.NORTH) ? 16d : this.boundingBoxWidthUpper,
						16d
				));
			default:
				return VoxelShapes.create(makeQuickAABB(
						state.get(FenceBlock.WEST)  ?  0d : this.boundingBoxWidthLower,
						0d,
						state.get(FenceBlock.NORTH) ?  0d : this.boundingBoxWidthLower,
						state.get(FenceBlock.EAST)  ? 16d : this.boundingBoxWidthUpper,
						16d,
						state.get(FenceBlock.SOUTH) ? 16d : this.boundingBoxWidthUpper
				));
		}
	}

    protected AxisAlignedBB makeQuickAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AxisAlignedBB(
                x1/16.0d, y1/16.0d,
                z1/16.0d, x2/16.0d,
                y2/16.0d, z2/16.0d);
    }

    enum PairHelper {
        NORTH(Direction.NORTH, FenceBlock.NORTH),
        EAST (Direction.EAST , FenceBlock.EAST ),
        SOUTH(Direction.SOUTH, FenceBlock.SOUTH),
        WEST (Direction.WEST , FenceBlock.WEST );

        final Direction facing;
        final BooleanProperty property;

        PairHelper(Direction facing, BooleanProperty property) {
            this.facing = facing;
            this.property = property;
        }

        static Direction getFacingFromPropertyWithAxis(BooleanProperty property, Direction.Axis axis) {
            switch (axis) {
                case X:
                    if (property == FenceBlock.NORTH) return Direction.DOWN;
                    if (property == FenceBlock.SOUTH) return Direction.UP;
                    if (property == FenceBlock.WEST ) return Direction.NORTH;
                    if (property == FenceBlock.EAST ) return Direction.SOUTH;
                    break;
                case Y:
                    if (property == FenceBlock.NORTH) return Direction.NORTH;
                    if (property == FenceBlock.SOUTH) return Direction.SOUTH;
                    if (property == FenceBlock.WEST ) return Direction.WEST;
                    if (property == FenceBlock.EAST ) return Direction.EAST;
                    break;
                case Z:
                    if (property == FenceBlock.NORTH) return Direction.UP;
                    if (property == FenceBlock.SOUTH) return Direction.DOWN;
                    if (property == FenceBlock.WEST ) return Direction.EAST;
                    if (property == FenceBlock.EAST ) return Direction.WEST;
                    break;
            }

            TwilightForestMod.LOGGER.warn("ConnectableRotatedPillar helper (getFacingFromPropertyWithAxis) had a problem? (property '{}' with axis '{}')", property.getName(), axis.getName());
            return Direction.UP;
        }

        static BooleanProperty getPropertyFromFacingWithAxis(Direction facing, Direction.Axis axis) {
            switch (axis) {
                case X:
                    switch (facing) {
                        case DOWN:
                            return FenceBlock.NORTH;
                        case UP:
                            return FenceBlock.SOUTH;
                        case NORTH:
                            return FenceBlock.WEST;
                        case SOUTH:
                            return FenceBlock.EAST;
                    }
                    break;
                case Y:
                    switch (facing) {
                        case NORTH:
                            return FenceBlock.NORTH;
                        case SOUTH:
                            return FenceBlock.SOUTH;
                        case WEST:
                            return FenceBlock.WEST;
                        case EAST:
                            return FenceBlock.EAST;
                    }
                    break;
                case Z:
                    switch (facing) {
                        case DOWN:
                            return FenceBlock.SOUTH;
                        case UP:
                            return FenceBlock.NORTH;
                        case WEST:
                            return FenceBlock.EAST;
                        case EAST:
                            return FenceBlock.WEST;
                    }
                    break;
            }

            TwilightForestMod.LOGGER.warn("ConnectableRotatedPillar helper (getPropertyFromFacingWithAxis) had a problem? (facing '{}' with axis '{}')", facing.getName(), axis.getName());
            return FenceBlock.NORTH;
        }
    }
}
