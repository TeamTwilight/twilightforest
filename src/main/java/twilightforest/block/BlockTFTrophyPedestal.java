package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.world.TFGenerationSettings;

//TODO 1.14: Thaumcraft is dead
//@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser")
public class BlockTFTrophyPedestal extends Block /*implements IInfusionStabiliser*/ {

	public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

	private static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F));

	public BlockTFTrophyPedestal(Properties props) {
		super(props);
		this.setDefaultState(getDefaultState().with(ACTIVE, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ACTIVE);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
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

	//TODO: Moved to BlockState
//	@Override
//	public float getBlockHardness(BlockState state, IBlockReader world, BlockPos pos) {
//		return state.get(ACTIVE) ? super.getBlockHardness(state, world, pos) : -1;
//	}

//	@Override
//	public boolean canStabaliseInfusion(World world, BlockPos blockPos) {
//		return true;
//	}
}
