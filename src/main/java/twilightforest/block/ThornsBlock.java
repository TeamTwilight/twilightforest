package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import twilightforest.util.TFDamageSources;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class ThornsBlock extends ConnectableRotatedPillarBlock implements SimpleWaterloggedBlock {

	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final float THORN_DAMAGE = 4.0F;

	ThornsBlock(Properties props) {
		super(props, 10);
		this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
	}

	@Override
	public boolean canConnectTo(BlockState state, boolean solidSide) {
		return (state.getBlock() instanceof ThornsBlock
						|| state.getBlock() == TFBlocks.THORN_ROSE.get()
						|| state.getBlock() == TFBlocks.THORN_LEAVES.get()
						|| state.getMaterial() == Material.PLANT
						|| state.getMaterial() == Material.DIRT);
	}

	@Nullable
	@Override
	public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter world, BlockPos pos, @Nullable Mob entity) {
		return BlockPathTypes.DAMAGE_CACTUS;
	}

	@Override
	@Deprecated
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
		entity.hurt(TFDamageSources.THORNS, THORN_DAMAGE);
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {

		if (state.getBlock() instanceof ThornsBlock && state.getValue(AXIS) == Direction.Axis.Y) {
			entityInside(state, world, pos, entity);
		}

		super.stepOn(world, pos, state, entity);
	}

	@Override
	public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		if (!player.getAbilities().instabuild) {
			if (!world.isClientSide) {
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
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	/**
	 * Grow thorns out of both the ends, then maybe in another direction too
	 */
	private void doThornBurst(Level world, BlockPos pos, BlockState state) {
		switch (state.getValue(AXIS)) {
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
		growThorns(world, pos, Direction.getRandom(world.random));
		growThorns(world, pos, Direction.getRandom(world.random));
		growThorns(world, pos, Direction.getRandom(world.random));
	}

	/**
	 * grow several green thorns in the specified direction
	 */
	private void growThorns(Level world, BlockPos pos, Direction dir) {
		int length = 1 + world.random.nextInt(3);

		for (int i = 1; i < length; i++) {
			BlockPos dPos = pos.relative(dir, i);

			if (world.isEmptyBlock(dPos)) {
				world.setBlock(dPos, TFBlocks.GREEN_THORNS.get().defaultBlockState().setValue(AXIS, dir.getAxis()), 2);
			} else {
				break;
			}
		}
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
