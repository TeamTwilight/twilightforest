package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class WallPillarBlock extends ConnectableRotatedPillarBlock implements SimpleWaterloggedBlock {
    //nightmares
    private final VoxelShape TOP_X = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    private final VoxelShape BOTTOM_X = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape PILLAR_X = Block.box(0.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D);
    private final VoxelShape NO_TOP_X = Shapes.or(PILLAR_X, BOTTOM_X);
    private final VoxelShape NO_BOTTOM_X = Shapes.or(PILLAR_X, TOP_X);
    private final VoxelShape FULL_X = Shapes.or(PILLAR_X, BOTTOM_X, TOP_X);

    private final VoxelShape TOP_Y = Block.box(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape BOTTOM_Y = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
    private final VoxelShape PILLAR_Y = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private final VoxelShape NO_TOP_Y = Shapes.or(PILLAR_Y, BOTTOM_Y);
    private final VoxelShape NO_BOTTOM_Y = Shapes.or(PILLAR_Y, TOP_Y);
    private final VoxelShape FULL_Y = Shapes.or(PILLAR_Y, BOTTOM_Y, TOP_Y);

    private final VoxelShape TOP_Z = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    private final VoxelShape BOTTOM_Z = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape PILLAR_Z = Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 16.0D);
    private final VoxelShape NO_TOP_Z = Shapes.or(PILLAR_Z, BOTTOM_Z);
    private final VoxelShape NO_BOTTOM_Z = Shapes.or(PILLAR_Z, TOP_Z);
    private final VoxelShape FULL_Z = Shapes.or(PILLAR_Z, BOTTOM_Z, TOP_Z);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public WallPillarBlock(Material material, double width, double height) {
        super(Properties.of(material).strength(1.5F, 6.0F).requiresCorrectToolForDrops().noOcclusion(), width, height);
        this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		switch (state.getValue(WallPillarBlock.AXIS)) {
            case X:
                return state.getValue(PipeBlock.WEST) && state.getValue(PipeBlock.EAST) ? PILLAR_X :
                        state.getValue(PipeBlock.WEST) ? NO_TOP_X : state.getValue(PipeBlock.EAST) ? NO_BOTTOM_X : FULL_X;
            case Y:
            default:
                return state.getValue(PipeBlock.UP) && state.getValue(PipeBlock.DOWN) ? PILLAR_Y :
                        state.getValue(PipeBlock.UP) ? NO_TOP_Y : state.getValue(PipeBlock.DOWN) ? NO_BOTTOM_Y : FULL_Y;
            case Z:
                return state.getValue(PipeBlock.NORTH) && state.getValue(PipeBlock.SOUTH) ? PILLAR_Z :
                        state.getValue(PipeBlock.NORTH) ? NO_TOP_Z : state.getValue(PipeBlock.SOUTH) ? NO_BOTTOM_Z : FULL_Z;
        }
    }

    @Override
    public boolean canConnectTo(BlockState state, boolean solidSide) {
        Block block = state.getBlock();
        return block instanceof WallPillarBlock;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return super.getStateForPlacement(context).setValue(WATERLOGGED, Boolean.valueOf(flag));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }

        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }
}
