package twilightforest.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static twilightforest.block.BlockTFConnectableRotatedPillar.PairHelper.getFacingFromPropertyWithAxis;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BlockTFConnectableRotatedPillar extends BlockRotatedPillar {
    protected final int boundingBoxLower;
    protected final int boundingBoxUpper;

    BlockTFConnectableRotatedPillar(Material materialIn, int boundingBoxLower, int boundingBoxUpper) {
        super(materialIn);
        this.boundingBoxLower = boundingBoxLower;
        this.boundingBoxUpper = boundingBoxUpper;

        this.setDefaultState(blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y)
                .withProperty(BlockFence.NORTH, false).withProperty(BlockFence.WEST, false)
                .withProperty(BlockFence.SOUTH, false).withProperty(BlockFence.EAST, false));
    }

    protected abstract IProperty[] getAdditionalProperties();

    private IProperty[] getPropertiesForInit(IProperty... additionalProperties) {
        IProperty[] propertiesWithSiding = new IProperty[additionalProperties.length + 5];
        IProperty[] booleanSides = new IProperty[] { BlockFence.NORTH, BlockFence.EAST, BlockFence.SOUTH, BlockFence.WEST };

        propertiesWithSiding[0] = AXIS;
        System.arraycopy(booleanSides, 0, propertiesWithSiding, 1, booleanSides.length);
        System.arraycopy(additionalProperties, 0, propertiesWithSiding, booleanSides.length + 1, additionalProperties.length);

        return propertiesWithSiding;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getPropertiesForInit(getAdditionalProperties()));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        // If our axis is rotated (i.e. not upright), then adjust the actual sides tested
        // This allows the entire model to be rotated in the assets in a cleaner way

        EnumFacing.Axis axis = state.getValue(AXIS);

        for (PairHelper pair : PairHelper.values()) {
            EnumFacing connectTo = getFacingFromPropertyWithAxis(pair.property, axis);
            state = state.withProperty(pair.property, canConnectTo(state, world.getBlockState(pos.offset(connectTo)), world, pos, connectTo));
        }

        return state;
    }

    protected boolean canConnectTo(IBlockState state, IBlockState otherState, IBlockAccess world, BlockPos pos, EnumFacing connectTo) {
        return state.getBlock() == otherState.getBlock() && state.getValue(AXIS) != connectTo.getAxis();
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean useActualState) {
        if (!useActualState) state = this.getActualState(state, world, pos);

        EnumFacing.Axis axis = state.getValue(AXIS);

        addCollisionBoxToList(pos, aabb, list, makeQuickAABB(
                axis == EnumFacing.Axis.X ? 16 : this.boundingBoxLower,
                axis == EnumFacing.Axis.Y ? 16 : this.boundingBoxLower,
                axis == EnumFacing.Axis.Z ? 16 : this.boundingBoxLower,
                axis == EnumFacing.Axis.X ?  0 : this.boundingBoxUpper,
                axis == EnumFacing.Axis.Y ?  0 : this.boundingBoxUpper,
                axis == EnumFacing.Axis.Z ?  0 : this.boundingBoxUpper));

        for (EnumFacing facing : EnumFacing.VALUES) {
            if (facing.getAxis() != axis && state.getValue(PairHelper.getPropertyFromFacingWithAxis(facing, axis))) {
                addCollisionBoxToList(pos, aabb, list, getSidedAABBStraight(facing, axis));
            }
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = this.getActualState(state, world, pos);

        switch (state.getValue(AXIS)) {
            case X:
                return makeQuickAABB(
                        0,
                        state.getValue(BlockFence.NORTH) ?  0 : this.boundingBoxLower,
                        state.getValue(BlockFence.WEST ) ?  0 : this.boundingBoxLower,
                        16,
                        state.getValue(BlockFence.SOUTH) ? 16 : this.boundingBoxUpper,
                        state.getValue(BlockFence.EAST ) ? 16 : this.boundingBoxUpper);
            case Z:
                return makeQuickAABB(
                        state.getValue(BlockFence.EAST ) ?  0 : this.boundingBoxLower,
                        state.getValue(BlockFence.SOUTH) ?  0 : this.boundingBoxLower,
                        0,
                        state.getValue(BlockFence.WEST ) ? 16 : this.boundingBoxUpper,
                        state.getValue(BlockFence.NORTH) ? 16 : this.boundingBoxUpper,
                        16);
            default:
                return makeQuickAABB(
                        state.getValue(BlockFence.WEST)  ?  0 : this.boundingBoxLower,
                        0,
                        state.getValue(BlockFence.NORTH) ?  0 : this.boundingBoxLower,
                        state.getValue(BlockFence.EAST)  ? 16 : this.boundingBoxUpper,
                        16,
                        state.getValue(BlockFence.SOUTH) ? 16 : this.boundingBoxUpper);
        }
    }

    protected AxisAlignedBB getSidedAABBStraight(EnumFacing facing, EnumFacing.Axis axis) {
        return makeQuickAABB(
                facing == EnumFacing.EAST  ? 16 : this.boundingBoxLower,
                facing == EnumFacing.UP    ? 16 : this.boundingBoxLower,
                facing == EnumFacing.SOUTH ? 16 : this.boundingBoxLower,
                facing == EnumFacing.WEST  ?  0 : this.boundingBoxUpper,
                facing == EnumFacing.DOWN  ?  0 : this.boundingBoxUpper,
                facing == EnumFacing.NORTH ?  0 : this.boundingBoxUpper);
    }

    protected AxisAlignedBB makeQuickAABB(int x1, int y1, int z1, int x2, int y2, int z2) {
        return new AxisAlignedBB(
                x1/16.0d, y1/16.0d,
                z1/16.0d, x2/16.0d,
                y2/16.0d, z2/16.0d);
    }

    enum PairHelper {
        NORTH(EnumFacing.NORTH, BlockFence.NORTH),
        EAST (EnumFacing.EAST , BlockFence.EAST ),
        SOUTH(EnumFacing.SOUTH, BlockFence.SOUTH),
        WEST (EnumFacing.WEST , BlockFence.WEST );

        final EnumFacing facing;
        final PropertyBool property;

        PairHelper(EnumFacing facing, PropertyBool property) {
            this.facing = facing;
            this.property = property;
        }

        static EnumFacing getFacingFromPropertyWithAxis(PropertyBool property, EnumFacing.Axis axis) {
            switch (axis) {
                case X:
                    if (property == BlockFence.NORTH) return EnumFacing.DOWN;
                    if (property == BlockFence.SOUTH) return EnumFacing.UP;
                    if (property == BlockFence.WEST ) return EnumFacing.NORTH;
                    if (property == BlockFence.EAST ) return EnumFacing.SOUTH;
                case Z:
                    if (property == BlockFence.NORTH) return EnumFacing.UP;
                    if (property == BlockFence.SOUTH) return EnumFacing.DOWN;
                    if (property == BlockFence.WEST ) return EnumFacing.EAST;
                    if (property == BlockFence.EAST ) return EnumFacing.WEST;
                default:
                    if (property == BlockFence.NORTH) return EnumFacing.NORTH;
                    if (property == BlockFence.SOUTH) return EnumFacing.SOUTH;
                    if (property == BlockFence.WEST ) return EnumFacing.WEST;
                    if (property == BlockFence.EAST ) return EnumFacing.EAST;
            }

            return EnumFacing.UP;
        }

        static PropertyBool getPropertyFromFacingWithAxis(EnumFacing facing, EnumFacing.Axis axis) {
            switch (axis) {
                case X:
                    if (facing == EnumFacing.DOWN ) return BlockFence.NORTH;
                    if (facing == EnumFacing.UP   ) return BlockFence.SOUTH;
                    if (facing == EnumFacing.NORTH) return BlockFence.WEST ;
                    if (facing == EnumFacing.SOUTH) return BlockFence.EAST ;
                    break;
                case Z:
                    if (facing == EnumFacing.UP   ) return BlockFence.NORTH;
                    if (facing == EnumFacing.DOWN ) return BlockFence.SOUTH;
                    if (facing == EnumFacing.EAST ) return BlockFence.WEST ;
                    if (facing == EnumFacing.WEST ) return BlockFence.EAST ;
                    break;
                case Y:
                    if (facing == EnumFacing.NORTH) return BlockFence.NORTH;
                    if (facing == EnumFacing.SOUTH) return BlockFence.SOUTH;
                    if (facing == EnumFacing.WEST ) return BlockFence.WEST ;
                    if (facing == EnumFacing.EAST ) return BlockFence.EAST ;
                    break;
            }

            TwilightForestMod.LOGGER.info("ConnectableRotatedPillar helper had a problem? " + facing.getName() + " with " + axis.getName());
            return BlockFence.NORTH;
        }
    }
}
