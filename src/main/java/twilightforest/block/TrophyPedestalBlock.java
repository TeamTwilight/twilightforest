package twilightforest.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.data.BlockTagGenerator;
import twilightforest.util.PlayerHelper;
import twilightforest.util.TFStats;
import twilightforest.world.registration.TFGenerationSettings;

import javax.annotation.Nullable;

public class TrophyPedestalBlock extends Block implements SimpleWaterloggedBlock {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape AABB = Shapes.create(new AABB(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F));

	public TrophyPedestalBlock(Properties props) {
		super(props);
		this.registerDefaultState(defaultBlockState().setValue(ACTIVE, false).setValue(WATERLOGGED, false));
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
		builder.add(ACTIVE, WATERLOGGED);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		world.updateNeighbourForOutputSignal(pos, this);
		if (world.isClientSide || state.getValue(ACTIVE) || !isTrophyOnTop(world, pos)) return;

		if (TFGenerationSettings.isProgressionEnforced(world)) {
			if (areNearbyPlayersEligible(world, pos)) {
				doPedestalEffect(world, pos, state);
			}
			warnIneligiblePlayers(world, pos);
		} else {
			doPedestalEffect(world, pos, state);
		}

		rewardNearbyPlayers(world, pos);
	}

	private boolean isTrophyOnTop(Level world, BlockPos pos) {
		return world.getBlockState(pos.above()).is(BlockTagGenerator.TROPHIES);
	}

	private void warnIneligiblePlayers(Level world, BlockPos pos) {
		for (Player player : world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(16.0D))) {
			if (!isPlayerEligible(player)) {
				player.displayClientMessage(new TranslatableComponent(TwilightForestMod.ID + ".trophy_pedestal.ineligible"), true);
			}
		}
	}

	private boolean areNearbyPlayersEligible(Level world, BlockPos pos) {
		for (Player player : world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(16.0D))) {
			if (isPlayerEligible(player)) return true;
		}
		return false;
	}

	private boolean isPlayerEligible(Player player) {
		return PlayerHelper.doesPlayerHaveRequiredAdvancements(player, TwilightForestMod.prefix("progress_lich"));
	}

	private void doPedestalEffect(Level world, BlockPos pos, BlockState state) {
		world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
		removeNearbyShields(world, pos);
		world.playSound(null, pos, TFSounds.PEDESTAL_ACTIVATE, SoundSource.BLOCKS, 4.0F, 0.1F);
	}

	private void rewardNearbyPlayers(Level world, BlockPos pos) {
		for (ServerPlayer player : world.getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(16.0D))) {
			TFAdvancements.PLACED_TROPHY_ON_PEDESTAL.trigger(player);
			player.awardStat(TFStats.TROPHY_PEDESTALS_ACTIVATED);
		}
	}

	private void removeNearbyShields(Level world, BlockPos pos) {
		for (int sx = -5; sx <= 5; sx++)
			for (int sy = -5; sy <= 5; sy++)
				for (int sz = -5; sz <= 5; sz++)
					if (world.getBlockState(pos.offset(sx, sy, sz)).getBlock() == TFBlocks.STRONGHOLD_SHIELD.get()) {
						world.destroyBlock(pos.offset(sx, sy, sz), false);
					}
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
		return state.getValue(ACTIVE) ? super.getDestroyProgress(state, player, worldIn, pos) : -1;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		Block trophy = worldIn.getBlockState(pos.above()).getBlock();
		if(trophy instanceof TrophyBlock) {
			return ((TrophyBlock)trophy).getComparatorValue();
		}
		return 0;
	}
}
