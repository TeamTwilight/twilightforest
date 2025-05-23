package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.entity.SlideBlock;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFEntities;

public class SliderBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {

	public static final IntegerProperty DELAY = IntegerProperty.create("delay", 0, 3);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final int TICK_TIME = 80;
	private static final int OFFSET_TIME = 20;
	private static final int PLAYER_RANGE = 32;
	private static final float BLOCK_DAMAGE = 5;
	private static final VoxelShape Y_BB = Shapes.create(new AABB(0.3125, 0, 0.3125, 0.6875, 1F, 0.6875));
	private static final VoxelShape Z_BB = Shapes.create(new AABB(0.3125, 0.3125, 0, 0.6875, 0.6875, 1F));
	private static final VoxelShape X_BB = Shapes.create(new AABB(0, 0.3125, 0.3125, 1F, 0.6875, 0.6875));

	public SliderBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, Direction.Axis.Y).setValue(DELAY, 0).setValue(WATERLOGGED, false));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		boolean flag = fluidstate.getType() == Fluids.WATER;
		return super.getStateForPlacement(context).setValue(WATERLOGGED, flag);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos facingPos, BlockState facingState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			access.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(reader));
		}

		return super.updateShape(state, reader, access, pos, direction, facingPos, facingState, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(DELAY, WATERLOGGED));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(AXIS)) {
			case X -> X_BB;
			case Z -> Z_BB;
			default -> Y_BB;
		};
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isClientSide() && this.isConnectedInRange(level, pos)) {
			//TODO calls for a creakstart sound effect, but it doesnt exist in the game files
			//world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, TFSounds.SLIDER, SoundCategory.BLOCKS, 0.75F, 1.5F);

			SlideBlock slideBlock = new SlideBlock(TFEntities.SLIDER.get(), level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
			level.addFreshEntity(slideBlock);
		}

		this.scheduleBlockUpdate(level, pos);
	}

	/**
	 * Check if there is any players in range, and also recursively check connected blocks
	 */
	public boolean isConnectedInRange(Level level, BlockPos pos) {
		Direction.Axis axis = level.getBlockState(pos).getValue(AXIS);

		return switch (axis) {
			case Y -> this.anyPlayerInRange(level, pos) || this.isConnectedInRangeRecursive(level, pos, Direction.UP) || this.isConnectedInRangeRecursive(level, pos, Direction.DOWN);
			case X -> this.anyPlayerInRange(level, pos) || this.isConnectedInRangeRecursive(level, pos, Direction.WEST) || this.isConnectedInRangeRecursive(level, pos, Direction.EAST);
			case Z -> this.anyPlayerInRange(level, pos) || this.isConnectedInRangeRecursive(level, pos, Direction.NORTH) || this.isConnectedInRangeRecursive(level, pos, Direction.SOUTH);
		};
	}

	private boolean isConnectedInRangeRecursive(Level level, BlockPos pos, Direction dir) {
		BlockPos dPos = pos.relative(dir);

		if (level.getBlockState(pos) == level.getBlockState(dPos)) {
			return this.anyPlayerInRange(level, dPos) || this.isConnectedInRangeRecursive(level, dPos, dir);
		} else {
			return false;
		}
	}

	private boolean anyPlayerInRange(Level level, BlockPos pos) {
		return level.getNearestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, PLAYER_RANGE, false) != null;
	}

	public void scheduleBlockUpdate(Level level, BlockPos pos) {
		int offset = level.getBlockState(pos).getValue(DELAY);
		int update = TICK_TIME - ((int) (level.getGameTime() - (offset * OFFSET_TIME)) % TICK_TIME);
		level.scheduleTick(pos, this, update);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		this.scheduleBlockUpdate(level, pos);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity living && entity.hurtServer(serverLevel, serverLevel.damageSources().source(TFDamageTypes.SLIDER), BLOCK_DAMAGE)) {
			double kx = (pos.getX() + 0.5 - entity.getX()) * 2.0;
			double kz = (pos.getZ() + 0.5 - entity.getZ()) * 2.0;

			living.knockback(2, kx, kz);
		}
	}
}
