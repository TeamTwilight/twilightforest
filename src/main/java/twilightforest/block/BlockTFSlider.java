package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.entity.EntityTFSlideBlock;
import twilightforest.entity.TFEntities;
import twilightforest.util.TFDamageSources;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTFSlider extends RotatedPillarBlock implements IWaterLoggable {

	public static final IntegerProperty DELAY = IntegerProperty.create("delay", 0, 3);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final int TICK_TIME = 80;
	private static final int OFFSET_TIME = 20;
	private static final int PLAYER_RANGE = 32;
	private static final float BLOCK_DAMAGE = 5;
	private static final VoxelShape Y_BB = VoxelShapes.create(new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 1F, 0.6875));
	private static final VoxelShape Z_BB = VoxelShapes.create(new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 1F));
	private static final VoxelShape X_BB = VoxelShapes.create(new AxisAlignedBB(0, 0.3125, 0.3125, 1F, 0.6875, 0.6875));

	protected BlockTFSlider() {
		super(Properties.create(Material.IRON, MaterialColor.DIRT).hardnessAndResistance(2.0F, 10.0F).tickRandomly().notSolid());
		this.setDefaultState(stateContainer.getBaseState().with(AXIS, Direction.Axis.Y).with(DELAY, 0).with(WATERLOGGED, false));
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
		builder.add(DELAY, WATERLOGGED);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(AXIS)) {
			case Y:
			default:
				return Y_BB;
			case X:
				return X_BB;
			case Z:
				return Z_BB;
		}
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!world.isRemote && this.isConnectedInRange(world, pos)) {
			//TODO calls for a creakstart sound effect, but it doesnt exist in the game files
			//world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, TFSounds.SLIDER, SoundCategory.BLOCKS, 0.75F, 1.5F);

			EntityTFSlideBlock slideBlock = new EntityTFSlideBlock(TFEntities.slider, world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
			world.addEntity(slideBlock);
		}

		scheduleBlockUpdate(world, pos);
	}

	/**
	 * Check if there is any players in range, and also recursively check connected blocks
	 */
	public boolean isConnectedInRange(World world, BlockPos pos) {
		Direction.Axis axis = world.getBlockState(pos).get(AXIS);

		switch (axis) {
			case Y:
				return this.anyPlayerInRange(world, pos) || this.isConnectedInRangeRecursive(world, pos, Direction.UP) || this.isConnectedInRangeRecursive(world, pos, Direction.DOWN);
			case X:
				return this.anyPlayerInRange(world, pos) || this.isConnectedInRangeRecursive(world, pos, Direction.WEST) || this.isConnectedInRangeRecursive(world, pos, Direction.EAST);
			case Z:
				return this.anyPlayerInRange(world, pos) || this.isConnectedInRangeRecursive(world, pos, Direction.NORTH) || this.isConnectedInRangeRecursive(world, pos, Direction.SOUTH);
			default:
				return this.anyPlayerInRange(world, pos);
		}
	}

	private boolean isConnectedInRangeRecursive(World world, BlockPos pos, Direction dir) {
		BlockPos dPos = pos.offset(dir);

		if (world.getBlockState(pos) == world.getBlockState(dPos)) {
			return this.anyPlayerInRange(world, dPos) || this.isConnectedInRangeRecursive(world, dPos, dir);
		} else {
			return false;
		}
	}

	private boolean anyPlayerInRange(World world, BlockPos pos) {
		return world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, PLAYER_RANGE, false) != null;
	}

	public void scheduleBlockUpdate(World world, BlockPos pos) {
		int offset = world.getBlockState(pos).get(DELAY);
		int update = TICK_TIME - ((int) (world.getGameTime() - (offset * OFFSET_TIME)) % TICK_TIME);
		world.getPendingBlockTicks().scheduleTick(pos, this, update);
	}

	@Override
	@Deprecated
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
		scheduleBlockUpdate(world, pos);
	}

	@Override
	@Deprecated
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		entity.attackEntityFrom(TFDamageSources.SLIDER, BLOCK_DAMAGE);
		if (entity instanceof LivingEntity) {
			double kx = (pos.getX() + 0.5 - entity.getPosX()) * 2.0;
			double kz = (pos.getZ() + 0.5 - entity.getPosZ()) * 2.0;

			((LivingEntity) entity).applyKnockback(2, kx, kz);
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("twilightforest.misc.nyi"));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
