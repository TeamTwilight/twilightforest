package twilightforest.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import twilightforest.block.entity.DryingRackBlockEntity;
import twilightforest.inventory.InventoryUtil;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

import java.util.Map;

public class DryingRackBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

	public static final MapCodec<DryingRackBlock> CODEC = simpleCodec(DryingRackBlock::new);

	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
		Direction.SOUTH, Block.box(0.0D, 12.0D, 12.0D, 16.0D, 16.0D, 16.0D),
		Direction.NORTH, Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 4.0D),
		Direction.WEST, Block.box(0.0D, 12.0D, 0.0D, 4.0D, 16.0D, 16.0D),
		Direction.EAST, Block.box(12.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D)));

	public DryingRackBlock(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		boolean flag = fluidstate.getType() == Fluids.WATER;
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(WATERLOGGED, flag);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack playerStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (!(level.getBlockEntity(pos) instanceof DryingRackBlockEntity rack))
			return super.useItemOn(playerStack, state, level, pos, player, hand, hitResult);

		if (rack.getTheItem().isEmpty() && !playerStack.isEmpty()) {
			rack.setTheItem(player.hasInfiniteMaterials() ? playerStack.copyWithCount(1) : playerStack.split(1));
			if (!level.isClientSide()) {
				level.playSound(null, pos, TFSounds.DRYING_RACK_ADD_ITEM.get(), SoundSource.BLOCKS, 1.0F, 0.75F + level.getRandom().nextFloat() * 0.5F);
				level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			}
		} else {
			if (!level.isClientSide()) {
				ItemStack item = rack.takeTheItem();
				if (!item.isEmpty()) {
					InventoryUtil.giveItemToPlayer(player, item);
					level.playSound(null, pos, TFSounds.DRYING_RACK_REMOVE_ITEM.get(), SoundSource.BLOCKS, 0.75F, 0.75F + level.getRandom().nextFloat() * 0.5F);
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				} else return InteractionResult.CONSUME;
			} else if (rack.getTheItem().isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbor, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, level, ticks, pos, directionToNeighbor, neighborPos, neighborState, random);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (level.getBlockEntity(pos) instanceof DryingRackBlockEntity rack && rack.isDrying() && random.nextInt(5) == 0) {
			Direction dir = state.getValue(FACING);
			ParticleUtils.spawnParticleOnFace(level, pos, dir, TFParticleType.DRYING_RACK.get(), Vec3.ZERO, 0.3F);
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DryingRackBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, TFBlockEntities.DRYING_RACK.get(), DryingRackBlockEntity::tick);
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}
}
