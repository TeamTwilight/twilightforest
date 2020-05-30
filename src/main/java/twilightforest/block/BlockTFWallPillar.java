package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import twilightforest.TwilightForestMod;

public class BlockTFWallPillar extends BlockTFConnectableRotatedPillar {

    protected static final BooleanProperty UP = BooleanProperty.create("up");
    protected static final BooleanProperty DOWN = BooleanProperty.create("down");

    BlockTFWallPillar(Material material, double width, double height) {
        super(Properties.create(material).hardnessAndResistance(1.5F, 10.0F), width, height);

        this.setDefaultState(this.getDefaultState().with(UP, false).with(DOWN, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(UP, DOWN);
    }

//    @Override
//    protected boolean canConnectTo(BlockState state, BlockState otherState, IBlockAccess world, BlockPos pos, Direction connectTo) {
//        return otherState.getBlock() == this && state.get(AXIS) == otherState.get(AXIS);
//    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return /*(!state.getValue(UP)) || (!state.getValue(DOWN)) ? FULL_BLOCK_AABB :*/ super.getShape(state, world, pos, context);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockAccess world, BlockPos pos) {
//        Direction.Axis axis = state.getValue(AXIS);
//        return super.getActualState(state.with(UP, canConnectTo(state, world.getBlockState(pos.offset(getFacingFromPropertyWithAxis(UP, axis))), world, pos, Direction.UP)).with(DOWN, canConnectTo(state, world.getBlockState(pos.offset(getFacingFromPropertyWithAxis(DOWN, axis))), world, pos, Direction.DOWN)), world, pos);
//    }

    private static Direction getFacingFromPropertyWithAxis(BooleanProperty property, Direction.Axis axis) {
        switch (axis) {
            case X:
                if (property == DOWN) return Direction.WEST;
                if (property == UP  ) return Direction.EAST;
                break;
            case Y:
                if (property == DOWN) return Direction.DOWN;
                if (property == UP  ) return Direction.UP;
                break;
            case Z:
                if (property == DOWN) return Direction.SOUTH;
                if (property == UP  ) return Direction.NORTH;
                break;
        }

        TwilightForestMod.LOGGER.warn("BlockTFWallPillar helper (getFacingFromPropertyWithAxis) had a problem? (property '{}' with axis '{}')", property.getName(), axis.getName());
        return BlockTFConnectableRotatedPillar.PairHelper.getFacingFromPropertyWithAxis(property, axis);
    }
}
