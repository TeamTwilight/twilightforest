package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTFWallPillar extends BlockTFConnectableRotatedPillar implements IWaterLoggable {
    //nightmares
    private final VoxelShape TOP_X = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    private final VoxelShape BOTTOM_X = Block.makeCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape PILLAR_X = Block.makeCuboidShape(0.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D);
    private final VoxelShape NO_TOP_X = VoxelShapes.or(PILLAR_X, BOTTOM_X);
    private final VoxelShape NO_BOTTOM_X = VoxelShapes.or(PILLAR_X, TOP_X);
    private final VoxelShape FULL_X = VoxelShapes.or(PILLAR_X, BOTTOM_X, TOP_X);

    private final VoxelShape TOP_Y = Block.makeCuboidShape(0.0D, 13.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape BOTTOM_Y = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);
    private final VoxelShape PILLAR_Y = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private final VoxelShape NO_TOP_Y = VoxelShapes.or(PILLAR_Y, BOTTOM_Y);
    private final VoxelShape NO_BOTTOM_Y = VoxelShapes.or(PILLAR_Y, TOP_Y);
    private final VoxelShape FULL_Y = VoxelShapes.or(PILLAR_Y, BOTTOM_Y, TOP_Y);

    private final VoxelShape TOP_Z = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    private final VoxelShape BOTTOM_Z = Block.makeCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape PILLAR_Z = Block.makeCuboidShape(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 16.0D);
    private final VoxelShape NO_TOP_Z = VoxelShapes.or(PILLAR_Z, BOTTOM_Z);
    private final VoxelShape NO_BOTTOM_Z = VoxelShapes.or(PILLAR_Z, TOP_Z);
    private final VoxelShape FULL_Z = VoxelShapes.or(PILLAR_Z, BOTTOM_Z, TOP_Z);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockTFWallPillar(Material material, double width, double height) {
        super(Properties.create(material).hardnessAndResistance(1.5F, 10.0F).setRequiresTool().harvestTool(ToolType.PICKAXE).notSolid(), width, height);
        this.setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		switch (state.get(BlockTFWallPillar.AXIS)) {
            case X:
                return state.get(SixWayBlock.WEST) && state.get(SixWayBlock.EAST) ? PILLAR_X :
                        state.get(SixWayBlock.WEST) ? NO_TOP_X : state.get(SixWayBlock.EAST) ? NO_BOTTOM_X : FULL_X;
            case Y:
            default:
                return state.get(SixWayBlock.UP) && state.get(SixWayBlock.DOWN) ? PILLAR_Y :
                        state.get(SixWayBlock.UP) ? NO_TOP_Y : state.get(SixWayBlock.DOWN) ? NO_BOTTOM_Y : FULL_Y;
            case Z:
                return state.get(SixWayBlock.NORTH) && state.get(SixWayBlock.SOUTH) ? PILLAR_Z :
                        state.get(SixWayBlock.NORTH) ? NO_TOP_Z : state.get(SixWayBlock.SOUTH) ? NO_BOTTOM_Z : FULL_Z;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("twilightforest.misc.nyi"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public boolean canConnectTo(BlockState state, boolean solidSide) {
        Block block = state.getBlock();
        return block instanceof BlockTFWallPillar;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        boolean flag = fluidstate.getFluid() == Fluids.WATER;
        return super.getStateForPlacement(context).with(WATERLOGGED, Boolean.valueOf(flag));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(WATERLOGGED);
    }
}
