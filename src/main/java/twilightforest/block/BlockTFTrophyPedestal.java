package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.data.BlockTagGenerator;
import twilightforest.util.PlayerHelper;
import twilightforest.world.TFGenerationSettings;

import javax.annotation.Nullable;

public class BlockTFTrophyPedestal extends Block implements IWaterLoggable {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F));

	public BlockTFTrophyPedestal(Properties props) {
		super(props);
		this.setDefaultState(getDefaultState().with(ACTIVE, false).with(WATERLOGGED, false));
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
		builder.add(ACTIVE, WATERLOGGED);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		world.updateComparatorOutputLevel(pos, this);
		if (world.isRemote || state.get(ACTIVE) || !isTrophyOnTop(world, pos)) return;

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

	private boolean isTrophyOnTop(World world, BlockPos pos) {
		return world.getBlockState(pos.up()).getBlock().isIn(BlockTagGenerator.TROPHIES);
	}

	private void warnIneligiblePlayers(World world, BlockPos pos) {
		for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos).grow(16.0D))) {
			if (!isPlayerEligible(player)) {
				player.sendStatusMessage(new TranslationTextComponent(TwilightForestMod.ID + ".trophy_pedestal.ineligible"), true);
			}
		}
	}

	private boolean areNearbyPlayersEligible(World world, BlockPos pos) {
		for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos).grow(16.0D))) {
			if (isPlayerEligible(player)) return true;
		}
		return false;
	}

	private boolean isPlayerEligible(PlayerEntity player) {
		return PlayerHelper.doesPlayerHaveRequiredAdvancements(player, TwilightForestMod.prefix("progress_lich"));
	}

	private void doPedestalEffect(World world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state.with(ACTIVE, true));
		removeNearbyShields(world, pos);
		world.playSound(null, pos, TFSounds.PEDESTAL_ACTIVATE, SoundCategory.BLOCKS, 4.0F, 0.1F);
	}

	private void rewardNearbyPlayers(World world, BlockPos pos) {
		for (ServerPlayerEntity player : world.getEntitiesWithinAABB(ServerPlayerEntity.class, new AxisAlignedBB(pos).grow(16.0D))) {
			TFAdvancements.PLACED_TROPHY_ON_PEDESTAL.trigger(player);
		}
	}

	private void removeNearbyShields(World world, BlockPos pos) {
		for (int sx = -5; sx <= 5; sx++)
			for (int sy = -5; sy <= 5; sy++)
				for (int sz = -5; sz <= 5; sz++)
					if (world.getBlockState(pos.add(sx, sy, sz)).getBlock() == TFBlocks.stronghold_shield.get()) {
						world.destroyBlock(pos.add(sx, sy, sz), false);
					}
	}

	@Override
	public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
		return state.get(ACTIVE) ? super.getPlayerRelativeBlockHardness(state, player, worldIn, pos) : -1;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		Block trophy = worldIn.getBlockState(pos.up()).getBlock();
		if(trophy instanceof BlockTFTrophy) {
			return ((BlockTFTrophy)trophy).getComparatorValue();
		}
		return 0;
	}
}
