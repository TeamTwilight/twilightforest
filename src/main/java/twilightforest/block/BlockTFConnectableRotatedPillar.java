package twilightforest.block;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
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
import java.util.List;

public abstract class BlockTFConnectableRotatedPillar extends BlockRotatedPillar {

    final double boundingBoxWidthLower;
    final double boundingBoxWidthUpper;

    private final double boundingBoxHeightLower;
    private final double boundingBoxHeightUpper;

    BlockTFConnectableRotatedPillar(Material material, double size) {
        this(material, material.getMaterialMapColor(), size, size);
    }

    BlockTFConnectableRotatedPillar(Material material, double width, double height) {
        this(material, material.getMaterialMapColor(), width, height);
    }

    BlockTFConnectableRotatedPillar(Material material, MapColor mapColor, double size) {
        this(material, mapColor, size, size);
    }

    BlockTFConnectableRotatedPillar(Material material, MapColor mapColor, double width, double height) {
        super(material, mapColor);

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

        this.setDefaultState(blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y)
                .withProperty(BlockFence.NORTH, false).withProperty(BlockFence.WEST, false)
                .withProperty(BlockFence.SOUTH, false).withProperty(BlockFence.EAST, false));
    }

    protected abstract IProperty[] getAdditionalProperties();

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this)
                .add(AXIS, BlockFence.NORTH, BlockFence.EAST, BlockFence.SOUTH, BlockFence.WEST)
                .add(getAdditionalProperties())
                .build();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        // If our axis is rotated (i.e. not upright), then adjust the actual sides tested
        // This allows the entire model to be rotated in the assets in a cleaner way

        EnumFacing.Axis axis = state.getValue(AXIS);

        for (PairHelper pair : PairHelper.values()) {
            EnumFacing connectTo = PairHelper.getFacingFromPropertyWithAxis(pair.property, axis);
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

        // Add main pillar
        addCollisionBoxToList(pos, aabb, list, makeQuickAABB(
                axis == EnumFacing.Axis.X ? 16d : this.boundingBoxWidthLower,
                axis == EnumFacing.Axis.Y ? 16d : this.boundingBoxWidthLower,
                axis == EnumFacing.Axis.Z ? 16d : this.boundingBoxWidthLower,
                axis == EnumFacing.Axis.X ?  0d : this.boundingBoxWidthUpper,
                axis == EnumFacing.Axis.Y ?  0d : this.boundingBoxWidthUpper,
                axis == EnumFacing.Axis.Z ?  0d : this.boundingBoxWidthUpper));

        // Add axillary pillar connections
        // Cycle through all possible faces
        for (EnumFacing facing : EnumFacing.VALUES) {
            // If the facing in loop doesn't cover the axis of the block and if that side's state is true, then add to list
            if (facing.getAxis() != axis && state.getValue(PairHelper.getPropertyFromFacingWithAxis(facing, axis))) {
                // Create AABB piece and add to list
                addCollisionBoxToList(pos, aabb, list, getSidedAABBStraight(facing, axis));
            }
        }
    }

    // Utility to make a bounding-box piece
    protected AxisAlignedBB getSidedAABBStraight(EnumFacing facing, EnumFacing.Axis axis) {
        return makeQuickAABB(
                facing == EnumFacing.EAST  ? 16d : axis == EnumFacing.Axis.X ? this.boundingBoxHeightLower : this.boundingBoxWidthLower,
                facing == EnumFacing.UP    ? 16d : axis == EnumFacing.Axis.Y ? this.boundingBoxHeightLower : this.boundingBoxWidthLower,
                facing == EnumFacing.SOUTH ? 16d : axis == EnumFacing.Axis.Z ? this.boundingBoxHeightLower : this.boundingBoxWidthLower,
                facing == EnumFacing.WEST  ?  0d : axis == EnumFacing.Axis.X ? this.boundingBoxHeightUpper : this.boundingBoxWidthUpper,
                facing == EnumFacing.DOWN  ?  0d : axis == EnumFacing.Axis.Y ? this.boundingBoxHeightUpper : this.boundingBoxWidthUpper,
                facing == EnumFacing.NORTH ?  0d : axis == EnumFacing.Axis.Z ? this.boundingBoxHeightUpper : this.boundingBoxWidthUpper);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = this.getActualState(state, world, pos);

        switch (state.getValue(AXIS)) {
            case X:
                return makeQuickAABB(
                        0d,
                        state.getValue(BlockFence.NORTH) ?  0d : this.boundingBoxWidthLower,
                        state.getValue(BlockFence.WEST ) ?  0d : this.boundingBoxWidthLower,
                        16d,
                        state.getValue(BlockFence.SOUTH) ? 16d : this.boundingBoxWidthUpper,
                        state.getValue(BlockFence.EAST ) ? 16d : this.boundingBoxWidthUpper
                );
            case Z:
                return makeQuickAABB(
                        state.getValue(BlockFence.EAST ) ?  0d : this.boundingBoxWidthLower,
                        state.getValue(BlockFence.SOUTH) ?  0d : this.boundingBoxWidthLower,
                        0d,
                        state.getValue(BlockFence.WEST ) ? 16d : this.boundingBoxWidthUpper,
                        state.getValue(BlockFence.NORTH) ? 16d : this.boundingBoxWidthUpper,
                        16d
                );
            default:
                return makeQuickAABB(
                        state.getValue(BlockFence.WEST)  ?  0d : this.boundingBoxWidthLower,
                        0d,
                        state.getValue(BlockFence.NORTH) ?  0d : this.boundingBoxWidthLower,
                        state.getValue(BlockFence.EAST)  ? 16d : this.boundingBoxWidthUpper,
                        16d,
                        state.getValue(BlockFence.SOUTH) ? 16d : this.boundingBoxWidthUpper
                );
        }
    }

    protected AxisAlignedBB makeQuickAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AxisAlignedBB(
                x1/16.0d, y1/16.0d,
                z1/16.0d, x2/16.0d,
                y2/16.0d, z2/16.0d);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
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
                    break;
                case Y:
                    if (property == BlockFence.NORTH) return EnumFacing.NORTH;
                    if (property == BlockFence.SOUTH) return EnumFacing.SOUTH;
                    if (property == BlockFence.WEST ) return EnumFacing.WEST;
                    if (property == BlockFence.EAST ) return EnumFacing.EAST;
                    break;
                case Z:
                    if (property == BlockFence.NORTH) return EnumFacing.UP;
                    if (property == BlockFence.SOUTH) return EnumFacing.DOWN;
                    if (property == BlockFence.WEST ) return EnumFacing.EAST;
                    if (property == BlockFence.EAST ) return EnumFacing.WEST;
                    break;
            }

            TwilightForestMod.LOGGER.warn("ConnectableRotatedPillar helper (getFacingFromPropertyWithAxis) had a problem? (property '{}' with axis '{}')", property.getName(), axis.getName());
            return EnumFacing.UP;
        }

        static PropertyBool getPropertyFromFacingWithAxis(EnumFacing facing, EnumFacing.Axis axis) {
            switch (axis) {
                case X:
                    switch (facing) {
                        case DOWN:
                            return BlockFence.NORTH;
                        case UP:
                            return BlockFence.SOUTH;
                        case NORTH:
                            return BlockFence.WEST;
                        case SOUTH:
                            return BlockFence.EAST;
                    }
                    break;
                case Y:
                    switch (facing) {
                        case NORTH:
                            return BlockFence.NORTH;
                        case SOUTH:
                            return BlockFence.SOUTH;
                        case WEST:
                            return BlockFence.WEST;
                        case EAST:
                            return BlockFence.EAST;
                    }
                    break;
                case Z:
                    switch (facing) {
                        case DOWN:
                            return BlockFence.SOUTH;
                        case UP:
                            return BlockFence.NORTH;
                        case WEST:
                            return BlockFence.EAST;
                        case EAST:
                            return BlockFence.WEST;
                    }
                    break;
            }

            TwilightForestMod.LOGGER.warn("ConnectableRotatedPillar helper (getPropertyFromFacingWithAxis) had a problem? (facing '{}' with axis '{}')", facing.getName(), axis.getName());
            return BlockFence.NORTH;
        }
    }
}
