package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import twilightforest.util.TFDamageSources;

import javax.annotation.Nullable;

public class BlockTFThorns extends BlockTFConnectableRotatedPillar implements IWaterLoggable {

	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final float THORN_DAMAGE = 4.0F;

	BlockTFThorns(Properties props) {
		super(props, 10);
		this.setDefaultState(getDefaultState().with(WATERLOGGED, false));
	}

	@Override
	public boolean canConnectTo(BlockState state, boolean solidSide) {
		return (state.getBlock() instanceof BlockTFThorns
						|| state.getBlock() == TFBlocks.thorn_rose.get()
						|| state.getBlock() == TFBlocks.thorn_leaves.get()
						|| state.getMaterial() == Material.PLANTS
						|| state.getMaterial() == Material.EARTH);
	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
		return PathNodeType.DAMAGE_CACTUS;
	}

	@Override
	@Deprecated
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		entity.attackEntityFrom(TFDamageSources.THORNS, THORN_DAMAGE);
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof BlockTFThorns && state.get(AXIS) == Direction.Axis.Y) {
			onEntityCollision(state, world, pos, entity);
		}

		super.onEntityWalk(world, pos, entity);
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
		if (!player.abilities.isCreativeMode) {
			if (!world.isRemote) {
				// grow more
				this.doThornBurst(world, pos, state);
			}
			return false;
		} else {
			return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
		}
	}

	@Override
	@Deprecated
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	/**
	 * Grow thorns out of both the ends, then maybe in another direction too
	 */
	private void doThornBurst(World world, BlockPos pos, BlockState state) {
		switch (state.get(AXIS)) {
			case Y:
				growThorns(world, pos, Direction.UP);
				growThorns(world, pos, Direction.DOWN);
				break;
			case X:
				growThorns(world, pos, Direction.EAST);
				growThorns(world, pos, Direction.WEST);
				break;
			case Z:
				growThorns(world, pos, Direction.NORTH);
				growThorns(world, pos, Direction.SOUTH);
				break;
		}

		// also try three random directions
		growThorns(world, pos, Direction.getRandomDirection(world.rand));
		growThorns(world, pos, Direction.getRandomDirection(world.rand));
		growThorns(world, pos, Direction.getRandomDirection(world.rand));
	}

	/**
	 * grow several green thorns in the specified direction
	 */
	private void growThorns(World world, BlockPos pos, Direction dir) {
		int length = 1 + world.rand.nextInt(3);

		for (int i = 1; i < length; i++) {
			BlockPos dPos = pos.offset(dir, i);

			if (world.isAirBlock(dPos)) {
				world.setBlockState(dPos, TFBlocks.green_thorns.get().getDefaultState().with(AXIS, dir.getAxis()), 2);
			} else {
				break;
			}
		}
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
