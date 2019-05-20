package twilightforest.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallbackCTM;
import twilightforest.client.ModelUtils;

public class BlockTFWallPillar extends BlockTFConnectableRotatedPillar implements ModelRegisterCallbackCTM {

    protected static final PropertyBool UP = PropertyBool.create("up");
    protected static final PropertyBool DOWN = PropertyBool.create("down");

    BlockTFWallPillar(Material material, double width, double height) {
        super(material, width, height);

        this.setDefaultState(this.getDefaultState().withProperty(UP, false).withProperty(DOWN, false));
    }

    @Override
    protected IProperty[] getAdditionalProperties() {
        return new IProperty[]{ UP, DOWN };
    }

    @Override
    protected boolean canConnectTo(IBlockState state, IBlockState otherState, IBlockAccess world, BlockPos pos, EnumFacing connectTo) {
        return otherState.getBlock() == this && state.getValue(AXIS) == otherState.getValue(AXIS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return /*(!state.getValue(UP)) || (!state.getValue(DOWN)) ? FULL_BLOCK_AABB :*/ super.getBoundingBox(state, world, pos);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing.Axis axis = state.getValue(AXIS);
        return super.getActualState(state.withProperty(UP, canConnectTo(state, world.getBlockState(pos.offset(getFacingFromPropertyWithAxis(UP, axis))), world, pos, EnumFacing.UP)).withProperty(DOWN, canConnectTo(state, world.getBlockState(pos.offset(getFacingFromPropertyWithAxis(DOWN, axis))), world, pos, EnumFacing.DOWN)), world, pos);
    }

    private static EnumFacing getFacingFromPropertyWithAxis(PropertyBool property, EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                if (property == DOWN) return EnumFacing.WEST;
                if (property == UP  ) return EnumFacing.EAST;
                break;
            case Y:
                if (property == DOWN) return EnumFacing.DOWN;
                if (property == UP  ) return EnumFacing.UP;
                break;
            case Z:
                if (property == DOWN) return EnumFacing.SOUTH;
                if (property == UP  ) return EnumFacing.NORTH;
                break;
        }

        TwilightForestMod.LOGGER.warn("BlockTFWallPillar helper (getFacingFromPropertyWithAxis) had a problem? (property '{}' with axis '{}')", property.getName(), axis.getName());
        return BlockTFConnectableRotatedPillar.PairHelper.getFacingFromPropertyWithAxis(property, axis);
    }

    @Override
    public void registerModel() {
        ModelUtils.registerToState(this, 0, this.getDefaultState());
    }
}
