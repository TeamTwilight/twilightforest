package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockTFForceField extends BlockTFConnectableRotatedPillar implements IWaterLoggable {

	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	BlockTFForceField(Block.Properties props) {
		super(props, 2);
		this.setDefaultState(getDefaultState().with(WATERLOGGED, false));
	}

	@Override
	protected AxisAlignedBB getSidedAABBStraight(Direction facing, Direction.Axis axis) {
		return makeQuickAABB(
				facing == Direction.EAST  || axis == Direction.Axis.X ? 16 : this.boundingBoxWidthLower,
				facing == Direction.UP    || axis == Direction.Axis.Y ? 16 : this.boundingBoxWidthLower,
				facing == Direction.SOUTH || axis == Direction.Axis.Z ? 16 : this.boundingBoxWidthLower,
				facing == Direction.WEST  || axis == Direction.Axis.X ?  0 : this.boundingBoxWidthUpper,
				facing == Direction.DOWN  || axis == Direction.Axis.Y ?  0 : this.boundingBoxWidthUpper,
				facing == Direction.NORTH || axis == Direction.Axis.Z ?  0 : this.boundingBoxWidthUpper);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
		if (adjacentBlockState.isIn(this)) {
			if (!side.getAxis().isHorizontal()) {
				return true;
			}

			if (state.get(SixWayBlock.FACING_TO_PROPERTY_MAP.get(side)) && adjacentBlockState.get(SixWayBlock.FACING_TO_PROPERTY_MAP.get(side.getOpposite()))) {
				return true;
			}
		}

		return super.isSideInvisible(state, adjacentBlockState, side);
	}

	@Override
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}

	public boolean canConnectTo(BlockState state, boolean solidSide) {
		Block block = state.getBlock();
		return !cannotAttach(block) && solidSide || block instanceof BlockTFForceField || block instanceof PaneBlock || block.isIn(BlockTags.WALLS);
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
