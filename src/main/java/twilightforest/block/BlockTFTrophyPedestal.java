package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.world.TFWorld;

import javax.annotation.Nullable;

//TODO 1.14: Thaumcraft is dead
//@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser")
public class BlockTFTrophyPedestal extends Block /*implements IInfusionStabiliser*/ {

	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final BooleanProperty LATENT = BooleanProperty.create("latent");

	private static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F));

	public BlockTFTrophyPedestal() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 2000.0F).sound(SoundType.STONE));
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14
		this.setDefaultState(getDefaultState().with(LATENT, true).with(FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, LATENT);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	//TODO: Check this
//	@Override
//	@Deprecated
//	public boolean isSolid(BlockState state) {
//		return false;
//	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (world.isRemote || !state.get(LATENT) || !isTrophyOnTop(world, pos)) return;

		if (TFWorld.isProgressionEnforced(world)) {
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
		return world.getBlockState(pos.up()).getBlock() instanceof BlockTFTrophy;
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
		//return TwilightForestMod.proxy.doesPlayerHaveAdvancement(player, TwilightForestMod.prefix("progress_lich"));
		return false; //TODO PLACEHOLDER
	}

	private void doPedestalEffect(World world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state.with(LATENT, false));
		removeNearbyShields(world, pos);
		world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.BLOCKS, 4.0F, 0.1F);
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

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing().getOpposite());
	}

	// todo ambiguous in 1.7, what was this supposed to be?
	@Override
	@Deprecated
	public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader world, BlockPos pos) {
		return state.get(LATENT) ? -1 : super.getPlayerRelativeBlockHardness(state, player, world, pos);
	}

	//	@Override
//	protected boolean canSilkHarvest() {
//		return false;
//	}
//
//	@Override
//	public boolean canSilkHarvest(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//		return false;
//	}
//
//	@Override
//	public int damageDropped(BlockState state) {
//		return 0;
//	}

//	@Override
//	public boolean canStabaliseInfusion(World world, BlockPos blockPos) {
//		return true;
//	}
}
